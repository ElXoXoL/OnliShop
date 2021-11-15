package com.example.onlishop.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.onlishop.base.Model
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.database.models.ShopOrder
import com.example.onlishop.utils.Crypt

class Order(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val orderType: String = "",
    val delivery: String = "",
    val cardNum: String = "",
    val cardDate: String = "",
    val date: String = "",
    val orderItems: List<OrderItem>
) : Model() {
    companion object {
        fun from(it: ShopOrder, items: List<OrderItem>, crypt: Crypt): Order = Order(
            id = it.id,
            userId = it.userId,
            name = it.name,
            phone = crypt.decrypt(it.phone),
            email = crypt.decrypt(it.email),
            orderType = it.orderType,
            delivery = crypt.decrypt(it.delivery),
            cardNum = crypt.decrypt(it.cardNum),
            cardDate = crypt.decrypt(it.cardDate),
            date = it.date,
            orderItems = items,
        )
    }
}