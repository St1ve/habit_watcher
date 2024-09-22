package com.razvictor.habitwatcher

import android.content.Context
import com.razvictor.habitwatcher.db.AppDatabase
import com.razvictor.habitwatcher.db.DataBaseModule
import com.razvictor.habitwatcher.root.RootComponent
import com.razvictor.habitwatcher.root.RootModule
import com.razvictor.habitwathcer.di.qualifiers.AppContext
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    RootModule::class,
    DataBaseModule::class,
])
interface MainDaggerComponent {

    val rootComponentFactory : RootComponent.Factory

    @AppContext
    fun provideApplicationContext() : Context

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @AppContext
            appContext : Context,
        ) : MainDaggerComponent
    }
}
