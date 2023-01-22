package com.pr7.kotlin_room_save_image.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun userDao():UserDao
}