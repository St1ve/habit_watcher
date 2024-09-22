package com.razvictor.habitwatcher.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.razvictor.habitwatcher.db.entities.DateCompletedEntity.Companion.COLUMN_HABIT_ID_NAME
import java.time.LocalDate

@Entity(
    tableName = "date_completed",
    indices = [Index(COLUMN_HABIT_ID_NAME)],
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = [HabitEntity.COLUMN_ID_NAME],
            childColumns = [COLUMN_HABIT_ID_NAME],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class DateCompletedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = COLUMN_HABIT_ID_NAME)
    val habitId: Long,
    val date: LocalDate,
) {
    companion object {
        const val COLUMN_HABIT_ID_NAME = "habitId"
    }
}
