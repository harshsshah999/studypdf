package com.app.hardik.studypdf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var dbref: DatabaseReference
lateinit var downloadno : TextView
lateinit var revenueno : TextView

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbref = FirebaseDatabase.getInstance().getReference()
        //Copy return statement and store it in new variable "View"
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        var customers = ArrayList<String>()
        var adapter = ArrayAdapter<String>(context,
            android.R.layout.simple_list_item_1,
            customers
            )
       var transactionData = view.findViewById<ListView>(R.id.transactions)
        //downloadno = view.findViewById(R.id.downloadno)
       // transactionData.addHeaderView(downloadno)
        var userno = view!!.findViewById<TextView>(R.id.userno)
        transactionData.adapter = adapter
        dbref.child("Transactions").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                var values = p0.getValue(Transactions::class.java).toString()
                Log.i("datasnap",p0.toString())
                customers.add(values)
                adapter.notifyDataSetChanged()

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
        dbref.child("Users").child("Students").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    userno.text = p0.childrenCount.toString()
                }
                else {
                    userno.text = "NA"
                }
            }

        })
        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
