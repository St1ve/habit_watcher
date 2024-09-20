package com.razvictor.habitwatcher.habitlist

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultHabitListComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
) : HabitListComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : HabitListComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultHabitListComponent
    }
}
