package com.razvictor.habitwatcher.new_habit

import com.arkivanov.decompose.ComponentContext

interface NewHabitComponent {

    fun onCreateHabitClick()

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext): NewHabitComponent
    }
}
