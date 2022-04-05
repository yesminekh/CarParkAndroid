package com.example.carypark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.carypark.login.Login

class intro02 : AppCompatActivity() {
    private var btnnext: Button?=null
    private var btnskip: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro02)
        btnnext = findViewById(R.id.btnnext)
        btnskip = findViewById(R.id.btnskip)
        btnnext!!.setOnClickListener {
            val mainIntent = Intent(this, intro03::class.java)
            startActivity(mainIntent)
            finish()
        }
        btnskip!!.setOnClickListener {
            val mainIntent = Intent(this, Login::class.java)
            startActivity(mainIntent)
            finish()
        }

    }
}