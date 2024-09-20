package com.razvictor.habitwatcher.nav_container

import kotlin.reflect.KClass

data class NavContainerUiState(
    val tabs: List<TabState>,
) {
    data class TabState(
        val id: KClass<out NavContainerComponent.Child>,
        val isSelected: Boolean,
    )
}
