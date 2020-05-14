package com.app.hardik.studypdf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.multilevelview.MultiLevelRecyclerView
import com.multilevelview.models.RecyclerViewItem
import com.app.hardik.studypdf.Item
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
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
        // Inflate the layout for this fragment
        //Copy return statement and store it in new variable "View"

        var view = inflater.inflate(R.layout.fragment_list, container, false)
        val multiLevelRecyclerView =
            view.findViewById(R.id.rv_list) as MultiLevelRecyclerView
        multiLevelRecyclerView.layoutManager = LinearLayoutManager(this.context)

       // (IGNORE THIS) val itemList  = recursivePopulateFakeData(0, 12) as MutableList<Item>

        //Level 0
        var itemList  = ArrayList<RecyclerViewItem>() as MutableList<Item>
        var item1 = Item(0)
        item1.setText("Engineering")
        var item2 = Item(0)
        item2.setText("Medical")

        //Level 1
        var subitemList  = ArrayList<RecyclerViewItem>() as MutableList<Item>
        var subitem1 = Item(1)
        subitem1.setText("Computer Engineering")
        var subitem2 = Item(1)
        subitem2.setText("Electrical Engineering")
        var subitem3 = Item(1)
        subitem3.setText("Mechanical Engineering")
        subitemList.add(subitem1)
        subitemList.add(subitem2)
        subitemList.add(subitem3)

        //Level 2
        var subsubitemList  = ArrayList<RecyclerViewItem>() as MutableList<Item>
        var subsubitem1 = Item(2)
        subsubitem1.setText("SEM 1")
        var subsubitem2 = Item(2)
        subsubitem2.setText("SEM 4")
        var subsubitem3 = Item(2)
        subsubitem3.setText("SEM 6")
        subsubitemList.add(subsubitem1)
        subsubitemList.add(subsubitem2)
        subsubitemList.add(subsubitem3)

        //Level 3
        var subsubsubitemList  = ArrayList<RecyclerViewItem>() as MutableList<Item>
        var subsubsubitem1 = Item(3)
        subsubsubitem1.setText("Applied Physics I")
        var subsubsubitem2 = Item(3)
        subsubsubitem2.setText("Applied Mathematics I")
        var subsubsubitem3 = Item(3)
        subsubsubitem3.setText("Engineering Mechanics")
        subsubsubitemList.add(subsubsubitem1)
        subsubsubitemList.add(subsubsubitem2)
        subsubsubitemList.add(subsubsubitem3)


        subsubitem1.addChildren(subsubsubitemList as MutableList<RecyclerViewItem>)
        subitem1.addChildren(subsubitemList as MutableList<RecyclerViewItem>)
        item1.addChildren(subitemList as MutableList<RecyclerViewItem>)
        itemList.add(item1)
        itemList.add(item2)

        val myAdapter = MyAdapter(view.context, itemList, multiLevelRecyclerView)

        multiLevelRecyclerView.adapter = myAdapter


        return view
    }

    //IGNORE THIS
    /*private fun recursivePopulateFakeData(
        levelNumber: Int,
        depth: Int
    ): List<*>? {
        var itemList = ArrayList<RecyclerViewItem>()
        var title = ""
        when (levelNumber){
            1 -> {
                title = "PQRST %d"
            }
            2 -> {
                title = "XYZ %d"
            }
            else -> {
                title = "ABCDE %d"
            }
        }
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

