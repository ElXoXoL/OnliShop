package com.example.onlishop.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.onlishop.base.Model

@Entity
data class ShopOrderItem(
    val id: Int = 0,
    val size: String = "",
    val count: Int = 1,
    val orderId: String = "",
    @PrimaryKey(autoGenerate = true) val orderItemId: Int = 0,
)