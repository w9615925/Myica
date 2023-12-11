package uk.ac.tees.mad.w9615925

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import android.Manifest
import android.location.Location
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places


class Hospital : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var placesClient: PlacesClient

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyA3JY3wDKQeK7J9SdEefd-bvb8MrkRri2o")
        }
        placesClient = Places.createClient(this)





        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableMyLocationIfPermitted()
    }

    private fun enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            moveToCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun moveToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                searchNearbyPetHospitals(it)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableMyLocationIfPermitted()
        }
    }

    private fun searchNearbyPetHospitals(location: Location) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return
        }

        val request = FindCurrentPlaceRequest.builder(listOf(Place.Field.NAME, Place.Field.LAT_LNG)).build()

        placesClient.findCurrentPlace(request).addOnSuccessListener { response ->
            for (placeLikelihood in response.placeLikelihoods) {
                val place = placeLikelihood.place
                if(place.types != null)
                {
                    if (place.types.contains(Place.Type.VETERINARY_CARE)) {
                        map.addMarker(MarkerOptions()
                            .title(place.name)
                            .position(place.latLng!!))
                    }
                }

            }
        }.addOnFailureListener { exception ->
            // Handle exception
        }
    }
}