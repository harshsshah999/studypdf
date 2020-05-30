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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Continuation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class Uploadsection : AppCompatActivity() {
    lateinit var choose: Button
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var firebaseStorage: StorageReference
    lateinit var databaseReference: DatabaseReference
    lateinit var dbrefr : DatabaseReference
    val PICK_PDF_CODE = 2342
    lateinit var textViewStatus: TextView
    lateinit var progressBar: ProgressBar
    lateinit var editTextFilename: EditText
    lateinit var filetitle: String
    lateinit var path: String
    lateinit var upload: Button
    lateinit var price: EditText
    lateinit var priceval: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploadsection)

        choose = findViewById(R.id.chooser)

        textViewStatus = findViewById(R.id.textViewStatus)
        progressBar = findViewById(R.id.progressbarupload)
        price = findViewById(R.id.price)
        upload = findViewById(R.id.upload)
        editTextFilename = findViewById(R.id.editTextFileName)
        filetitle = intent.getStringExtra("name")
        path = intent.getStringExtra("path") + "/"
        editTextFilename.setText(filetitle)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Uploads/" + path)
        dbrefr = firebaseDatabase.getReference()
        firebaseStorage = FirebaseStorage.getInstance().getReference()
        upload.isEnabled = false
    }


    fun choosefile(v: View) {
        getPDF()
    }



    private fun getPDF() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            /*val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)*/
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            );
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
                upload.isEnabled = true
                Toast.makeText(this,"Type Appropriate Name for your File!",Toast.LENGTH_SHORT).show()
                upload.setOnClickListener{
                    if(editTextFilename.text.isNullOrBlank() || price.text.isNullOrBlank()){
                        Toast.makeText(this,"You can't leave fields empty!",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        priceval = price.text.toString().trim()
                    uploadFile2(data.data)
                    }
                }
            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /* private fun uploadFile(data: Uri) {
        progressBar.visibility = View.VISIBLE
        val filename = editTextFilename.getText().toString()
        val sRef: StorageReference =
            firebaseStorage.child(path + filename + ".pdf")
        val uploadTask = sRef.putFile(data)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                progressBar.visibility = View.GONE
                textViewStatus.text = "File Uploaded Successfully"
                 val upload = Upload(
                     filename,
                     taskSnapshot.storage.getDownloadUrl().result.toString()
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

    } */

    private fun uploadFile2 (data: Uri){
        progressBar.visibility = View.VISIBLE
        val filename = editTextFilename.getText().toString().trim()
        val sRef: StorageReference =
            firebaseStorage.child(path + System.currentTimeMillis() + ".pdf")
        var uploadTask = sRef.putFile(data)
        uploadTask
            .continueWithTask{task ->
            if(!task.isSuccessful){
                task.exception?.let {
                    throw it
                }
            }
            sRef.downloadUrl
        }
            .addOnCompleteListener{task ->
            if(task.isSuccessful){
                val downloadUri = task.result
                val upload = Upload(
                    filename,
                    downloadUri.toString(),priceval
                )
                textViewStatus.text = "File Uploaded Successfully"
                progressBar.visibility = View.GONE
                val key = databaseReference.push().getKey()!!
                databaseReference.child(key).setValue(upload)
                dbrefr.child("Links").child(filename).child("url").setValue(downloadUri.toString())
                dbrefr.child("Links").child(filename).child("encryptname").setValue(key)
            }
            else{
                Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
            }
        }
        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress = ((100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount).toInt()
            textViewStatus.setText("$progress % Uploading...")
        }
    }
}


