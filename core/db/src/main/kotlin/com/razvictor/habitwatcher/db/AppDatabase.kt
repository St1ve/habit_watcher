package com.razvictor.habitwatcher.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.razvictor.habitwatcher.db.dao.HabitDao
import com.razvictor.habitwatcher.db.entities.DateCompletedEntity
import com.razvictor.habitwatcher.db.entities.HabitEntity

@Database(
    entities = [
        HabitEntity::class,
        DateCompletedEntity::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}
