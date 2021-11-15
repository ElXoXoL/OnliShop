package com.example.onlishop.utils

import android.app.Activity
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat


object LinkOpenHelper {

    fun openPrivacyPolicy(activity: Activity){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.termsfeed.com/live/533142b5-b798-4ecf-93d2-448b5b68a68c"))
        activity.startActivity(browserIntent)
    }

    fun openHelpPhone(activity: Activity) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:+380672314213")
        }
        activity.startActivity(intent)
    }

}