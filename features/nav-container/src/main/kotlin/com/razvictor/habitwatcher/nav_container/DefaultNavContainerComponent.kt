package com.razvictor.habitwatcher.nav_container

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.razvictor.habitwatcher.habitlist.HabitListComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

class DefaultNavContainerComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
    private val habitListComponentFactory: HabitListComponent.Factory,
) : NavContainerComponent, ComponentContext by componentContext {

    private val _uiState: MutableValue<NavContainerUiState> = MutableValue(
        NavContainerUiState(
            tabs = listOf(
                NavContainerUiState.TabState(
                    id = NavContainerComponent.Child.List::class,
                    isSelected = true,
                ),
                NavContainerUiState.TabState(
                    id = NavContainerComponent.Child.Statistics::class,
                    isSelected = false,
                )
            )
        )
    )
    override val uiState: Value<NavContainerUiState> = _uiState

    override fun onTabSelected(tabId: KClass<out NavContainerComponent.Child>) {
        _uiState.update { currentState ->
            NavContainerUiState(currentState.tabs.map { tab -> tab.copy(isSelected = tab.id == tabId) })
        }
        val config = when (tabId) {
            NavContainerComponent.Child.List::class -> Config.List
            NavContainerComponent.Child.Statistics::class -> Config.Statistics
            else -> error("Unknown tabId: $tabId")
        }
        nav.push(config)
    }

    private val nav = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, NavContainerComponent.Child>> = childStack(
        source = nav,
        initialConfiguration = Config.List,
        serializer = Config.serializer(),
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(config: Config, context: ComponentContext): NavContainerComponent.Child = when (config) {
        is Config.List -> NavContainerComponent.Child.List(habitListComponentFactory(context))
        is Config.Statistics -> NavContainerComponent.Child.Statistics
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object List : Config

        @Serializable
        data object Statistics : Config
    }

    @AssistedFactory
    fun interface Factory : NavContainerComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultNavContainerComponent
    }
}
