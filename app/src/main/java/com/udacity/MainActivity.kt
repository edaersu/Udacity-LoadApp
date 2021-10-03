package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager

    private lateinit var downloadManager: DownloadManager

    private lateinit var repositoryOption: ButtonState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        notificationManager = getSystemService(
            NotificationManager::class.java
        ) as NotificationManager

        registerReceiver(
            receiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        custom_button.setOnClickListener {
            val selectedRepository = radioGroup.getRepositoryOptionSelected()
            if (selectedRepository != null) {
                repositoryOption = selectedRepository
                download()
            } else {
                Toast.makeText(this, R.string.select_file_message, Toast.LENGTH_SHORT).show()
            }
        }

        createChannel()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.download),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                description = getString(R.string.app_description)
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                return
            }
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                val cursor = downloadManager.query(
                    DownloadManager.Query().setFilterById(downloadID)
                )
                cursor.moveToFirst()
                val status = cursor.getInt(
                    cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                )
                repositoryOption = repositoryOption.copy(status = status)
                notificationManager.sendNotification(
                    this@MainActivity,
                    repositoryOption
                )
            }
        }
    }

    private fun download() {
        val request = DownloadManager.Request(Uri.parse(repositoryOption.url))
            .setTitle(getString(R.string.app_name))
            .setDescription(getString(R.string.app_description))
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        downloadID = downloadManager.enqueue(request)
    }
}
