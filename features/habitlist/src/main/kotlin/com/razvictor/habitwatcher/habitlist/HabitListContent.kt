package com.razvictor.habitwatcher.habitlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.razvictor.habitwatcher.features.habitlist.R
import com.razvictor.habitwatcher.habitlist.HabitListUiState.HabitUiState

@Composable
fun HabitListContent(
    component: HabitListComponent,
    modifier: Modifier = Modifier
) {
    val uiState by component.uiState.subscribeAsState()

    var fabIsVisible by remember { mutableStateOf(true) }
    val fabNestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                when {
                    available.y < -1 -> fabIsVisible = false
                    available.y > 1 -> fabIsVisible = true
                }

                return Offset.Zero
            }
        }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            AnimatedVisibility(
                visible = fabIsVisible,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                FloatingActionButton(
                    onClick = { component.onNewHabitClick() },
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }

        }
    ) { innerPadding ->
        if (uiState.habits.isEmpty()) {
            EmptyList(Modifier.padding(innerPadding))
        } else {
            HabitList(
                fabNestedScrollConnection = fabNestedScrollConnection,
                habits = uiState.habits,
                onCardClick = component::onCardClick,
                onToggleClick = component::onMarkHabitClick,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun EmptyList(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.empty_list),
            modifier = Modifier.align(Alignment.Center),
            style = Typography().headlineLarge,
        )
    }
}

@Composable
private fun HabitList(
    fabNestedScrollConnection: NestedScrollConnection,
    habits: List<HabitUiState>,
    onCardClick: (Long) -> Unit,
    onToggleClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .nestedScroll(fabNestedScrollConnection)
    ) {
        items(
            count = habits.count(),
            key = { index -> habits[index].id },
        ) { index ->
            val habit = habits[index]
            HabitCard(
                habitState = habit,
                onCardClick = { onCardClick(habit.id) },
                onActionToggle = { onToggleClick(habit.id, habit.isDone) }
            )
        }
    }
}

@Composable
private fun HabitCard(
    habitState: HabitUiState,
    onCardClick: () -> Unit,
    onActionToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onCardClick,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = habitState.name,
                style = Typography().headlineMedium,
            )

            ActionButton(
                toggled = habitState.isDone,
                onActionToggle = onActionToggle,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
private fun ActionButton(
    toggled: Boolean,
    onActionToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier
            .animateContentSize()
            .padding(end = 8.dp),
        onClick = onActionToggle,
    ) {
        AnimatedVisibility(toggled) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
        val labelRes = if (toggled) R.string.action_button_toggled_label else R.string.action_button_untoggle_label
        Text(text = stringResource(labelRes))
    }
}
