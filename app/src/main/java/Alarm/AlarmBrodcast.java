package Alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.appnote_3.NotificationMessage;
import com.example.appnote_3.R;

import java.util.Calendar;

public class AlarmBrodcast extends BroadcastReceiver {
    private byte[] img;

    private AlarmManager alarmManager;
    private Calendar calendar;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String content = bundle.getString("content");
        String date = bundle.getString("day") + " " + bundle.getString("time");
        img = bundle.getByteArray("img");


//        Click on Notification
        Intent intent1 = new Intent(context, NotificationMessage.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("message", title);

        //
//        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
//        vibrator.vibrate(10000);
//
////        Music
//        Intent intentInReceiver = new Intent(context, Music.class);
//        context.startService(intentInReceiver);
//        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, intentInReceiver,PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent1);

        //Notification Builder
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");



        //here we set all the properties for the notification
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
//        if (img.length >  0){
//            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
//            contentView.setImageViewBitmap(R.id.id_img_view_notifi, bitmap);
//        }else{
//            contentView.setImageViewResource(R.id.id_img_view_notifi,R.mipmap.ic_launcher);
//        }
        contentView.setImageViewResource(R.id.id_img_view_notifi, R.mipmap.ic_launcher);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);
        contentView.setTextViewText(R.id.id_textV_title_notifi, title);
        contentView.setTextViewText(R.id.id_textV_time_and_day_notifi, content);
        mBuilder.setSmallIcon(R.drawable.ic_alarm_clock);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
        mBuilder.setContent(contentView);
        mBuilder.setContentIntent(pendingIntent);

        //we have to create notification channel after api level 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Notification notification = mBuilder.build();
        notificationManager.notify(1, notification);


    }

}
