package com.example.basicsocket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class entrance : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)
        supportActionBar?.hide()
        val name:TextInputEditText=findViewById(R.id.name)
        val namelayout:TextInputLayout=findViewById(R.id.nameLayout)
        val room:TextInputEditText=findViewById(R.id.room)
        val roomlayout:TextInputLayout=findViewById(R.id.roomlayout)
        val join:Button=findViewById(R.id.join)
        name.doOnTextChanged { text, start, before, count ->
            if (text?.length!! >15) {
                namelayout.error="Character limit exceeded"
            }
            else if (text.length==0){
                namelayout.error="This field should be filled"
            }
            else{
                namelayout.error=null
            }
        }
        room.doOnTextChanged { text, start, before, count ->
            if (text?.length!=6) {
                roomlayout.error="Enter a 6 Character code!!!"
            }
            else{
                roomlayout.error=null
            }
        }
        join.setOnClickListener {
               if(name.length() in 1..14 &&room.length()==6) {
                   val intent = Intent(this, MainActivity::class.java)
                   intent.putExtra("name", name.text.toString())
                   intent.putExtra("room", room.text.toString())
                   startActivity(intent)
               }
            else{
                Toast.makeText(this,"Make Sure Errors are cleared",Toast.LENGTH_LONG).show()
               }
        }
    }
}