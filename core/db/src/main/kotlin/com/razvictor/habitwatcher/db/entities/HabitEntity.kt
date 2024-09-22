package com.razvictor.habitwatcher.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    val name: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
