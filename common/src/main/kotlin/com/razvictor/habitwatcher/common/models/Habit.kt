package com.razvictor.habitwatcher.common.models

import java.time.LocalDate

data class Habit(
    val id: Long,
    val name: String,
    val datesCompleted: List<LocalDate>,
)
