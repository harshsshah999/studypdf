package com.app.hardik.studypdf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*

class Introscreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introscreen)

        //Variable Declaration
        val welcomeimg = findViewById<ImageView>(R.id.imageView2)
        val welcometxt = findViewById<TextView>(R.id.textView2)
        val seek = findViewById<SeekBar>(R.id.seekBar)
        val nextbtn = findViewById<Button>(R.id.next)
        val leftslidebtn = findViewById<Button>(R.id.rightslide)
        val rightslidebtn = findViewById<Button>(R.id.leftslide)
        val skip = findViewById<Button>(R.id.skip)
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
                nextbtn.text = "Next"
                return
            }
            else{
                if (seek.progress == 1){
                    welcomeimg.setImageResource(R.drawable.appintro_indicator_dot_grey)
                    welcometxt.setText("Introtxt2")
                    nextbtn.text = "Next"
                    nextbtn.text = "Next"
                    return
                }
                else{
                    if  (seek.progress == 2) {
                        welcomeimg.setImageResource(R.drawable.appintro_indicator_dot_white)
                        welcometxt.setText("Introtxt3")
                        nextbtn.text = "Next"
                        return
                    }
                    else{
                        welcomeimg.setImageResource(R.drawable.ic_appintro_done_white)
                        welcometxt.setText("Introtxt4")
                        nextbtn.text = "Done"
                        return
                    }
                } }
        }

        //function for next slide & previous slide
        fun nextslide(){
            if(seek.progress != seek.max)
            {
                seek.progress = seek.progress + 1
                nextbtn.text = "Next"
                leftanim()
                change()
            }
            if (seek.progress==seek.max)
            {
                nextbtn.text = "Done"
            }
        }
        fun previousslide(){
            if(seek.progress != seek.min){
                seek.progress = seek.progress - 1
                nextbtn.text = "Next"
                rightanim()
                change()
            }
        }

        //Skip button function
        fun gotologin (){
            startActivity(Intent(this,LoginPage::class.java))
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
        nextbtn.setOnClickListener {
            if(nextbtn.text=="Done")
            {
                gotologin()
            }
            nextslide()
        }
        rightslidebtn.setOnClickListener {
            previousslide()
        }
        leftslidebtn.setOnClickListener {
            nextslide()
        }
        skip.setOnClickListener{
            gotologin()
        }
    }
}
