package com.razvictor.habitwatcher.statistics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun StatisticsContent(
    component: StatisticsComponent,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier
            .fillMaxSize(),
    ) {
        // FIXME: Replace box
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text("Statistics")
        }
    }
}
