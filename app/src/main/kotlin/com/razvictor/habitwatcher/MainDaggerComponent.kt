package com.razvictor.habitwatcher

import com.razvictor.habitwatcher.root.RootComponent
import com.razvictor.habitwatcher.root.RootModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RootModule::class])
interface MainDaggerComponent {

    val rootComponentFactory : RootComponent.Factory
}
