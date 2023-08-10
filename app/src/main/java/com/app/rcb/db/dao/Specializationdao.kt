package com.app.rcb.db.dao

import androidx.room.*
import com.app.rcb.db.entity.Specialization


@Dao
interface Specializationdao {

    @Query("SELECT * FROM Specialization")
    @OnConflictStrategy
    fun getAllSpecialization(): List<Specialization>

    @Insert
    fun insert(vararg objspecialization: Specialization)

    @Delete
    fun delete(objspecialization: Specialization)

    @Insert
    fun insertList(specializationList: List<Specialization>)

    @Update
    fun updateTodo(vararg objstate: Specialization)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(specializationList: List<Specialization>)



}