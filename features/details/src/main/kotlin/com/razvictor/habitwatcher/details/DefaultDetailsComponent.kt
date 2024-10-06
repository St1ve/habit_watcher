package com.razvictor.habitwatcher.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.razvictor.habitwatcher.common.repository.HabitRepository
import com.razvictor.habitwatcher.details.DetailsUiState.HeaderState
import com.razvictor.habitwatcher.uikit.component.calendar.CalendarState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate

class DefaultDetailsComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("habitId") private val habitId: Long,
    @Assisted private val onDeleteHabit: () -> Unit,
    private val habitRepository: HabitRepository,
) : DetailsComponent, ComponentContext by componentContext {

    private val retainedInstance = instanceKeeper.getOrCreate {
        DetailsRetainedInstance(habitId = habitId, habitRepository = habitRepository)
    }
    override val uiState: Value<DetailsUiState> = retainedInstance.mUiState

    override fun onSaveClick() {
        retainedInstance.saveChanges(habitId = habitId, newName = uiState.value.headerState.title)
    }

    override fun onDeleteClick() {
        retainedInstance.deleteHabit(habitId) { onDeleteHabit() }
    }

    override fun onHeaderNameChanged(newTitle: String) {
        retainedInstance.mUiState.update { oldState ->
            oldState.copy(headerState = oldState.headerState.copy(title = newTitle))
        }
    }

    override fun onEditClick() {
        retainedInstance.mUiState.update { oldState ->
            oldState.copy(headerState = oldState.headerState.copy(state = HeaderState.State.EDIT))
        }
    }

    override fun onPreviousMonthClick() {
        TODO("Not yet implemented")
    }

    override fun onNextMonthClick() {
        TODO("Not yet implemented")
    }

    override fun onCalendarCellClick(id: String) {
        // FIXME: Fix this logic
        val isDone = uiState.value.calendarState.gridState.weeks.find {
            it.cells.find { cell ->
                cell is CalendarState.GridState.WeekState.CellState.Data
                    && cell.id == id
                    && cell.backgroundAlpha > 0
            } != null
        } != null
        retainedInstance.toggleHabitDone(
            habitId = habitId,
            isDone = isDone,
            dateCompletionStr = id,
        )
    }

    @AssistedFactory
    fun interface Factory : DetailsComponent.Factory {
        override fun invoke(
            @Assisted("habitId") habitId: Long,
            onDeleteHabit: () -> Unit,
            componentContext: ComponentContext
        ): DefaultDetailsComponent
    }
}

internal class DetailsRetainedInstance(
    habitId: Long,
    private val habitRepository: HabitRepository,
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    val mUiState = MutableValue(DetailsUiState.DEFAULT)

    init {
        habitRepository
            .listenHabit(habitId)
            .onEach { habit -> mUiState.update { habit.toUi() } }
            .launchIn(scope)

    }

    fun deleteHabit(habitId: Long, onComplete: () -> Unit) {
        scope.launch {
            habitRepository.deleteHabit(habitId)
        }.invokeOnCompletion {
            onComplete()
        }
    }

    fun saveChanges(habitId: Long, newName: String) {
        scope.launch {
            habitRepository.editHabit(id = habitId, name = newName)
        }
    }

    fun toggleHabitDone(habitId: Long, isDone: Boolean, dateCompletionStr: String) {
        scope.launch {
            val dateCompletion = LocalDate.parse(dateCompletionStr)
            if (isDone) {
                habitRepository.resetCompletionHabit(habitId, dateCompletion)
            } else {
                habitRepository.completeHabit(habitId, dateCompletion)
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}
