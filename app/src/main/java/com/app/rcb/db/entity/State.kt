package com.app.rcb.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "State")
data class State(
    @PrimaryKey
    val id: String,
    val name: String,
    val country_id: Int

)
