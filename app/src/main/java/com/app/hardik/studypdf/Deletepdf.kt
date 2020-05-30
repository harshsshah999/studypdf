package com.app.hardik.studypdf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.database.*

class Deletepdf : AppCompatActivity() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    var parentname : String = ""
    var path : String = ""
    var enc: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deletepdf)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference()
        val arrayList: ArrayList<String> = ArrayList()
        var arrayAdapter: ArrayAdapter<String?>
        val listView = findViewById<ListView>(R.id.pdflist)
        arrayAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_list_item_1,
            arrayList as List<String?>?
        )
        databaseReference.child("Links").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val pdfname = p0.key.toString()
                arrayList.add(pdfname)

                listView.adapter = arrayAdapter
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                arrayAdapter.notifyDataSetChanged()
            }

        })

        listView.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                databaseReference.child("Links").child(arrayList.get(position)).setValue(null)
                databaseReference.child("Links").child(arrayList.get(position)).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {


                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        parentname = p0.child("parent").value.toString()
                        enc = p0.child("encryptname").value.toString()
                        Log.i("logingParent",parentname)
                        Log.i("logingEncryptname",enc)
                        databaseReference.child("SubjectPath").addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                path = p0.child(parentname).value.toString()
                                Log.i("logingPath",path)
                                val finalpath = path+"/"+enc
                                Log.i("logingFpath",finalpath)
                                databaseReference.child("Uploads").child(finalpath).setValue(null)
                            }

                        })

                    }

                })

            }

        })


    }
}