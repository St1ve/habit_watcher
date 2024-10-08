package com.razvictor.habitwatcher.common.repository

import com.razvictor.habitwatcher.common.mappers.toDomain
import com.razvictor.habitwatcher.common.models.Habit
import com.razvictor.habitwatcher.db.dao.HabitDao
import com.razvictor.habitwatcher.db.entities.DateCompletedEntity
import com.razvictor.habitwatcher.db.entities.HabitEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
) : HabitRepository {

    override fun listenHabits(): Flow<List<Habit>> {
        return habitDao
            .listenHabits()
            .map { habitDbs -> habitDbs.toDomain() }
    }

    override fun listenHabit(id: Long): Flow<Habit> {
        return habitDao
            .listenHabit(id)
            .map { habitDb -> habitDb.toDomain() }
    }

    override suspend fun createHabit(name: String) {
        val entity = HabitEntity(name = name)

        habitDao.insertHabits(entity)
    }

    override suspend fun deleteHabit(id: Long) {
        habitDao.deleteHabit(id)
    }

    override suspend fun editHabit(id: Long, name: String) {
        habitDao.renameHabit(id, name)
    }

    override suspend fun resetCompletionHabit(habitId: Long, date: LocalDate) {
        habitDao.resetCompletionHabit(habitId, date)
    }

    override suspend fun toggleHabit(habitId: Long, date: LocalDate) {
        val habit = habitDao.getHabit(habitId)
        if (habit.datesCompleted.contains(date)) {
            resetCompletionHabit(habitId, date)
        } else {
            completeHabit(habitId, date)
        }
    }

    override suspend fun completeHabit(habitId: Long, date: LocalDate) {
        val entity = DateCompletedEntity(
            habitId = habitId,
            date = date
        )

        habitDao.insertDateCompleted(entity)
    }
}
