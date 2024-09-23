package com.razvictor.habitwatcher.details

import com.razvictor.habitwatcher.uikit.component.calendar.CalendarState

data class DetailsUiState(
    val headerState: HeaderState,
    val calendarState: CalendarState
) {
    data class HeaderState(
        val title: String,
        val state: State,
    ) {
        enum class State {
            EDIT, DATA
        }

        companion object {
            val DEFAULT = HeaderState(
                title = "",
                state = State.DATA
            )
        }
    }

    companion object {
        val DEFAULT = DetailsUiState(
            headerState = HeaderState.DEFAULT,
            calendarState = CalendarState.DEFAULT,
        )
    }
}
