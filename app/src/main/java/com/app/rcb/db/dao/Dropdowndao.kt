package com.app.rcb.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.rcb.db.entity.Dropdown

@Dao
interface Dropdowndao {

    @Query("SELECT * FROM Dropdown")
    fun getAlldropdown(): List<Dropdown>

    @Insert
    fun insert(vararg objdropdown: Dropdown)

    @Delete
    fun delete(objdropdown: Dropdown)

    @Insert
    fun insertList(dropdownList: List<Dropdown>)

    @Update
    fun updateTodo(vararg objdropdown: Dropdown)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dropdownList: List<Dropdown>)

}