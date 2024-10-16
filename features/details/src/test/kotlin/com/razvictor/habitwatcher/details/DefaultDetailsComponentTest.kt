package com.razvictor.habitwatcher.details

import TestInstanceKeeper
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.razvictor.habitwatcher.common.repository.HabitRepository
import com.razvictor.habitwatcher.details.DetailsUiState.HeaderState
import com.razvictor.habitwatcher.details.DetailsUiState.HeaderState.State
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verifySequence
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions

@Extensions(ExtendWith(MockKExtension::class))
internal class DefaultDetailsComponentTest(
    @MockK(relaxed = true) private val lifecycle: Lifecycle,
    @MockK(relaxed = true) private val retainedInstance: DetailsRetainedInstance,
    @MockK private val onDeleteHabit: () -> Unit,
    @MockK private val habitRepository: HabitRepository,
) {
    private val mockHabitId = 123L

    @Test
    @DisplayName("Нажатие на кнопку сохранить")
    fun test_onSaveClick() {
        /* Given */
        val mockUiState = DetailsUiState.DEFAULT.copy(
            headerState = HeaderState(title = "Test", state = State.EDIT)
        )
        every {
            retainedInstance.mUiState
        } returns MutableValue(mockUiState)
        val component = createComponent()
        justRun {
            retainedInstance.saveChanges(any(), any())
        }

        /* When */
        component.onSaveClick()

        /* Then */
        verifySequence {
            retainedInstance.mUiState
            retainedInstance.saveChanges(mockHabitId, "Test")
        }
    }

    @Test
    @DisplayName("Нажатие на кнопку удалить")
    fun test_onDeleteClick() {
        /* Given */
        every {
            retainedInstance.mUiState
        } returns MutableValue(DetailsUiState.DEFAULT)
        val component = createComponent()
        justRun {
            retainedInstance.deleteHabit(any(), any())
        }

        /* When */
        component.onDeleteClick()

        /* Then */
        verifySequence {
            retainedInstance.mUiState
            retainedInstance.deleteHabit(mockHabitId, any())
        }
    }

    @Test
    @DisplayName("Смена названия привычки")
    fun test_onHeaderNameChanged() {
        /* Given */
        every {
            retainedInstance.mUiState
        } returns MutableValue(
            DetailsUiState.DEFAULT.copy(
                headerState = HeaderState.DEFAULT.copy(title = "Old title")
            )
        )
        val component = createComponent()
        val mockNewTitle = "New title"

        /* When */
        component.onHeaderNameChanged(mockNewTitle)

        /* Then */
        verifySequence {
            retainedInstance.mUiState
            retainedInstance.mUiState
        }

        assertEquals(mockNewTitle, retainedInstance.mUiState.value.headerState.title)
    }

    @Test
    @DisplayName("Нажать на кнопку редактирования привычки")
    fun test_onEditClick() {
        /* Given */
        every {
            retainedInstance.mUiState
        } returns MutableValue(
            DetailsUiState.DEFAULT.copy(headerState = HeaderState.DEFAULT.copy(state = State.DATA))
        )
        val component = createComponent()

        /* When */
        component.onEditClick()

        /* Then */
        verifySequence {
            retainedInstance.mUiState
            retainedInstance.mUiState
        }

        assertEquals(State.EDIT, retainedInstance.mUiState.value.headerState.state)
    }

    @Test
    @DisplayName("Нажать на кнопку предыдущего месяца")
    fun test_onPreviousMonthClick() {
        /* Given */
        every {
            retainedInstance.mUiState
        } returns MutableValue(DetailsUiState.DEFAULT)
        val component = createComponent()
        justRun {
            retainedInstance.selectPrevMonth()
        }

        /* When */
        component.onPreviousMonthClick()

        /* Then */
        verifySequence {
            retainedInstance.mUiState
            retainedInstance.selectPrevMonth()
        }
    }

    @Test
    @DisplayName("Нажать на кнопку следующего месяца")
    fun test_onNextMonthClick() {
        /* Given */
        every {
            retainedInstance.mUiState
        } returns MutableValue(DetailsUiState.DEFAULT)
        val component = createComponent()
        justRun {
            retainedInstance.selectNextMonth()
        }

        /* When */
        component.onNextMonthClick()

        /* Then */
        verifySequence {
            retainedInstance.mUiState
            retainedInstance.selectNextMonth()
        }
    }

    @Test
    @DisplayName("Нажать на ячейку дня календаря")
    fun test_onCalendarCellClick() {
        /* Given */
        every {
            retainedInstance.mUiState
        } returns MutableValue(DetailsUiState.DEFAULT)
        val component = createComponent()
        justRun {
            retainedInstance.toggleHabitDone(any(), any())
        }
        val mockCellId = "Cell id"

        /* When */
        component.onCalendarCellClick(mockCellId)

        /* Then */
        verifySequence {
            retainedInstance.mUiState
            retainedInstance.toggleHabitDone(mockHabitId, mockCellId)
        }
    }

    private fun createComponent(): DefaultDetailsComponent {
        return DefaultDetailsComponent(
            componentContext = DefaultComponentContext(
                lifecycle = lifecycle,
                instanceKeeper = TestInstanceKeeper(retainedInstance)
            ),
            habitId = mockHabitId,
            onDeleteHabit = onDeleteHabit,
            habitRepository = habitRepository,
        )
    }
}
