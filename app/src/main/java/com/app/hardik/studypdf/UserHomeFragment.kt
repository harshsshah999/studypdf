package com.app.hardik.studypdf

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.multilevelview.MultiLevelRecyclerView
import com.multilevelview.models.RecyclerViewItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserHomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var dbrefer: DatabaseReference
    lateinit var db: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var myAdapter: UserAdapter
    lateinit var StreamList: MutableList<Item>
    lateinit var welcome: TextView

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
        val view = inflater.inflate(R.layout.fragment_user_home, container, false)
        welcome = view.findViewById(R.id.welcometext)
        auth = FirebaseAuth.getInstance()
        db= FirebaseDatabase.getInstance()
        dbrefer=db.getReference()
        val user = auth.currentUser
        dbrefer.child("Auth").child("AllUsers").child(user!!.uid).child("Username").addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                welcome.text = "Welcome "+p0.value.toString()
            }

        })

        val multiLevelRecyclerView =
            view.findViewById(R.id.rv_list) as MultiLevelRecyclerView
        multiLevelRecyclerView.layoutManager = LinearLayoutManager(this.context)
        //Default Element to include
        StreamList = ArrayList<RecyclerViewItem>() as MutableList<Item>
        var item = Item(0)
        item.setText("Feel Free to Buy")
        StreamList.add(item)

        readlist()
        myAdapter = UserAdapter(view.context, StreamList, multiLevelRecyclerView)
        multiLevelRecyclerView.adapter = myAdapter
        val pullToRefresh: SwipeRefreshLayout = view.findViewById(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            reload()
            pullToRefresh.isRefreshing = false
        }
        // Inflate the layout for this fragment
        return view
    }
    fun readlist(){
        dbrefer.child("StreamList").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context,p0.toString(), Toast.LENGTH_LONG).show()
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val DeptList = ArrayList<RecyclerViewItem>() as MutableList<Item>
                val stream = Item(0)
                stream.setText(p0.key.toString())
                StreamList.add(stream)
                for (i in p0.children) {
                    val dept = Item(1)
                    dept.setText(i.key.toString())
                    DeptList.add(dept)
                    stream.addChildren(DeptList as MutableList<RecyclerViewItem>)
                    val Semlist = ArrayList<RecyclerViewItem>() as MutableList<Item>
                    for (j in i.children) {
                        val sem = Item(2)
                        sem.setText(j.key.toString())
                        Semlist.add(sem)
                        dept.addChildren(Semlist as MutableList<RecyclerViewItem>)
                        val Sublist = ArrayList<RecyclerViewItem>() as MutableList<Item>
                        for (k in j.children){
                            val sub = Item(3)
                            sub.setText(k.key.toString())
                            Sublist.add(sub)
                            sem.addChildren(Sublist as MutableList<RecyclerViewItem>)
                        }
                    }
                }
                myAdapter.notifyDataSetChanged()

            }
            override fun onChildRemoved(p0: DataSnapshot) {
                reload()
            }
        })
    }
    fun reload () {
        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false)
        }
        ft.detach(this).attach(this).commit()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
