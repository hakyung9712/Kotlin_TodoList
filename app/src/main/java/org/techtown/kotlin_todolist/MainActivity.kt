package org.techtown.kotlin_todolist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.techtown.kotlin_todolist.Room.Memo
import org.techtown.kotlin_todolist.Room.MemoDB
import org.techtown.kotlin_todolist.Room.MemoDao
import org.techtown.kotlin_todolist.Room.UserDB
import java.util.*
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback as SimpleCallback1

class MainActivity : AppCompatActivity() {
    private var memoDb: MemoDB? = null
    private var memoList = listOf<Memo>()
    lateinit var mAdapter: MemoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        memoDb = MemoDB.getInstance(this)
        mAdapter = MemoAdapter(this, memoList)

        //메모 추가한 것 보여주기
        val r = Runnable {
            try {
                memoList = memoDb?.memoDao?.findMemo()!!
                mAdapter = MemoAdapter(this, memoList)
                mAdapter.notifyDataSetChanged()

                memo_recyclerView.adapter = mAdapter
                memo_recyclerView.layoutManager = LinearLayoutManager(this)
                memo_recyclerView.setHasFixedSize(true)
            } catch (e: Exception) {
                Log.d("tag", "Error - $e")
            }
        }

        val thread = Thread(r)
        thread.start()

        var itemTouchHelper = ItemTouchHelper(createItemTouchCallback())
        itemTouchHelper.attachToRecyclerView(memo_recyclerView)


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

        //메모추가 버튼 기능
        main_add.setOnClickListener {
            val intent = Intent(this, MemoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }

    //스와이프로 삭제처리
    private fun createItemTouchCallback(): ItemTouchHelper.SimpleCallback {
        var helper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                //여기서의 position은 recyclerview에서의 0,1,2,3.. 순서
                deleteMemo(mAdapter.getMemoId(position))
                memo_recyclerView.adapter!!.notifyItemRemoved(position)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
        }
        return helper
    }

    private fun deleteMemo(id:Int){
        val memoDb: MemoDB?=MemoDB.getInstance(this)
        val memoDao: MemoDao =memoDb!!.memoDao
        Thread{memoDb!!.memoDao.delete(id)}.start()
    }

}
