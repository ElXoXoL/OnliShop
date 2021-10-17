package com.example.onlishop.network.items

import com.example.onlishop.database.models.ShopGroup
import com.example.onlishop.database.models.ShopItem

interface ItemsService {

    fun getItems(): List<ShopItem>

}