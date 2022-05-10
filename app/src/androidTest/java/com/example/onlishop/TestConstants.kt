package com.example.onlishop

import com.example.onlishop.database.models.ShopGroup
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.models.*

object TestConstants {

    val S_GROUP_0 = ShopGroup(
        id = 0,
        parentGroupId = null,
        name = "Все"
    )
    val GROUP_0 = Group.from(S_GROUP_0)

    val S_GROUP_1 = ShopGroup(
        id = 1,
        parentGroupId = 0,
        name = "Мужское"
    )
    val GROUP_1 = Group.from(S_GROUP_1)

    val S_GROUP_2 = ShopGroup(
        id = 11,
        parentGroupId = 1,
        name = "Верхняя одежда"
    )
    val GROUP_2 = Group.from(S_GROUP_2)

    val S_GROUPS = listOf(
        S_GROUP_0,
        S_GROUP_1,
        S_GROUP_2,
    )
    val GROUPS = S_GROUPS.map(Group::from)

    val S_ITEM_1 = ShopItem(
        id = 1,
        groupId = 11,
        name = "АНОРАК",
        description = "",
        imageDrawable = 1,
        price = 2200,
        sizes = "s,m,l,xl"
    )
    val ITEM_1 = Item.from(S_ITEM_1)
    val S_ITEM_2 = ShopItem(
        id = 2,
        groupId = 11,
        name = "ЖИЛЕТКА",
        description = "",
        imageDrawable = 2,
        price = 2850,
        sizes = "m,l,xl,xxl"
    )
    val ITEM_2 = Item.from(S_ITEM_2)
    val S_ITEM_3 = ShopItem(
        id = 9,
        groupId = 13,
        name = "T-SHIRT",
        description = "",
        imageDrawable = 9,
        price = 1600,
        sizes = "s,m,l,xl"
    )
    val ITEM_3 = Item.from(S_ITEM_3)

    val S_ITEMS =  listOf(
        S_ITEM_1,
        S_ITEM_2,
        S_ITEM_3,
    )
    val ITEMS = S_ITEMS.map(Item::from)

    val USER = User(
        id = "1",
        name = "Dud",
        phone = "+380671231212",
        email = "dud@dud.dud",
        pass = "123q",
        confirmPass = "123q"
    )

    val ORDER_1 = Order(
        id = "1",
        userId = "",
        name = "",
        phone = "",
        email = "",
        orderType = "",
        delivery = "",
        cardNum = "",
        cardDate = "",
        date = "",
        orderItems = listOf(
            OrderItem(
                ITEM_1, "l", 2
            )
        ),
    )

    val ORDER_2 = Order(
        id = "2",
        userId = "",
        name = "",
        phone = "",
        email = "",
        orderType = "",
        delivery = "",
        cardNum = "",
        cardDate = "",
        date = "",
        orderItems = listOf(
            OrderItem(
                ITEM_1, "l", 2
            ),
            OrderItem(
                ITEM_2, "xl", 1
            )
        ),
    )
}