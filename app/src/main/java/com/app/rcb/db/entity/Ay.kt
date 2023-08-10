package com.app.rcb.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ay(
    @PrimaryKey
    val ay_id:Int,
    val ay_name:String)



