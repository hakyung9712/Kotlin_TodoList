package org.techtown.kotlin_todolist.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="user")
class User( var email:String?,
           @ColumnInfo(name="pwd") var pwd:String?,
            @PrimaryKey(autoGenerate = true) var id:Int =0){
    constructor():this("","")
}