package com.example.onlishop.network.items

import com.example.onlishop.database.models.ShopGroup
import com.example.onlishop.database.models.ShopItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ItemsService {

    fun getItems(): List<ShopItem>

    fun getItemsRx(): Single<List<ShopItem>>

}