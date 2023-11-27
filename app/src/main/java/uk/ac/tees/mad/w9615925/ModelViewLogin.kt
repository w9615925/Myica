package uk.ac.tees.mad.w9615925

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel()  {

    private val loginResultLiveData = MutableLiveData<LoginResult>()
    val loginResultObserver: LiveData<LoginResult> get() = loginResultLiveData

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(email: String, password: String) {

    }

    fun register(email: String, pass: String){

    }

    sealed class LoginResult {
        data class LoginSuccess(val user: FirebaseUser?) : LoginResult()

    }
}
