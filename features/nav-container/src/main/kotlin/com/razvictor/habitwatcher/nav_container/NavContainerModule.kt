package com.razvictor.habitwatcher.nav_container

import com.razvictor.habitwatcher.habitlist.HabitListModule
import com.razvictor.habitwatcher.statistics.StatisticsModule
import dagger.Binds
import dagger.Module

@Module(includes = [HabitListModule::class, StatisticsModule::class])
interface NavContainerModule {
    @Binds
    fun componentFactory(factory: DefaultNavContainerComponent.Factory) : NavContainerComponent.Factory
}
