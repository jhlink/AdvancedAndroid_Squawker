package android.example.com.squawker.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkDatabase;
import android.example.com.squawker.provider.SquawkProvider;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class SquawkFirebaseMessagingService extends FirebaseMessagingService {
    private static final String LOG_TAG = SquawkFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        if (data != null) {
            String test = data.get("test");
            String author = data.get("author");
            String authorKey = data.get("authorKey");
            String message = data.get("message");
            String date = data.get("date");

            Log.d(LOG_TAG, "This is... " );

            FCMNotify(getApplicationContext(), author, message);

            ContentValues cv = new ContentValues();
            cv.put(SquawkContract.COLUMN_AUTHOR_KEY, authorKey);
            cv.put(SquawkContract.COLUMN_AUTHOR, author);
            cv.put(SquawkContract.COLUMN_DATE, date);
            cv.put(SquawkContract.COLUMN_MESSAGE, message);

            ContentResolver resolver = getContentResolver();
            resolver.insert(SquawkProvider.SquawkMessages.CONTENT_URI, cv);
        }

    }

    private final static int PENDING_INTENT_NOTIF_ID = 42;
    private static final int FCM_CHANNEL_ID = 7;

    // COMP (7) Create a method called remindUserBecauseCharging which takes a Context.
    // This method will create a notification for charging. It might be helpful
    // to take a look at this guide to see an example of what the code in this method will look like:
    // https://developer.android.com/training/notify-user/build-notification.html
    public static void FCMNotify(Context context, String title, String textBody) {
        // COMP (8) Get the NotificationManager using context.getSystemService
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context
                .NOTIFICATION_SERVICE);

        String bodySubstring = textBody.substring(0, 30);

        NotificationCompat.Builder dasNotifBuilder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(title)
                        .setSmallIcon(R.drawable.ic_duck)
                        .setContentText(bodySubstring)
                        .setContentIntent(contentIntent(context))
                        .setAutoCancel(true);

        notificationManager.notify(FCM_CHANNEL_ID, dasNotifBuilder.build());
    }


    public static PendingIntent contentIntent(Context context) {

        Intent openMainActivityIntent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_NOTIF_ID,
                openMainActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }
}
