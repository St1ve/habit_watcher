package com.razvictor.habitwatcher.habitlist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultHabitListComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onNewHabitClick: () -> Unit,
) : HabitListComponent, ComponentContext by componentContext {

    override fun onNewHabitClick() { onNewHabitClick.invoke() }

    private val _uiState = MutableValue(HabitListUiState(emptyList()))
    override val uiState: Value<HabitListUiState> = _uiState

    @AssistedFactory
    interface Factory : HabitListComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onNewHabitClick: () -> Unit,
        ): DefaultHabitListComponent
    }
}
