package uk.ac.tees.mad.w9615925

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var  login :Button
    lateinit var editTextEmail :EditText
    lateinit var editTextPassword :EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (auth.currentUser != null) {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        findViewById<TextView>(R.id.login_redirect).setOnClickListener{
            startActivity(Intent(this,registerActivity::class.java))
        }
        login = findViewById<Button>(R.id.login_button)
        editTextEmail = findViewById<EditText>(R.id.email_input)
        editTextPassword = findViewById<EditText>(R.id.password_input)



        login.setOnClickListener {

            var email = editTextEmail.text.toString()
            var password = editTextPassword.text.toString()
            var valid:Boolean = true

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_LONG).show()
               valid=false
            }

            if (!isValidPassword(password)) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_LONG).show()
              valid=false
            }
            if(valid)
            {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user: FirebaseUser? = auth.currentUser
                            Toast.makeText(this, "Login SuccessFul", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, DashboardActivity::class.java))
                        } else {
                            Toast.makeText(this, "User Not Found", Toast.LENGTH_LONG).show()
                        }
                    }
            }


        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

}