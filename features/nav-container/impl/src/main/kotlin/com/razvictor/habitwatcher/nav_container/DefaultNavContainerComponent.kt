package com.razvictor.habitwatcher.nav_container

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

class DefaultNavContainerComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
) : NavContainerComponent, ComponentContext by componentContext {

    private val nav = StackNavigation<Config>()

//    override val stack: Value<ChildStack<*, NavContainerComponent.Child>> = childStack(
//        source = nav,
//        initialConfiguration = Config.List,
//        serializer = Config.serializer(),
//        handleBackButton = true,
//        childFactory = ::child,
//    )
//
//    private fun child(config: Config, context: ComponentContext): RootComponent.Child = when (config) {
//        is Config.List -> NavContainer(navContainerComponent(context))
//    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object List : Config
    }

    @AssistedFactory
    fun interface Factory : NavContainerComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultNavContainerComponent
    }
}
