package com.razvictor.habitwatcher.habitlist

import com.arkivanov.decompose.ComponentContext

interface HabitListComponent {

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext) : HabitListComponent
    }
}
