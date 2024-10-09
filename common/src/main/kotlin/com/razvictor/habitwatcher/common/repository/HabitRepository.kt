package com.razvictor.habitwatcher.common.repository

import com.razvictor.habitwatcher.common.models.Habit
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitRepository {
    fun listenHabits() : Flow<List<Habit>>
    fun listenHabit(id: Long) : Flow<Habit>
    suspend fun createHabit(name: String)
    suspend fun deleteHabit(id: Long)
    suspend fun editHabit(id: Long, name: String)
    suspend fun completeHabit(habitId: Long, date: LocalDate)
    suspend fun resetCompletionHabit(habitId: Long, date: LocalDate)
    suspend fun toggleHabit(habitId: Long, date: LocalDate)
}
