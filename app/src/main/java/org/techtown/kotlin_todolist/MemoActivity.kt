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
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.memo.*
import org.techtown.kotlin_todolist.Room.Memo
import org.techtown.kotlin_todolist.Room.MemoDB
import org.techtown.kotlin_todolist.Room.MemoDao
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MemoActivity : AppCompatActivity() {
    val memo=Memo()
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
            saveMemo(title,content)

            //시간, 분 전달(알람)
            val hour=updateTimeInView().substring(0,2).toInt()
            val min=updateTimeInView().substring(3).toInt()
            ReminderWorker.runAt(hour,min)

            //날짜 전달까지는 안됨
            val month=updateDateInView().substring(0,2).toInt()
            val day=updateDateInView().substring(3,5).toInt()
            val year=updateDateInView().substring(6).toInt()
            //ReminderWorker.runAt(year,month,day,hour,min)



            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

    }
    //선택한 날짜로 setText
    private fun updateDateInView():String{
        val myFormat="MM/dd/yyyy"
        val sdf=SimpleDateFormat(myFormat,Locale.US)
        memo_memo_date!!.text=sdf.format(cal.getTime())

        return sdf.format(cal.getTime())
    }
    //선택한 시간으로 setText
    private fun updateTimeInView():String{
        memo_memo_time!!.text=SimpleDateFormat("HH:mm").format(cal.time)

        return SimpleDateFormat("HH:mm").format(cal.time)
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
    private fun getDelayTime():Long{
        val c=Calendar.getInstance()

        val hour=cal.get(Calendar.HOUR)
        val min=cal.get(Calendar.MINUTE)
        val sec=cal.get(Calendar.SECOND)

        val dataFormat=SimpleDateFormat("HH:mm:ss",Locale.KOREA)
        val todayDate=Date(System.currentTimeMillis())
        val currentTime=dataFormat.format(todayDate)

        val d1=dataFormat.parse("${memo.hour}:${memo.minute}:00")
        val d2=dataFormat.parse(currentTime)
        val diff=d1.time-d2.time

        return diff
    }


}