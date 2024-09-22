package com.razvictor.habitwatcher.new_habit

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.razvictor.habitwatcher.common.repository.HabitRepository
import com.razvictor.habitwatcher.new_habit.NewHabitUiState.FieldState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DefaultNewHabitComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onNewHabitCreated: () -> Unit,
    private val habitRepository: HabitRepository,
) : NewHabitComponent, ComponentContext by componentContext {

    private val retainedInstance = instanceKeeper.getOrCreate {
        NewHabitRetainedInstance(habitRepository = habitRepository)
    }
    override val uiState: Value<NewHabitUiState> = retainedInstance.mUiState

    override fun onNameChanged(newName: String) {
        retainedInstance.mUiState.update { oldUiState ->
            oldUiState.copy(name = newName, fieldState = FieldState.OKAY)
        }
    }

    override fun onCreateHabitClick() {
        val name = uiState.value.name
        if (name.isEmpty()) {
            retainedInstance.mUiState.update { oldState -> oldState.copy(fieldState = FieldState.ERROR) }
            return
        }
        retainedInstance.createNewHabit(name = name, onNewHabitCreated = onNewHabitCreated)
    }

    @AssistedFactory
    interface Factory : NewHabitComponent.Factory {
        override fun invoke(componentContext: ComponentContext, onNewHabitCreated: () -> Unit): DefaultNewHabitComponent
    }
}

internal class NewHabitRetainedInstance(
    private val habitRepository: HabitRepository,
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    val mUiState = MutableValue(NewHabitUiState.DEFAULT)

    fun createNewHabit(
        name: String,
        onNewHabitCreated: () -> Unit,
    ) {
        scope.launch {
            habitRepository.createHabit(name)
        }.invokeOnCompletion {
            onNewHabitCreated()
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}
