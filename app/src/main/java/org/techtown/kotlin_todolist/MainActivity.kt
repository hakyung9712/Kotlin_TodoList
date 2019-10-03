package org.techtown.kotlin_todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.techtown.kotlin_todolist.Room.UserDB

class MainActivity : AppCompatActivity() {
    private var userDb: UserDB?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userDb=UserDB.getInstance(this)

        val r= Runnable {  }

        val thread=Thread(r)
        thread.start()
    }
}
