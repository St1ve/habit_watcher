package com.razvictor.habitwatcher.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.razvictor.habitwatcher.nav_container.NavContainerComponent
import com.razvictor.habitwatcher.root.RootComponent.Child.NavContainer
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
    private val navContainerComponentFactory: NavContainerComponent.Factory,
) : RootComponent, ComponentContext by componentContext {

    private val nav = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = nav,
            initialConfiguration = Config.NavContainer,
            serializer = Config.serializer(),
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(config: Config, context: ComponentContext): RootComponent.Child = when (config) {
        is Config.NavContainer -> NavContainer(navContainerComponent(context))
    }

    private fun navContainerComponent(context: ComponentContext): NavContainerComponent =
        navContainerComponentFactory(componentContext = context)

    @Serializable
    private sealed interface Config {
        @Serializable
        data object NavContainer : Config
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultRootComponent
    }
}
