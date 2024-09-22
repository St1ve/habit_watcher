package com.razvictor.habitwatcher.common.mappers

import com.razvictor.habitwatcher.common.models.Habit
import com.razvictor.habitwatcher.db.dbs.HabitDb

internal fun List<HabitDb>.toDomain() = this.map { habitDb -> habitDb.toDomain() }
internal fun HabitDb.toDomain() = Habit(
    id = this.id,
    name = this.name,
    datesCompleted = this.datesCompleted,
)
