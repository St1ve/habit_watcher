package com.razvictor.habitwatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import com.razvictor.habitwatcher.root.RootContent
import com.razvictor.habitwatcher.uikit.HabitWatcherTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val component = DaggerMainDaggerComponent.create().rootComponentFactory(defaultComponentContext())

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
