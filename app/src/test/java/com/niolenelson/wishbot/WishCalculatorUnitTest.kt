package com.niolenelson.wishbot

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WishCalculatorUnitTest {

    @Test
    fun is_wish_time_when_month_day_hour_minute_equal() {
        val currentDate = LocalDateTime.of(1992, 5, 5, 5, 5)
        assertTrue(WishCalculator.isWishTime(currentDate))
    }

    @Test
    fun isnt_wish_time_when_month_day_hour_minute_not_equal() {
        val currentDate = LocalDateTime.of(1992, 5, 5, 5, 6)
        assertFalse(WishCalculator.isWishTime(currentDate))
    }

    @Test
    fun next_wish_time_should_be_after_1992_5month_5day_5hour_6minute() {
        val currentDate = LocalDateTime.of(1992, 5, 5, 5, 6)

        val currentDateMS = currentDate.format(DateTimeFormatter.BASIC_ISO_DATE).toLong()
        val nextWishTime = WishCalculator.getNextWishTime(currentDate)

        assertTrue(nextWishTime > currentDateMS)
    }

    @Test
    fun next_wish_time_should_be_next_month_if_a_minute_past_wish_time() {
        val currentDate = LocalDateTime.of(1992, 5, 5, 5, 6)

        val expectedDate = LocalDateTime.of(1992, 6, 6, 6, 6)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        assertEquals(Date(expectedDate), Date(WishCalculator.getNextWishTime(currentDate)))
    }

    @Test
    fun next_wish_time_should_be_next_month_if_an_hour_past_wish_time() {
        val currentDate = LocalDateTime.of(1992, 5, 5, 6, 5)

        val expectedDate = LocalDateTime.of(1992, 6, 6, 6, 6)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        assertEquals(Date(expectedDate), Date(WishCalculator.getNextWishTime(currentDate)))
    }

    @Test
    fun next_wish_time_should_be_next_month_if_a_month_past_wish_time() {
        val currentDate = LocalDateTime.of(1992, 6, 5, 5, 5)

        val expectedDate = LocalDateTime.of(1992, 6, 6, 6, 6)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        assertEquals(Date(expectedDate), Date(WishCalculator.getNextWishTime(currentDate)))
    }

    @Test
    fun should_roll_over_to_next_year_if_current_date_at_december() {
        val currentDate = LocalDateTime.of(1992, 12, 13, 12, 12)
        val expectedDate = LocalDateTime.of(1993, 1, 1, 1, 1)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        assertEquals(Date(expectedDate), Date(WishCalculator.getNextWishTime(currentDate)))
    }

}