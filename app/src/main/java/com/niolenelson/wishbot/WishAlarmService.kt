package com.niolenelson.wishbot

import android.app.*
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Binder
import android.util.Log
import android.os.IBinder
import android.provider.AlarmClock
import android.support.v4.app.AlarmManagerCompat.setAlarmClock
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import java.util.*

/**
 * every time a wish time triggers, we set the next wish time
 */
class WishAlarmService : Service() {

    private val channelId = "wishalarmbot"

    private var notificationManager: NotificationManager? = null

    private val mBinder = WishServiceBinder()

    inner class WishServiceBinder : Binder() {
        internal val service: WishAlarmService
            get() = this@WishAlarmService
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notification = NotificationCompat.Builder(applicationContext, channelId)
                .setPriority(0)
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentText("MAKE A WISH")
                .build()
            notificationManager?.notify(0, notification)
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        this.registerReceiver(receiver, IntentFilter("STUFF"))
        val intent = Intent()
        intent.action = "STUFF"
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, FLAG_CANCEL_CURRENT)
        val info = AlarmManager.AlarmClockInfo(Date().time + 5000, pendingIntent)
        val alarmManager: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAlarmClock(info, pendingIntent)

        notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
        notificationManager?.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("LocalService", "Received start id $startId: $intent")
        return Service.START_NOT_STICKY
    }
}