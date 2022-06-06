package com.example.socialmedia.Activity.Notification;

import static androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.socialmedia.Activity.MainActivity;
import com.example.socialmedia.Activity.PostActivity;
import com.example.socialmedia.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class NotificationService extends FirebaseMessagingService {
    private static String CHANNEL_ID = "my_fb_channel";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.e("TAG", "onMessageReceived: "+message.getData().toString());
        Log.d("TAG", "onMessageReceived: " + message.getNotification().getBody().toString());

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isNotification" , true) ;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager =(NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE)  ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CreateNotification(notificationManager);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_heart)
                        .setContentTitle("Social Media")
                        .setContentText(message.getNotification().getBody().toString())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        int notificationID = new Random().nextInt() ;
        notificationManager.notify(notificationID , notificationBuilder.build());


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateNotification(NotificationManager notificationManager) {
        String channelName = "channel";
        NotificationChannel notificationChannel =
                new NotificationChannel(
                        CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH
                );
        notificationChannel.setDescription("my channel");
        notificationChannel.enableVibration(true);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.GREEN);

        notificationManager.createNotificationChannel(notificationChannel);
    }

}
