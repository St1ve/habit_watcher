package com.razvictor.habitwatcher.uikit.component.calendar

import com.razvictor.habitwatcher.uikit.component.calendar.CalendarState.GridState.WeekState
import com.razvictor.habitwatcher.uikit.component.calendar.CalendarState.GridState.WeekState.CellState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

fun LocalDate.toUi(
    datesCompleted: List<LocalDate>,
): CalendarState {
    val header = month.getDisplayName(TextStyle.FULL, Locale.getDefault())

    val weekDays = DayOfWeek.entries.map { dayOfWeek -> dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()) }
    val legend = CalendarState.LegendState(weekDays)

    val dateWithBeginOfMonth = withDayOfMonth(1)
    val daysSkipAtBeginning = dateWithBeginOfMonth.dayOfWeek.ordinal

    val daysSkipAtEnd = DayOfWeek.SUNDAY.ordinal - withDayOfMonth(lengthOfMonth()).dayOfWeek.ordinal

    val cellsNumber = lengthOfMonth() + daysSkipAtBeginning + daysSkipAtEnd
    val weekNumber = cellsNumber / 7
    val weeks = (0 until weekNumber).map { weekIndex ->
        val cellState = (0 until 7).map { dayIndex ->
            when {

                weekIndex == 0 && daysSkipAtBeginning > 0 && dayIndex < daysSkipAtBeginning -> {
                    CellState.Empty
                }

                weekIndex == weekNumber - 1 && daysSkipAtEnd > 0 && dayIndex >= 7 - daysSkipAtEnd -> {
                    CellState.Empty
                }

                else -> {
                    val day = weekIndex * 7 + dayIndex - daysSkipAtBeginning.toLong()
                    val dateOfDay = dateWithBeginOfMonth.plusDays(day)

                    CellState.Data(
                        id = dateOfDay.toString(),
                        name = (day + 1).toString(),
                        backgroundAlpha = if (datesCompleted.contains(dateOfDay)) {
                            1f
                        } else {
                            0f
                        }
                    )
                }
            }
        }

        WeekState(cellState)
    }
    val gridState = CalendarState.GridState(weeks = weeks)

    return CalendarState(
        header = header,
        legend = legend,
        gridState = gridState,
    )
}
