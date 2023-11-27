package uk.ac.tees.mad.w9615925

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class registerActivity : AppCompatActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var registerButton: Button
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var editTextName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton = findViewById<Button>(R.id.ca_button)
        editTextEmail = findViewById<EditText>(R.id.ca_email_input)
        editTextPassword = findViewById<EditText>(R.id.ca_password_input)
        editTextName = findViewById<EditText>(R.id.name)

        registerButton.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val name = editTextName.text.toString()

            var valid = true

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_LONG).show()
                valid = false
            }

            if (!isValidPassword(password)) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_LONG).show()
                valid = false
            }

            if (valid) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Update user profile here with the name, if necessary
                            val user = auth.currentUser
                            Toast.makeText(this, "Registration Successful, Please Login", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                        } else {
                            Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
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
