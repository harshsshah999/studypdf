package com.app.hardik.studypdf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import kotlinx.android.synthetic.main.activity_userscreen.*

class userscreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userscreen)

        //Variable Declaration
        val welcomeimg = findViewById<ImageView>(R.id.imageView2)
        val welcometxt = findViewById<TextView>(R.id.textView2)
        val seek = findViewById<SeekBar>(R.id.seekBar)
        val nextbtn = findViewById<Button>(R.id.button2)
        var done :Int = 0
        lateinit var anim : Animation

        //Create Animation
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.to_left)

        //function for changing images and texts
        fun change(){
            if (seek.progress == 1){
                //Trigger Animation
                welcomeimg.startAnimation(anim)
                welcometxt.startAnimation(anim)
                welcomeimg.setImageResource(R.drawable.ic_launcher_background)
                welcometxt.setText("Introtxt1")
            }
            else{
                if (seek.progress == 2){
                    welcomeimg.startAnimation(anim)
                    welcometxt.startAnimation(anim)
                    welcomeimg.setImageResource(R.drawable.appintro_indicator_dot_grey)
                    welcometxt.setText("Introtxt2")
                }
                else{
                    welcomeimg.startAnimation(anim)
                    welcometxt.startAnimation(anim)
                    welcomeimg.setImageResource(R.drawable.appintro_indicator_dot_white)
                    welcometxt.setText("Introtxt3")
                } }
        }

        //function for nextslide
        fun nextslide(){
            //if (done == 1){intent}
            seek.progress = seek.progress + 1
            if (seek.progress==seek.max){nextbtn.text = "Done"
                done =1
            }
            change()
         }

        //seekbar class creation
        seek?.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                 //To change body of created functions use File | Settings | File Templates.
                change()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
               //To change body of created functions use File | Settings | File Templates.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //To change body of created functions use File | Settings | File Templates.
            }
        })

        //onClick listeners for images,texts,buttons
        nextbtn.setOnClickListener { nextslide() }
        welcomeimg.setOnClickListener { nextslide() }
        welcometxt.setOnClickListener { nextslide() }

    }
}
