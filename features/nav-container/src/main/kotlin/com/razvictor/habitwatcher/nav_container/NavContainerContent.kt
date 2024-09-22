package com.razvictor.habitwatcher.nav_container

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.razvictor.habitwatcher.features.nav_container.R
import com.razvictor.habitwatcher.habitlist.HabitListContent
import com.razvictor.habitwatcher.statistics.StatisticsContent

@Composable
fun NavContainerContent(
    component: NavContainerComponent,
    modifier: Modifier = Modifier,
) {
    val uiState by component.uiState.subscribeAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                uiState.tabs.forEach { tab ->
                    val (label, icon) = when (tab.id) {
                        NavContainerComponent.Child.List::class -> {
                            stringResource(R.string.tab_list) to Icons.Filled.Home
                        }

                        NavContainerComponent.Child.Statistics::class -> {
                            stringResource(R.string.tab_statistics) to Icons.Filled.Info
                        }

                        else -> error("Unknown id: ${tab.id}")
                    }

                    NavigationBarItem(
                        icon = {
                            when (tab.id) {
                                NavContainerComponent.Child.List::class -> Icon(icon, contentDescription = null)
                                NavContainerComponent.Child.Statistics::class -> Icon(icon, contentDescription = null)
                            }
                        },
                        label = { Text(label) },
                        selected = tab.isSelected,
                        onClick = { component.onTabSelected(tab.id) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Children(
            stack = component.stack,
            modifier = modifier.padding(innerPadding),
        ) {
            when (val child = it.instance) {
                is NavContainerComponent.Child.List -> HabitListContent(child.component)
                is NavContainerComponent.Child.Statistics -> StatisticsContent(child.component)
            }
        }
    }
}
