package com.gmail.pavkascool.c12_notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonShow, buttonChange, buttonPend, buttonStyled;
    private final String CHANNEL_ID = "MY_ID";
    private final int note = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonShow = findViewById(R.id.notific);
        buttonShow.setOnClickListener(this);
        buttonChange = findViewById(R.id.notific1);
        buttonChange.setOnClickListener(this);
        buttonPend = findViewById(R.id.notific2);
        buttonPend.setOnClickListener(this);
        buttonStyled = findViewById(R.id.notific3);
        buttonStyled.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.notific:
                showBasicNotification();
                break;
            case R.id.notific1:
                changeBasicNotification();
                break;
            case R.id.notific2:
                pendingBasicNotification();
                break;
            case R.id.notific3:
                styledBasicNotification();
        }
    }

    private void styledBasicNotification() {
        String text = "long tex... " +
                "long tex... " +
                "long tex... " +
                "long tex... " +
                "long tex... ";
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(text);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My Title")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(bigTextStyle);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.notify(note, notification);
        }
    }

    private void showBasicNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My Title")
                .setContentText("My Text")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.notify(note, notification);
        }
    }
    private void changeBasicNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My NEW Title")
                .setContentText("My Text")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.notify(note, notification);
        }
    }

    private void pendingBasicNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My NEW Title")
                .setContentText("My Text")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.notify(note, notification);
        }
    }
}
