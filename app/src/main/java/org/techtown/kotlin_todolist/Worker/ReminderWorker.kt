package org.techtown.kotlin_todolist

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.coroutineScope
import org.techtown.kotlin_todolist.MainActivity
import java.net.SocketException
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class ReminderWorker(appContext: Context,workerParams: WorkerParameters):CoroutineWorker(appContext,workerParams) {
    lateinit var notificationManager:NotificationManager
    lateinit var notificationChannel:NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId="org.techtown.kotlin_todolist"
    private val description="Test notification"

    companion object{
        private const val REMINDER_WORK_NAME="reminder"
        private const val PARAM_NAME="name"

        fun runAt(hour:Int,min:Int){
            val workManager=WorkManager.getInstance()

            val alarmTime= LocalTime.of(hour,min)
            var now=LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            //var now=LocalDateTime.of(year,month,day,hour,min)
            val nowTime=now.toLocalTime()

            if(nowTime==alarmTime ||nowTime.isAfter(alarmTime)){
                //now=now.plusDays(1)
            }
            now=now.withHour(alarmTime.hour).withMinute(alarmTime.minute)
            //now=now.withYear(now.year).withMonth(now.monthValue).withDayOfMonth(now.dayOfMonth).withHour(now.hour).withMinute(now.minute)

            val duration= Duration.between(LocalDateTime.now(),now)
            //val duration= Duration.between(now,now)


            val data=workDataOf(PARAM_NAME to "Timer 01")

            val workRequest= OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(duration.seconds, TimeUnit.SECONDS)
                .setInputData(data)
                .build()

            workManager.enqueueUniqueWork(REMINDER_WORK_NAME,ExistingWorkPolicy.REPLACE,workRequest)
        }

        fun cancel(){
            val workManager = WorkManager.getInstance()
            workManager.cancelUniqueWork(REMINDER_WORK_NAME)
        }
    }
    override suspend fun doWork(): Result= coroutineScope {
        val worker=this@ReminderWorker
        val context=applicationContext

        val name=inputData.getString(PARAM_NAME)
        var isScheduleNext=true

        try{
            //푸시 알람 오도록 설정
            notificationManager=applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationChannel= NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor= Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
            Log.d("Sample", "SimpleWorker Working...")
            val intent= Intent(applicationContext, MainActivity::class.java)
            val pendingIntent= PendingIntent.getActivity(applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)


            builder=
                Notification.Builder(applicationContext,channelId).setContentTitle("ToDo List").setContentText("할일을 확인해주세요").setSmallIcon(R.drawable.notification_icon_background).setContentIntent(pendingIntent).setAutoCancel(true)
            notificationManager.notify(1234,builder.build())
            Result.success()
        }
        catch (e:Exception){
            //3번 재시도
            if(runAttemptCount>3){
                return@coroutineScope Result.success()
            }
            //네트워크 문제
            when(e.cause) {
                is SocketException -> {
                    //Timber.e(e.toString(), e.message)
                    Log.d("time","에러"+e.message)
                    isScheduleNext = false
                    Result.retry()
                }
                else -> {
                    Log.d("time","에러2"+e)
                    //Timber.e(e)
                    Result.failure()
                }
            }
        }
        finally {
            // only schedule next day if not retry, else it will overwrite the retry attempt
            // - because we use uniqueName with ExistingWorkPolicy.REPLACE
            /*if (isScheduleNext) {
                runAt() // schedule for next day
            }*/
        }
    }

}