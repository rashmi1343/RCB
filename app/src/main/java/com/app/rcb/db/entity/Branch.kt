package com.app.rcb.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Branch(
    @PrimaryKey
    val bm_id:Int,
    val bm_branchtitle:String,
    val bm_branchdescription:String)

