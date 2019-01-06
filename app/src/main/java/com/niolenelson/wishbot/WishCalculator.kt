package com.niolenelson.wishbot

import java.time.LocalDateTime
import java.time.ZoneId

/**
 * wish times are always same number for day, month, hour, minute
 */
object WishCalculator {
    fun isWishTime(date: LocalDateTime? = LocalDateTime.now()): Boolean {
        val month = date!!.monthValue
        val day = date.dayOfMonth
        val hour = date.hour
        val minute = date.minute

        //return month == day && month == hour && month == minute
        return true
    }

    fun getNextWishTime(date: LocalDateTime? = LocalDateTime.now()): Long {
        val year = date!!.year
        val month = date.monthValue
        val day = date.dayOfMonth
        val hour = date.hour
        val minute = date.minute

        var alarmMonth = month
        var alarmYear = year
        if (day > month || hour > month || minute > month) {
            // you missed all opportunities this month
            alarmMonth = month + 1
        }

        if (alarmMonth > 12) {
            alarmMonth -= 12
            alarmYear = year + 1
        }

        val nextAlarmDate = LocalDateTime.of(alarmYear, alarmMonth, alarmMonth, alarmMonth, alarmMonth)

        return nextAlarmDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}