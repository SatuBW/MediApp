package com.example.satu.mediapp;

/**
 * Created by Satu on 2016-01-05.
 */

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.Calendar;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";
    public static final String INFO_EXTRA = "INFO_EXTRA";
    private MediaPlayer mMediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("tag", "ODEBRANY !!" );
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();
        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();
        String string = extras.getString(INFO_EXTRA);
    // notyfikacja
       Intent resultIntent = new Intent(context, BilansActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        (string != null ? new Intent() : resultIntent),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.eskulap)
                        .setContentTitle("Przypomnienie !")
                        .setContentText((string != null ? string : "Wykonaj badanie cisnienia " ));


        mBuilder.setContentIntent(resultPendingIntent);


        int mNotificationId = (string != null ? 002 : 001 );

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean aBoolean = defaultSharedPreferences.getBoolean("notifications_new_message_vibrate", true);
        if (aBoolean){
            Vibrator v;
            v=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(300);
        }
        alarm(context);

// koniec notyfikacji
        //Release the lock
        wl.release();
    }
    private void alarm(Context context){
        SharedPreferences getAlarms = PreferenceManager.
                getDefaultSharedPreferences(context);
        String alarms = getAlarms.getString("notifications_new_message_ringtone", "content://settings/system/notification_sound");
        Uri uri = Uri.parse(alarms);
        playSound(context, uri);

        //call mMediaPlayer.stop(); when you want the sound to stop
    }

    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 5 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi);
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    public void CancelOneAlarm(Context context, int ID)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(Context context){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }
    public void setRepeatedNotification(int ID, int hh, int mm, int ss,Context context, String info) {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(INFO_EXTRA, info);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hh);
        calendar.set(Calendar.MINUTE, mm);
        calendar.set(Calendar.SECOND,ss);
        long startUpTime = calendar.getTimeInMillis();
        if (System.currentTimeMillis() > startUpTime) {
            startUpTime = startUpTime + 24*60*60*1000;
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                startUpTime, AlarmManager.INTERVAL_DAY, pendingIntent);

        Log.v("tag", "Start! ID = "+ ID );

    }


}