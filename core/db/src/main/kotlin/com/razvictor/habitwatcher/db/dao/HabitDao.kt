package com.razvictor.habitwatcher.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.razvictor.habitwatcher.db.dbs.HabitDb
import com.razvictor.habitwatcher.db.entities.DateCompletedEntity
import com.razvictor.habitwatcher.db.entities.HabitEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(vararg habit: HabitEntity)

    @Query(
        """
            SELECT habits.id, habits.name, GROUP_CONCAT(dates.date) AS datesCompleted
            FROM habits
            LEFT JOIN date_completed AS dates ON habits.id = dates.habitId
            GROUP BY habits.id
        """
    )
    fun listenHabits(): Flow<List<HabitDb>>

    // TODO: Добавить ограничение на период дат завершения
    @Query(
        """
            SELECT habits.id, habits.name, GROUP_CONCAT(dates.date) AS datesCompleted
            FROM habits
            LEFT JOIN date_completed AS dates ON habits.id = dates.habitId
            WHERE habits.id = :habitId
            GROUP BY habits.id
        """
    )
    fun listenHabit(habitId: Long) : Flow<HabitDb>

    @Query(
        """
            SELECT habits.id, habits.name, GROUP_CONCAT(dates.date) AS datesCompleted
            FROM habits
            LEFT JOIN date_completed AS dates ON habits.id = dates.habitId
            WHERE habits.id = :habitId
            GROUP BY habits.id
        """
    )
    suspend fun getHabit(habitId: Long) : HabitDb

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Long)

    @Query("UPDATE habits SET name = :name WHERE id = :id")
    suspend fun renameHabit(id: Long, name: String)

    @Query("DELETE FROM date_completed WHERE habitId = :habitId AND date = :completionDate")
    suspend fun resetCompletionHabit(habitId: Long, completionDate: LocalDate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDateCompleted(dateCompletedEntity: DateCompletedEntity)
}
