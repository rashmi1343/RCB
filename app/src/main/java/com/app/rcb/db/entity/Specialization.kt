package com.app.rcb.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Specialization (
    @PrimaryKey
    val sm_id: Int,
    val sm_center_id: Int,
    val sm_program_id: Int,
    val sm_title: String,
    val sm_description:String,
    val sm_status:Int
)