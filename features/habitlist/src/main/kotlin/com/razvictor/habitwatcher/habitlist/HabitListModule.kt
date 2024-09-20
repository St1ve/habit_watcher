package com.razvictor.habitwatcher.habitlist

import dagger.Binds
import dagger.Module

@Module
interface HabitListModule {
    @Binds
    fun componentFactory(factory: DefaultHabitListComponent.Factory) : HabitListComponent.Factory
}
