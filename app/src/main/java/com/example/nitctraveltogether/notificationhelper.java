package com.example.nitctraveltogether;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class notificationhelper extends AppCompatActivity {


    public static void displaynotification(Context context, String title, String body){

//        Intent i=new Intent(context, Drawer.class);
//        PendingIntent pendingIntent=PendingIntent.getActivity(context,100,i,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "tushar_m180499ca")
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
     //   Toast.makeText(context,"Notification sending",Toast.LENGTH_SHORT).show();
    }

}
