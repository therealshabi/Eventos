package app.com.thetechnocafe.eventos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class EventosFirebaseMessagingService extends FirebaseMessagingService {
    final static String TAG = "Notificas";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, HomeStreamActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("Eventos");
        if (remoteMessage.getNotification() != null) {
            notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        }
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.drawable.calendar);
        notificationBuilder.setColor(getResources().getColor(R.color.colorAccent));
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
