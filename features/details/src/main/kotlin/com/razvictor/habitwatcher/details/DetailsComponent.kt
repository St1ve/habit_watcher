package com.razvictor.habitwatcher.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface DetailsComponent {
    val uiState: Value<DetailsUiState>

    fun onSaveClick()
    fun onDeleteClick()
    fun onHeaderNameChanged(newTitle: String)
    fun onEditClick()

    fun onPreviousMonthClick()
    fun onNextMonthClick()
    fun onCalendarCellClick(id: String)

    fun interface Factory {
        operator fun invoke(
            habitId: Long,
            onDeleteHabit: () -> Unit,
            componentContext: ComponentContext
        ): DetailsComponent
    }
}
