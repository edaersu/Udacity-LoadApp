package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val repositoryOption = intent.getParcelableExtra<ButtonState>(ARG_REPOSITORY_OPTION)
        repositoryOption?.let {
            filename.setText(it.nameResId)
            status.setText(it.formatStatus())
            cancelNotification()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cancelNotification() {
        val nm = getSystemService(
            NotificationManager::class.java
        ) as NotificationManager
        nm.cancelAll()
    }
}
