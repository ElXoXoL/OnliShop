package com.example.onlishop.models

data class BagItem(
    val bagItemId: Int,
    val item: Item,
    val size: String,
    var count: Int
)