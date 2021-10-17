package com.example.onlishop.global

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

class PreferenceRepository(context: Context) {

    companion object {
        private const val REFERRER = "ref"
    }

    private val prefs = context.getSharedPreferences("OnliShop", MODE_PRIVATE)

    var isReferrerHandled: Boolean
        get() = prefs.getBoolean(REFERRER, false)
        set(value) = prefs.edit { putBoolean(REFERRER, value) }

}