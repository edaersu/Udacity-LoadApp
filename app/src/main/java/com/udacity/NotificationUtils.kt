package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

const val CHANNEL_ID = "channelId"
private const val NOTIFICATION_ID = 0
const val ARG_REPOSITORY_OPTION = "arg_repository_option"
const val RESULT_CODE = 0

fun NotificationManager.sendNotification(
    applicationContext: Context,
    repositoryOption: ButtonState
) {
    val detailIntent = Intent(applicationContext, DetailActivity::class.java).apply {
        putExtra(ARG_REPOSITORY_OPTION, repositoryOption)
    }

    val detailPendingIntent: PendingIntent? = TaskStackBuilder.create(applicationContext).run {
        addNextIntentWithParentStack(detailIntent)
        getPendingIntent(RESULT_CODE, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    val builder = NotificationCompat.Builder(
        applicationContext,
        CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(
            applicationContext.getString(R.string.notification_title)
        )
        .setContentText(
            applicationContext.getString(R.string.notification_description)
        )
        .addAction(
            0,
            applicationContext.getString(R.string.notification_button),
            detailPendingIntent
        )

    notify(NOTIFICATION_ID, builder.build())
}
