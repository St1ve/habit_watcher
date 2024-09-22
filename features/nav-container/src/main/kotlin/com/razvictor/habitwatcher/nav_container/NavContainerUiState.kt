package com.razvictor.habitwatcher.nav_container

import kotlin.reflect.KClass

data class NavContainerUiState(
    val tabs: List<TabState>,
) {
    data class TabState(
        val id: KClass<out NavContainerComponent.Child>,
        val isSelected: Boolean,
    )

    companion object {
        val DEFAULT = NavContainerUiState(
            tabs = listOf(
                TabState(
                    id = NavContainerComponent.Child.List::class,
                    isSelected = true,
                ),
                TabState(
                    id = NavContainerComponent.Child.Statistics::class,
                    isSelected = false,
                )
            )
        )
    }
}
