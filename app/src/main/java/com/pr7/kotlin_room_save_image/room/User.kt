package com.pr7.kotlin_room_save_image.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pr7.kotlin_room_save_image.Constants.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class User constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val uid:Int,
    @ColumnInfo(name = "name")
    val name:String,
    @ColumnInfo(name="image")
    val image:ByteArray
)