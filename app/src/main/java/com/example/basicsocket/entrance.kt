package com.example.basicsocket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class entrance : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)
        supportActionBar?.hide()
        val name:EditText=findViewById(R.id.name)
        val room:EditText=findViewById(R.id.room)
        val join:Button=findViewById(R.id.join)
        join.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("name",name.text.toString())
            intent.putExtra("room",room.text.toString())
            startActivity(intent)
        }
    }
}