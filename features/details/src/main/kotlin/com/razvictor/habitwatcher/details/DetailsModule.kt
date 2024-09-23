package com.razvictor.habitwatcher.details

import dagger.Binds
import dagger.Module

@Module
interface DetailsModule {
    @Binds
    fun componentFactory(factory: DefaultDetailsComponent.Factory) : DetailsComponent.Factory
}
