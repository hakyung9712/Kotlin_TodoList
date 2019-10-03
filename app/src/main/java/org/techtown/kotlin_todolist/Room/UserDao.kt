package org.techtown.kotlin_todolist.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE email = (:email)")
    fun findUser(email: String): List<User>

    @Query("SELECT * FROM user WHERE email = (:email) AND pwd = (:pwd)")
    fun userLogin(email: String, pwd: String): List<User>

    @Insert(onConflict = REPLACE)
    fun insert(user: User)
}