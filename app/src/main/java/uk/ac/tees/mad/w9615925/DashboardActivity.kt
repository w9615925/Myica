package uk.ac.tees.mad.w9615925

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import java.util.concurrent.Executor
import kotlin.random.Random

class DashboardActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    lateinit var hospital :ImageView
    lateinit var disease :ImageView
    lateinit var food:ImageView
    lateinit var sounds :ImageView

    private lateinit var authFirebase: FirebaseAuth;
    val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        hospital = findViewById<ImageView>(R.id.imagepetshospital)
        disease = findViewById(R.id.imagedogdisease)
        food = findViewById(R.id.imagefood)
        sounds = findViewById(R.id.imageSound)

        hospital.setOnClickListener{
            go(Hospital::class.java)
        }
        sounds.setOnClickListener{
            go(Sounds::class.java)
        }
        disease.setOnClickListener{
            go(Disease::class.java)
        }
        food.setOnClickListener{
            go(Food::class.java)
        }

        authFirebase = FirebaseAuth.getInstance()



        findViewById<Button>(R.id.signout).setOnClickListener {
            authFirebase.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        loadCatImage()
        loadCatFact()
    }

    fun go(classs:Class<*>)
    {
        startActivity(Intent(applicationContext,classs))

    }

    fun checkForLogin() {
        if (authFirebase.currentUser == null) {
            var inte = Intent(this, LoginActivity::class.java)
            startActivity(inte)
        }else{
            //startBiometricAuth()
        }
    }

    private fun startBiometricAuth() {
        executor = ContextCompat.getMainExecutor(applicationContext)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence)
            {
                super.onAuthenticationError(errorCode, errString)

                FirebaseAuth.getInstance().signOut()
                finish()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Please Authenticate")
            .setSubtitle("use Fingerprint")
            .setNegativeButtonText("NO Account Password")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        biometricPrompt.authenticate(promptInfo)


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
                        Glide.with(applicationContext).load(imageUrl).into(imageView)
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
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        checkForLogin()
    }
}