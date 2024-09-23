package com.razvictor.habitwatcher.uikit.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.razvictor.habitwatcher.uikit.HabitWatcherTheme
import com.razvictor.habitwatcher.uikit.component.calendar.CalendarState.GridState
import com.razvictor.habitwatcher.uikit.component.calendar.CalendarState.GridState.WeekState
import com.razvictor.habitwatcher.uikit.component.calendar.CalendarState.GridState.WeekState.CellState
import com.razvictor.habitwatcher.uikit.component.calendar.CalendarState.LegendState

@Composable
fun Calendar(
    state: CalendarState,
    onCellClick: (String) -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        HeaderPager(
            text = state.header,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
        )
        CalendarDayLegend(uiState = state.legend)
        CalendarGrid(
            state = state.gridState,
            onCellClick = onCellClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCalendar() {
    val weekDayNames = listOf(
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat",
    )
    val legendState = LegendState(
        weekDaysNames = weekDayNames,
    )
    val weeks = (0..5).map { weekIndex ->
        val cellState = (0..5).map {
            CellState.Data(
                id = weekIndex.toString(),
                name = (5 % weekIndex + it).toString(),
                backgroundAlpha = 0f
            )
        }
        WeekState(
            cells = cellState
        )
    }
    val gridState = GridState(
        weeks = weeks
    )
    val state = CalendarState(
        header = "January",
        legend = legendState,
        gridState = gridState
    )
    HabitWatcherTheme {
        Calendar(state, onCellClick = {}, onPreviousClick = {}, onNextClick = {})
    }
}

@Composable
private fun HeaderPager(
    text: String,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
        }

        // FIXME: Add animation
//        AnimatedContent(
//            targetState = yearMonth.item,
//            transitionSpec = pagerTransitionSpec,
//            modifier = Modifier.fillMaxWidth(0.8f),
//            label = "CalendarPager"
//        ) { targetYearMonth ->
//            val month = targetYearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
//            val year = targetYearMonth.year
//            val label = if (year == Year.now().value) month else "$month $year"
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
//        }

        IconButton(onClick = onNextClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
        }
    }
}

@Composable
private fun CalendarGrid(
    state: GridState,
    onCellClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        state.weeks.map { weekState ->
            Row(modifier = Modifier.fillMaxWidth()) {
                weekState.cells.map { cellState ->
                    when (cellState) {
                        is CellState.Empty -> Spacer(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f))

                        is CellState.Data -> DataCell(
                            state = cellState,
                            onClick = { onCellClick(cellState.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    heightDp = 100
)
@Composable
private fun PreviewHeaderPager() {
    HabitWatcherTheme {
        HeaderPager(text = "January", onPreviousClick = {}, onNextClick = {})
    }
}

@Composable
private fun CalendarDayLegend(
    uiState: LegendState,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        uiState.weekDaysNames.map { day ->
            Text(
                text = day,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(
    showBackground = true,
    heightDp = 25
)
@Composable
private fun PreviewCalendarDayLegend() {
    val uiState = LegendState(
        weekDaysNames = (0..6).map { "Day $it" }
    )
    HabitWatcherTheme {
        CalendarDayLegend(uiState)
    }
}

@Composable
private fun DataCell(
    state: CellState.Data,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .padding(4.dp)
            .background(
                shape = RoundedCornerShape(CornerSize(8.dp)),
                color = colorScheme.secondaryContainer.copy(alpha = state.backgroundAlpha),
            )
            .clip(RoundedCornerShape(CornerSize(8.dp)))
            .clickable(onClick = onClick)
    ) {
        Text(
            text = state.name,
            color = colorScheme.onSecondaryContainer,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
