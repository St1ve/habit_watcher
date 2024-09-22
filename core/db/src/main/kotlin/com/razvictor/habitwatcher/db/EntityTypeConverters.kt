package com.razvictor.habitwatcher.db

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class EntityTypeConverters {
    @TypeConverter
    fun fromLocalDateToLong(date: LocalDate): Long {
        val zoneId: ZoneId = ZoneId.systemDefault()
        return date.atStartOfDay(zoneId).toEpochSecond()
    }

    @TypeConverter
    fun fromTimestampToLocalDate(timeStamp: Long): LocalDate {
        return LocalDate.ofInstant(Instant.ofEpochSecond(timeStamp), ZoneId.systemDefault())
    }

    @TypeConverter
    fun fromStringToList(str: String?): List<LocalDate> {
        val timeStamps = if (str.isNullOrEmpty()) {
            emptyList()
        } else {
            str.split(",")
        }

        val zoneId = ZoneId.systemDefault()
        return timeStamps.map { timeStampStr ->
            LocalDate.ofInstant(Instant.ofEpochSecond(timeStampStr.toLong()), zoneId)
        }
    }
}
