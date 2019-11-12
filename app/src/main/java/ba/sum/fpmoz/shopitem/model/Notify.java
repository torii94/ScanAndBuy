package ba.sum.fpmoz.shopitem.model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ba.sum.fpmoz.shopitem.Main;
import ba.sum.fpmoz.shopitem.R;

public class Notify extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent= new Intent(this, Main.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder ncb= new NotificationCompat.Builder(this);
        ncb.setContentTitle("FCM NOTIFICATION");
        ncb.setContentText(remoteMessage.getNotification().getBody());
        ncb.setAutoCancel(true);

        ncb.setSmallIcon(R.mipmap.sale);
        ncb.setContentIntent(pi);
        NotificationManager nm= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0,ncb.build());
    }
}
