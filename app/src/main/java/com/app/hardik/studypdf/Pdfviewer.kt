package com.app.hardik.studypdf

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.android.synthetic.main.activity_pdfviewer.*

class Pdfviewer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfviewer)
        val pdfView = findViewById<PDFView>(R.id.pdfView)
        pdfView.fromUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/studypdf-6de89.appspot.com/o/StreamList%2FEngineering%2FComputer%20Science%2FSEM%201%2FAC%2FAC.pdf?alt=media&token=853f0f49-0bcd-437a-bbec-ba28462e5b54"))
            .load()

    }
}