package com.razvictor.habitwatcher.statistics

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultStatisticsComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
) : StatisticsComponent, ComponentContext by componentContext {

    @AssistedFactory
    fun interface Factory : StatisticsComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultStatisticsComponent
    }
}
