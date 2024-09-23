package com.razvictor.habitwatcher.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.razvictor.habitwatcher.details.DetailsComponent
import com.razvictor.habitwatcher.nav_container.NavContainerComponent
import com.razvictor.habitwatcher.new_habit.NewHabitComponent
import com.razvictor.habitwatcher.root.RootComponent.Child.Details
import com.razvictor.habitwatcher.root.RootComponent.Child.NavContainer
import com.razvictor.habitwatcher.root.RootComponent.Child.NewHabit
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject internal constructor(
    @Assisted componentContext: ComponentContext,
    private val navContainerComponentFactory: NavContainerComponent.Factory,
    private val newHabitComponentFactory: NewHabitComponent.Factory,
    private val detailsComponentFactory: DetailsComponent.Factory,
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
        is Config.NewHabit -> NewHabit(newHabitComponent(context))
        is Config.Details -> Details(detailsComponent(habitId = config.habitId, context = context))
    }

    private fun newHabitComponent(context: ComponentContext): NewHabitComponent =
        newHabitComponentFactory(
            componentContext = context,
            onNewHabitCreated = { nav.pop() }
        )

    private fun navContainerComponent(context: ComponentContext): NavContainerComponent =
        navContainerComponentFactory(
            componentContext = context,
            onNewHabitClick = { nav.pushNew(Config.NewHabit) },
            onDetailsHabitClick = { habitId -> nav.pushNew(Config.Details(habitId = habitId)) }
        )

    private fun detailsComponent(habitId: Long, context: ComponentContext) =
        detailsComponentFactory(
            habitId = habitId,
            componentContext = context,
            onDeleteHabit = { nav.pop() }
        )

    @Serializable
    private sealed interface Config {
        @Serializable
        data object NavContainer : Config

        @Serializable
        data object NewHabit : Config

        @Serializable
        data class Details(val habitId: Long) : Config
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultRootComponent
    }
}
