package com.razvictor.habitwatcher.habitlist

import TestInstanceKeeper
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.razvictor.habitwatcher.common.repository.HabitRepository
import io.mockk.every
import io.mockk.excludeRecords
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions

@OptIn(ExperimentalCoroutinesApi::class)
@Extensions(ExtendWith(MockKExtension::class))
internal class DefaultHabitListComponentTest(
    @MockK(relaxed = true) private val lifecycle: Lifecycle,
    @MockK private val onNewHabitClick: () -> Unit,
    @MockK private val habitRepository: HabitRepository,
    @MockK private val onDetailsHabitClick: (Long) -> Unit,
    @MockK(relaxed = true) private val retainedInstance: HabitListRetainedInstance,
) {

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        every {
            habitRepository.listenHabits()
        } returns flowOf()
        excludeRecords {
            habitRepository.listenHabits()
        }
    }

    @Test
    @DisplayName("Проверка нажатия на создание новой привычки")
    fun test_onNewHabitClick() {
        /* Given */
        justRun {
            onNewHabitClick()
        }
        every {
            retainedInstance.mUiState
        } returns MutableValue(HabitListUiState.DEFAULT)
        val component = createComponent()

        /* When */
        component.onNewHabitClick()

        /* Then */
        verifySequence {
            retainedInstance.mUiState
            onNewHabitClick()
        }
    }

    @Test
    @DisplayName("Проверка нажатия на отметить привычку")
    fun test_onMarkHabitClick() {
        /* Given */
        justRun {
            retainedInstance.toggleHabitDone(any(), any())
        }
        every {
            retainedInstance.mUiState
        } returns MutableValue(HabitListUiState.DEFAULT)
        val component = createComponent()
        val mockId = 123L
        val mockIsDone = true

        /* When */
        component.onMarkHabitClick(mockId, mockIsDone)

        /* Then */
        verifySequence {
            retainedInstance.mUiState
            retainedInstance.toggleHabitDone(mockId, mockIsDone)
        }
    }

    @Test
    @DisplayName("Проверка нажатия на карточку привычки")
    fun test_onCardClick() {
        /* Given */
        justRun {
            onDetailsHabitClick(any())
        }
        every {
            retainedInstance.mUiState
        } returns MutableValue(HabitListUiState.DEFAULT)
        val component = createComponent()
        val mockId = 123L

        /* When */
        component.onCardClick(mockId)

        /* Then */
        verifySequence {
            retainedInstance.mUiState
            onDetailsHabitClick(mockId)
        }
    }

    private fun createComponent(): DefaultHabitListComponent {
        return DefaultHabitListComponent(
            componentContext = DefaultComponentContext(
                lifecycle = lifecycle,
                instanceKeeper = TestInstanceKeeper(retainedInstance)
            ),
            habitRepository = habitRepository,
            onNewHabitClick = onNewHabitClick,
            onDetailsHabitClick = onDetailsHabitClick
        )
    }
}
