import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.razvictor.habitwatcher.common.repository.HabitRepository
import com.razvictor.habitwatcher.new_habit.DefaultNewHabitComponent
import com.razvictor.habitwatcher.new_habit.NewHabitRetainedInstance
import com.razvictor.habitwatcher.new_habit.NewHabitUiState
import com.razvictor.habitwatcher.new_habit.NewHabitUiState.FieldState
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verifySequence
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions

@Extensions(ExtendWith(MockKExtension::class))
internal class DefaultNewHabitComponentTest(
    @MockK(relaxed = true) private val lifecycle: Lifecycle,
    @MockK private val onNewHabitCreated: () -> Unit,
    @MockK private val habitRepository: HabitRepository,
    @MockK(relaxed = true) private val newHabitRetainedInstance: NewHabitRetainedInstance,
) {
    @Test
    @DisplayName("Проверка смена имени привычки")
    fun test_onNameChanged() {
        /* Given */
        every {
            newHabitRetainedInstance.mUiState
        } returns MutableValue(NewHabitUiState.DEFAULT.copy(name = "Old name", fieldState = FieldState.ERROR))
        @SpyK
        val component = createDefaultNewHabitComponent()

        /* When */
        component.onNameChanged("New name")

        /* Then */
        assertEquals("New name", component.uiState.value.name)
        assertEquals(FieldState.OKAY, component.uiState.value.fieldState)

        verifySequence {
            newHabitRetainedInstance.mUiState
            newHabitRetainedInstance.mUiState
        }

    }

    @Test
    @DisplayName("Проверка нажатия на кнопку создания привычки при заполненом поле имени")
    fun test_onCreateHabitClick_habitNameNotEmpty() {
        /* Given */
        justRun {
            newHabitRetainedInstance.createNewHabit(any(), any())
        }
        every {
            newHabitRetainedInstance.mUiState
        } returns MutableValue(NewHabitUiState.DEFAULT.copy(name = "Some name"))
        @SpyK
        val component = createDefaultNewHabitComponent()

        /* When */
        component.onCreateHabitClick()

        /* Then */
        assertEquals("Some name", component.uiState.value.name)

        verifySequence {
            newHabitRetainedInstance.mUiState
            newHabitRetainedInstance.createNewHabit(any(), any())
        }
    }

    @Test
    @DisplayName("Проверка нажатия на кнопку создания привычки при НЕ заполненом поле имени")
    fun test_onCreateHabitClick_habitNameEmpty() {
        /* Given */
        justRun {
            newHabitRetainedInstance.createNewHabit(any(), any())
        }
        every {
            newHabitRetainedInstance.mUiState
        } returns MutableValue(NewHabitUiState.DEFAULT)
        @SpyK
        val component = createDefaultNewHabitComponent()

        /* When */
        component.onCreateHabitClick()

        /* Then */
        assertEquals("", component.uiState.value.name)
        assertEquals(FieldState.ERROR, component.uiState.value.fieldState)

        verifySequence {
            newHabitRetainedInstance.mUiState
            newHabitRetainedInstance.mUiState
        }
    }

    private fun createDefaultNewHabitComponent(): DefaultNewHabitComponent {
        return DefaultNewHabitComponent(
            componentContext = DefaultComponentContext(
                lifecycle = lifecycle,
                instanceKeeper = TestInstanceKeeper(newHabitRetainedInstance)
            ),
            onNewHabitCreated = onNewHabitCreated,
            habitRepository = habitRepository
        )
    }
}
