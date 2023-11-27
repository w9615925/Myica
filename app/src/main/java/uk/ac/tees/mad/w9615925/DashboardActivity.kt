package uk.ac.tees.mad.w9615925

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import kotlin.random.Random

class DashboardActivity : AppCompatActivity() {

    private lateinit var authFirebase: FirebaseAuth;
    val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        authFirebase = FirebaseAuth.getInstance()
        checkForLogin()

        findViewById<Button>(R.id.signout).setOnClickListener {
            authFirebase.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        loadCatImage()
        loadCatFact()
    }

    fun checkForLogin() {
        if (authFirebase.currentUser == null) {
            var inte: Intent = Intent(this, LoginActivity::class.java)
            startActivity(inte)
        }

    }

    private fun loadCatImage() {

        val request = Request.Builder()
            .url("https://api.api-ninjas.com/v1/cats?family_friendly=5")
            .addHeader("X-Api-Key", "y+1NKg0aiozlLHal6NxzKQ==BwJSMLrwJPSiveUh")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonArray = JSONArray(response.body!!.string())
                    val randomIndex = Random.nextInt(jsonArray.length())
                    val imageUrl = jsonArray.getJSONObject(randomIndex).getString("image_link")

                    runOnUiThread {
                        val imageView = findViewById<ImageView>(R.id.wallimage)
                        Glide.with(this@DashboardActivity).load(imageUrl).into(imageView)
                    }
                }
            }
        })
    }

    private fun loadCatFact() {
        val request = Request.Builder()
            .url("https://brianiswu-cat-facts-v1.p.rapidapi.com/facts")
            .addHeader("X-RapidAPI-Key", "f6d96b246cmsh9f60d5199ed066dp178283jsn2441bcb10c02")
            .addHeader("X-RapidAPI-Host", "brianiswu-cat-facts-v1.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // Handle the error appropriately in the UI, e.g., show a toast message
                runOnUiThread {
                    Toast.makeText(
                        applicationContext,
                        "Failed to load cat facts",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonArray = JSONArray(response.body!!.string())
                    val randomIndex = Random.nextInt(jsonArray.length())
                    val fact = jsonArray.getJSONObject(randomIndex).getString("text")

                    runOnUiThread {
                        val textView = findViewById<TextView>(R.id.heading)
                        textView.text = fact
                    }
                } else {
                    // Handle the scenario where the response is not successful
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "Error fetching cat facts",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }
        })
    }
}