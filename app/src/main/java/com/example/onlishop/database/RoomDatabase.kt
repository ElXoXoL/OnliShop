package com.example.onlishop.database

import com.example.onlishop.database.daos.ShopGroupDao
import com.example.onlishop.database.daos.ShopItemDao
import com.example.onlishop.database.models.ShopGroup
import com.example.onlishop.database.models.ShopItem


class RoomDatabase(private val database: AppDatabase) {

    val shopItems = ShopItems()

    inner class ShopItems{

        private val dao: ShopItemDao
            get() = database.itemDao()

        suspend fun getItems(): List<ShopItem> = dao.getAll()

        suspend fun setItems(list: List<ShopItem>) = dao.insertAll(list)

        suspend fun count(): Int = dao.getItemsCount()

        suspend fun nuke() = dao.nukeAll()

    }

    val shopGroups = ShopGroups()

    inner class ShopGroups{

        private val dao: ShopGroupDao
            get() = database.groupDao()

        suspend fun getItems(): List<ShopGroup> = dao.getAll()

        suspend fun setItems(list: List<ShopGroup>) = dao.insertAll(list)

        suspend fun count(): Int = dao.getItemsCount()

        suspend fun nuke() = dao.nukeAll()

    }
}