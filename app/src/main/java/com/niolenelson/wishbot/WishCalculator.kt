package com.niolenelson.wishbot

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * wish times are always same number for day, month, hour, minute
 */
open class WishCalculator {
    open fun getCurrentDate(): LocalDateTime {
        return LocalDateTime.now()
    }

    open fun isWishTime(): Boolean {
        val date = getCurrentDate()
        val month = date.monthValue
        val day = date.dayOfMonth
        val hour = date.hour
        val minute = date.minute

        return  month == day && month == hour && month == minute
    }

    open fun getNextWishTime(): Long {
        val date = LocalDateTime.now()
        val year = date.year
        val month = date.monthValue
        val day = date.dayOfMonth

        var alarmMonth = month
        var alarmYear = year
        if (day > month) {
            // you missed all opportunities this month
            alarmMonth = month + 1
        }

        if (alarmMonth > 12) {
            alarmMonth = alarmMonth.rem(12)
            alarmYear = year + 1
        }
        val nextAlarmDate = LocalDateTime.of(alarmYear, alarmMonth, alarmMonth, alarmMonth, alarmMonth)

        return nextAlarmDate.format(DateTimeFormatter.BASIC_ISO_DATE).toLong()
    }
}