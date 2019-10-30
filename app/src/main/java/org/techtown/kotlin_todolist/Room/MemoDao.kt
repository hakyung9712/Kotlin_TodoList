package org.techtown.kotlin_todolist.Room

import androidx.room.*

@Dao
interface MemoDao {
    @Query("SELECT * FROM memo")
    fun findMemo():List<Memo>

    @Query("SELECT * FROM memo WHERE id= (:id)")
    fun findMemoId(id:Int):List<Memo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memo: Memo)


    @Query("DELETE FROM memo WHERE id = (:id)")
    fun delete(id:Int)
}