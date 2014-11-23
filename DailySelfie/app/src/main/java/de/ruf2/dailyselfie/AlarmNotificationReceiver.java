package de.ruf2.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by becks on 20.11.2014.
 */


public class AlarmNotificationReceiver extends BroadcastReceiver {
    // Notification ID to allow for future updates
    private static final int MY_NOTIFICATION_ID = 1;
    private static final String TAG = "AlarmNotificationReceiver";

    // Notification Text Elements
    private final CharSequence tickerText = "Are are took you daily selfie already?";
    private final CharSequence contentTitle = "Daily Selfie";
    private final CharSequence contentText = "Time for another selfie!!";

    // Notification Action Elements
    private Intent mNotificationIntent;
    private PendingIntent mContentIntent;

    // Notification Sound and Vibration on Arrival
    private final Uri soundURI = Uri
            .parse("android.resource://de.ruf2.dailyselfie/"
                    + R.raw.alarm_rooster);
    private final long[] mVibratePattern = { 0, 200, 200, 300 };

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "alarm received");

        // The Intent to be used when the user clicks on the Notification View
        mNotificationIntent = new Intent(context, SelfieViewActivity.class);
        mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // The PendingIntent that wraps the underlying Intent
        mContentIntent = PendingIntent.getActivity(context, 0,
                mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the Notification
      RemoteViews mContentView = new RemoteViews(context.getPackageName(), R.layout.notification);

        // TODO: Set the notification View's text to
        // reflect whether the download completed
        // successfully
        mContentView.setTextViewText(R.id.text, contentText);

        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setTicker(tickerText)
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_action_camera_hd))
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(mContentIntent)
                .setSound(soundURI)
                .setVibrate(mVibratePattern);


        // Get the NotificationManager
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // Pass the Notification to the NotificationManager:
        mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());


        // Log occurence of notify() call
        Log.i(TAG, "Sending notification at:"
                + DateFormat.getDateTimeInstance().format(new Date()));

    }
}
