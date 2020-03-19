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
        val leftslidebtn = findViewById<Button>(R.id.button3)
        val rightslidebtn = findViewById<Button>(R.id.button5)
        lateinit var animl : Animation
        lateinit var animr : Animation

        //Create Animation
        animl = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.to_left)
        animr = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.to_right)

        //function for changing images and texts
        fun leftanim(){
            //Trigger Left Animation
            welcomeimg.startAnimation(animl)
            welcometxt.startAnimation(animl)
            return
        }
        fun rightanim(){
            //Trigger Right Animation
            welcomeimg.startAnimation(animr)
            welcometxt.startAnimation(animr)
            return
        }
        fun change(){
            if (seek.progress == 0){
                welcomeimg.setImageResource(R.drawable.ic_launcher_background)
                welcometxt.setText("Introtxt1")
                return
            }
            else{
                if (seek.progress == 1){
                    welcomeimg.setImageResource(R.drawable.appintro_indicator_dot_grey)
                    welcometxt.setText("Introtxt2")
                    return
                }
                else{
                    if  (seek.progress == 2) {
                        welcomeimg.setImageResource(R.drawable.appintro_indicator_dot_white)
                        welcometxt.setText("Introtxt3")
                        return
                    }
                    else{
                        welcomeimg.setImageResource(R.drawable.ic_appintro_done_white)
                        welcometxt.setText("Introtxt4")
                        return
                    }
                } }
        }

        //function for next slide & previous slide
        fun nextslide(){
            if(seek.progress != seek.max){
            seek.progress = seek.progress + 1
            leftanim()
            change()}
            if (seek.progress==seek.max){nextbtn.text = "Done" }
         }
        fun previousslide(){
            if(seek.progress != seek.min){
            seek.progress = seek.progress - 1
            rightanim()
            change()
            }
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
        rightslidebtn.setOnClickListener {previousslide()}
        leftslidebtn.setOnClickListener {nextslide()}
    }
}
