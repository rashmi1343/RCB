package com.app.rcb.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Center(
    @PrimaryKey
    val center_id:String,
    val center_name:String,
    val center_code:String)
