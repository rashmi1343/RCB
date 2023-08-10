package com.app.rcb.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.rcb.db.entity.Ay

@Dao
interface aydao {

    @Query("SELECT * FROM Ay")
    fun getAllay(): List<Ay>

    @Insert
    fun insert(vararg objay: Ay)

    @Delete
    fun delete(objay: Ay)

    @Insert
    fun insertBranchList(ayList: List<Ay>)

    @Update
    fun updatebranch(vararg objay: Ay)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(objay: List<Ay>)
}