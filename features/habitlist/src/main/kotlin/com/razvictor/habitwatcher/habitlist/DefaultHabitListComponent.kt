package com.razvictor.habitwatcher.habitlist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.razvictor.habitwatcher.common.repository.HabitRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate

class DefaultHabitListComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onNewHabitClick: () -> Unit,
    private val habitRepository: HabitRepository,
) : HabitListComponent, ComponentContext by componentContext {

    private val retainedInstance = instanceKeeper.getOrCreate {
        HabitListRetainedInstance(habitRepository)
    }
    override val uiState: Value<HabitListUiState> = retainedInstance.mUiState

    init {
        retainedInstance.listenHabits()
    }

    override fun onNewHabitClick() {
        onNewHabitClick.invoke()
    }

    override fun onMarkHabitClick(id: Long, isDone: Boolean) {
        retainedInstance.toggleHabitDone(habitId = id, isDone = isDone)
    }

    override fun onCardClick(id: Long) {
        TODO("Not yet implemented")
    }

    @AssistedFactory
    interface Factory : HabitListComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onNewHabitClick: () -> Unit,
        ): DefaultHabitListComponent
    }
}

internal class HabitListRetainedInstance(
    private val habitRepository: HabitRepository,
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(SupervisorJob())
    val mUiState = MutableValue(HabitListUiState.DEFAULT)

    fun listenHabits() {
        habitRepository
            .listenHabits()
            .onEach { habits ->
                mUiState.update { oldState -> oldState.copy(habits = habits.toUi()) }
            }
            .launchIn(scope)
    }

    fun toggleHabitDone(habitId: Long, isDone: Boolean) {
        scope.launch {
            val currentDate = LocalDate.now()
            if (isDone) {
                habitRepository.resetCompletionHabit(habitId, currentDate)
            } else {
                habitRepository.completeHabit(habitId, currentDate)
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}
