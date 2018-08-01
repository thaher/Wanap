package labs.bridge.wanap.services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import labs.bridge.wanap.R;
import labs.bridge.wanap.activities.ReportsActivity;
import labs.bridge.wanap.models.Event;
import labs.bridge.wanap.models.Status;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    static int notificationCount = 0;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("WANAP_MESSAGE", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("WANAP_MESSAGE", "Message data payload: " + remoteMessage.getData());

                sendNotification(remoteMessage);


//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("\"WANAP_MESSAGE\"", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }



    private void sendNotification(RemoteMessage remoteMessage) {


        NotificationCompat.Builder notificationBuilder;
        if(remoteMessage.getData().get("data_type").equals("status"))
        {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Status>>() {}.getType();
            ArrayList<Status> statuses = gson.fromJson(remoteMessage.getData().get("data_content"), type);
            Intent intent = new Intent(this, ReportsActivity.class).putExtra("statuses",remoteMessage.getData().get("data_content"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                    PendingIntent.FLAG_ONE_SHOT);

             notificationBuilder =
                    new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle("Wanap Status")
                            .setContentText(String.valueOf(statuses.size())+" device status receieved")
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent);
        }
        else {

            Gson gson = new Gson();
            Type type = new TypeToken<Event>() {}.getType();
            Event event = gson.fromJson(remoteMessage.getData().get("data_content"), type);
            Intent intent = new Intent(this, ReportsActivity.class).putExtra("event",event);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                    PendingIntent.FLAG_ONE_SHOT);

             notificationBuilder =
                    new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle("Wanap Event")
                            .setContentText(String.valueOf(event.getEventName())+" "+event.getEventDescription())
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent);

        }





        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationCount++ , notificationBuilder.build());
    }

}
