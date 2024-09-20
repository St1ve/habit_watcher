package com.razvictor.habitwatcher.nav_container

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface NavContainerComponent {
    // FIXME: Uncomment
//    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        data object List : Child()
        data object Statistics : Child()
    }

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext) : NavContainerComponent
    }
}
