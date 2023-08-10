package com.app.rcb.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Program(
    @PrimaryKey
    val pm_id:Int,
    val pm_center_id:String,
    val pm_program_title:String,
    val pm_description:String,
    val pm_code:String,
    val pm_printname:String)

