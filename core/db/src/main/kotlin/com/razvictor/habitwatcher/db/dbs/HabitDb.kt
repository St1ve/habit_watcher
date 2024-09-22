package com.razvictor.habitwatcher.db.dbs

import java.time.LocalDate

data class HabitDb(
    val id: Long,
    val name: String,
    val datesCompleted: List<LocalDate>,
)
