package com.app.rcb.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.rcb.db.entity.Center

@Dao
interface Centerdao {

    @Query("SELECT * FROM Center")
    fun getAllCenter(): List<Center>

    @Insert
    fun insert(vararg objcenter: Center)

    @Delete
    fun delete(objcenter: Center)

    @Insert
    fun insertCenterList(centerList: List<Center>)

    @Update
    fun updatecenter(vararg objcenter: Center)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(objcenter: List<Center>)
}