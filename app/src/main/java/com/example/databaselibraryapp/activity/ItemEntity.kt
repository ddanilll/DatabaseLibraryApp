package com.example.databaselibraryapp.activity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val name: String,
    val info: String,
    val createdAt: Long = System.currentTimeMillis()
)
