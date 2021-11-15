package com.example.onlishop.models

import com.example.onlishop.base.Model
import com.example.onlishop.database.models.ShopUser
import com.example.onlishop.utils.Crypt

class User(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val pass: String = "",
    val confirmPass: String = "",
): Model() {
    companion object {
        fun from(shopUser: ShopUser?, crypt: Crypt): User? {
            if (shopUser == null) return null
            return User(
                id = shopUser.id,
                name = shopUser.name,
                phone = crypt.decrypt(shopUser.phone),
                email = crypt.decrypt(shopUser.email),
                pass = crypt.decrypt(shopUser.pass),
                confirmPass = crypt.decrypt(shopUser.pass)
            )
        }
    }
}