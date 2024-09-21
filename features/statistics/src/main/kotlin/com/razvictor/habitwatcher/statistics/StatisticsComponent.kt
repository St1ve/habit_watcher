package com.razvictor.habitwatcher.statistics

import com.arkivanov.decompose.ComponentContext

interface StatisticsComponent {

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext): StatisticsComponent
    }
}
