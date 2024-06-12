package com.movies.streamy.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.movies.streamy.BuildConfig
import com.movies.streamy.R


class Notifications {

    companion object {
        private var instance: Notifications? = null
        private const val APP_ID: String = BuildConfig.APPLICATION_ID

        const val CHANNEL_NO_AUDIO = "$APP_ID.NO_AUDIO"
        const val CHANNEL_AUDIO_DEFAULT = "$APP_ID.AUDIO_DEFAULT"
        const val CHANNEL_AUDIO_VT_BIKE = "$APP_ID.AUDIO_VT_BIKE"
        const val CHANNEL_AUDIO_VT_OTHER = "$APP_ID.AUDIO_VT_OTHER"

        const val ID_DEFAULT = 32144123
        const val ID_FOREGROUND_SERVICE = 32144124
        const val RQ_LOGIN_ACTIVITY = 32144125
        const val RQ_MAIN_ACTIVITY = 32144200

        var uriAudioDefault: Uri? = null
        private var uriAudioVTBike: Uri? = null
        private var uriAudioVTOther: Uri? = null

        fun getInstance(context: Context): Notifications? {
            if (instance == null) {
                instance = Notifications(context)
            }
            return instance
        }

        fun getNotification(
            channelId: String,
            notificationBuilder: NotificationCompat.Builder
        ): Notification {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                when (channelId) {
                    CHANNEL_AUDIO_DEFAULT -> notificationBuilder.setSound(uriAudioDefault)
                    CHANNEL_AUDIO_VT_BIKE -> notificationBuilder.setSound(uriAudioVTBike)
                    CHANNEL_AUDIO_VT_OTHER -> notificationBuilder.setSound(uriAudioVTOther)
                    CHANNEL_NO_AUDIO -> notificationBuilder.setSound(null)
                    else -> {

                    }
                }
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) notificationBuilder.setContentTitle(
                "Riley Driver"
            )
            return notificationBuilder //.setContentTitle("Riley Driver")
                .setSmallIcon(R.mipmap.ic_launcher_monochrome)
                .setOngoing(true) // ensures the notification is persistent and non-dismissable
                .build()
        }

    }

    class Notifications(context: Context) {
        private val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        fun notify(channelId: String, notificationBuilder: NotificationCompat.Builder) {
            notificationManager.notify(ID_DEFAULT, getNotification(channelId, notificationBuilder))
        }

        fun notify(
            notificationId: Int,
            channelId: String,
            notificationBuilder: NotificationCompat.Builder
        ) {
            notificationManager.notify(
                notificationId,
                getNotification(channelId, notificationBuilder)
            )
        }

        fun cancelNotification(notificationId: Int) {
            notificationManager.cancel(notificationId)
        }

        init {
            uriAudioDefault = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            uriAudioVTBike = Uri.parse(
                java.lang.String.format(
                    "android.resource://%s/%s",
                    context.packageName,
                    R.raw.outdoor_alert_mp3
                )
            )
            uriAudioVTOther = Uri.parse(
                java.lang.String.format(
                    "android.resource://%s/%s",
                    context.packageName,
                    R.raw.indoor_alert_mp3
                )
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channels: MutableList<NotificationChannel> = ArrayList(0)
                val channelNoAudio = NotificationChannel(
                    CHANNEL_NO_AUDIO,
                    CHANNEL_NO_AUDIO,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                val channelAudioDefault = NotificationChannel(
                    CHANNEL_AUDIO_DEFAULT,
                    CHANNEL_AUDIO_DEFAULT,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channelAudioDefault.setSound(
                    uriAudioDefault,
                    AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build()
                )
                val channelAudioVTBike = NotificationChannel(
                    CHANNEL_AUDIO_VT_BIKE,
                    CHANNEL_AUDIO_VT_BIKE,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channelAudioVTBike.setSound(
                    uriAudioVTBike,
                    AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build()
                )
                val channelAudioVTOther = NotificationChannel(
                    CHANNEL_AUDIO_VT_OTHER,
                    CHANNEL_AUDIO_VT_OTHER,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channelAudioVTOther.setSound(
                    uriAudioVTOther,
                    AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build()
                )
                channels.add(channelNoAudio)
                channels.add(channelAudioDefault)
                channels.add(channelAudioVTBike)
                channels.add(channelAudioVTOther)
                notificationManager.createNotificationChannels(channels)
            }
        }
    }

}