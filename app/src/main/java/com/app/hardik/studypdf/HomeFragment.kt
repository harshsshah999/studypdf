package com.app.hardik.studypdf

import android.app.ActionBar
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_users.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

var menu = mutableListOf<Any>()         //List of usernames from Users
var email = mutableListOf<Any>()        //List of emails from Users
var costs = mutableListOf<Any>()        //List of costs from transaction
var finalcost = mutableListOf<Any>()    //List of total cost per usernames
var costname = mutableListOf<Any>()     //List of usernames from transaction
var pdf = mutableListOf<Any>()          //List of pdf's from transaction
var date = mutableListOf<Any>()          //List of date's from transaction
var position: Int = 0                   //positon of card clicked
var Total_revenue: Int = 0

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    var count: Int = 0
    var last: Int = 0
    var count2: Int = 0
    var last2: Int = 0
    var index:Int = 0
    var costnamefirst: Int = 0
    var costnamelast: Int = 0
    var cost: Int = 0

    lateinit var names: String
    lateinit var pdfs: String
    lateinit var revenue: String
    lateinit var dates: String

    var sorteddates = listOf<Any>()

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
        val view = inflater.inflate(R.layout.fragment_home,container,false)
        if (menu.isEmpty()){
            Log.i("Empty","True")
        }
        else{
            menu.clear()
            email.clear()
            costs.clear()
            finalcost.clear()
            costname.clear()
            pdf.clear()
            date.clear()
            position = 0
            Total_revenue = 0
            Log.i("Empty","False")
        }
        //Fetching Details from database to mutable lists
        var Done :Boolean = false           //Used to know if fetching from dg is over or not
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference()
        databaseReference.child("Users").child("Students").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                //Saves usernames in menu list
                p0.children.mapNotNullTo(menu) {
                    it.child("Username").value
                }
                //Saves emails in email list
                p0.children.mapNotNullTo(email) {
                    it.child("Email").value
                }
                if(p0.exists()){
                    userno.text = p0.childrenCount.toString()
                }
                else {
                    userno.text = "NA"
                }
            }
        })
        databaseReference.child("Transactions").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.mapNotNullTo(costs) {
                    it.child("value").value
                }
                p0.children.mapNotNullTo(costname) {
                    it.child("name").value
                }
                p0.children.mapNotNullTo(pdf) {
                    it.child("pdf").value
                }
                p0.children.mapNotNullTo(date) {
                    it.child("date").value
                }
                if(p0.exists()){
                    downloadno.text = p0.childrenCount.toString()
                }
                else {
                    downloadno.text = "NA"
                }

                // Calculation of total cost
                count = menu.indexOfFirst { true }
                last  = menu.indexOfLast { true }
                while (count <= last){
                    costnamefirst = costname.indexOfFirst { true }
                    costnamelast = costname.indexOfLast { true }
                    while ( costnamefirst <= costnamelast ){
                        if (costname.get(costnamefirst) == menu.get(count))
                        {
                            cost += Integer.parseInt(costs.get(costnamefirst).toString())
                        }
                        costnamefirst += 1
                    }
                    Total_revenue = Total_revenue + cost
                    finalcost.add(count,cost)
                    cost = 0
                    count += 1
                }
                revenueno.text = Total_revenue.toString() + " ₹"


                //Sorting lists associated with transactions according to dates
                val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

                 sorteddates = date.sortedByDescending {
                    LocalDate.parse(it as CharSequence?, dateTimeFormatter)
                }
                count = sorteddates.indexOfFirst { true }
                last  = sorteddates.indexOfLast { true }
                index = date.indexOfFirst { true }
                while (count <= last){
                    count2 = date.indexOfFirst { true }
                    last2 = date.indexOfLast { true }
                    while (count2 <= last2){
                        if (sorteddates[count] == date[count2]){

                            var temp = date[count2]
                            date[count2]=date[count]
                            date[count]=temp

                            temp = costname[count2]
                            costname[count2]= costname[count]
                            costname[count] = temp

                            temp = pdf[count2]
                            pdf[count2]= pdf[count]
                            pdf[count] = temp

                            temp = costs[count2]
                            costs[count2]= costs[count]
                            costs[count] = temp

                            count2 += 1
                        }
                        else{count2 += 1}
                    }
                    count += 1
                }

               Done = true
            }
        })

        //Creating cards
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                //Call your function here
                if (Done == true){
                    Log.d("Menu after done", menu.toString())
                    count = costname.indexOfFirst { true }
                    last  = costname.indexOfLast { true }
                    while (count <= last){
                        if(count == 0 || date[count] != date[count-1]){
                            dates = date.get(count).toString()
                            newdatecard()           //Dates
                        }
                        names = costname.get(count).toString()
                        pdfs = pdf.get(count).toString()
                        revenue = costs.get(count).toString()
                        new()                       //User information
                        count = count + 1
                    }

                }
                else {
                    handler.postDelayed(this, 1000)//1 sec delay
                }
            }
        }, 0)
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

    fun newdatecard() {
        // Initialize a new CardView instance
        val card_view = CardView(view!!.context)

        // Initialize a new LayoutParams instance, CardView width and height
        val layoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT, // CardView width
            ActionBar.LayoutParams.WRAP_CONTENT // CardView height
        )

        // Set bottom margin for card view
        layoutParams.bottomMargin = 15

        layoutParams.leftMargin = 20

        // Set the card view layout params
        card_view.layoutParams = layoutParams

        // Set the card view corner radius
        card_view.radius = 50F

        // Set the card view content padding
        card_view.setContentPadding(25, 25, 25, 25)

        // Set the card view background color
        card_view.setCardBackgroundColor(Color.rgb(254,137,1))

        // Set card view elevation
        card_view.cardElevation = 20F

        // Set card view maximum elevation
        card_view.maxCardElevation = 12F

        // Set a click listener for card view
        card_view.setOnClickListener {
           // position = card_view.id
            // startActivity(Intent(view!!.context,ExpandCard::class.java))
        }

        // Add an TextView to the CardView
        card_view.addView(generatedate())

        //Add horizontal line
        val line = View(view!!.context)
        val layoutParamsline = ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT, // View width
            2 // View height
        )
        layoutParamsline.bottomMargin = 20
        line.layoutParams = layoutParamsline
        line.setBackgroundColor(Color.GRAY)
        transactions.addView(line)

        // Finally, add the CardView in root layout
        transactions.addView(card_view)
    }

    fun new() {
        // Initialize a new CardView instance
        val card_view = CardView(view!!.context)
        card_view.id = count

        // Initialize a new LayoutParams instance, CardView width and height
        val layoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT, // CardView width
            ActionBar.LayoutParams.WRAP_CONTENT // CardView height
        )

        // Set bottom margin for card view
        layoutParams.bottomMargin = 20

        // Set the card view layout params
        card_view.layoutParams = layoutParams

        // Set the card view corner radius
        card_view.radius = 12F

        // Set the card view content padding
        card_view.setContentPadding(25, 25, 25, 25)

        // Set the card view background color
        card_view.setCardBackgroundColor(Color.rgb(98,0,238))

        // Set card view elevation
        card_view.cardElevation = 20F

        // Set card view maximum elevation
        card_view.maxCardElevation = 12F

        // Set a click listener for card view
        card_view.setOnClickListener {
            position = card_view.id
           // startActivity(Intent(view!!.context,ExpandCard::class.java))
        }

        // Add an TextView to the CardView
        card_view.addView(generatename())
        card_view.addView(generatepdf())
        card_view.addView(generateprice())

        // Finally, add the CardView in root layout
        transactions.addView(card_view)
    }

    //Custom method to generate an text view
    private fun generatename(): TextView {
        val textView = TextView(view?.context)
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT )
        params.leftMargin = 20
        textView.layoutParams = params
        textView.setTextColor(Color.WHITE)
        textView.text = "Name : " + names
        textView.textSize = 22F
        return textView
    }
    private fun generatepdf(): TextView {
        val textView = TextView(view?.context)
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT )
        params.setMargins(20,80,0,0)
        textView.layoutParams = params
        textView.setTextColor(Color.WHITE)
        textView.text = "PDF : " + pdfs
        textView.textSize = 18F
        return textView
    }
    private fun generateprice(): TextView {
        val textView = TextView(view?.context)
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT )
        params.setMargins(20,150,0,0)
        textView.layoutParams = params
        textView.setTextColor(Color.WHITE)
        textView.text = "Cost : " + revenue
        textView.textSize = 20F
        return textView
    }
    private fun generatedate(): TextView {
        val textView = TextView(view?.context)
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT )
        textView.layoutParams = params
        textView.setTextColor(Color.WHITE)
        textView.text = dates
        textView.textSize = 15F
        return textView
    }

}
