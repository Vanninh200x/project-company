package Alarm;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.appnote_3.NotificationMessage;
import com.example.appnote_3.R;



public class AlarmBrodcast extends BroadcastReceiver {
    private byte[] img = new byte[]{};

    @Override
    public void onReceive(Context context, Intent intent) {

        int Count =0;
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String content = bundle.getString("content");
        String date = bundle.getString("time") +" "+bundle.getString("day");
        img = bundle.getByteArray("img");

        Log.e("CHECK_IMG_1", img+"");
        Log.e("TITLE_BR", title +"");
//        Click on Notification
        Intent intent1 = new Intent(context, NotificationMessage.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("message", title);

        //Notification Builder

//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1 , intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");

        //here we set all the properties for the notification
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        contentView.setImageViewBitmap(R.id.id_img_view_notifi, bitmap);

//        contentView.setImageViewResource(R.id.id_img_view_notifi, R.mipmap.ic_launcher);


//        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingSwitchIntent.cancel();
        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);

        contentView.setTextViewText(R.id.id_textV_title_notifi, title);
        contentView.setTextViewText(R.id.id_textV_time_and_day_notifi, content);
        contentView.setTextViewText(R.id.id_textV_time_and_day_notifi_2, date);
        mBuilder.setSmallIcon(R.drawable.ic_alarm_clock);
//      MUSIC
//        Uri defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/mysound");

//        Uri myUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ context.getPackageName() + "/raw/filemp3.mp3");
//
//        Log.e("CHECK_URI", myUri +"");
//        mBuilder.setSound(myUri);
//        mBuilder.setLights(Color.BLUE, 500, 500);
//        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

//      VRIBATE



        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.setAutoCancel(true);


        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH ;
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
        notificationManager.notify( 1, notification);
    }
}
