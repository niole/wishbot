package com.niolenelson.wishbot

import java.util.*

object WishCalculator {
    fun getNextWishTime(): Long {
        return Date().time + 5000
    }
}