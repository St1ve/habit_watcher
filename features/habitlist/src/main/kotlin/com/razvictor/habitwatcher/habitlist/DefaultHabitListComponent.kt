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
import kotlinx.coroutines.Dispatchers
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
    @Assisted private val onDetailsHabitClick: (Long) -> Unit,
) : HabitListComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val retainedInstance = instanceKeeper.getOrCreate {
        HabitListRetainedInstance(habitRepository)
    }

    init {
        habitRepository
            .listenHabits()
            .onEach { habits ->
                retainedInstance.mUiState.update { oldState -> oldState.copy(habits = habits.toUi()) }
            }
            .launchIn(scope)
    }

    override val uiState: Value<HabitListUiState> = retainedInstance.mUiState

    override fun onNewHabitClick() {
        onNewHabitClick.invoke()
    }

    override fun onMarkHabitClick(id: Long) {
        retainedInstance.toggleHabitDone(habitId = id)
    }

    override fun onCardClick(id: Long) {
        onDetailsHabitClick(id)
    }

    @AssistedFactory
    interface Factory : HabitListComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onNewHabitClick: () -> Unit,
            onDetailsHabitClick: (Long) -> Unit,
        ): DefaultHabitListComponent
    }
}

internal class HabitListRetainedInstance(
    private val habitRepository: HabitRepository,
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    val mUiState = MutableValue(HabitListUiState.DEFAULT)

    fun toggleHabitDone(habitId: Long) {
        scope.launch {
            val currentDate = LocalDate.now()
            habitRepository.toggleHabit(habitId, currentDate)
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }
}
