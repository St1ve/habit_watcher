package com.razvictor.habitwatcher.new_habit

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.razvictor.habitwatcher.features.newhabit.R
import com.razvictor.habitwatcher.new_habit.NewHabitUiState.FieldState

const val SCREEN_PREFIX_TEST_TAG = "NewHabitContent"
fun screenTag(elementTag: String) = "${SCREEN_PREFIX_TEST_TAG}_$elementTag"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewHabitContent(
    component: NewHabitComponent,
    modifier: Modifier = Modifier,
) {
    val uiState by component.uiState.subscribeAsState()
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Scaffold(
        modifier = modifier.testTag(screenTag("container")),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.new_habit_title),
                        modifier = Modifier.testTag(screenTag("title_text"))
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressedDispatcher?.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            HabitName(
                name = uiState.name,
                fieldState = uiState.fieldState,
                onValueChange = component::onNameChanged,
            )
            CreateButton(onClick = component::onCreateHabitClick)
        }
    }
}

@Composable
private fun HabitName(
    name: String,
    fieldState: FieldState,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier
            .testTag(screenTag("habitName"))
            .padding(top = 16.dp)
            .padding(horizontal = 32.dp)
            .fillMaxWidth(),
        value = name,
        isError = fieldState == FieldState.ERROR,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.habit_name_label)) },
        maxLines = 3,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next
        ),
    )
}

@Composable
private fun CreateButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier
            .testTag(screenTag("createButton"))
            .padding(top = 18.dp, start = 32.dp, end = 32.dp),
        onClick = onClick,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            text = stringResource(R.string.create_habit_button_name)
        )
    }
}
