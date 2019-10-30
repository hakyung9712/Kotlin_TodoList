package org.techtown.kotlin_todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.memo.*
import org.techtown.kotlin_todolist.Room.Memo
import org.techtown.kotlin_todolist.Room.MemoDB
import org.techtown.kotlin_todolist.Room.MemoDao
import java.text.SimpleDateFormat
import java.util.*


class MemoActivity : AppCompatActivity() {
    var cal= Calendar.getInstance()
    //val save=getSharedPreferences("memo", Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo)


        val dateSetListener=object:DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR,year)
                cal.set(Calendar.MONTH,monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                updateDateInView()
            }
        }
        val timeSetListener=object:TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(view: TimePicker?, hourOfday: Int, minute: Int) {
                cal.set(Calendar.HOUR_OF_DAY,hourOfday)
                cal.set(Calendar.MINUTE,minute)
                updateTimeInView()
            }
        }

        memo_memo_date.setOnClickListener {
            DatePickerDialog(this@MemoActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        memo_memo_time.setOnClickListener {
            TimePickerDialog(this@MemoActivity,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),true).show()
        }

        memo_ok_btn.setOnClickListener {
            val title=memo_memo_title.text.toString()
            val content=memo_memo_content.text.toString()
            saveMemo(title,content);
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

    }
    //선택한 날짜로 setText
    private fun updateDateInView(){
        val myFormat="MM/dd/yyyy"
        val sdf=SimpleDateFormat(myFormat,Locale.US)
        memo_memo_date!!.text=sdf.format(cal.getTime())
    }
    //선택한 시간으로 setText
    private fun updateTimeInView(){
        memo_memo_time!!.text=SimpleDateFormat("HH:mm").format(cal.time)
    }

    //메모 저장
    private fun saveMemo(title:String,content:String){
        val memoDb: MemoDB?=MemoDB.getInstance(this)
        val memoDao: MemoDao =memoDb!!.memoDao

        val memo=Memo(title,content,cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE))
        Thread{memoDb!!.memoDao.insert(memo)}.start()
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}