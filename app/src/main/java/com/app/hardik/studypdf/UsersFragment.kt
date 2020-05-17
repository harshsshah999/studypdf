package com.app.hardik.studypdf

import android.app.ActionBar
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.fragment_users.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UsersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UsersFragment : Fragment() {
    lateinit var create : Button

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
        val view: View = inflater!!.inflate(R.layout.fragment_users, container, false)
        create=view.findViewById(R.id.createcard)
        // Set a click listener for button widget
        create.setOnClickListener{

            // Initialize a new CardView instance
            val card_view = CardView(view?.context)

            // Initialize a new LayoutParams instance, CardView width and height
            val layoutParams = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, // CardView width
                ActionBar.LayoutParams.WRAP_CONTENT // CardView height
            )

            // Set bottom margin for card view
            layoutParams.bottomMargin = 50

            // Set the card view layout params
            card_view.layoutParams = layoutParams

            // Set the card view corner radius
            card_view.radius = 12F

            // Set the card view content padding
            card_view.setContentPadding(25,25,25,25)

            // Set the card view background color
            card_view.setCardBackgroundColor(Color.LTGRAY)

            // Set card view elevation
            card_view.cardElevation = 8F

            // Set card view maximum elevation
            card_view.maxCardElevation = 12F

            // Set a click listener for card view
            card_view.setOnClickListener{
                Toast.makeText(
                    view.context,
                    "Card clicked.",
                    Toast.LENGTH_SHORT).show()
            }

            // Add an ImageView to the CardView
            card_view.addView(generateImageView())
            card_view.addView(generatename())
            card_view.addView(generatetitle())
            card_view.addView(generatedate())
            card_view.addView(generateprice())

            // Finally, add the CardView in root layout
            root2.addView(card_view)
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
         * @return A new instance of fragment UsersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UsersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    // Custom method to generate an image view
    private fun generateImageView(): ImageView {
        val imageView = ImageView(view?.context)
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, 250)
        imageView.layoutParams = params
        imageView.setImageResource(R.drawable.ic_launcher_background)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return imageView
    }
    //Custom method to generate an text view
    private fun generatename(): TextView {
        val textView = TextView(view?.context)
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT )
        params.leftMargin = 300
        textView.layoutParams = params
        textView.text = "Abhishek"
        textView.textSize = 22F
        return textView
    }
    private fun generatetitle(): TextView {
        val textView = TextView(view?.context)
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT )
        params.setMargins(300,70,0,0)
        textView.layoutParams = params
        textView.text = "SPCC.pdf"
        textView.textSize = 20F
        return textView
    }
    private fun generatedate(): TextView {
        val textView = TextView(view?.context)
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT )
        params.setMargins(300,210,0,0)
        textView.layoutParams = params
        textView.text = "20/2/2020"
        textView.textSize = 20F
        return textView
    }
    private fun generateprice(): TextView {
        val textView = TextView(view?.context)
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT )
        params.setMargins(300,140,0,0)
        textView.layoutParams = params
        textView.text = "Price : 40 Rs"
        textView.textSize = 20F
        return textView
    }

}
