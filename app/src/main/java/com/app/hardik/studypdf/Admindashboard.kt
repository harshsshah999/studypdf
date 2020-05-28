package com.app.hardik.studypdf

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView



class Admindashboard : AppCompatActivity() {
lateinit var bottomNavigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admindashboard)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
        bottomNavigation.checkItem(R.id.navigation_home)
        openFragment(HomeFragment.newInstance("", ""));

    }
    internal fun BottomNavigationView.checkItem(actionId: Int) {
        menu.findItem(actionId)?.isChecked = true
    }

    fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
   var navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener? =
       object : BottomNavigationView.OnNavigationItemSelectedListener {
          override fun onNavigationItemSelected(item: MenuItem): Boolean {
               when (item.getItemId()) {
                   R.id.navigation_settings -> {
                       openFragment(SettingFragment.newInstance("", ""))
                       return true
                   }
                   R.id.navigation_upload -> {
                       openFragment(UploadFragment.newInstance("", ""))
                       return true
                   }
                   R.id.navigation_home -> {
                       openFragment(HomeFragment.newInstance("", ""))

                       return true
                   }
                   R.id.navigation_list -> {
                       openFragment(ListFragment.newInstance("", ""))
                       return true
                   }
                   R.id.navigation_users -> {
                       openFragment(UsersFragment.newInstance("", ""))
                       return true
                   }
               }
               return false
           }
       }



}
