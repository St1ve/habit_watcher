package com.razvictor.habitwatcher.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.razvictor.habitwatcher.nav_container.NavContainerComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class NavContainer(val component: NavContainerComponent) : Child()
    }

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext) : RootComponent
    }
}
