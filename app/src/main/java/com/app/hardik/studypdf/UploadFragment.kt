package com.app.hardik.studypdf


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.multilevelview.MultiLevelRecyclerView
import com.multilevelview.models.RecyclerViewItem
import kotlinx.android.synthetic.main.activity_uploadsection.view.*
import kotlinx.android.synthetic.main.fragment_upload.*
import kotlinx.android.synthetic.main.fragment_upload.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

object activate {
    var isClickable : Int = 1
    var title : String = ""
    var currentpath: String = ""
}
/**
 * A simple [Fragment] subclass.
 * Use the [UploadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var dbrefer: DatabaseReference
    lateinit var db: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var myAdapter: UploadAdapter
    lateinit var StreamList: MutableList<Item>
    lateinit var gotoupload: Button

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
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_upload, container, false)
        val multiLevelRecyclerView =
                view.findViewById(R.id.rv_list) as MultiLevelRecyclerView
        multiLevelRecyclerView.layoutManager = LinearLayoutManager(this.context)
        //Default Element to include
        StreamList = ArrayList<RecyclerViewItem>() as MutableList<Item>
        gotoupload = view.findViewById<Button>(R.id.gotoupload);
        var item = Item(0)
        if(activate.isClickable == 1) {
            item.setText("Choose Category of File!")
            item.setSecondText("Long Click on Subject to Select it. (You can Only Select Subject!)")
            view.gotoupload.setActivated(false)
            gotoupload.setEnabled(false)

        }
        else if (activate.isClickable == 0){
            item.setText("You have Selected "+activate.title+" .")
            item.setSecondText("Long Click Here to Cancel and select other subject")
            view.gotoupload.setActivated(true)
            gotoupload.setEnabled(true)

        }
        StreamList.add(item)
        db= FirebaseDatabase.getInstance()
        dbrefer=db.getReference()
        readlist()
        myAdapter = UploadAdapter(view.context, StreamList, multiLevelRecyclerView,UploadFragment())
        multiLevelRecyclerView.adapter = myAdapter
        val pullToRefresh: SwipeRefreshLayout = view.findViewById(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            reload()
            pullToRefresh.isRefreshing = false
        }
        view.refresh.setOnClickListener{
            reload()
        }
        view.gotoupload.setOnClickListener{
            if(view.gotoupload.isActivated) {
                val intent = Intent(getActivity(), Uploadsection::class.java)
                intent.putExtra("name",activate.title)
                intent.putExtra("path",activate.currentpath)
                getActivity()!!.startActivity(intent)
            }
        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UploadFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UploadFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun readlist (){
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

}
