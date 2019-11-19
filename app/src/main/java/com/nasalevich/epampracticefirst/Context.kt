package com.nasalevich.epampracticefirst

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showLongToast(@StringRes text: Int){
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}