package com.app.hardik.studypdf

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.resources.TextAppearance
import org.w3c.dom.Text
import java.io.File


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
            .onDrawAll { canvas, pageWidth, pageHeight, displayedPage ->
                val paint = Paint()

                paint.style = Paint.Style.FILL
                paint.color = Color.GRAY
                paint.textSize = pageWidth*4/100
                paint.textAlign = Paint.Align.CENTER

                canvas.drawText("Some Text", pageWidth/3, 50F, paint)
            }
            .load()


    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }


}