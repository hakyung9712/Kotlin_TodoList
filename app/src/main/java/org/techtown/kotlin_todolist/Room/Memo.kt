package org.techtown.kotlin_todolist.Room

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName="memo")
class Memo @Ignore constructor(
    var title:String?= null,
    var content:String?=null,
    var year:Int?=null,
    var month:Int?=null,
    var day:Int?=null,
    var hour:Int?=null,
    var minute:Int?=null,
    @PrimaryKey(autoGenerate = true)     var id: Int =0){

    constructor():this("","",0,0,0,0,0)
}