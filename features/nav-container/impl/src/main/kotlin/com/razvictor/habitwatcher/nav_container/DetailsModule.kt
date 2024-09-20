package com.razvictor.habitwatcher.nav_container

import dagger.Binds
import dagger.Module

@Module
interface DetailsModule {

    @Binds
    fun componentFactory(impl: DefaultNavContainerComponent.Factory): NavContainerComponent.Factory
}
