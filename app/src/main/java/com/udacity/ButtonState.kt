package com.udacity

import android.app.DownloadManager
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ButtonState(
    val nameResId: Int,
    val url: String,
    val status: Int = -1
) : Parcelable {

    fun formatStatus(): Int {
        return when (status) {
            DownloadManager.STATUS_SUCCESSFUL -> R.string.status_success
            else -> R.string.status_unsuccessful
        }
    }
}
