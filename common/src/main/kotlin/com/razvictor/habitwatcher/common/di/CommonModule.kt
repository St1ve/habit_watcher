package com.razvictor.habitwatcher.common.di

import com.razvictor.habitwatcher.common.repository.HabitRepository
import com.razvictor.habitwatcher.common.repository.HabitRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface CommonModule {
    @Binds
    fun bindHabitRepository(repository: HabitRepositoryImpl): HabitRepository
}
