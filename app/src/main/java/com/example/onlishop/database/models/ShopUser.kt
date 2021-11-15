package com.example.onlishop.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.onlishop.base.Model

@Entity
data class ShopUser(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val pass: String = "",
)