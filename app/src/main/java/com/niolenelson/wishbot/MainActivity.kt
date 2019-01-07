package com.niolenelson.wishbot

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        startFlow()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun startFlow() {
        val serviceIntent = Intent(this.baseContext, WishAlarmService::class.java)
        this.baseContext.startService(serviceIntent)
        val activityLayout = findViewById<LinearLayout>(R.id.main)

        val inflater = LayoutInflater.from(applicationContext)
        if (WishCalculator.isWishTime()) {
            Log.i("MainActivity", "Time to make a wish")
            inflater.inflate(R.layout.countdown_layout, activityLayout, true)
            val counterContainer = findViewById<TextView>(R.id.counter)
            val currentTime = LocalDateTime.now(ZoneId.systemDefault())
            findViewById<TextView>(R.id.date).text = formatTime(Date().time)
            findViewById<Button>(R.id.wish_button).setOnClickListener { onWish() }
            counterContainer.text = (60 - currentTime.second).toString()
            handleCountdown(counterContainer)
        } else {
            val nextWishTime = formatTime(WishCalculator.getNextWishTime())
            Log.i("MainActivity", "Next wish time at: $nextWishTime.")
            inflater.inflate(R.layout.next_countdown_layout, activityLayout, true)
            val nextWishDateContainer = findViewById<TextView>(R.id.future_wish_date)
            nextWishDateContainer.text = nextWishTime
        }
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
                   runOnUiThread {
                       findViewById<LinearLayout>(R.id.main).removeAllViewsInLayout()
                       val inflater = LayoutInflater.from(applicationContext)
                       inflater.inflate(R.layout.out_of_time_layout, findViewById<LinearLayout>(R.id.main), true)
                       setupGoBackButton()
                       findViewById<TextView>(R.id.next_wish_time).text = formatTime(WishCalculator.getNextWishTime())
                   }
               }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val serviceIntent = Intent(this.baseContext, WishAlarmService::class.java)
        this.baseContext.startService(serviceIntent)
    }

    private fun formatTime(time: Long): String {
        val date = Calendar.getInstance()
        date.timeInMillis = time

        return LocalDateTime.of(
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.DAY_OF_MONTH),
            date.get(Calendar.HOUR_OF_DAY),
            date.get(Calendar.MINUTE),
            date.get(Calendar.SECOND)
        ).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
    }

   private fun onWish() {
       val inflater = LayoutInflater.from(applicationContext)
       findViewById<LinearLayout>(R.id.main).removeAllViewsInLayout()
       inflater.inflate(R.layout.successful_wish_layout, findViewById<LinearLayout>(R.id.main), true)

        setupGoBackButton()

       val colorAnimation = findViewById<LinearLayout>(R.id.rainbow_animation).background as AnimationDrawable
       colorAnimation.setEnterFadeDuration(250)
       colorAnimation.setExitFadeDuration(250)
       colorAnimation.start()

       val textAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_wish_text)
       textAnimation.reset()
       val hooray = findViewById<TextView>(R.id.hooray)
       val may_wishes = findViewById<TextView>(R.id.may_wishes)
       hooray.clearAnimation()
       hooray.startAnimation(textAnimation)
       may_wishes.clearAnimation()
       may_wishes.startAnimation(textAnimation)
   }

   private fun setupGoBackButton() {
       findViewById<Button>(R.id.back_button).setOnClickListener {
           findViewById<LinearLayout>(R.id.main).removeAllViewsInLayout()
           startFlow()
       }
   }
}
