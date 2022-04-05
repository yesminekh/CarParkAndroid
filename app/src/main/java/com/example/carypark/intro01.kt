package com.example.carypark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.carypark.login.Login

class intro01 : AppCompatActivity() {
    private var btnnext:Button?=null
    private var btnskip:Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro01)
        btnnext = findViewById(R.id.btnNext)
        btnskip = findViewById(R.id.btnSkip)
        btnnext!!.setOnClickListener {
            val mainIntent = Intent(this, intro02::class.java)
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