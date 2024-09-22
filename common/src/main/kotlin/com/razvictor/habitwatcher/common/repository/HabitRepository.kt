package com.razvictor.habitwatcher.common.repository

import com.razvictor.habitwatcher.common.models.Habit
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitRepository {
    fun listenHabits() : Flow<List<Habit>>
    suspend fun getHabit(id: Long) : Habit
    suspend fun createHabit(name: String)
    suspend fun deleteHabit(id: Long)
    suspend fun completeHabit(habitId: Long, date: LocalDate)
    suspend fun resetCompletionHabit(habitId: Long, date: LocalDate)
}
