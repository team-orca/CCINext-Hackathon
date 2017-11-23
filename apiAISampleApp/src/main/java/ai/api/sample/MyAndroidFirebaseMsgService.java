package ai.api.sample;

/**
 * Created by deneyim on 16/12/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    int mNotificationId = 18;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //create notification
        Map<String, String> data = remoteMessage.getData();
        if (data.containsKey("click_action")) {
            ClickActionHelper.startActivity(data.get("click_action"), null, this);
        }
        createNotification(remoteMessage.getNotification().getBody());
    }

    private void createNotification( String messageBody) {
        /*Intent intent = new Intent( this , AITextSampleActivity. class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, AITextSampleActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);*/




        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("Çırak")
                .setContentText(messageBody)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX);

        Intent resultIntent = new Intent(this, SurveyActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack
        stackBuilder.addParentStack(AITextSampleActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mNotificationId, builder.build());
       /* builder.setContentIntent(contentIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notification.contentIntent = contentIntent;
        notificationManager.notify(mNotificationId, notification);*/

    }
}