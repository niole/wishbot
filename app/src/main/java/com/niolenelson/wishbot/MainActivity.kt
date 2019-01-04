package com.niolenelson.wishbot

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serviceIntent = Intent(this.baseContext, WishAlarmService::class.java)
        this.baseContext.startService(serviceIntent)
        val activityLayout = findViewById<LinearLayout>(R.id.main)

        val inflater = LayoutInflater.from(applicationContext)
        if (WishCalculator.isWishTime()) {
            Log.i("MainActivity", "Time to make a wish")
            inflater.inflate(R.layout.countdown_layout, activityLayout, true)
        } else {
            val nextWishTime = Date(WishCalculator.getNextWishTime()).toString()
            Log.i("MainActivity", "Next wish time at: $nextWishTime.")
            inflater.inflate(R.layout.next_countdown_layout, activityLayout, true)
            val nextWishDateContainer = findViewById<TextView>(R.id.future_wish_date)
            nextWishDateContainer.text = nextWishTime
        }
    }
}
