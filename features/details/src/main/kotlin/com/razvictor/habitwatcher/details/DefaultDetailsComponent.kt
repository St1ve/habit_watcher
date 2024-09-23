package com.razvictor.habitwatcher.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DefaultDetailsComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("habitId") private val habitId: Long,
    @Assisted private val onDeleteHabit: () -> Unit,
    private val habitRepository: HabitRepository,
) : DetailsComponent, ComponentContext by componentContext {

    private val retainedInstance = instanceKeeper.getOrCreate {
        DetailsRetainedInstance(habitRepository = habitRepository)
    }
    override val uiState: Value<DetailsUiState> = retainedInstance.mUiState

    init {
        retainedInstance.getHabit(habitId)
    }

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
    private val habitRepository: HabitRepository,
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    val mUiState = MutableValue(DetailsUiState.DEFAULT)

    fun getHabit(habitId: Long) {
        habitRepository
            .listenHabit(habitId)
            .onEach { habit ->
                // TODO: Подумать, что если редактировать имя и прожимать на календаре статусы одновременно
                mUiState.update { habit.toUi() }
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

    override fun onDestroy() {
        scope.cancel()
    }
}
