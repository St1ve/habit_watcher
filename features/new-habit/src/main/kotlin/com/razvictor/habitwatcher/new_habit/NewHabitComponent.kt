package com.razvictor.habitwatcher.new_habit

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface NewHabitComponent {

    val uiState: Value<NewHabitUiState>

    fun onNameChanged(newName: String)
    fun onCreateHabitClick()

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext, onNewHabitCreated: () -> Unit): NewHabitComponent
    }
}
