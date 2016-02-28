package xyz.donot.quetzal.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import xyz.donot.quetzal.R;
import xyz.donot.quetzal.view.activity.MainActivity;


public class NewMentionNotification {

  private static final String NOTIFICATION_TAG = "NewMention";


  public static void notify(final Context context,
                            final String txtString, final int number) {
    final Resources res = context.getResources();

    final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
    final String title ="返信";
    final String text = res.getString(R.string.new_mention_notification_placeholder_text_template, txtString);
    final String ringtone= PreferenceManager.getDefaultSharedPreferences(context).getString("notifications_ringtone","");
    final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
      .setSound(Uri.parse(ringtone))
      .setSmallIcon(R.drawable.ic_stat_new_mention)
      .setContentTitle(title)
      .setContentText(text)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setLargeIcon(picture)
      .setTicker(txtString)
      .setNumber(number)
      .setContentIntent(PendingIntent.getActivity(context, 0,new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
      .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(text)
        .setBigContentTitle(title))
      .addAction(
        R.drawable.ic_action_stat_reply,
        res.getString(R.string.action_reply),
        PendingIntent.getActivity(context, 0,new Intent(context, MainActivity.class), 0))
      .setAutoCancel(true);
    notify(context, builder.build());
  }

  @TargetApi(Build.VERSION_CODES.ECLAIR)
  private static void notify(final Context context, final Notification notification) {
    final NotificationManager nm = (NotificationManager) context
      .getSystemService(Context.NOTIFICATION_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
      nm.notify(NOTIFICATION_TAG, 0, notification);
    } else {
      nm.notify(NOTIFICATION_TAG.hashCode(), notification);
    }
  }
  @TargetApi(Build.VERSION_CODES.ECLAIR)
  public static void cancel(final Context context) {
    final NotificationManager nm = (NotificationManager) context
      .getSystemService(Context.NOTIFICATION_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
      nm.cancel(NOTIFICATION_TAG, 0);
    } else {
      nm.cancel(NOTIFICATION_TAG.hashCode());
    }
  }
}
