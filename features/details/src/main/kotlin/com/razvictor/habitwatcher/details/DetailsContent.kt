package com.razvictor.habitwatcher.details

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.razvictor.habitwatcher.details.DetailsUiState.HeaderState
import com.razvictor.habitwatcher.features.details.R

@Composable
fun DetailsContent(
    component: DetailsComponent,
    modifier: Modifier = Modifier,
) {
    val uiState by component.uiState.subscribeAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        DetailsAppBar(uiState = uiState.headerState, component = component)
    }
}

@Composable
private fun DetailsAppBar(
    uiState: HeaderState,
    component: DetailsComponent,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    AnimatedVisibility(
        visible = uiState.state == HeaderState.State.EDIT,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        EditHeader(
            uiState = uiState,
            component = component,
            onBackClick = { onBackPressedDispatcher?.onBackPressed() },
        )
    }

    AnimatedVisibility(
        visible = uiState.state == HeaderState.State.DATA,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        DataAppBar(
            uiState = uiState,
            component = component,
            onBackClick = { onBackPressedDispatcher?.onBackPressed() },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DataAppBar(
    uiState: HeaderState,
    component: DetailsComponent,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(uiState.title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        },
        actions = {
            IconButton(onClick = component::onEditClick) { Icon(Icons.Filled.Edit, null) }
        },
    )
}

@Composable
private fun EditHeader(
    uiState: HeaderState,
    component: DetailsComponent,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        EditAppBar(
            component = component,
            onBackClick = onBackClick,
        )
        OutlinedTextField(
            value = uiState.title,
            onValueChange = component::onHeaderNameChanged,
            label = { Text(stringResource(R.string.habit_name_label)) },
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            isError = uiState.title.isEmpty(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditAppBar(
    component: DetailsComponent,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        },
        actions = {
            IconButton(onClick = component::onSaveClick) { Icon(Icons.Rounded.Check, null) }
            IconButton(onClick = component::onDeleteClick) { Icon(Icons.Filled.Delete, null) }
        },
    )
}
