package com.app.hardik.studypdf

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class Uploadsection : AppCompatActivity() {
lateinit var choose : Button
lateinit var upload : Button
lateinit var firebaseDatabase: FirebaseDatabase
lateinit var firebaseAuth: FirebaseAuth
lateinit var firebaseStorage: StorageReference
lateinit var databaseReference: DatabaseReference
val PICK_PDF_CODE = 2342
lateinit var textViewStatus: TextView
lateinit var progressBar: ProgressBar
lateinit var filename: String
lateinit var editTextFilename: EditText
lateinit var filetitle: String
lateinit var path: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploadsection)

        choose = findViewById(R.id.chooser)
        upload = findViewById(R.id.upload)

        textViewStatus = findViewById(R.id.textViewStatus)
        progressBar = findViewById(R.id.progressbarupload)
        editTextFilename = findViewById(R.id.editTextFileName)
        filetitle = intent.getStringExtra("name")
        path = intent.getStringExtra("path")+"/"
        editTextFilename.setText(filetitle)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference(path)
        firebaseStorage = FirebaseStorage.getInstance().getReference()

    }


    fun choosefile (v: View){
        getPDF()
    }
    fun uploadfiles (v:View){


    }
    private fun getPDF (){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
            return
        }

        //creating an intent for file chooser
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_CODE)

    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            //if a file is selected
            if (data.data != null) {
                //uploading the file
                uploadFile(data.data)
            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show()
            }
        }
    }

        private fun uploadFile(data:Uri){
            progressBar.visibility = View.VISIBLE
            val sRef: StorageReference =
                firebaseStorage.child(path + System.currentTimeMillis() + ".pdf")
            sRef.putFile(data)
                .addOnSuccessListener { taskSnapshot ->
                    progressBar.visibility = View.GONE
                    textViewStatus.text = "File Uploaded Successfully"
                    val upload = Upload(
                        editTextFilename.getText().toString(),
                        taskSnapshot.storage.getDownloadUrl().toString()
                    )
                    databaseReference.child(databaseReference.push().getKey()!!).setValue(upload)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        applicationContext,
                        exception.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                    textViewStatus.setText("$progress % Uploading...");
                }
        }
}
