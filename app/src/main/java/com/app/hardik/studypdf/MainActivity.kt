package com.app.hardik.studypdf
//Splashscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

            //Global declaration of variables
            lateinit var anim : Animation
            lateinit var logo : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declaration of variable for first time run
        val isFirstRun =
            getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getBoolean("isFirstRun", true)

            //Assigning ID's of Views to Variables
            logo = findViewById(R.id.logo)

            //Create Animation
            anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in)

            //Animation Class functions
            anim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }
            override fun onAnimationEnd(animation: Animation) {

                //Shared Preference Used here
                if (isFirstRun) {
                    //Open acivity only once
                    startActivity(Intent(this@MainActivity, Introscreen::class.java))
                    finish()
                }
                //Else opens next activity
                else{
                    startActivity(Intent(this@MainActivity, LoginPage::class.java))
                    finish()
                }

                getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).commit()

            }
            override fun onAnimationRepeat(animation: Animation) {
            }
        })

            //Trigger Animation
            logo.startAnimation(anim)
    }
}
