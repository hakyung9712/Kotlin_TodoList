package org.techtown.kotlin_todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.techtown.kotlin_todolist.Room.Memo

class MemoAdapter(val context: Context, val memos:List<Memo>):
    RecyclerView.Adapter<MemoAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(memos[position])
    }

    override fun getItemCount(): Int {
        return memos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view=LayoutInflater.from(context).inflate(R.layout.memo_item,parent,false)
        return Holder(view)
    }


    inner class Holder(itemView: View?) :RecyclerView.ViewHolder(itemView!!){
        val memo_title=itemView?.findViewById<TextView>(R.id.memo_title)
        //val memo_content=itemView?.findViewById<TextView>(R.id.memo_content)

        fun bind(memo:Memo){
            memo_title?.text=memo.title
            //memo_content?.text=memo.content
        }

    }


}