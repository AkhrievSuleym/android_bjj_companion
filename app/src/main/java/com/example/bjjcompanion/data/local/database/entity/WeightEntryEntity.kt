package com.example.bjjcompanion.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "weight_entries",
    indices = [Index(value = ["date"], unique = true)]
)
data class WeightEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val weight: Float, // kg

    val date: Long, // timestamp (normalized to start of day)

    val note: String? = null // optional note
)
