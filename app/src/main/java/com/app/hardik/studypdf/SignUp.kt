package com.app.hardik.studypdf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailtext: EditText
    private lateinit var passwordtext: EditText
    private lateinit var confpasstext: EditText
    private lateinit var nametext: EditText
    private lateinit var signup: Button
    private lateinit var olduser: Button
    lateinit var email: String
    lateinit var password: String
    lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        //assigning views
        emailtext = findViewById(R.id.email)
        passwordtext = findViewById(R.id.password)
        confpasstext = findViewById(R.id.confpassword)
        nametext = findViewById(R.id.fullname)
        signup = findViewById(R.id.signup)
        olduser = findViewById(R.id.olduser)

    }
    fun createAccount (email:String ,password:String,name:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "createUserWithEmail:success")
                    startActivity(Intent(this,userdashboard::class.java))
                    val user = auth.currentUser

                } else if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.") {
                    // If sign in fails, display a message to the user.
                    Log.i("Error of",task.exception.toString())
                    Toast.makeText(baseContext, "Email Address is Invalid!",
                        Toast.LENGTH_LONG).show()
                }
                else if(task.exception.toString() == "com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]"){
                    Log.i("Error of",task.exception.toString())
                    Toast.makeText(baseContext, "Password is too Small! (At least 6 characters)",
                        Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(baseContext, "Unknown Error Occurred!",Toast.LENGTH_LONG).show()
                }

                // ...
            }

    }
    fun olduser (v: View){
        startActivity(Intent(this,LoginPage::class.java))
    }
    fun Signup (v: View){
        if (emailtext.text.isNullOrEmpty() || passwordtext.text.isNullOrEmpty() || confpasstext.text.isNullOrEmpty() || nametext.text.isNullOrEmpty())
        {
            Toast.makeText(this,"You can't Leave a Field Empty!",Toast.LENGTH_LONG).show()
        }
        else if(passwordtext.text.toString() != confpasstext.text.toString())
        {
            Toast.makeText(this,"Passwords Doesn't Match!",Toast.LENGTH_LONG).show()
        }
        else {
            email = emailtext.text.toString()
            password = passwordtext.text.toString()
            name = nametext.text.toString()
            createAccount(email,password,name)
        }

    }
}
