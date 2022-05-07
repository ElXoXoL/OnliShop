package com.example.onlishop.models

import com.example.onlishop.base.Model
import com.example.onlishop.database.models.ShopItem

data class Item(
        val id: Int = 0,
        val groupId: Int = 0,
        val name: String = "",
        val description: String = "",
        val imageDrawable: Int = 0,
        val price: Int = 0,
        val sizes: List<Size> = emptyList(),
    ): Model() {
        companion object {
                fun from(it: ShopItem): Item = Item(
                        id = it.id,
                        groupId = it.groupId,
                        name = it.name,
                        description = it.description,
                        imageDrawable = it.imageDrawable,
                        price = it.price,
                        sizes = it.sizes
                                .split(",")
                                .dropLastWhile { it.isEmpty() }
                                .map { Size(it, false) }
                )
        }
}