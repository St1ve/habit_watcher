package com.razvictor.habitwatcher.nav_container

import dagger.Binds
import dagger.Module

@Module
interface NavContainerModule {
    @Binds
    fun componentFactory(factory: DefaultNavContainerComponent.Factory) : NavContainerComponent.Factory
}
