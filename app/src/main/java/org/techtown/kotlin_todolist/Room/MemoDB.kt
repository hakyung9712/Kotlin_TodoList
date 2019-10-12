package org.techtown.kotlin_todolist.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Memo::class],version=1)
abstract class MemoDB:RoomDatabase() {
    abstract val memoDao:MemoDao

    //자바의 static을 대신 지원. 클래스의 내부 정보에 접근할 수 있는 함수가 필요할 때
    companion object{
        private var INSTANCE: MemoDB?=null

        fun getInstance(context: Context):MemoDB?{
            if(INSTANCE ==null){
                //작업중이던 쓰레드가 마칠때까지 다른 쓰레드에게 제어권이 넘어가지 않게 보호
                synchronized(MemoDB::class){
                    INSTANCE= Room.databaseBuilder(context.applicationContext,
                        MemoDB::class.java,"memo.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}