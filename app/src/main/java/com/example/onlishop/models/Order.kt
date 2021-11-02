package com.example.onlishop.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.onlishop.base.Model
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.database.models.ShopOrder

class Order(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val orderType: String = "",
    val delivery: String = "",
    val cardNum: String = "",
    val date: String = "",
    val orderItems: List<OrderItem>
) : Model() {
    companion object {
        fun from(it: ShopOrder, items: List<OrderItem>): Order = Order(
            id = it.id,
            userId = it.userId,
            name = it.name,
            phone = it.phone,
            email = it.email,
            orderType = it.orderType,
            delivery = it.delivery,
            cardNum = it.cardNum,
            date = it.date,
            orderItems = items,
        )
    }
}