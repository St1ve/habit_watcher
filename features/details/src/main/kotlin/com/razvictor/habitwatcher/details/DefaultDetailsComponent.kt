package com.razvictor.habitwatcher.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.razvictor.habitwatcher.common.models.Habit
import com.razvictor.habitwatcher.common.repository.HabitRepository
import com.razvictor.habitwatcher.details.DetailsUiState.HeaderState
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
        retainedInstance.selectPrevMonth()
    }

    override fun onNextMonthClick() {
        retainedInstance.selectNextMonth()
    }

    override fun onCalendarCellClick(id: String) {
        retainedInstance.toggleHabitDone(
            habitId = habitId,
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
    private var selectedCalendarDate = LocalDate.now()
    private var habit: Habit? = null
    val mUiState = MutableValue(DetailsUiState.DEFAULT)

    init {
        // FIXME: Поправить логику, чтобы не брать сразу все данные, а вытаскивать порционно
        habitRepository
            .listenHabit(habitId)
            .onEach { habit ->
                this.habit = habit
                mUiState.update { habit.toUi(selectedCalendarDate) }
            }
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

    fun toggleHabitDone(habitId: Long, dateCompletionStr: String) {
        scope.launch {
            val dateCompletion = LocalDate.parse(dateCompletionStr)
            habitRepository.toggleHabit(habitId, dateCompletion)
        }
    }

    fun selectNextMonth() {
        selectedCalendarDate = selectedCalendarDate.plusMonths(1)
        mUiState.update { oldState -> habit?.toUi(selectedCalendarDate) ?: oldState }
    }

    fun selectPrevMonth() {
        selectedCalendarDate = selectedCalendarDate.minusMonths(1)
        mUiState.update { oldState -> habit?.toUi(selectedCalendarDate) ?: oldState }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}
