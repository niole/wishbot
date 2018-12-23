package com.niolenelson.wishbot.receiver

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import com.niolenelson.wishbot.WishAlarmService


class StartWishAlarmServiceBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val serviceIntent = Intent(context, WishAlarmService::class.java)
            context.startService(serviceIntent)
        }
    }
}