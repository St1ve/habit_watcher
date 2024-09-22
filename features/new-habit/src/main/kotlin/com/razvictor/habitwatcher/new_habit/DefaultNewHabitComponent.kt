package com.razvictor.habitwatcher.new_habit

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultNewHabitComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
) : NewHabitComponent, ComponentContext by componentContext {

    override fun onCreateHabitClick() {
        TODO("Not yet implemented")
    }

    @AssistedFactory
    interface Factory : NewHabitComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultNewHabitComponent
    }
}
