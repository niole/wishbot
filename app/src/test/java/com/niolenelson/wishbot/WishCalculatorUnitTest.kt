package com.niolenelson.wishbot

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.*
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WishCalculatorUnitTest {
    lateinit var wC: WishCalculator

    @BeforeAll
    fun beforeAll() {
        wC = mock(WishCalculator::class.java)
        `when`(wC.getNextWishTime()).thenCallRealMethod()
        `when`(wC.isWishTime()).thenCallRealMethod()
    }

    @AfterAll
    fun afterAll() {
       println("DONE")
    }

    @Test
    fun is_wish_time_when_month_day_hour_minute_equal() {
        val wishTime = LocalDateTime.of(1992, 5, 5, 5, 5)
        `when`(wC.getCurrentDate()).thenReturn(wishTime)
        assertTrue(wC.isWishTime())
    }

    @Test
    fun isnt_wish_time_when_month_day_hour_minute_not_equal() {
        val wishTime = LocalDateTime.of(1992, 5, 5, 5, 6)
        `when`(wC.getCurrentDate()).thenReturn(wishTime)
        assertFalse(wC.isWishTime())
    }
}