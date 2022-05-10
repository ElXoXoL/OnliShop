package com.example.onlishop.models

data class OrderItem(
    val item: Item,
    val size: String,
    var count: Int
)