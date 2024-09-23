package com.razvictor.habitwatcher.details

import com.razvictor.habitwatcher.common.models.Habit
import com.razvictor.habitwatcher.details.DetailsUiState.HeaderState
import com.razvictor.habitwatcher.uikit.component.calendar.toUi
import java.time.LocalDate

fun Habit.toUi(): DetailsUiState {
    val headerState = HeaderState(
        title = this.name,
        state = HeaderState.State.DATA
    )

    return DetailsUiState(
        headerState = headerState,
        // FIXME: Replace by selected date 
        calendarState = LocalDate.now().toUi(datesCompleted)
    )
}
