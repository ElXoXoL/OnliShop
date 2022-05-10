package com.example.onlishop.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShopItem(
    @PrimaryKey val id: Int = 0,
    val groupId: Int = 0,
    val name: String = "",
    val description: String = "",
    val imageDrawable: Int = 0,
    val price: Int = 0,
    val sizes: String = "",
)