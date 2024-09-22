package com.razvictor.habitwatcher.habitlist

data class HabitListUiState(
    val habits: List<HabitUiState>,
) {
   data class HabitUiState(
       val id: Long,
       val name: String,
       val isDone: Boolean,
   )

    companion object {
        val DEFAULT = HabitListUiState(habits = emptyList())
    }
}
