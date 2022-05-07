package com.example.onlishop.models

import com.example.onlishop.base.Model
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.database.models.ShopOrder
import com.example.onlishop.database.models.ShopOrderItem

data class OrderItem(
    val orderItemId: Int,
    val item: Item,
    val size: String,
    var count: Int
) : Model()