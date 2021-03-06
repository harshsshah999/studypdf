package com.app.hardik.studypdf

import android.os.Bundle
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
        openFragment(HomeFragment.newInstance("", ""));

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
