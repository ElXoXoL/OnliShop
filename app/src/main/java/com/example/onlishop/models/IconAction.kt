package com.example.onlishop.models

import androidx.annotation.DrawableRes

data class IconAction(
    @DrawableRes val icon: Int,
    val action: () -> Unit = {}
)
