package com.app.hardik.studypdf

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.multilevelview.MultiLevelRecyclerView
import com.multilevelview.models.RecyclerViewItem
import kotlinx.android.synthetic.main.content_list.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
object Apple {
    var updateClicked : Int = 0
}
//Ritesh
class ListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var dbrefer: DatabaseReference
    lateinit var db: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var myAdapter: MyAdapter
    lateinit var StreamList: MutableList<Item>
    var t = 0


    //
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
        //Copy return statement and store it in new variable "View"
        var view = inflater.inflate(R.layout.content_list, container, false)
        val multiLevelRecyclerView =
            view.findViewById(R.id.rv_list) as MultiLevelRecyclerView
        multiLevelRecyclerView.layoutManager = LinearLayoutManager(this.context)
        //Default Element to include
        StreamList = ArrayList<RecyclerViewItem>() as MutableList<Item>
        var item = Item(0)
        item.setText("All Available Categories")
        item.setSecondText("Swipe Down to Refresh List")
        if(Apple.updateClicked == 1){
            item.setText("New +")
            item.setSecondText("If You are adding New List Or for custom add Click here")
        }
        else{
            item.setText("All Available Categories")
            item.setSecondText("Swipe Down to Refresh List")
        }
        StreamList.add(item)
        db= FirebaseDatabase.getInstance()
        dbrefer=db.getReference()

        firstread()
        myAdapter = MyAdapter(view.context, StreamList, multiLevelRecyclerView)
        multiLevelRecyclerView.adapter = myAdapter

        view.add.setOnClickListener{


            if(t==0) {
                Apple.updateClicked = 1
                t=1
                Toast.makeText(view.context,"ADD Mode is ON. Long Click on parent element to add",Toast.LENGTH_LONG).show()
            }
            else if(t==1){
                Apple.updateClicked = 0
                t=0
                Toast.makeText(view.context,"ADD Mode is OFF",Toast.LENGTH_LONG).show()
            }
            else if (t==2){
                Toast.makeText(view.context,"Disable DELETE Mode First",Toast.LENGTH_LONG).show()
            }
            reload()

        }
        view.delete.setOnClickListener{
            if(t==0){
                Apple.updateClicked = 2
                t=2
                Toast.makeText(view.context,"DELETE Mode is ON. Long Click on element to delete it!",Toast.LENGTH_LONG).show()
            }
            else if(t==1){
                Toast.makeText(view.context,"Disable ADD Mode First",Toast.LENGTH_LONG).show()
            }
            else if (t==2){
                Apple.updateClicked = 0
                t=0
                Toast.makeText(view.context,"DELETE Mode is OFF",Toast.LENGTH_LONG).show()

            }
            reload()
        }
        val pullToRefresh: SwipeRefreshLayout = view.findViewById(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
                        reload()
                      pullToRefresh.isRefreshing = false
        }
        return view

    }

    fun firstread () {
        dbrefer.child("StreamList").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context,p0.toString(),Toast.LENGTH_LONG).show()
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
         * @return A new instance of fragment ListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}

/* Value Event
DataSnapshot { key = StreamList, value = {Engineering={Computer Science={SEM 6={CSS=CSS, SE=SE, DWM=DWM, SPCC=SPCC, ML=ML}, SEM 3={AOA=AOA}, SEM 1={AC=AC, BEE=BEE, EM=EM, AM=AM, AP=AP}, SEM 2={AC2=AC2, AP2=AP2, SPA=SPA, AM2=AM2, ED=ED}}, Electrical Engineering={SEM 3={EX3=EX3, EX2=EX2, EX1=EX1}, SEM 4={EE=EE}, SEM 1={EE1=EE1, EE3=EE3, EE2=EE2}, SEM 2={EXE2=EXE2, EXE3=EXE3, EXE1=EXE1}}, Information Technology={SEM 3={IT=IT}, SEM 1={CS=CS, CE=CE, IT=IT}, SEM 2={CS1=CS1, IT1=IT1, CE1=CE1}}}} }
DataSnapshot { key = Engineering, value = {Computer Science={SEM 6={CSS=CSS, SE=SE, DWM=DWM, SPCC=SPCC, ML=ML}, SEM 3={AOA=AOA}, SEM 1={AC=AC, BEE=BEE, EM=EM, AM=AM, AP=AP}, SEM 2={AC2=AC2, AP2=AP2, SPA=SPA, AM2=AM2, ED=ED}}, Electrical Engineering={SEM 3={EX3=EX3, EX2=EX2, EX1=EX1}, SEM 4={EE=EE}, SEM 1={EE1=EE1, EE3=EE3, EE2=EE2}, SEM 2={EXE2=EXE2, EXE3=EXE3, EXE1=EXE1}}, Information Technology={SEM 3={IT=IT}, SEM 1={CS=CS, CE=CE, IT=IT}, SEM 2={CS1=CS1, IT1=IT1, CE1=CE1}}} }

        for ( i in 0..depth-1 ) {
            var item = Item(levelNumber)
            item.setText(String.format(Locale.ENGLISH, title, i));
            item.setSecondText(String.format(Locale.ENGLISH, title.toLowerCase(), i));
            if(depth % 2 == 0){
                item.addChildren(recursivePopulateFakeData(levelNumber + 1, depth/2) as MutableList<RecyclerViewItem>?)
            }
            //Log.i("itemt",item.toString())
            itemList.add(item)
            //Log.i("itemlistt",itemList.toString())
        }
        return itemList;
    } */