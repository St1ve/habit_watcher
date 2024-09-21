package com.razvictor.habitwatcher.habitlist

data class HabitListUiState(
    val habits: List<HabitUiState>,
) {
   data class HabitUiState(
       val id: Int,
       val name: String,
       val isDone: Boolean,
   )
}
