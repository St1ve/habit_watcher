package com.razvictor.habitwatcher.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @ColumnInfo(name = COLUMN_ID_NAME)
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
) {
    companion object {
        const val COLUMN_ID_NAME = "id"
    }
}
