package org.techtown.kotlin_todolist.Room

import androidx.room.*

@Dao
interface MemoDao {
    @Query("SELECT * FROM memo")
    fun findMemo():List<Memo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memo: Memo)

    @Delete
    fun delete(memo:Memo)
}