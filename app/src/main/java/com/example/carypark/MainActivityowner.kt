package com.example.carypark

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.carypark.fragements.*
import com.example.carypark.login.Login

class MainActivityowner : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_activityowner)
        supportFragmentManager.beginTransaction().replace(R.id.frame, Map()).commit()
        title="welcome ParkOwner"

        val homeBtn = findViewById<ImageView>(R.id.home_icon)
        val historyBtn = findViewById<ImageView>(R.id.history_icon)
        val addBtn = findViewById<ImageView>(R.id.park_icon)
        val settingsBtn = findViewById<ImageView>(R.id.settings_icon)
        val profileBtn = findViewById<ImageView>(R.id.profile_icon)




        val toolbar  =  findViewById<Toolbar>(R.id.toolbar) ;
        setSupportActionBar(toolbar)

        homeBtn.setOnClickListener(clickListener)
        historyBtn.setOnClickListener(clickListener)
        addBtn.setOnClickListener(clickListener)
        settingsBtn.setOnClickListener(clickListener)
        profileBtn.setOnClickListener(clickListener)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        lateinit var mSharedPref: SharedPreferences
        when(item.itemId){

            R.id.logout ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Logout")
                builder.setMessage("logout")
                builder.setPositiveButton("Yes"){ dialogInterface, which ->
                    getSharedPreferences("UserPref", MODE_PRIVATE).edit().clear().apply()
                    val mainIntent = Intent(this, Login::class.java)
                    startActivity(mainIntent)
                    finish()
                }
                builder.setNegativeButton("No"){dialogInterface, which ->
                    dialogInterface.dismiss()
                }
                builder.create().show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private val clickListener : View.OnClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.home_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, Map()).commit()
            }
            R.id.history_icon -> {

                getSupportFragmentManager().beginTransaction().replace(R.id.frame, IncommingRides()).commit()
            }
            R.id.settings_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, Settings()).commit()
            }
            R.id.park_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, listParkings()).commit()
            }


            R.id.profile_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, Profile()).commit()
            }



        }
    }

}
