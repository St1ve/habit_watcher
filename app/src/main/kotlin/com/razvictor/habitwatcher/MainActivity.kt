package com.razvictor.habitwatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.razvictor.habitwatcher.root.RootContent
import com.razvictor.habitwatcher.uikit.HabitWatcherTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val component = DaggerMainDaggerComponent
            .factory()
            .create(appContext = applicationContext)
            .rootComponentFactory(defaultComponentContext())

        setContent {
            HabitWatcherTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        // FIXME: Check for new way to set system bar padding
                        .windowInsetsPadding(WindowInsets.systemBars),
                ) {
                    RootContent(component = component, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
