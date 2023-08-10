package com.app.rcb.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.app.rcb.db.entity.State
import kotlinx.coroutines.flow.Flow

@Dao
interface Statedao {

    @Query("SELECT * FROM State WHERE country_id = 101")
    @OnConflictStrategy
    fun getAllState(): List<State>

    @Insert
    fun insert(vararg objstate: State)

    @Delete
    fun delete(objstate: State)

    @Insert
    fun insertList(stateList: List<State>)

    @Update
    fun updateTodo(vararg objstate: State)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stateList: List<State>)

}