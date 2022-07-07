package com.example.basicsocket

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {
    val gson: Gson = Gson()
    lateinit var msocket: Socket
    lateinit var re: RecyclerView
    lateinit var input: EditText
    lateinit var name: String
    lateinit var room: String
    lateinit var data: String
    private val IMAGE_PICK_CODE = 1000
    var list = arrayListOf<message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var cl = 0
        val actionbar: androidx.appcompat.app.ActionBar? = supportActionBar
        try {
            msocket = IO.socket("https://socket-chat45.herokuapp.com/")
          //  msocket =IO.socket("https://192.168.31.238:3000")
        } catch (e: URISyntaxException) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
        }
        name = intent.getStringExtra("name").toString()
        room = intent.getStringExtra("room").toString()
        actionbar!!.title = "ROOM NO : $room"
        msocket.connect()
        msocket.on(Socket.EVENT_CONNECT) {
            val userdata = userdata(name, room)
            val jsondata2 = gson.toJson(userdata)
            msocket.emit("user", jsondata2)

        }
        msocket.on("connection") {
            runOnUiThread {
                data = it[0].toString()
                add_message(message("ME", data, 3))
            }
        }

        re = findViewById(R.id.recycler)
        input = findViewById(R.id.editText)
        val btn: FloatingActionButton = findViewById(R.id.btn)
        input.doOnTextChanged { text, start, before, count ->
            if (text?.trim()?.isEmpty() == true) {
                btn.setImageResource(R.drawable.ic_photo_size_select)
                cl = 0
            } else {
                cl = 1
                btn.setImageResource(R.drawable.ic_send)
            }
        }
        re.layoutManager = LinearLayoutManager(this)

        btn.setOnClickListener {
            if (cl == 1) {
                val senddata = senddata(input.text.toString())
                val jsondata = gson.toJson(senddata)
                msocket.emit("chat message", jsondata)
                add_message(message("YOU ", input.text.toString(), 2))
                input.hidekeyboard()
                input.text.clear()
            } else {
                pickImagefromGallery()
            }
        }
        msocket.on("chat message") {
            runOnUiThread {
                val mychat: recieve = gson.fromJson(it[0].toString(), recieve::class.java)

                add_message(message(mychat.username, mychat.chat, 1))
            }
        }
        msocket.on("chat image") {
            runOnUiThread {
                val mychat: recieve = gson.fromJson(it[0].toString(), recieve::class.java)
                add_message(message(mychat.username, mychat.chat, 5))
            }
        }
        msocket.on("dis") {
            runOnUiThread {
                add_message(message("1", it[0].toString() + " has left the chat ", 3))
            }
        }

    }

    private fun pickImagefromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    override fun onBackPressed() {
        msocket.emit("dis")
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val send_img_data = senddata(data?.dataString.toString())
            val img_jsondata = gson.toJson(send_img_data)
            msocket.emit("chat message", img_jsondata)
            add_message(message("g", data?.dataString.toString(), 4))
        }
    }


    private fun add_message(message: message) {
        list.add(message)
        re.adapter = adapter(this, list)
        re.scrollToPosition(list.size - 1)
    }


    fun View.hidekeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }


}
