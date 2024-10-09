package com.razvictor.habitwatcher.common.repository

import com.razvictor.habitwatcher.common.mappers.toDomain
import com.razvictor.habitwatcher.common.models.Habit
import com.razvictor.habitwatcher.db.dao.HabitDao
import com.razvictor.habitwatcher.db.dbs.HabitDb
import com.razvictor.habitwatcher.db.entities.DateCompletedEntity
import com.razvictor.habitwatcher.db.entities.HabitEntity
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import java.time.LocalDate

@Extensions(ExtendWith(MockKExtension::class))
class HabitRepositoryImplTest(
    @MockK private val habitDao: HabitDao,
) {
    private val repository = HabitRepositoryImpl(
        habitDao = habitDao,
    )

    @Test
    @DisplayName("Проверка подписки на список привычек")
    fun test_listenHabits() = runTest {
        /* Given */
        val mockHabitListDb = listOf(
            HabitDb(
                id = 1,
                name = "Habit 1",
                datesCompleted = listOf(
                    LocalDate.of(2024, 10, 1),
                    LocalDate.of(2024, 10, 4)
                ),
            )
        )
        coEvery {
            habitDao.listenHabits()
        } returns flowOf(mockHabitListDb)
        val mockHabitList = listOf(
            Habit(
                id = 1,
                name = "Habit 1",
                datesCompleted = listOf(
                    LocalDate.of(2024, 10, 1),
                    LocalDate.of(2024, 10, 4)
                ),
            )
        )
        mockkStatic(List<HabitDb>::toDomain)
        coEvery {
            any<List<HabitDb>>().toDomain()
        } returns mockHabitList

        /* When */
        val actual = repository.listenHabits().first()

        /* Then */
        assertEquals(mockHabitList, actual)
        coVerifySequence {
            habitDao.listenHabits()
            mockHabitListDb.toDomain()
        }
        unmockkStatic(List<HabitDb>::toDomain)
    }

    @Test
    @DisplayName("Проверка подписки на привычеку")
    fun test_listenHabit() = runTest {
        /* Given */
        val mockHabitId = 1L
        val mockHabitDb = HabitDb(id = 1, name = "1", datesCompleted = emptyList())
        coEvery {
            habitDao.listenHabit(any())
        } returns flowOf(mockHabitDb)
        val mockHabit = Habit(
            id = 1,
            name = "Habit 1",
            datesCompleted = listOf(
                LocalDate.of(2024, 10, 1),
                LocalDate.of(2024, 10, 4)
            ),
        )
        mockkStatic(List<HabitDb>::toDomain)
        coEvery {
            any<HabitDb>().toDomain()
        } returns mockHabit

        /* When */
        val actual = repository.listenHabit(mockHabitId).first()

        /* Then */
        assertEquals(mockHabit, actual)

        coVerifySequence {
            habitDao.listenHabit(mockHabitId)
            mockHabitDb.toDomain()
        }
        unmockkStatic(List<HabitDb>::toDomain)
    }

    @Test
    @DisplayName("Проверка создания новой заметки")
    fun test_createHabit() = runTest {
        /* Given */
        val mockName = "Test habit name"
        coJustRun {
            habitDao.insertHabits(any())
        }

        /* When */
        repository.createHabit(mockName)

        /* Then */
        val expectedEntity = HabitEntity(name = mockName)
        coVerifySequence {
            habitDao.insertHabits(expectedEntity)
        }
    }

    @Test
    @DisplayName("Проверка удаления привычки")
    fun test_deleteHabit() = runTest {
        /* Given */
        val mockId = 1L
        coJustRun {
            habitDao.deleteHabit(any())
        }

        /* When */
        repository.deleteHabit(mockId)

        /* Then*/
        coVerifySequence {
            habitDao.deleteHabit(mockId)
        }
    }

    @Test
    @DisplayName("Проверка смена имени привычки")
    fun test_editHabit() = runTest {
        /* Given */
        val mockId = 1L
        val mockName = "New name"
        coJustRun {
            habitDao.renameHabit(any(), any())
        }

        /* When */
        repository.editHabit(mockId, mockName)

        /* Then */
        coVerifySequence {
            habitDao.renameHabit(mockId, mockName)
        }
    }

    @Test
    @DisplayName("Проверка сброса выполнения привычки")
    fun test_resetCompletionHabit() = runTest {
        /* Given */
        val mockHabitId = 1L
        val mockDate = LocalDate.of(2024, 1, 1)
        coJustRun {
            habitDao.resetCompletionHabit(any(), any())
        }

        /* When */
        repository.resetCompletionHabit(mockHabitId, mockDate)

        /* Then */
        coVerifySequence {
            habitDao.resetCompletionHabit(mockHabitId, mockDate)
        }
    }

    @Test
    @DisplayName("Проверка проставления выполнения привычки")
    fun test_toggleHabit_habitNotCompleted() = runTest {
        /* Given */
        val mockHabitId = 1L
        val mockDate = LocalDate.of(2024, 1, 1)
        val mockHabitDb = HabitDb(id = mockHabitId, name = "Habit 1", datesCompleted = emptyList())
        coEvery {
            habitDao.getHabit(any())
        } returns mockHabitDb
        coJustRun {
            habitDao.insertDateCompleted(any())
        }

        /* When */
        repository.toggleHabit(mockHabitId, mockDate)

        /* Then */
        val expectedEntity = DateCompletedEntity(
            habitId = mockHabitId,
            date = mockDate
        )
        coVerifySequence {
            habitDao.getHabit(mockHabitId)
            habitDao.insertDateCompleted(expectedEntity)
        }
    }

    @Test
    @DisplayName("Проверка сброса причины при уже проставленной")
    fun test_toggleHabit_habitCompleted() = runTest {
        /* Given */
        val mockHabitId = 1L
        val mockDate = LocalDate.of(2024, 1, 1)
        val mockHabitDb = HabitDb(id = mockHabitId, name = "Habit 1", datesCompleted = listOf(mockDate))
        coEvery {
            habitDao.getHabit(any())
        } returns mockHabitDb
        coJustRun {
            habitDao.resetCompletionHabit(any(), any())
        }

        /* When */
        repository.toggleHabit(mockHabitId, mockDate)

        /* Then */
        coVerifySequence {
            habitDao.getHabit(mockHabitId)
            habitDao.resetCompletionHabit(mockHabitId, mockDate)
        }
    }

    @Test
    @DisplayName("Проверка завершения привычки")
    fun test_completeHabit() = runTest {
        /* Given */
        val mockHabitId = 1L
        val mockDate = LocalDate.of(2024, 1, 1)
        coJustRun {
            habitDao.insertDateCompleted(any())
        }

        /* When */
        repository.completeHabit(mockHabitId, mockDate)

        /* Then */
        val expectedEntity = DateCompletedEntity(
            habitId = mockHabitId,
            date = mockDate
        )
        coVerifySequence {
            habitDao.insertDateCompleted(expectedEntity)
        }
    }
}
