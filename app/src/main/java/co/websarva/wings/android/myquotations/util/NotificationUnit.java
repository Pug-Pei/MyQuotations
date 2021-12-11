package co.websarva.wings.android.myquotations.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import co.websarva.wings.android.myquotations.activity.QuotationContentActivity;
import co.websarva.wings.android.myquotations.R;
import co.websarva.wings.android.myquotations.quotations.Quotations;


public class NotificationUnit {
    private static final String TAG = NotificationUnit.class.getSimpleName();
    private static final String CHANNEL_ID = "my_quotations_notification_channel";
    Context context =  MyApplication.getInstance();

   public void makeQuotationNotification(String message, String title, Context context, Quotations notificationInstance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android8（Oreo）以上の場合、NotificationChannelを作成
            String name = title;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            // Add the channel
            NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

       Intent intent = new Intent(context, QuotationContentActivity.class);
       intent.putExtra("selected_quotation", notificationInstance);

       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
       PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //notificationチャンネルを取得
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_menu_book_24);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[0]);
        builder.setContentIntent(pendingIntent).setAutoCancel(true);

        //notification発動
        NotificationManagerCompat.from(context).notify(1, builder.build());
    }


}
