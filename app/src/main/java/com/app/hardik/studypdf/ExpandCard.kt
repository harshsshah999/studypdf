package com.app.hardik.studypdf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

class ExpandCard : AppCompatActivity() {
    var costnamefirst: Int = 0
    var costnamelast: Int = 0

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expand_card)
        val Username = findViewById<TextView>(R.id.Expand_Username)
        Username.text = menu.get(position).toString()
        val Revenue = findViewById<TextView>(R.id.Revenue)
        Revenue.text = "Total Revenue: " + finalcost.get(position).toString() + " Rs"


            val list: MutableList<String> = ArrayList()

        costnamefirst = costname.indexOfFirst { true }
        costnamelast = costname.indexOfLast { true }
            var pdfname: String
            var pdfcost: String
            var count: Int = 0
        while ( costnamefirst <= costnamelast ){
            if (costname.get(costnamefirst) == Username.text)
            {
                pdfname = pdf.get(costnamefirst).toString()
                pdfcost = costs.get(costnamefirst).toString()
                list.add(count,"PDF: "+pdfname+"   Cost: "+pdfcost+" Rs")
                count += 1
            }
            costnamefirst += 1
        }
            Log.d("List",list.toString())
            // use arrayadapter and define an array
            val arrayAdapter: ArrayAdapter<*>
            // access the listView from xml file
            val pdflist = findViewById<ListView>(R.id.PDF_LIST)
            arrayAdapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list)
            pdflist.adapter = arrayAdapter

        }
}
