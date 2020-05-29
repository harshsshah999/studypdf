package com.app.hardik.studypdf

import android.Manifest
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import java.io.*
import java.net.URL
import java.net.URLConnection


class Pdfviewer : AppCompatActivity() {

    lateinit var progressBar: ProgressBar
    lateinit var pdfView: PDFView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_pdfviewer)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        val path = intent.getStringExtra("path")
        progressBar = findViewById(R.id.pdfprogressBar)
        pdfView = findViewById<PDFView>(R.id.pdfView)
        val yourFile = File(path)
        Log.i("bingo",yourFile.toString())
        pdfView.fromFile(yourFile)
            .load()


    }

}