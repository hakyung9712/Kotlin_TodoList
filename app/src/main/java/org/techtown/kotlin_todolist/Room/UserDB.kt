package org.techtown.kotlin_todolist.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class],version=1)
abstract class UserDB:RoomDatabase() {
    abstract val userDao:UserDao

    companion object{
        private var INSTANCE: UserDB?=null

        fun getInstance(context: Context):UserDB?{
            if(INSTANCE ==null){
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