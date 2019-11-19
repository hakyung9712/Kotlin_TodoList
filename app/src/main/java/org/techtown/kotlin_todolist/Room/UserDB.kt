package org.techtown.kotlin_todolist.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class],version=1,exportSchema = false)
abstract class UserDB:RoomDatabase() {
    abstract val userDao:UserDao

    //자바의 static을 대신 지원. 클래스의 내부 정보에 접근할 수 있는 함수가 필요할 때
    companion object{
        private var INSTANCE: UserDB?=null

        fun getInstance(context: Context):UserDB?{
            if(INSTANCE ==null){
                //작업중이던 쓰레드가 마칠때까지 다른 쓰레드에게 제어권이 넘어가지 않게 보호
                synchronized(UserDB::class){
                    INSTANCE= Room.databaseBuilder(context.applicationContext,
                        UserDB::class.java,"user.db")
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