package com.niolenelson.wishbot

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.util.Log
import android.os.IBinder
import android.provider.AlarmClock
import android.support.v4.app.AlarmManagerCompat.setAlarmClock
import java.util.*

/**
 * every time a wish time triggers, we set the next wish time
 */
class WishAlarmService : Service() {
    private var nextWishTime = Date().time + 5000

    private val mBinder = WishServiceBinder()

    inner class WishServiceBinder : Binder() {
        internal val service: WishAlarmService
            get() = this@WishAlarmService
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Never gets hit
            println("RECEIVE")
            println(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        this.registerReceiver(receiver, IntentFilter("STUFF"))
        val intent = Intent()
        intent.action = "STUFF"
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, FLAG_CANCEL_CURRENT)
        val info = AlarmManager.AlarmClockInfo(Date().time + 5000, pendingIntent)
        val alarmManager: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAlarmClock(info, pendingIntent)
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("LocalService", "Received start id $startId: $intent")
        return Service.START_NOT_STICKY
    }
}