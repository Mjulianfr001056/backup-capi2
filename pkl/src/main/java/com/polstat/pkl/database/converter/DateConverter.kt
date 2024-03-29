package com.polstat.pkl.database.converter

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}

/**
 * QC 23/Jan/2024
 * Hapus redundant date?.time.toLong() ke date?.time
 */