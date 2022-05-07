package com.example.onlishop.models

import com.example.onlishop.base.Model
import com.example.onlishop.database.models.ShopItem

data class BagItem(
    val bagItemId: Int,
        val item: Item,
        val size: String,
        var count: Int
    ): Model() {

}