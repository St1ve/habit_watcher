package com.razvictor.habitwatcher.uikit.component.calendar

import androidx.compose.runtime.Immutable

data class CalendarState(
    val header: String,
    val legend: LegendState,
    val gridState: GridState,
) {
    data class LegendState(val weekDaysNames: List<String>) {
        companion object {
            val DEFAULT = LegendState(emptyList())
        }
    }

    data class GridState(val weeks: List<WeekState>) {

        data class WeekState(val cells: List<CellState>) {

            @Immutable
            sealed interface CellState {
                data object Empty : CellState
                data class Data(
                    val id: String,
                    val name: String,
                    val backgroundAlpha: Float,
                ) : CellState
            }
        }

        companion object {
            val DEFAULT = GridState(emptyList())
        }
    }

    companion object {
        val DEFAULT = CalendarState(
            header = "",
            legend = LegendState.DEFAULT,
            gridState = GridState.DEFAULT
        )
    }
}
