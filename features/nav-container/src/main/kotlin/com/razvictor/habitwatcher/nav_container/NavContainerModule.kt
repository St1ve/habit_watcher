package com.razvictor.habitwatcher.nav_container

import com.razvictor.habitwatcher.habitlist.HabitListModule
import dagger.Binds
import dagger.Module

@Module(includes = [HabitListModule::class])
interface NavContainerModule {
    @Binds
    fun componentFactory(factory: DefaultNavContainerComponent.Factory) : NavContainerComponent.Factory
}
