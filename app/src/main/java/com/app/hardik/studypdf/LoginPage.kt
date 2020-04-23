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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class LoginPage : AppCompatActivity() {
    lateinit var loginbtn : Button
    lateinit var newusrbtn : Button
    lateinit var emailtext : EditText
    lateinit var passwordtext : EditText
    lateinit var email: String
    lateinit var password: String
    private lateinit var databaseRef: DatabaseReference
    private lateinit var database: FirebaseDatabase

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        //Variables
        loginbtn = findViewById(R.id.login)
        newusrbtn = findViewById(R.id.newuser)
        emailtext = findViewById(R.id.email)
        passwordtext = findViewById(R.id.password)
        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference().child("Auth").child("AllUsers")

        //Sign up Intent Fun

    }
    fun loginAccount (email:String,password:String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.i(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    onAuthSuccess(user!!)

                } else if(task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted."){
                    Toast.makeText(baseContext, "User Doesn't Exist.",
                        Toast.LENGTH_LONG).show()
                }
                else if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.") {
                    // If sign in fails, display a message to the user.
                    Log.i("Error of",task.exception.toString())
                    Toast.makeText(baseContext, "Email Address is Invalid!",
                        Toast.LENGTH_LONG).show()
                }
                else if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password."){
                    Toast.makeText(baseContext, "Password is Incorrect!",
                        Toast.LENGTH_LONG).show()
                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.i("Error of", task.exception.toString())
                    Toast.makeText(baseContext, "Unknown Error Occurred",
                        Toast.LENGTH_LONG).show()
                    // ...
                }

                // ...
            }
    }
    fun onAuthSuccess(user: FirebaseUser) {
        databaseRef.child(user.uid).child("UserID").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                val flag = p0.value.toString()
                //Log.i("flagID",flag)
                if(flag=="1") {
                    startActivity(Intent(this@LoginPage, userdashboard::class.java))
                }
                else if (flag=="2"){
                    startActivity(Intent(this@LoginPage, Admindashboard::class.java))
                }
            }

        })

    }
    fun newuser (v: View){
        startActivity(Intent(this,SignUp::class.java))
    }
    fun Login (v: View){
        if (emailtext.text.isNullOrEmpty() || passwordtext.text.isNullOrEmpty())
        {
            Toast.makeText(this,"You can't leave a field empty!", Toast.LENGTH_LONG).show()
        }

        else {
            email = emailtext.text.toString()
            password = passwordtext.text.toString()
            loginAccount(email,password)
        }

    }


}
