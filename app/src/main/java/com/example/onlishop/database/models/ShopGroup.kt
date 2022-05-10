package com.example.onlishop.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShopGroup(
    @PrimaryKey val id: Int = 0,
    val parentGroupId: Int? = null,
    val name: String = "",
)