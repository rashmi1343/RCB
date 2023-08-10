package com.app.rcb.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.rcb.db.entity.Branch

@Dao
interface Branchdao {

    @Query("SELECT * FROM Branch")
    fun getAllBranch(): List<Branch>

    @Insert
    fun insert(vararg objbranch: Branch)

    @Delete
    fun delete(objbranch: Branch)

    @Insert
    fun insertBranchList(branchList: List<Branch>)

    @Update
    fun updatebranch(vararg objbranch: Branch)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(objbranch: List<Branch>)
}