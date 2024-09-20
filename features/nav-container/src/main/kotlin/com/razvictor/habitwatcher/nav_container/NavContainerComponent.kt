package com.razvictor.habitwatcher.nav_container

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.razvictor.habitwatcher.habitlist.HabitListComponent
import kotlin.reflect.KClass

interface NavContainerComponent {
    val stack: Value<ChildStack<*, Child>>

    val uiState: Value<NavContainerUiState>
    fun onTabSelected(tabId: KClass<out Child>)

    sealed class Child {
        data class List(val component: HabitListComponent) : Child()
        data object Statistics : Child()
    }

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext) : NavContainerComponent
    }
}
