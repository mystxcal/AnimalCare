package com.example.animalcare;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseNotification extends FirebaseMessagingService {

    FirebaseAuth firebaseAuth;
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i("MYTOKEN",s);
    }

    private final String ADMIN_CHANNEL_ID ="admin_channel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null && !firebaseUser.isEmailVerified()) {

            Log.i("USERNAME", "->"+remoteMessage.getData().get("userName"));
            Log.i("PHONENO", "->"+remoteMessage.getData().get("mobileNo"));
            Log.i("ANIMALTYPE","->"+ remoteMessage.getData().get("animalType"));
            Log.i("Location","->"+ remoteMessage.getData().get("location"));
            Log.i("LAT", "->"+remoteMessage.getData().get("lat"));
            Log.i("LNG", "->"+remoteMessage.getData().get("lng"));

            Bundle bundle = new Bundle();
            bundle.putString("USERNAME",remoteMessage.getData().get("userName"));
            bundle.putString("PHONENO",remoteMessage.getData().get("mobileNo"));
            bundle.putString("ANIMALTYPE",remoteMessage.getData().get("animalType"));
            bundle.putString("LAT",remoteMessage.getData().get("lat"));
            bundle.putString("LNG",remoteMessage.getData().get("lng"));
            bundle.putString("Location",remoteMessage.getData().get("location"));
            bundle.putString("FromWhere","NOTIF");
            final Intent intent = new Intent(this, RescueActivity.class);
            intent.putExtras(bundle);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationID = new Random().nextInt(3000);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupChannels(notificationManager);
            }

            Log.i("ANDROO", "Falseq");

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.doggylogo);

            //Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            Uri notificationSoundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.buzzermessage);


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_refresh_round)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message"))
                    .setAutoCancel(true)
                    .setSound(notificationSoundUri)
                    .setContentIntent(pendingIntent);

            //try {
//                Log.i("USERNAME", "->"+remoteMessage.getData().get("userName"));
//                Log.i("ANIMALTYPE","->"+ remoteMessage.getData().get("animalType"));
//                Log.i("Location","->"+ remoteMessage.getData().get("location"));
//                Log.i("LAT", "->"+remoteMessage.getData().get("lat"));
//                Log.i("LNG", "->"+remoteMessage.getData().get("lng"));
//            }catch (Exception e){
//                Log.i("WHATISERROR","->" +e.getMessage());
//            }


            //Set notification color to match your app color template
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            notificationManager.notify(notificationID, notificationBuilder.build());


            // super.onMessageReceived(remoteMessage);

//        Log.i("NOTIFMSG","RECEIVED");
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
//                .setContentTitle(remoteMessage.getNotification().getTitle())
//                .setContentText(remoteMessage.getNotification().getBody())
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setStyle(new NotificationCompat.BigTextStyle())
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setAutoCancel(true);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";
Log.i("ANDROO","TRUE");
        //Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.buzzermessage);
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
        adminChannel.setSound(sound, audioAttributes);

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

}
