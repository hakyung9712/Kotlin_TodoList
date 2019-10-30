package org.techtown.kotlin_todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_update.*
import kotlinx.android.synthetic.main.memo.*
import kotlinx.android.synthetic.main.memo_item.*
import org.techtown.kotlin_todolist.Room.Memo
import org.techtown.kotlin_todolist.Room.MemoDB
import org.techtown.kotlin_todolist.Room.MemoDao
import org.techtown.kotlin_todolist.Room.UserDao
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UpdateActivity:AppCompatActivity() {
    private val memoList=ArrayList<Memo>()
    var cal= Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

 /*       val dateSetListener=object: DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR,year)
                cal.set(Calendar.MONTH,monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                updateDateInView()
            }
        }
        val timeSetListener=object: TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(view: TimePicker?, hourOfday: Int, minute: Int) {
                cal.set(Calendar.HOUR_OF_DAY,hourOfday)
                cal.set(Calendar.MINUTE,minute)
                updateTimeInView()
            }
        }

       update_date.setOnClickListener {
            DatePickerDialog(this@UpdateActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        update_time.setOnClickListener {
            TimePickerDialog(this@UpdateActivity,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),true).show()
        }*/

        var memo_id=getIntent().getIntExtra("memo_id",0)

        //값 받아오기
        val title=intent.getStringExtra("title");
        val content=intent.getStringExtra("content");
        val year=intent.getIntExtra("year",0);
        val month=intent.getIntExtra("month",0);
        val monthOfyear=month+1
        val day=intent.getIntExtra("day",0);
        val hour=intent.getIntExtra("hour",0);
        val minute=intent.getIntExtra("minute",0);

        update_title.setText(title)
        update_content.setText(content)
        Log.d("Memo","날짜 "+year+month+day+" 시간"+hour+minute)

        update_date.setText(year.toString()+"/"+monthOfyear.toString()+"/"+day.toString())
        update_time.setText(hour.toString()+":"+minute.toString())

        update_ok_btn.setOnClickListener {
            val title=update_title.text.toString()
            val content=update_content.text.toString()
            //삭제 후 추가하기
            deleteMemo(title,content,memo_id)
            saveMemo(title,content)
        }

    }
    private fun updateDateInView(){
        val myFormat="MM/dd/yyyy"
        val sdf= SimpleDateFormat(myFormat,Locale.US)
        memo_memo_date!!.text=sdf.format(cal.getTime())
    }
    //선택한 시간으로 setText
    private fun updateTimeInView(){
        memo_memo_time!!.text= SimpleDateFormat("HH:mm").format(cal.time)
    }

    //원래 메모 삭제
    private fun deleteMemo(title:String,content:String,id:Int){
        val memoDb: MemoDB?=MemoDB.getInstance(this)
        val memoDao: MemoDao =memoDb!!.memoDao
        val memo=Memo(title,content,cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE))
        Thread{memoDb!!.memoDao.delete(id)}.start()
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