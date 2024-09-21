package com.razvictor.habitwatcher.statistics

import dagger.Binds
import dagger.Module

@Module
interface StatisticsModule {

    @Binds
    fun componentFactory(factory: DefaultStatisticsComponent.Factory) : StatisticsComponent.Factory
}
