package com.razvictor.habitwatcher.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.razvictor.habitwatcher.db.entities.HabitEntity

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(vararg habit: HabitEntity)
}
