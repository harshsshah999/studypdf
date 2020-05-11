package com.app.hardik.studypdf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlin.random.Random

class userdashboard : AppCompatActivity() {
    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var userauth : FirebaseAuth
    lateinit var name : String
    lateinit var helloname : TextView
    var user : FirebaseUser? = null

    //Scrap Buttons
    lateinit var se : Button
    lateinit var spcc : Button
    lateinit var tcs : Button
    lateinit var css : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdashboard)
        helloname = findViewById(R.id.welcome)
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference()
        userauth = FirebaseAuth.getInstance()
        user = userauth.currentUser
        databaseReference.child("Users").child("Students").child(user!!.uid).child("Username").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                 name = p0.value.toString()
                helloname.text ="Welcome " + name
            }

        })




        //Testing Code , Need to be Scrapped after testing. Buttons will NOT be the options for the PDF's.
        // We will have a ListView for that
        se = findViewById(R.id.SE)
        spcc = findViewById(R.id.SPCC)
        tcs = findViewById(R.id.TCS)
        css = findViewById(R.id.CSS)

    }
    fun pdfwrite(pdf:String,value:String){
        var rand : String
        rand = Random.nextInt(100,10000).toString()
        databaseReference.child("Transactions").child("Transactions ID :- "+rand).child("name").setValue(name)
        databaseReference.child("Transactions").child("Transactions ID :- "+rand).child("pdf").setValue(pdf)
        databaseReference.child("Transactions").child("Transactions ID :- "+rand).child("value").setValue(value)

    }

    fun onClick(v: View){
        when (v.id){
            R.id.SE ->pdfwrite("SE","50 RS")
            R.id.SPCC ->pdfwrite("SPCC","70 RS")
            R.id.TCS ->pdfwrite("TCS","100 RS")
            R.id.CSS ->pdfwrite("CSS","90 RS")
            else -> {

            }
        }
        Toast.makeText(applicationContext,"PDF Bought Successfully",Toast.LENGTH_SHORT).show()
    }
}
