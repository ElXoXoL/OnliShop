package com.example.onlishop.models

import com.example.onlishop.base.Model
import com.example.onlishop.database.models.ShopGroup

class Group (
        val id: Int = 0,
        val parentGroupId: Int? = null,
        val name: String = "",
        var isSelected: Boolean = false
): Model() {
        companion object {
                fun from(it: ShopGroup): Group = Group(
                        it.id, it.parentGroupId, it.name
                )
        }
}