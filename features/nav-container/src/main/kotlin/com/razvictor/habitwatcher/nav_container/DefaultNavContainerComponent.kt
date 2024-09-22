package com.razvictor.habitwatcher.nav_container

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.razvictor.habitwatcher.habitlist.HabitListComponent
import com.razvictor.habitwatcher.new_habit.NewHabitComponent
import com.razvictor.habitwatcher.statistics.StatisticsComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

class DefaultNavContainerComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
    private val habitListComponentFactory: HabitListComponent.Factory,
    private val statisticsComponentFactory: StatisticsComponent.Factory,
    @Assisted private val onNewHabitClick: () -> Unit,
) : NavContainerComponent, ComponentContext by componentContext {

    // FIXME: Save state on configuration changed
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
        nav.bringToFront(config)
    }

    private val nav = StackNavigation<Config>()

    // FIXME: Remove stack
    override val stack: Value<ChildStack<*, NavContainerComponent.Child>> = childStack(
        source = nav,
        initialConfiguration = Config.List,
        serializer = Config.serializer(),
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(config: Config, context: ComponentContext): NavContainerComponent.Child = when (config) {
        is Config.List -> NavContainerComponent.Child.List(habitListComponent(context))
        is Config.Statistics -> NavContainerComponent.Child.Statistics(statisticsComponentFactory(context))
    }

    private fun habitListComponent(context: ComponentContext): HabitListComponent =
        habitListComponentFactory(
            componentContext = context,
            onNewHabitClick = onNewHabitClick,
        )

    @Serializable
    private sealed interface Config {

        @Serializable
        data object List : Config

        @Serializable
        data object Statistics : Config
    }

    @AssistedFactory
    fun interface Factory : NavContainerComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onNewHabitClick: () -> Unit,
        ): DefaultNavContainerComponent
    }
}
