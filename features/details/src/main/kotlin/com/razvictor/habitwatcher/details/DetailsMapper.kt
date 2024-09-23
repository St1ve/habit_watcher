package com.razvictor.habitwatcher.details

import com.razvictor.habitwatcher.common.models.Habit
import com.razvictor.habitwatcher.details.DetailsUiState.HeaderState

fun Habit.toUi(): DetailsUiState {
    val headerState = HeaderState(
        title = this.name,
        state = HeaderState.State.DATA
    )

    return DetailsUiState(
        headerState = headerState,
    )
}
