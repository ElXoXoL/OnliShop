package com.example.onlishop.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.onlishop.base.Model

@Entity
data class ShopOrder(
    @PrimaryKey val id: Int = 0,
    val items: String = "",
    val userId: Int = 0,
    val isBucketOrder: Boolean = true,
)