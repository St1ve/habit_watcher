package com.razvictor.habitwatcher.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date_completed")
data class DateCompletedEntity(
    val habitId: Long,
    val date: Long,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
