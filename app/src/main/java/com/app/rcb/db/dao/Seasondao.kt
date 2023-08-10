package com.app.rcb.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.rcb.db.entity.Season

@Dao
interface Seasondao {

    @Query("SELECT * FROM Season")
    fun getAllSeason(): List<Season>

    @Insert
    fun insert(vararg objseason: Season)

    @Delete
    fun delete(objseason: Season)

    @Insert
    fun insertList(seasonList: List<Season>)

    @Update
    fun updateTodo(vararg objseason: Season)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(objseason: List<Season>)

}