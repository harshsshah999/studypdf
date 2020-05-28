package com.app.hardik.studypdf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Pdflist : AppCompatActivity() {
    lateinit var dbrefer: DatabaseReference
    lateinit var db: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var myAdapter: UserAdapter
    lateinit var StreamList: MutableList<Item>
    lateinit var pdflist: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        pdflist = findViewById(R.id.tittle)
        db = FirebaseDatabase.getInstance()
        dbrefer = db.getReference()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdflist)

    }
}
