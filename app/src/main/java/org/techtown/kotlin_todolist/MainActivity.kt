package org.techtown.kotlin_todolist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.techtown.kotlin_todolist.Room.Memo
import org.techtown.kotlin_todolist.Room.MemoDB
import org.techtown.kotlin_todolist.Room.UserDB

class MainActivity : AppCompatActivity() {
    private var memoDb: MemoDB?=null
    private var memoList= listOf<Memo>()
    lateinit var mAdapter:MemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        memoDb= MemoDB.getInstance(this)
        mAdapter=MemoAdapter(this,memoList)

        //메모 추가한 것 보여주기
        val r=Runnable{
            try{
                memoList=memoDb?.memoDao?.findMemo()!!
                mAdapter=MemoAdapter(this,memoList)
                mAdapter.notifyDataSetChanged()

                memo_recyclerView.adapter=mAdapter
                memo_recyclerView.layoutManager=LinearLayoutManager(this)
                memo_recyclerView.setHasFixedSize(true)
            }catch (e:Exception){
                Log.d("tag","Error - $e")
            }
        }

        val thread=Thread(r)
        thread.start()




        //로그아웃 기능
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
            finish()
        }
    }
}
