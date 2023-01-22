package com.pr7.kotlin_room_save_image.room

import androidx.room.*
import com.pr7.kotlin_room_save_image.Constants.TABLE_NAME

@Dao
interface UserDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllUsers():List<User>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)
    @Update
    fun updateUser(user: User)
    @Delete
    fun deleteUser(user: User)
    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAllUsers()
    @Query("SELECT * FROM $TABLE_NAME WHERE id=:idd ")
    fun loadbyid(idd:Int):User
}