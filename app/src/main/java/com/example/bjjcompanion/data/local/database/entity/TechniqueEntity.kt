package com.example.bjjcompanion.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "techniques",
    indices = [
        Index(value = ["created_at"]),
        Index(value = ["category"])
    ]
)
data class TechniqueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val category: String, // takedown, submission, sweep, defense, control

    val description: String? = null, // optional notes (max 2000 chars)

    @ColumnInfo(name = "created_at")
    val createdAt: Long // timestamp
)
