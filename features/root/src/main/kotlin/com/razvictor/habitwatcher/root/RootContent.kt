package com.razvictor.habitwatcher.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.razvictor.habitwatcher.nav_container.NavContainerContent
import com.razvictor.habitwatcher.new_habit.NewHabitContent

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.NavContainer -> NavContainerContent(child.component)
            is RootComponent.Child.NewHabit -> NewHabitContent(child.component)
        }
    }
}
