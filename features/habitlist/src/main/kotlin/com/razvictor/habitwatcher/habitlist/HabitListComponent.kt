package com.razvictor.habitwatcher.habitlist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface HabitListComponent {

    val uiState: Value<HabitListUiState>

    fun onNewHabitClick()
    fun onMarkHabitClick(id: Long, isDone: Boolean)
    fun onCardClick(id: Long)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onNewHabitClick: () -> Unit,
        ): HabitListComponent
    }
}
