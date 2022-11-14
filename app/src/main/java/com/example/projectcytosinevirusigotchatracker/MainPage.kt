package com.example.projectcytosinevirusigotchatracker

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.widget.ImageButton
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainPage : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var buttonsFragment: ButtonsFragment
    lateinit var accountFragment: AccountFragment

    private var GPSForeground: GPSService? = null

    lateinit var StartGPS : ImageButton
    lateinit var StopGPS : ImageButton

    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as GPSService.LocalBinder
            GPSForeground= binder.service
           // foregroundOnlyLocationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
           GPSForeground = null
            //foregroundOnlyLocationServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val BottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)


            homeFragment = HomeFragment()
            supportFragmentManager.beginTransaction().replace(R.id.Frag, homeFragment).commit()

        GPSForeground?.subscribeToLocationUpdates()

        val serviceIntent = Intent(this, GPSService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)

      //  val ButtonView : View = LayoutInflater.inflate(this, null)

       // StartGPS = findViewById(R.id.StartGPS)
       // StopGPS = findViewById(R.id.StopGPS)
        /*

        StartGPS.setOnClickListener({
            var GPS: Intent = Intent(this, GPSService::class.java).apply { }
           startService(GPS)


        })

         */


        BottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.bottom_nav_home -> {
                    homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.Frag, homeFragment).commit()

                }

                R.id.bottom_nav_button -> {
                    buttonsFragment = ButtonsFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.Frag, buttonsFragment).commit()

                }

                R.id.bottom_nav_account -> {
                    accountFragment = AccountFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.Frag, accountFragment).commit()

                }
            }
            true

        }
    }


}