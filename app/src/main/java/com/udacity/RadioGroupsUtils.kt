package com.udacity

import android.widget.RadioGroup

fun RadioGroup.getRepositoryOptionSelected(): ButtonState? {
    return when (checkedRadioButtonId) {
        R.id.radioButtonGlide -> ButtonState(
            R.string.glide_option,
            "https://github.com/bumptech/glide"
        )
        R.id.radioButtonLoadApp -> ButtonState(
            R.string.load_app_option,
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        )
        R.id.radioButtonRetrofit -> ButtonState(
            R.string.retrofit_option,
            "https://github.com/square/retrofit"
        )
        else -> null
    }
}