import com.razvictor.habitwatcher.uikit.component.calendar.CalendarState
import com.razvictor.habitwatcher.uikit.component.calendar.CalendarState.GridState.WeekState
import com.razvictor.habitwatcher.uikit.component.calendar.CalendarState.GridState.WeekState.CellState
import com.razvictor.habitwatcher.uikit.component.calendar.toUi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CalendarMapperTest {

    @Test
    @DisplayName("Проверка маппинга месяца с началом в понедельник и 5-ю неделями")
    fun test_noSkipDaysBegin_with5Weeks() {
        val mockDate = LocalDate.of(2024, 7, 1)
        val expectedWeeks = listOf(
            WeekState(cells = (0 until 7).map { mockCellData(currentDate = mockDate, daysFromStart = it.toLong()) }),
            WeekState(cells = (7 until 14).map { mockCellData(currentDate = mockDate, daysFromStart = it.toLong()) }),
            WeekState(cells = (14 until 21).map { mockCellData(currentDate = mockDate, daysFromStart = it.toLong()) }),
            WeekState(cells = (21 until 28).map { mockCellData(currentDate = mockDate, daysFromStart = it.toLong()) }),
            WeekState(cells = ((28 until 31).map {
                mockCellData(
                    currentDate = mockDate,
                    daysFromStart = it.toLong()
                )
            }) + (31 until 35).map { CellState.Empty }),
        )
        val expectedGrid = CalendarState.GridState(weeks = expectedWeeks)
        val expectedHeader = "July"
        val expected = CalendarState(
            header = expectedHeader,
            legend = expectedLegend,
            gridState = expectedGrid,
        )

        val actual = mockDate.toUi(emptyList())

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Проверка маппинга месяца с началом в четверг и 5-ю неделями")
    fun test_Skip3DaysBegin_with5Weeks() {
        val mockDate = LocalDate.of(2024, 8, 1)
        val expectedWeeks = listOf(
            WeekState(cells = ((0 until 3).map { CellState.Empty }) + (0 until 4).map {
                mockCellData(
                    currentDate = mockDate,
                    daysFromStart = it.toLong()
                )
            }),
            WeekState(cells = (4 until 11).map { mockCellData(currentDate = mockDate, daysFromStart = it.toLong()) }),
            WeekState(cells = (11 until 18).map { mockCellData(currentDate = mockDate, daysFromStart = it.toLong()) }),
            WeekState(cells = (18 until 25).map { mockCellData(currentDate = mockDate, daysFromStart = it.toLong()) }),
            WeekState(cells = ((25 until 31).map {
                mockCellData(
                    currentDate = mockDate,
                    daysFromStart = it.toLong()
                )
            }) + listOf(CellState.Empty)),
        )
        val expectedGrid = CalendarState.GridState(weeks = expectedWeeks)
        val expectedHeader = "August"
        val expected = CalendarState(
            header = expectedHeader,
            legend = expectedLegend,
            gridState = expectedGrid,
        )

        val actual = mockDate.toUi(emptyList())

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Проверка маппинга месяца с началом в воскресенье и 6-ю неделями")
    fun test_Skip6DaysBegin_with6Weeks() {
        val mockDate = LocalDate.of(2023, 10, 1)
        val expectedWeeks = listOf(
            WeekState(cells = ((0 until 6).map { CellState.Empty }) + listOf(
                mockCellData(
                    currentDate = mockDate,
                    daysFromStart = 0
                )
            )),
            WeekState(cells = (1 until 8).map { mockCellData(currentDate = mockDate, daysFromStart = it.toLong()) }),
            WeekState(cells = (8 until 15).map { mockCellData(currentDate = mockDate, daysFromStart = it.toLong()) }),
            WeekState(cells = (15 until 22).map { mockCellData(currentDate = mockDate, daysFromStart = it.toLong()) }),
            WeekState(cells = (22 until 29).map { mockCellData(currentDate = mockDate, daysFromStart = it.toLong()) }),
            WeekState(cells = ((29 until 31).map {
                mockCellData(
                    currentDate = mockDate,
                    daysFromStart = it.toLong()
                )
            }) + (31 until 36).map { CellState.Empty }),
        )
        val expectedGrid = CalendarState.GridState(weeks = expectedWeeks)
        val expectedHeader = "October"
        val expected = CalendarState(
            header = expectedHeader,
            legend = expectedLegend,
            gridState = expectedGrid,
        )

        val actual = mockDate.toUi(emptyList())

        assertEquals(expected, actual)
    }

    private fun mockCellData(
        currentDate: LocalDate,
        daysFromStart: Long,
        alpha: Float = 0f
    ): CellState.Data {
        val id = currentDate.plusDays(daysFromStart)
        return CellState.Data(
            id = id.toString(), name = (daysFromStart + 1).toString(), backgroundAlpha = alpha
        )
    }

    private companion object {
        val expectedLegend = CalendarState.LegendState(
            weekDaysNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        )
    }
}
