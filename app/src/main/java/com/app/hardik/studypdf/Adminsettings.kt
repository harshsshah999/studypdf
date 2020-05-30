package com.app.hardik.studypdf

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class Adminsettings : AppCompatActivity() {

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    val keyMap = hashMapOf<String,String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adminsettings)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference()
        val arrayList: ArrayList<String> = ArrayList()
        var arrayAdapter: ArrayAdapter<String?>
        val listView = findViewById<ListView>(R.id.userslist)
        databaseReference.child("Users").child("Students").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val user = p0.child("Username").value.toString()
                keyMap.put(p0.child("Username").value.toString(),p0.key.toString())
                arrayList.add(user)
                arrayAdapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_list_item_1,
                    arrayList as List<String?>?
                )
                listView.adapter = arrayAdapter
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
            listView.setOnItemClickListener(object: AdapterView.OnItemClickListener{
                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val uid = keyMap.get(arrayList.get(position))!!
                    databaseReference.child("blockedUsers").addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.hasChild(uid)){
                                databaseReference.child("blockedUsers").child(uid).setValue(null)
                                Toast.makeText(this@Adminsettings,"User is Successfully Unblocked",Toast.LENGTH_LONG).show()
                            }
                            else{
                                databaseReference.child("blockedUsers").child(uid).setValue(true)
                                Toast.makeText(this@Adminsettings,"User is Successfully Blocked",Toast.LENGTH_LONG).show()

                            }
                        }

                    })
                }

            })

    }
}