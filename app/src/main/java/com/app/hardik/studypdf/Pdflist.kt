package com.app.hardik.studypdf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.multilevelview.MultiLevelRecyclerView
import com.multilevelview.models.RecyclerViewItem

class Pdflist : AppCompatActivity() {
    lateinit var dbrefer: DatabaseReference
    lateinit var db: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var myAdapter: Pdfadapter
    lateinit var PdfList: MutableList<Item>
    lateinit var pdflistname: TextView
    var path : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdflist)
        pdflistname = findViewById(R.id.tittle)
        db = FirebaseDatabase.getInstance()
        dbrefer = db.getReference()
        val name = intent.getStringExtra("name")
        path = intent.getStringExtra("path")
        val multiLevelRecyclerView = findViewById(R.id.rv_list) as MultiLevelRecyclerView
        multiLevelRecyclerView.layoutManager = LinearLayoutManager(this)
        //Default Element to include
        PdfList = ArrayList<RecyclerViewItem>() as MutableList<Item>
        var item = Item(0)
        item.setText("All Avaliable Pdfs for Subject $name!")
        item.setSecondText("Click to buy them")
        PdfList.add(item)
        readlist()
        myAdapter = Pdfadapter(applicationContext,PdfList,multiLevelRecyclerView)
        multiLevelRecyclerView.adapter = myAdapter
        multiLevelRecyclerView.setOnItemClick { view, item, position ->
            if(PdfList.get(position).getSecondText().equals("Click to buy them")){

            }
            else {
                val intent = Intent(this,Paymentpage::class.java)
                intent.putExtra("pdfname",PdfList.get(position).text.toString())
                var priceof = PdfList.get(position).getSecondText().toString()
                var subprice = priceof.substringBefore(" ","error").trim()
                //Log.i("subprice",subprice)
                intent.putExtra("price",subprice)
                startActivity(intent)
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
                PdfList.add(item)
                myAdapter.notifyDataSetChanged()
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })

    }
}
