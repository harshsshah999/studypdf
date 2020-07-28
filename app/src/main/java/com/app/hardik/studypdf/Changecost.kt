package com.app.hardik.studypdf

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class Changecost : AppCompatActivity() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    var parentname : String = ""
    var path : String = ""
    var enc: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changecost)

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
        databaseReference.child("Links").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val pdfname = p0.key.toString()
                val price = p0.child("price").value.toString()
                arrayList.add(pdfname+"."+" \n "+"₹ "+price)

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
                val alert: AlertDialog.Builder = AlertDialog.Builder(this@Changecost)
                var edittext = EditText(this@Changecost)
                alert.setMessage("Enter New Cost")
                alert.setView(edittext)
                alert.setPositiveButton(
                    "Change"
                ) { dialog, whichButton ->
                    val newcost = edittext.text.toString()
                    val itemname = arrayList.get(position).substringBefore("."," ")
                    databaseReference.child("Links").child(itemname)
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {


                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                parentname = p0.child("parent").value.toString()
                                enc = p0.child("encryptname").value.toString()
                               // Log.i("logingParent", parentname)
                               // Log.i("logingEncryptname", enc)
                                databaseReference.child("SubjectPath")
                                    .addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {

                                        }

                                        override fun onDataChange(p0: DataSnapshot) {
                                            path = p0.child(parentname).value.toString()
                                            Log.i("logingPath", path)
                                            val finalpath = path + "/" + enc
                                            Log.i("logingFpath", finalpath)
                                            databaseReference.child("Uploads").child(finalpath).child("price")
                                                .setValue(newcost)
                                        }

                                    })

                            }

                        })
                    databaseReference.child("Links").child(itemname).child("price").setValue(newcost)
                    Toast.makeText(
                        applicationContext,
                        "PDF Price changed successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                    startActivity(intent)
                }
                alert.setNegativeButton(
                    "Cancel"
                ) { dialog, whichButton ->
                    // Canceled.
                }
                alert.show()
            }

        })


    }
}