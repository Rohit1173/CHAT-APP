package com.example.basicsocket

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {
    val gson: Gson = Gson()
    lateinit var msocket: Socket
    lateinit var re: RecyclerView
    lateinit var input: EditText
    lateinit var name: String
    lateinit var room:String
    lateinit var data: String
    var list = arrayListOf<message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        try {
            msocket = IO.socket("https://socket-chat45.herokuapp.com/")
        } catch (e: URISyntaxException) {

        }
        name = intent.getStringExtra("name").toString()
        room =intent.getStringExtra("room").toString()
        msocket.connect()
        msocket.on(Socket.EVENT_CONNECT){
            val userdata = userdata(name, room)
            val jsondata2 = gson.toJson(userdata)
            msocket.emit("user",jsondata2)

        }
        msocket.on("connection") {
            runOnUiThread {
                data = it[0].toString()
                add_message(message("ME", data,3))
            }
        }

        re = findViewById(R.id.recycler)
        input = findViewById(R.id.editText)
        val btn: Button = findViewById(R.id.btn)
        re.layoutManager = LinearLayoutManager(this)

        btn.setOnClickListener {
            val senddata =senddata(input.text.toString())
            val jsondata = gson.toJson(senddata)
            msocket.emit("chat message", jsondata)
            add_message(message("YOU ", input.text.toString(),2))
            input.hidekeyboard()
            input.text.clear()
        }
        msocket.on("chat message") {
            runOnUiThread {
                val mychat: recieve = gson.fromJson(it[0].toString(), recieve::class.java)

                add_message(message(mychat.username,mychat.chat,1))
            }
        }

    }



    private fun add_message(message: message) {
        list.add(message)
        re.adapter = adapter(this,list)
        re.scrollToPosition(list.size - 1)
    }

    fun View.hidekeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}
