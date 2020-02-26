package com.app.hardik.studypdf
//Splashscreen

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

            //Global declaration of variables
            lateinit var anim : Animation
            lateinit var logo : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            //Assigning ID's of Views to Variables
            logo = findViewById(R.id.logo)

            //Create Animation
            anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in)

            //Animation Class functions
            anim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }
            override fun onAnimationEnd(animation: Animation) {
                startActivity(Intent(this@MainActivity, userscreen::class.java))
                // userscreen.class is the activity to go after showing the splash screen.
            }
            override fun onAnimationRepeat(animation: Animation) {
            }
        })

            //Trigger Animation
            logo.startAnimation(anim)
    }
}
