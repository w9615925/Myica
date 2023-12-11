package uk.ac.tees.mad.w9615925

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.transition.Visibility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Disease : AppCompatActivity() {

    private var imageData: Uri? = null
    private lateinit var imageViewSelectedImage: ImageView
    private lateinit var pb:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disease)
        pb =findViewById(R.id.progressBar)

        val spinnerPetType: Spinner = findViewById(R.id.spinnerPetType)
        val editTextProblemDescription: EditText = findViewById(R.id.editTextProblemDescription)
        val buttonUploadImage: Button = findViewById(R.id.buttonUploadImage)
        val buttonSubmit: Button = findViewById(R.id.buttonSubmit)
        imageViewSelectedImage = findViewById(R.id.imageViewSelectedImage)


        val petTypes = arrayOf("Dog", "Cat", "Bird", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, petTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPetType.adapter = adapter


        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    imageData = result.data!!.data
                    imageViewSelectedImage.setImageURI(imageData)
                }
            }

        buttonUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getContent.launch(intent)
        }

        buttonSubmit.setOnClickListener {
            pb.visibility=View.VISIBLE
            val selectedPet = spinnerPetType.selectedItem.toString()
            val problemDescription = editTextProblemDescription.text.toString()

            imageData?.let { uri ->
                uploadImageToFirebase(uri) { imageUrl ->

                    saveDataToRealtimeDatabase(selectedPet, problemDescription, imageUrl)
                }
            }
        }
    }

    private fun uploadImageToFirebase(uri: Uri, onSuccess: (String) -> Unit) {
        val fileName = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        val ref = FirebaseStorage.getInstance().reference.child("images/$fileName")

        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"faiklereceive mail for appointment",Toast.LENGTH_LONG).show()
                pb.visibility=View.GONE
            }
    }

        private fun saveDataToRealtimeDatabase(petType: String, description: String, imageUrl: String) {
            val databaseRef = FirebaseDatabase.getInstance().getReference("PetDiseases")
            val diseaseId = databaseRef.push().key

            val petDiseaseInfo = hashMapOf(
                "petType" to petType,
                "description" to description,
                "imageUrl" to imageUrl,
                "user" to (FirebaseAuth.getInstance().currentUser?.uid ?: "")
            )

            if (diseaseId != null) {
                databaseRef.child(diseaseId).setValue(petDiseaseInfo)
                    .addOnSuccessListener {
                      Toast.makeText(applicationContext,"Raised Request, You will receive mail for appointment",Toast.LENGTH_LONG).show()
                       startActivity(Intent(applicationContext,DashboardActivity::class.java))
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext,"failed",Toast.LENGTH_LONG).show()
                        pb.visibility=View.GONE
                    }
            }
        }
}
