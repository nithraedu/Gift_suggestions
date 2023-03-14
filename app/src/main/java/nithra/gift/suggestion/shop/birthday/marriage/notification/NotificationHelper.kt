package nithra.gift.suggestion.shop.birthday.marriage.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import nithra.gift.suggestion.shop.birthday.marriage.R
import java.io.IOException
import java.io.InputStream
import java.net.URL

internal class NotificationHelper(var context: Context) : ContextWrapper(
    context
) {
    private var manager: NotificationManager? = null
        private get() {
            if (field == null) {
                field = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return field
        }
    var chan1: NotificationChannel? = null

    fun Notification_bm(
        id: Int,
        title: String,
        body: String?,
        imgg: String,
        style: String,
        bm: String?,
        sund_chk1: Int,
        activity: Class<*>?
    ) {
        try {
            val mUri: Uri
            mUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            println("mUri : $mUri")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val nb: Notification.Builder
                if (style == "bt") {
                    val chan1 = NotificationChannel(
                        PRIMARY_CHANNEL,
                        "Primary Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    chan1.lightColor = Color.GREEN
                    chan1.setShowBadge(true)
                    chan1.setSound(
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                            .build()
                    )
                    nb = Notification.Builder(context, PRIMARY_CHANNEL)
                        .setContentTitle("Gift Suggestions")
                        .setContentText("")
                        .setContentIntent(resultPendingIntent(bm, body, id, activity))
                        .setSmallIcon(smallIcon)
                        .setColor(Color.parseColor("#0d50d9"))
                        .setLargeIcon(LargeIcon(imgg))
                        .setGroup("" + title)
                        .setStyle(bigtext1("Gift Suggestions", "", bm))
                        .setAutoCancel(true)
                } else {
                    val chan1 = NotificationChannel(
                        PRIMARY_CHANNEL,
                        "Primary Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    chan1.lightColor = Color.parseColor("#0d50d9")
                    chan1.setShowBadge(true)
                    chan1.setSound(
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                            .build()
                    )
                    nb = Notification.Builder(context, PRIMARY_CHANNEL)
                        .setContentTitle(bm)
                        .setContentText("")
                        .setContentIntent(resultPendingIntent(bm, body, id, activity))
                        .setSmallIcon(smallIcon)
                        .setColor(Color.parseColor("#0d50d9"))
                        .setLargeIcon(LargeIcon(imgg))
                        .setGroup("" + title)
                        .setStyle(bigimg1("Gift Suggestions", bm, imgg))
                        .setAutoCancel(true)
                }
                notify(id, nb)
            } else {
                val myNotification: Notification
                myNotification = if (style == "bt") {
                    NotificationCompat.Builder(context, PRIMARY_CHANNEL)
                        .setSound(mUri)
                        .setSmallIcon(smallIcon)
                        .setColor(Color.parseColor("#0d50d9"))
                        .setLargeIcon(getlogo1())
                        .setAutoCancel(true)
                        .setPriority(2)
                        .setContentIntent(resultPendingIntent(bm, body, id, activity))
                        .setContentTitle("Gift Suggestions")
                        .setContentText("")
                        .setGroup("" + title)
                        .setStyle(bigtext("Gift Suggestions", "", bm))
                        .build()
                } else {
                    NotificationCompat.Builder(context, PRIMARY_CHANNEL)
                        .setSound(mUri)
                        .setSmallIcon(smallIcon)
                        .setColor(Color.parseColor("#0d50d9"))
                        .setLargeIcon(getlogo1())
                        .setAutoCancel(true)
                        .setPriority(2)
                        .setContentIntent(resultPendingIntent(bm, body, id, activity))
                        .setContentTitle(bm)
                        .setContentText("")
                        .setGroup("" + title)
                        .setStyle(bigimg("Gift Suggestions", bm, imgg))
                        .build()
                }
                notify(id, myNotification)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun Notification1(
        id: Int,
        title: String,
        body1: String?,
        imgg: String,
        style: String,
        bm: String,
        sund_chk1: Int,
        activity: Class<*>?
    ) {
        try {
            val mUri: Uri
            mUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val nb: Notification.Builder
                if (style == "bt") {
                    val chan1 = NotificationChannel(
                        PRIMARY_CHANNEL,
                        "Primary Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    chan1.lightColor = Color.parseColor("#0d50d9")
                    chan1.setShowBadge(true)
                    chan1.setSound(
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                            .build()
                    )
                    nb = Notification.Builder(context, PRIMARY_CHANNEL)
                        .setContentTitle(title)
                        .setContentText("")
                        .setContentIntent(resultPendingIntent(bm, body1, id, activity))
                        .setSmallIcon(smallIcon)
                        .setColor(Color.parseColor("#0d50d9"))
                        .setLargeIcon(LargeIcon(imgg))
                        .setGroup("" + title)
                        .setStyle(bigtext1(title, "Gift Suggestions", ""))
                        .setAutoCancel(true)
                } else {
                    val chan1 = NotificationChannel(
                        PRIMARY_CHANNEL,
                        "Primary Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    chan1.lightColor = Color.parseColor("#0d50d9")
                    chan1.setShowBadge(true)
                    chan1.setSound(
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                            .build()
                    )
                    nb = Notification.Builder(context, PRIMARY_CHANNEL)
                        .setContentTitle(title)
                        .setContentText("")
                        .setContentIntent(resultPendingIntent1(bm))
                        .setSmallIcon(smallIcon)
                        .setColor(Color.parseColor("#0d50d9"))
                        .setLargeIcon(LargeIcon(imgg))
                        .setGroup("" + title)
                        .setStyle(bigimg1(title, "Gift Suggestions", imgg))
                        .setAutoCancel(true)
                }
                notify(id, nb)
            } else {
                val myNotification: Notification
                myNotification = if (style == "bt") {
                    NotificationCompat.Builder(context, PRIMARY_CHANNEL)
                        .setSound(mUri)
                        .setSmallIcon(smallIcon)
                        .setColor(Color.parseColor("#0d50d9"))
                        .setLargeIcon(getlogo1())
                        .setAutoCancel(true)
                        .setPriority(2)
                        .setContentIntent(resultPendingIntent(bm, body1, id, activity))
                        .setContentTitle(title)
                        .setContentText("")
                        .setGroup("" + title)
                        .setStyle(bigtext(title, "Gift Suggestions", ""))
                        .build()
                } else {
                    NotificationCompat.Builder(context, PRIMARY_CHANNEL)
                        .setSound(mUri)
                        .setSmallIcon(smallIcon)
                        .setColor(Color.parseColor("#0d50d9"))
                        .setLargeIcon(getlogo1())
                        .setAutoCancel(true)
                        .setPriority(2)
                        .setContentIntent(resultPendingIntent1(bm))
                        .setContentTitle(title)
                        .setGroup("" + title)
                        .setContentText("")
                        .setStyle(bigimg(title, "Gift Suggestions", imgg))
                        .build()
                }
                notify(id, myNotification)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun Notification_custom(
        id: Int,
        titlee: String,
        body: String?,
        imgg: String?,
        style: String,
        bm: String?,
        sund_chk1: Int,
        activity: Class<*>?
    ) {
        try {
            val mUri: Uri
            mUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val contentView = RemoteViews(packageName, R.layout.notification_shown_st)
                contentView.setImageViewResource(R.id.image, getlogo())
                contentView.setTextViewText(R.id.title, bm)
                var mBuilder: Notification.Builder? = null
                mBuilder = if (style == "bt") {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Notification.Builder(context, PRIMARY_CHANNEL)
                            .setSmallIcon(smallIcon)
                            .setColor(Color.parseColor("#6460AA"))
                            .setGroup("" + titlee)
                            .setCustomContentView(contentView)
                    } else {
                        Notification.Builder(context, PRIMARY_CHANNEL)
                            .setSmallIcon(smallIcon)
                            .setColor(Color.parseColor("#6460AA"))
                            .setGroup(bm)
                            .setCustomContentView(contentView)
                    }
                } else {
                    val expandView = RemoteViews(packageName, R.layout.notification_shown_bi)
                    expandView.setImageViewResource(R.id.image, getlogo())
                    expandView.setTextViewText(R.id.title, bm)
                    expandView.setImageViewBitmap(R.id.imgg, LargeIcon(imgg!!))
                    Notification.Builder(context, PRIMARY_CHANNEL)
                        .setSmallIcon(smallIcon)
                        .setColor(Color.parseColor("#6460AA"))
                        .setCustomContentView(contentView)
                        .setGroup(bm)
                        .setCustomBigContentView(expandView)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    mBuilder.setStyle(Notification.DecoratedCustomViewStyle())
                }
                val notification = mBuilder.build()
                notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
                notification.flags = notification.flags or Notification.FLAG_SHOW_LIGHTS
                notification.contentIntent = resultPendingIntent(bm, body, id, activity)
                manager!!.notify(id, notification)
            } else {
                val contentView = RemoteViews(packageName, R.layout.notification_shown_st)
                contentView.setImageViewResource(R.id.image, getlogo())
                contentView.setTextViewText(R.id.title, bm)
                var mBuilder: NotificationCompat.Builder? = null
                mBuilder = if (style == "bt") {
                    NotificationCompat.Builder(context)
                        .setSmallIcon(smallIcon)
                        .setColor(Color.parseColor("#6460AA"))
                        .setGroup("" + titlee)
                        .setContent(contentView)
                } else {
                    val expandView = RemoteViews(packageName, R.layout.notification_shown_bi)
                    expandView.setImageViewResource(R.id.image, getlogo())
                    expandView.setTextViewText(R.id.title, bm)
                    expandView.setImageViewBitmap(R.id.imgg, LargeIcon(imgg!!))
                    NotificationCompat.Builder(context)
                        .setSmallIcon(smallIcon)
                        .setColor(Color.parseColor("#6460AA"))
                        .setGroup("" + titlee)
                        .setContent(contentView)
                        .setCustomBigContentView(expandView)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    mBuilder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                }
                val notification = mBuilder.build()
                if (sund_chk1 == 0) {
                    notification.defaults = notification.defaults or Notification.DEFAULT_SOUND
                } else {
                    notification.sound = mUri
                }
                notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
                notification.flags = notification.flags or Notification.FLAG_SHOW_LIGHTS
                notification.contentIntent = resultPendingIntent(bm, body, id, activity)
                manager!!.notify(id, notification)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun notify(id: Int, notification: Notification.Builder) {
        manager!!.notify(id, notification.build())
    }

    fun notify(id: Int, myNotification: Notification?) {
        manager!!.notify(id, myNotification)
    }

    private val smallIcon: Int
        private get() = R.drawable.gift

    private fun getlogo(): Int {
        return R.drawable.gift_suggestions_round
    }

    private fun getlogo1(): Bitmap {
        return BitmapFactory.decodeResource(resources, getlogo())
    }

    private fun LargeIcon(url: String): Bitmap {
        var remote_picture = BitmapFactory.decodeResource(resources, getlogo())
        remote_picture = if (url.length > 5) {
            try {
                BitmapFactory.decodeStream(URL(url).content as InputStream)
            } catch (e: IOException) {
                e.printStackTrace()
                BitmapFactory.decodeResource(resources, getlogo())
            }
        } else {
            BitmapFactory.decodeResource(resources, getlogo())
        }
        return remote_picture
    }

    fun bigtext(
        Title: String?,
        Summary: String?,
        bigText: String?
    ): NotificationCompat.BigTextStyle {
        return NotificationCompat.BigTextStyle()
            .setBigContentTitle(Title)
            .setSummaryText(Summary)
            .bigText(bigText)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun bigtext1(Title: String?, Summary: String?, bigText: String?): Notification.BigTextStyle {
        return Notification.BigTextStyle()
            .setBigContentTitle(Title)
            .setSummaryText(Summary)
            .bigText(bigText)
    }

    fun bigimg(Title: String?, Summary: String?, imgg: String): NotificationCompat.BigPictureStyle {
        return NotificationCompat.BigPictureStyle()
            .setBigContentTitle(Title) // .setSummaryText(Summary)
            .bigPicture(LargeIcon(imgg))
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun bigimg1(Title: String?, Summary: String?, imgg: String): Notification.BigPictureStyle {
        return Notification.BigPictureStyle()
            .setBigContentTitle(Title)
            .setSummaryText(Summary)
            .bigPicture(LargeIcon(imgg))
    }

    fun resultPendingIntent(
        titt: String?,
        msgg: String?,
        idd: Int,
        activity: Class<*>?
    ): PendingIntent? {
        val intent = set_intent(context, idd, titt, msgg, activity)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(ST_Activity::class.java)
        stackBuilder.addNextIntent(intent)


        val i: Int
        i = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return stackBuilder.getPendingIntent(System.currentTimeMillis().toInt(), i)
    }

    fun resultPendingIntent1(url: String): PendingIntent? {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("" + url))
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntent(intent)

        val i: Int
        i = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return stackBuilder.getPendingIntent(System.currentTimeMillis().toInt(), i)
    }

    fun set_intent(
        context: Context?,
        iddd: Int,
        titt: String?,
        msgg: String?,
        activity: Class<*>?
    ): Intent {
        val intent: Intent
        intent = Intent(context, activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("message", msgg)
        intent.putExtra("title", titt)
        intent.putExtra("idd", iddd)
        intent.putExtra("Noti_add", 1)
        return intent
    }

    companion object {
        const val PRIMARY_CHANNEL = "default"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan1 = NotificationChannel(
                PRIMARY_CHANNEL,
                "Primary Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            chan1!!.lightColor = Color.GREEN
            chan1!!.setShowBadge(true)
            chan1!!.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            manager!!.createNotificationChannel(chan1!!)
        }
    }
}