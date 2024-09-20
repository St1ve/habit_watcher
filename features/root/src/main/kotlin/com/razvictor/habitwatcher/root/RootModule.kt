package com.razvictor.habitwatcher.root

import com.razvictor.habitwatcher.nav_container.NavContainerModule
import dagger.Binds
import dagger.Module

@Module(includes = [NavContainerModule::class])
interface RootModule {

    @Binds
    fun componentFactory(factory: DefaultRootComponent.Factory) : RootComponent.Factory
}
