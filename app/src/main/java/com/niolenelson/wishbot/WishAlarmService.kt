package com.niolenelson.wishbot

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.util.Log
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import java.time.LocalDateTime
import java.util.*

/**
 * every time a wish time triggers, we set the next wish time
 */
class WishAlarmService : Service() {

    private val RECEIVER_INTENT_ACTION_ID = "com.niole.nelson.wishbot"

    private val channelId = "wishbot"

    private var notificationManager: NotificationManager? = null

    private val mBinder = WishServiceBinder()

    inner class WishServiceBinder : Binder() {
        internal val service: WishAlarmService
            get() = this@WishAlarmService
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val activitySpec = Intent(context, MainActivity::class.java)
            val activityOpener = PendingIntent.getActivity(context, 1, activitySpec, PendingIntent.FLAG_UPDATE_CURRENT)
            val notification = NotificationCompat.Builder(applicationContext, channelId)
                .setPriority(0)
                .setSmallIcon(R.drawable.star_icon)
                .setContentText("It's time to make a wish!")
                .setContentIntent(activityOpener)
                .build()
            notificationManager?.notify(0, notification)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        setupAlarms()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("WishAlarmService", "Received start id $startId: $intent")
        setupAlarms()
        return Service.START_STICKY
    }

    private fun setupAlarms() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        this.registerReceiver(receiver, IntentFilter(RECEIVER_INTENT_ACTION_ID))

        var nextAlertTime = WishCalculator.getNextWishTime()
        for (i in 0..11) {
            val alarmManager: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent()
            intent.action = RECEIVER_INTENT_ACTION_ID
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = nextAlertTime
            calendar.add(Calendar.MINUTE, i * 1)

            val time = WishCalculator.getNextWishTime(LocalDateTime.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
            ))

            val pendingIntent = PendingIntent.getBroadcast(this, time.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)

            nextAlertTime = time
        }

        val channel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
        notificationManager!!.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}