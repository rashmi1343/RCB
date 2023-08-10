package com.app.rcb.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*

import com.app.rcb.db.entity.Program

@Dao
interface Programdao {

    @Query("SELECT * FROM Program")
    fun getAllprogram(): List<Program>

    @Insert
    fun insert(vararg objprogram: Program)

    @Delete
    fun delete(objprogram: Program)

    @Insert
    fun insertList(programList: List<Program>)

    @Update
    fun updateTodo(vararg objprogram: Program)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(programList: List<Program>)

}