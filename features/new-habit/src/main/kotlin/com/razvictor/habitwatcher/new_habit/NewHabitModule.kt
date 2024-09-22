package com.razvictor.habitwatcher.new_habit

import dagger.Binds
import dagger.Module

@Module
interface NewHabitModule {

    @Binds
    fun bindNewHabitComponentFactory(factory: DefaultNewHabitComponent.Factory): NewHabitComponent.Factory
}
