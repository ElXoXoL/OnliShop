package com.example.onlishop.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.onlishop.base.Model
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.database.models.ShopOrder

data class OrderCheck(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val orderType: String = "",
    val delivery: String = "",
    val cardNum: String = "",
    val cardDate: String = "",
    val cardCvc: String = "",
)