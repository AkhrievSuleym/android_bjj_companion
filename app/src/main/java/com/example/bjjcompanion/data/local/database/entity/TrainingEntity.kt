package com.example.bjjcompanion.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "trainings",
    indices = [Index(value = ["date"])]
)
data class TrainingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val date: Long, // timestamp

    val type: String, // grappling, sparring, strength, cardio

    @ColumnInfo(name = "duration_minutes")
    val durationMinutes: Int,

    val notes: String? = null // optional notes (max 1000 chars)
)
