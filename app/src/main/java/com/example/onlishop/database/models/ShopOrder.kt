package com.example.onlishop.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShopOrder(
    @PrimaryKey(autoGenerate = true) val autoId: Int = 0,
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
)