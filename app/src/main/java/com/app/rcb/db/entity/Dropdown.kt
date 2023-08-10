package com.app.rcb.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dropdown (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val dropdown_dd_id:Int,
    val dropdown_valueid:Int,
    val dropdown_name:String,
    val dropdown_status:Int
)