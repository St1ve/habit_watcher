package com.razvictor.habitwatcher.new_habit

data class NewHabitUiState(
    val name: String,
    val fieldState: FieldState,
) {
    enum class FieldState {
        ERROR, OKAY
    }

    companion object {
        val DEFAULT = NewHabitUiState(name = "", fieldState = FieldState.OKAY)
    }
}
