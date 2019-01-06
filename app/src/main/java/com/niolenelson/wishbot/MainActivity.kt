package com.niolenelson.wishbot

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()

        val serviceIntent = Intent(this.baseContext, WishAlarmService::class.java)
        this.baseContext.startService(serviceIntent)
        val activityLayout = findViewById<LinearLayout>(R.id.main)

        val inflater = LayoutInflater.from(applicationContext)
        if (WishCalculator.isWishTime()) {
            Log.i("MainActivity", "Time to make a wish")
            inflater.inflate(R.layout.countdown_layout, activityLayout, true)
            val counterContainer = findViewById<TextView>(R.id.counter)
            val currentTime = LocalDateTime.now(ZoneId.systemDefault())
            counterContainer.text = (60 - currentTime.second).toString()
            handleCountdown(counterContainer)
        } else {
            val date = Calendar.getInstance()
            date.timeInMillis = WishCalculator.getNextWishTime()

            val nextWishTime = LocalDateTime.of(
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH) + 1,
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.HOUR_OF_DAY),
                date.get(Calendar.MINUTE)
            ).format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm"))

            Log.i("MainActivity", "Next wish time at: $nextWishTime.")
            inflater.inflate(R.layout.next_countdown_layout, activityLayout, true)
            val nextWishDateContainer = findViewById<TextView>(R.id.future_wish_date)
            nextWishDateContainer.text = nextWishTime
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun handleCountdown(counterContainer: TextView) {
        Timer("counter", false).schedule(1000) {
            val currentTime = LocalDateTime.now(ZoneId.systemDefault())
            val second = currentTime.second
            counterContainer.text = (60 - second).toString()
            if (second < 59) {
                handleCountdown(counterContainer)
            } else {
               Timer("counter", false).schedule(1000) {
                   counterContainer.text = "0"
               }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val serviceIntent = Intent(this.baseContext, WishAlarmService::class.java)
        this.baseContext.startService(serviceIntent)
    }

}
