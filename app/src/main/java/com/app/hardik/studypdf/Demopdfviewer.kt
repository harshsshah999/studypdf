package com.app.hardik.studypdf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

class Demopdfviewer : AppCompatActivity() {
    lateinit var pdfView: PDFView
    lateinit var full: Button
    lateinit var pdfname: String
    lateinit var price: String
    lateinit var url: String
    lateinit var key: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_demopdfviewer)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        pdfView = findViewById(R.id.demopdf)
        full = findViewById(R.id.fullbutton)
         pdfname = intent.getStringExtra("pdfname2")
        price = intent.getStringExtra("price")
        url = intent.getStringExtra("url")
        key = intent.getStringExtra("key")
        Log.i("pdfname2 list",pdfname)
        val path = intent.getStringExtra("path")
        val yourFile = File(path)
        Log.i("demo",yourFile.toString())
        pdfView.fromFile(yourFile)
            .pages(0)
            .load()



    }
    fun onClick (v: View){
        val intent = Intent(this,Paymentpage::class.java)
        intent.putExtra("pdfname",pdfname)
        intent.putExtra("price",price)
        intent.putExtra("url",url)
        intent.putExtra("key",key)
        startActivity(intent)
    }
}