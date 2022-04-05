package com.example.carypark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.carypark.login.Login

class intro03 : AppCompatActivity() {
    private var btngetstarted: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro03)
        btngetstarted = findViewById(R.id.btnstart)
        btngetstarted!!.setOnClickListener {
            val mainIntent = Intent(this, Login::class.java)
            startActivity(mainIntent)
            finish()
        }
    }
}