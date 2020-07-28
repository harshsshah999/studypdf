package com.app.hardik.studypdf

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.multilevelview.MultiLevelRecyclerView
import com.multilevelview.models.RecyclerViewItem
import java.io.File

class Pdflist : AppCompatActivity() {
    lateinit var dbrefer: DatabaseReference
    lateinit var db: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var myAdapter: Pdfadapter
    lateinit var PdfList: MutableList<Item>
    lateinit var pdflistname: TextView
    lateinit var storage: FirebaseStorage
    lateinit var progressDialog: ProgressDialog
    lateinit var storageRef: StorageReference
    var demointent = Intent()
    var path : String = ""
    var url : String = ""
    val urlMap = hashMapOf<String,String>()
    val keyMap = hashMapOf<String,String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdflist)
        pdflistname = findViewById(R.id.tittle)
        db = FirebaseDatabase.getInstance()
        dbrefer = db.getReference()
        val name = intent.getStringExtra("name")
        path = intent.getStringExtra("path")
        progressDialog = ProgressDialog(this)
        storage = Firebase.storage
        storageRef = storage.reference
        progressDialog.setTitle("Loading Preview...")
        progressDialog.setCanceledOnTouchOutside(false)
        demointent = Intent(this,Demopdfviewer::class.java)

        val multiLevelRecyclerView = findViewById(R.id.rv_list) as MultiLevelRecyclerView
        multiLevelRecyclerView.layoutManager = LinearLayoutManager(this)
        //Default Element to include
        PdfList = ArrayList<RecyclerViewItem>() as MutableList<Item>
        var item = Item(0)
        item.setText("All Avaliable Pdfs for Subject $name!")
        item.setSecondText("Click to Buy or Preview")
        PdfList.add(item)
        readlist()

        myAdapter = Pdfadapter(applicationContext,PdfList,multiLevelRecyclerView)
        multiLevelRecyclerView.adapter = myAdapter
        multiLevelRecyclerView.setOnItemClick { view, item, position ->
            if(PdfList.get(position).getSecondText().equals("Click to Buy or Preview")){

            }
            else {
                val builder = AlertDialog.Builder(this)
                //set title for alert dialog
                val pdf = PdfList.get(position).getText()+".pdf"
                builder.setTitle("Buying PDF "+pdf)
                //set message for alert dialog
                builder.setMessage("You can preview or buy this pdf")
                builder.setPositiveButton("Buy PDF"){
                        dialogInterface, which ->
                    val intent = Intent(this,Paymentpage::class.java)
                    intent.putExtra("pdfname",PdfList.get(position).text.toString())
                    var priceof = PdfList.get(position).getSecondText().toString()
                    var subprice = priceof.substringBefore(" ","error").trim()
                    //Log.i("subprice",subprice)
                    intent.putExtra("price",subprice)
                    intent.putExtra("url",urlMap.get(PdfList.get(position).text.toString()))
                    intent.putExtra("key",keyMap.get(PdfList.get(position).text.toString()))
                    startActivity(intent)
                }

                builder.setNeutralButton("Preview PDF"){dialogInterface , which ->
                    demointent.putExtra("pdfname2",PdfList.get(position).text.toString())
                    Log.i("pdfname2 list",PdfList.get(position).text.toString())
                    var priceof = PdfList.get(position).getSecondText().toString()
                    var subprice = priceof.substringBefore(" ","error").trim()
                    //Log.i("subprice",subprice)
                    demointent.putExtra("price",subprice)
                    demointent.putExtra("url",urlMap.get(PdfList.get(position).text.toString()))
                    demointent.putExtra("key",keyMap.get(PdfList.get(position).text.toString()))
                   url = urlMap.get(PdfList.get(position).text.toString())!!
                    downloadTask()
                }

                builder.setNegativeButton("Cancel"){dialogInterface, which ->
                    Toast.makeText(applicationContext,"Canceled",Toast.LENGTH_SHORT).show()
                }
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }

        }

    }

    fun readlist(){
        dbrefer.child("Uploads").child(path).addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                var item = Item(0)
                item.setText(p0.child("name").value.toString())
                item.setSecondText(p0.child("price").value.toString()+" \u20B9")
                urlMap.put(p0.child("name").value.toString(),p0.child("url").value.toString())
                keyMap.put(p0.child("name").value.toString(),p0.key.toString())
                PdfList.add(item)
                myAdapter.notifyDataSetChanged()
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })

    }
    fun downloadTask() {
        val httpsReference = storage.getReferenceFromUrl(
            url
        )
        val rootPath =
            File(Environment.getExternalStorageDirectory(), ".rha/"+keyMap.get(PdfList.get(position).text.toString()))
        val localFile = File(rootPath, "demo.pdf")

        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }
        httpsReference.getFile(localFile)
            .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot?> {
                Log.e("firebase ", ";local tem file created  created $localFile")
                //  updateDb(timestamp,localFile.toString(),position);
                progressDialog.dismiss()
                path = localFile.absolutePath
                Log.i("pathh",path)

                demointent.putExtra("path",path)

                startActivity(demointent)
            }).addOnFailureListener(OnFailureListener { exception ->
                Log.e(
                    "firebase ",
                    ";local tem file not created  created $exception"
                )
                Toast.makeText(this,"Error:- $exception",Toast.LENGTH_LONG).show()
            }).addOnProgressListener {
                progressDialog.show()
                val progress: Double =
                    100.0 * it.getBytesTransferred() / it.getTotalByteCount()
                progressDialog.setMessage(
                    "Loaded " +
                            progress.toInt() + "%"
                )
            }
    }
}
