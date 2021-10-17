package com.example.onlishop.network.groups

import com.example.onlishop.database.models.ShopGroup

class GroupsServiceImpl: GroupsService {

    override fun getGroups(): List<ShopGroup> {
        return listOf(
            ShopGroup(
                id = 0,
                parentGroupId = null,
                name = "Все"
            ),
            ShopGroup(
                id = 1,
                parentGroupId = 0,
                name = "Мужское"
            ),
            ShopGroup(
                id = 2,
                parentGroupId = 0,
                name = "Жеское"
            ),
            ShopGroup(
                id = 11,
                parentGroupId = 1,
                name = "Верхняя одежда"
            ),
            ShopGroup(
                id = 12,
                parentGroupId = 1,
                name = "Штаны/шорты"
            ),
            ShopGroup(
                id = 13,
                parentGroupId = 1,
                name = "Футболки"
            ),
            ShopGroup(
                id = 21,
                parentGroupId = 2,
                name = "Верхняя одежда"
            ),
            ShopGroup(
                id = 22,
                parentGroupId = 2,
                name = "Штаны/шорты"
            ),
            ShopGroup(
                id = 23,
                parentGroupId = 2,
                name = "Футболки"
            ),
        )
    }

}