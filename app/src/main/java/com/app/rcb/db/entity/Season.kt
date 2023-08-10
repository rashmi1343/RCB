package com.app.rcb.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Season(
    @PrimaryKey
    val season_id:Int,
    val season_name:String,
    val season_description:String,
    val season_start_month:String,
    val season_end_month:String,
    val season_status:Int,
    val season_duration:String)

