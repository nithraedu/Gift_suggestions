package nithra.gift.suggestion.shop.birthday.marriage.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import nithra.gift.suggestion.shop.birthday.marriage.R;


public class NotificationHelper extends ContextWrapper {
    private NotificationManager manager;
    public static final String PRIMARY_CHANNEL = "default";
    NotificationChannel chan1 = null;
    Context context;

    public NotificationHelper(Context ctx) {
        super(ctx);
        context = ctx;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan1 = new NotificationChannel(PRIMARY_CHANNEL, "Primary Channel", NotificationManager.IMPORTANCE_HIGH);
            chan1.setLightColor(Color.GREEN);
            chan1.setShowBadge(true);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(chan1);
        }
    }


    public void Notification_bm(int id, String title, String body, String imgg, String style, String bm, int sund_chk1, Class activity) {
        try {
            Uri mUri;
            mUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            System.out.println("mUri : " + mUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder nb;
                if (style.equals("bt")) {

                    NotificationChannel
                            chan1 = new NotificationChannel(PRIMARY_CHANNEL, "Primary Channel", NotificationManager.IMPORTANCE_DEFAULT);

                    chan1.setLightColor(Color.GREEN);
                    chan1.setShowBadge(true);
                    chan1.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                            .build());


                    nb = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setContentTitle("Inventions & Inventors")
                            .setContentText("")
                            .setContentIntent(resultPendingIntent(bm, body, id, activity))
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#309975"))
                            .setLargeIcon(LargeIcon(imgg))
                            .setGroup("" + title)
                            .setStyle(bigtext1("Inventions & Inventors", "", bm))
                            .setAutoCancel(true);
                } else {

                    NotificationChannel
                            chan1 = new NotificationChannel(PRIMARY_CHANNEL, "Primary Channel", NotificationManager.IMPORTANCE_DEFAULT);

                    chan1.setLightColor(Color.parseColor("#309975"));
                    chan1.setShowBadge(true);
                    chan1.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                            .build());

                    nb = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setContentTitle(bm)
                            .setContentText("")
                            .setContentIntent(resultPendingIntent(bm, body, id, activity))
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#309975"))
                            .setLargeIcon(LargeIcon(imgg))
                            .setGroup("" + title)
                            .setStyle(bigimg1("Inventions & Inventors", bm, imgg))
                            .setAutoCancel(true);
                }
                notify(id, nb);
            } else {
                Notification myNotification;
                if (style.equals("bt")) {
                    myNotification = new NotificationCompat.Builder(context,PRIMARY_CHANNEL)
                            .setSound(mUri)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#309975"))
                            .setLargeIcon(getlogo1())
                            .setAutoCancel(true)
                            .setPriority(2)
                            .setContentIntent(resultPendingIntent(bm, body, id, activity))
                            .setContentTitle("Inventions & Inventors")
                            .setContentText("")
                            .setGroup("" + title)
                            .setStyle(bigtext("Inventions & Inventors", "", bm))
                            .build();
                } else {
                    myNotification = new NotificationCompat.Builder(context,PRIMARY_CHANNEL)
                            .setSound(mUri)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#309975"))
                            .setLargeIcon(getlogo1())
                            .setAutoCancel(true)
                            .setPriority(2)
                            .setContentIntent(resultPendingIntent(bm, body, id, activity))
                            .setContentTitle(bm)
                            .setContentText("")
                            .setGroup("" + title)
                            .setStyle(bigimg("Inventions & Inventors", bm, imgg))
                            .build();
                }
                notify(id, myNotification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Notification1(int id, String title, String body1, String imgg, String style, String bm, int sund_chk1, Class activity) {
        try {
            Uri mUri;
            mUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder nb;
                if (style.equals("bt")) {

                    NotificationChannel
                            chan1 = new NotificationChannel(PRIMARY_CHANNEL, "Primary Channel", NotificationManager.IMPORTANCE_DEFAULT);

                    chan1.setLightColor(Color.parseColor("#309975"));
                    chan1.setShowBadge(true);
                    chan1.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                            .build());

                    nb = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setContentTitle(title)

                            .setContentText("")
                            .setContentIntent(resultPendingIntent(bm, body1, id, activity))
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#309975"))
                            .setLargeIcon(LargeIcon(imgg))
                            .setGroup("" + title)
                            .setStyle(bigtext1(title, "Inventions & Inventors", ""))
                            .setAutoCancel(true);
                } else {

                    NotificationChannel
                            chan1 = new NotificationChannel(PRIMARY_CHANNEL, "Primary Channel", NotificationManager.IMPORTANCE_DEFAULT);

                    chan1.setLightColor(Color.parseColor("#309975"));
                    chan1.setShowBadge(true);
                    chan1.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                            .build());

                    nb = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setContentTitle(title)

                            .setContentText("")
                            .setContentIntent(resultPendingIntent1(bm))
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#309975"))
                            .setLargeIcon(LargeIcon(imgg))
                            .setGroup("" + title)
                            .setStyle(bigimg1(title, "Inventions & Inventors", imgg))
                            .setAutoCancel(true);
                }
                notify(id, nb);
            } else {
                Notification myNotification;
                if (style.equals("bt")) {
                    myNotification = new NotificationCompat.Builder(context,PRIMARY_CHANNEL)
                            .setSound(mUri)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#309975"))
                            .setLargeIcon(getlogo1())
                            .setAutoCancel(true)
                            .setPriority(2)
                            .setContentIntent(resultPendingIntent(bm, body1, id, activity))
                            .setContentTitle(title)
                            .setContentText("")
                            .setGroup("" + title)
                            .setStyle(bigtext(title, "Inventions & Inventors", ""))
                            .build();
                } else {
                    myNotification = new NotificationCompat.Builder(context,PRIMARY_CHANNEL)
                            .setSound(mUri)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#309975"))
                            .setLargeIcon(getlogo1())
                            .setAutoCancel(true)
                            .setPriority(2)
                            .setContentIntent(resultPendingIntent1(bm))
                            .setContentTitle(title)
                            .setGroup("" + title)
                            .setContentText("")
                            .setStyle(bigimg(title, "Inventions & Inventors", imgg))
                            .build();
                }
                notify(id, myNotification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Notification_custom(int id, String titlee, String body, String imgg, String style, String bm, int sund_chk1, Class activity) {
        try {
            Uri mUri;
            mUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_shown_st);
                contentView.setImageViewResource(R.id.image, getlogo());
                contentView.setTextViewText(R.id.title, bm);
                Notification.Builder mBuilder = null;
                if (style.equals("bt")) {
                    mBuilder = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#309975"))
                            .setGroup("" + titlee)
                            .setCustomContentView(contentView);
                } else {
                    RemoteViews expandView = new RemoteViews(getPackageName(), R.layout.notification_shown_bi);
                    expandView.setImageViewResource(R.id.image, getlogo());
                    expandView.setTextViewText(R.id.title, bm);
                    expandView.setImageViewBitmap(R.id.imgg, LargeIcon(imgg));
                    mBuilder = new Notification.Builder(context, PRIMARY_CHANNEL)
                            .setSmallIcon(getSmallIcon())
                            .setGroup("" + titlee)
                            .setColor(Color.parseColor("#309975"))
                            .setCustomContentView(contentView)
                            .setCustomBigContentView(expandView);
                }
                Notification notification = mBuilder.build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    notification.priority |= Notification.PRIORITY_MAX;
                }
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.flags |= Notification.DEFAULT_LIGHTS;
                notification.contentIntent = resultPendingIntent(bm, body, id, activity);
                getManager().notify(id, notification);
            } else {
                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_shown_st);
                contentView.setImageViewResource(R.id.image, getlogo());
                contentView.setTextViewText(R.id.title, bm);
                NotificationCompat.Builder mBuilder = null;
                if (style.equals("bt")) {
                    mBuilder = new NotificationCompat.Builder(context,PRIMARY_CHANNEL)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#309975"))
                            .setGroup("" + titlee)
                            .setContent(contentView);
                } else {
                    RemoteViews expandView = new RemoteViews(getPackageName(), R.layout.notification_shown_bi);
                    expandView.setImageViewResource(R.id.image, getlogo());
                    expandView.setTextViewText(R.id.title, bm);
                    expandView.setImageViewBitmap(R.id.imgg, LargeIcon(imgg));
                    mBuilder = new NotificationCompat.Builder(context,PRIMARY_CHANNEL)
                            .setSmallIcon(getSmallIcon())
                            .setColor(Color.parseColor("#309975"))
                            .setGroup("" + titlee)
                            .setContent(contentView)
                            .setCustomBigContentView(expandView);
                }
                Notification notification = mBuilder.build();
                if (sund_chk1 == 0) {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                } else {
                    notification.sound = mUri;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    notification.priority |= Notification.PRIORITY_MAX;
                }
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;
                notification.contentIntent = resultPendingIntent(bm, body, id, activity);
                getManager().notify(id, notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void notify(int id, Notification.Builder notification) {
        getManager().notify(id, notification.build());
    }

    public void notify(int id, Notification myNotification) {
        getManager().notify(id, myNotification);
    }

    private int getSmallIcon() {
        return R.drawable.ic_add_shop;
    }

    private int getlogo() {
        return R.drawable.ic_add_shop;
    }

    private Bitmap getlogo1() {
        return BitmapFactory.decodeResource(getResources(), getlogo());
    }

    private Bitmap LargeIcon(String url) {
        Bitmap remote_picture = BitmapFactory.decodeResource(getResources(), getlogo());
        if (url.length() > 5) {
            try {
                remote_picture = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            } catch (IOException e) {
                e.printStackTrace();
                remote_picture = BitmapFactory.decodeResource(getResources(), getlogo());
            }
        } else {
            remote_picture = BitmapFactory.decodeResource(getResources(), getlogo());
        }
        return remote_picture;
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.BigTextStyle bigtext(String Title, String Summary, String bigText) {
        return new NotificationCompat.BigTextStyle()
                .setBigContentTitle(Title)
                .setSummaryText(Summary)
                .bigText(bigText);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Notification.BigTextStyle bigtext1(String Title, String Summary, String bigText) {
        return new Notification.BigTextStyle()
                .setBigContentTitle(Title)
                .setSummaryText(Summary)
                .bigText(bigText);
    }

    public NotificationCompat.BigPictureStyle bigimg(String Title, String Summary, String imgg) {
        return new NotificationCompat.BigPictureStyle()
                .setBigContentTitle(Title)
                .bigPicture(LargeIcon(imgg));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Notification.BigPictureStyle bigimg1(String Title, String Summary, String imgg) {
        return new Notification.BigPictureStyle()
                .setBigContentTitle(Title)
                .setSummaryText(Summary)
                .bigPicture(LargeIcon(imgg));
    }

    public PendingIntent resultPendingIntent(String titt, String msgg, int idd, Class activity) {
        Intent intent = set_intent(context, idd, titt, msgg, activity);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ST_Activity.class);
        stackBuilder.addNextIntent(intent);
        return stackBuilder.getPendingIntent((int) System.currentTimeMillis(), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public PendingIntent resultPendingIntent1(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("" + url));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        return stackBuilder.getPendingIntent((int) System.currentTimeMillis(), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public Intent set_intent(Context context, int iddd, String titt, String msgg, Class activity) {
        Intent intent;
        intent = new Intent(context, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", msgg);
        intent.putExtra("title", titt);
        intent.putExtra("idd", iddd);
        intent.putExtra("Noti_add", 1);
        return intent;
    }
}

