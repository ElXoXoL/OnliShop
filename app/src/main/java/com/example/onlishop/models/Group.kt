package com.example.onlishop.models

import com.example.onlishop.database.models.ShopGroup

data class Group (
        val id: Int = 0,
        val parentGroupId: Int? = null,
        val name: String = "",
        var isSelected: Boolean = false
) {
        companion object {
                fun from(it: ShopGroup): Group = Group(
                        it.id, it.parentGroupId, it.name
                )
        }
}