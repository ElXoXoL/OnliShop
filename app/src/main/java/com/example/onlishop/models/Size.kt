package com.example.onlishop.models

import com.example.onlishop.base.Model

data class Size(
    val size: String = "",
    var isSelected: Boolean = false
): Model()