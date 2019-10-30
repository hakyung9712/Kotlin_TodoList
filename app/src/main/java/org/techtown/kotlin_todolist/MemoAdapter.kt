package org.techtown.kotlin_todolist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.memo_item.view.*
import org.techtown.kotlin_todolist.Room.Memo

class MemoAdapter(val context: Context, val memos:List<Memo>):
    RecyclerView.Adapter<MemoAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view=LayoutInflater.from(context).inflate(R.layout.memo_item,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return memos.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        memos[position].let { memo->
            with(holder){
                memo_title?.text=memo.title
            }
        }

        holder.memo_layout.setOnClickListener{
            //MemoActivity는 되는데 Update는 안됨
            val intent= Intent(context,UpdateActivity::class.java)
            intent.putExtra("memo_id",memos.get(position).id)
            intent.putExtra("title",memos[position].title)
            intent.putExtra("content",memos[position].content)
            intent.putExtra("year",memos[position].year)
            intent.putExtra("month",memos[position].month)
            intent.putExtra("day",memos[position].day)
            intent.putExtra("hour",memos[position].hour)
            intent.putExtra("minute",memos[position].minute)

            (context as Activity).startActivityForResult(intent,1000)
        }
    }


    inner class Holder(view: View) :RecyclerView.ViewHolder(view){
        val memo_title=view.memo_title
        val memo_layout:LinearLayout=view.item_layout
        //fun bind(memo:Memo){
         //   memo_title?.text=memo.title
        //}

    }

    fun getMemoId(position:Int):Int{
        val id=memos[position].id
        return id
    }


}