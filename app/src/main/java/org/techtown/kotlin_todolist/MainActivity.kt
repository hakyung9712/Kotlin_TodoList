package org.techtown.kotlin_todolist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.techtown.kotlin_todolist.Room.UserDB

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_logout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            val auto = getSharedPreferences("auto", Context.MODE_PRIVATE)
            val editor = auto.edit()
            editor.clear()
            editor.commit()
            Toast.makeText(this, "로그아웃합니다.", Toast.LENGTH_LONG).show()
            finish()
        }
        main_add.setOnClickListener {
            val intent = Intent(this, MemoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }
}
