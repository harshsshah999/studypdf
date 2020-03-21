package com.app.hardik.studypdf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class LoginPage : AppCompatActivity() {
    lateinit var loginbtn : Button
    lateinit var newusrbtn : Button
    lateinit var email : EditText
    lateinit var password : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        //Variables
        loginbtn = findViewById(R.id.login)
        newusrbtn = findViewById(R.id.newuser)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)

        //Sign up Intent Fun

    }
    fun newuser (v: View){
        startActivity(Intent(this,SignUp::class.java))
    }

}
