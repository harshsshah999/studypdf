package com.app.hardik.studypdf
//Splashscreen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


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
        val isLoggedin = getSharedPreferences("Loggedin", Context.MODE_PRIVATE).getBoolean("isLoggedin", false)
        val flag = getSharedPreferences("Loggedin",Context.MODE_PRIVATE).getString("Flag","Null")


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

                    if (isLoggedin){
                        if (isOnline(this@MainActivity)){
                            if (flag == "1"){
                                startActivity(Intent(this@MainActivity,userdashboard::class.java))
                            }
                            else if (flag == "2"){
                                startActivity(Intent(this@MainActivity,Admindashboard::class.java))
                            }
                            else{
                                startActivity(Intent(this@MainActivity,LoginPage::class.java))
                            }

                        }
                        else{
                            if (flag == "1"){
                                startActivity(Intent(this@MainActivity,userdashboard::class.java))
                            }
                            else if (flag == "2"){
                                startActivity(Intent(this@MainActivity,Admindashboard::class.java))
                            }
                            else{
                                startActivity(Intent(this@MainActivity,LoginPage::class.java))
                            }
                            Toast.makeText(this@MainActivity,"You are offline",Toast.LENGTH_LONG).show()
                        }

                    }
                    else{
                        startActivity(Intent(this@MainActivity, LoginPage::class.java))
                        finish()
                    }
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

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}
