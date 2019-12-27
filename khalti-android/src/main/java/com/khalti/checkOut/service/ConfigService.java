package com.khalti.checkOut.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.khalti.R;
import com.khalti.checkOut.helper.Config;
import com.khalti.utils.EmptyUtil;
import com.khalti.utils.LogUtil;
import com.khalti.utils.Store;

import java.util.Timer;
import java.util.TimerTask;

public class ConfigService extends Service {

    public static final String CHANNEL_ID = "CONFIG_SERVICE_003";
    @Nullable
    private static Config config;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Timer timer = new Timer();
        timer.schedule(new print(), 0, 5000);

        if (EmptyUtil.isNull(config)) {
            config = Store.getConfig();
        }

        createNotification();
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Nullable
    public static Config getConfig() {
        return config;
    }

    public void stopService() {
        stopForeground(true);
        stopSelf();
    }

    private void createNotification() {
        NotificationCompat.Builder builder;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Test title")
                .setContentText("Test description")
                .setSmallIcon(R.mipmap.cards);

        if (EmptyUtil.isNotNull(manager) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Foreground Service Channel", NotificationManager.IMPORTANCE_NONE);
            manager.createNotificationChannel(channel);
        }
        Notification notification = builder.build();

        startForeground(2, notification);
    }

    //region
    class print extends TimerTask {
        public void run() {
//            LogUtil.checkpoint("Abhi hum jinda hai");
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        LogUtil.checkpoint("config service removed");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.checkpoint("config service unbound");
        return super.onUnbind(intent);
    }

    private void restart() {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        if (EmptyUtil.isNotNull(alarmService)) {
            alarmService.set(
                    AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + 1000,
                    restartServicePendingIntent);
        }
    }
    //endregion
}