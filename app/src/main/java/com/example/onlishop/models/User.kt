package com.example.onlishop.models

import com.example.onlishop.base.Model
import com.example.onlishop.database.models.ShopUser

class User(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val pass: String = "",
    val confirmPass: String = "",
): Model() {
    companion object {
        fun from(shopUser: ShopUser?): User? {
            if (shopUser == null) return null
            return User(
                id = shopUser.id,
                name = shopUser.name,
                phone = shopUser.phone,
                email = shopUser.email,
                pass = shopUser.pass,
                confirmPass = shopUser.pass
            )
        }
    }
}