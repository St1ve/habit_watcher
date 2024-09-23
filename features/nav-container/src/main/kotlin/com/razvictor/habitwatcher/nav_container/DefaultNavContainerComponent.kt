package com.razvictor.habitwatcher.nav_container

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.razvictor.habitwatcher.habitlist.HabitListComponent
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
    @Assisted private val onDetailsHabitClick: (Long) -> Unit,
) : NavContainerComponent, ComponentContext by componentContext {

    private val retainedInstance = instanceKeeper.getOrCreate { NavContainerRetainedInstance() }
    override val uiState: Value<NavContainerUiState> = retainedInstance.mUiState

    override fun onTabSelected(tabId: KClass<out NavContainerComponent.Child>) {
        retainedInstance.mUiState.update { currentState ->
            NavContainerUiState(currentState.tabs.map { tab -> tab.copy(isSelected = tab.id == tabId) })
        }
        val config = when (tabId) {
            NavContainerComponent.Child.List::class -> Config.List
            NavContainerComponent.Child.Statistics::class -> Config.Statistics
            else -> error("Unknown tabId: $tabId")
        }
        nav.replaceCurrent(config)
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
        is Config.List -> NavContainerComponent.Child.List(habitListComponent(context))
        is Config.Statistics -> NavContainerComponent.Child.Statistics(statisticsComponentFactory(context))
    }

    private fun habitListComponent(context: ComponentContext): HabitListComponent =
        habitListComponentFactory(
            componentContext = context,
            onNewHabitClick = onNewHabitClick,
            onDetailsHabitClick = onDetailsHabitClick,
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
            onDetailsHabitClick: (Long) -> Unit,
        ): DefaultNavContainerComponent
    }
}

internal class NavContainerRetainedInstance : InstanceKeeper.Instance {
    val mUiState = MutableValue(NavContainerUiState.DEFAULT)
}
