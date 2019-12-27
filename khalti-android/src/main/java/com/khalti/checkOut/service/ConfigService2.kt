package com.khalti.checkOut.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.khalti.R
import com.khalti.checkOut.helper.Config
import com.khalti.checkOut.service.ConfigServiceComm.GetConfig
import com.khalti.utils.EmptyUtil
import com.khalti.utils.FileStorageUtil
import com.khalti.utils.LogUtil
import com.khalti.utils.Store
import java.util.*

class ConfigService2 : Service() {
    var config: Config? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val timer = Timer()
        timer.schedule(Print(), 0, 5000)
        if (EmptyUtil.isNull(config)) {
            config = Store.getConfig()
        }
        createNotification()

        val stopService = ConfigServiceComm.StopService {
            stopService()
        }

        val getConfig = GetConfig { config }

        FileStorageUtil.writeIntoFile(applicationContext, "stop_service", stopService)
        FileStorageUtil.writeIntoFile(applicationContext, "get_config", getConfig)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun stopService() {
        stopForeground(true)
        stopSelf()
    }

    private fun createNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Test title")
                .setContentText("Test description")
                .setSmallIcon(R.mipmap.cards)
        if (EmptyUtil.isNotNull(manager) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel", NotificationManager.IMPORTANCE_NONE)
            manager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        startForeground(2, notification)
    }

    internal inner class Print : TimerTask() {
        override fun run() {
            LogUtil.checkpoint("Abhi hum jinda hai")
        }
    }

    companion object {
        const val CHANNEL_ID = "CONFIG_SERVICE_003"
    }
}