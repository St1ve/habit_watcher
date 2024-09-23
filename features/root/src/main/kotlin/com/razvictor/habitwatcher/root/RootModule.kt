package com.razvictor.habitwatcher.root

import com.razvictor.habitwatcher.details.DetailsModule
import com.razvictor.habitwatcher.nav_container.NavContainerModule
import com.razvictor.habitwatcher.new_habit.NewHabitModule
import dagger.Binds
import dagger.Module

@Module(includes = [
    NavContainerModule::class,
    NewHabitModule::class,
    DetailsModule::class,
])
interface RootModule {

    @Binds
    fun componentFactory(factory: DefaultRootComponent.Factory) : RootComponent.Factory
}
