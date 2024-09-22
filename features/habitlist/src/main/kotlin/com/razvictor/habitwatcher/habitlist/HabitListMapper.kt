package com.razvictor.habitwatcher.habitlist

import com.razvictor.habitwatcher.common.models.Habit
import java.time.LocalDate

internal fun List<Habit>.toUi(): List<HabitListUiState.HabitUiState> {
    val currentDate = LocalDate.now()
    return map { habit ->
        HabitListUiState.HabitUiState(
            id = habit.id,
            name = habit.name,
            isDone = habit.datesCompleted.contains(currentDate),
        )
    }
}
