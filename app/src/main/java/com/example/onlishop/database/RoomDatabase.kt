package com.example.onlishop.database

import com.example.onlishop.database.daos.ShopBagItemDao
import com.example.onlishop.database.daos.ShopGroupDao
import com.example.onlishop.database.daos.ShopItemDao
import com.example.onlishop.database.models.ShopBagItem
import com.example.onlishop.database.models.ShopGroup
import com.example.onlishop.database.models.ShopItem


class RoomDatabase(private val database: AppDatabase) {

    val shopItems = ShopItems()

    inner class ShopItems{

        private val dao: ShopItemDao = database.itemDao()

        suspend fun getItems(): List<ShopItem> = dao.getAll()

        suspend fun getItems(groupId: Int): List<ShopItem> = dao.loadForGroup(groupId)

        suspend fun getItems(search: String): List<ShopItem> = dao.loadForSearch(search)

        suspend fun getItem(itemId: Int): ShopItem? = dao.loadSingle(itemId)

        suspend fun setItems(list: List<ShopItem>) = dao.insertAll(list)

        suspend fun count(): Int = dao.getItemsCount()

        suspend fun nuke() = dao.nukeAll()

    }

    val shopGroups = ShopGroups()

    inner class ShopGroups{

        private val dao: ShopGroupDao = database.groupDao()

        suspend fun getItems(): List<ShopGroup> = dao.getAll()

        suspend fun setItems(list: List<ShopGroup>) = dao.insertAll(list)

        suspend fun count(): Int = dao.getItemsCount()

        suspend fun nuke() = dao.nukeAll()

    }

    val shopBag = ShopBag()

    inner class ShopBag{

        private val dao: ShopBagItemDao = database.bagDao()

        suspend fun getItems(): List<ShopBagItem> = dao.getAll()

        suspend fun setItems(list: List<ShopBagItem>) = dao.insertAll(list)

        suspend fun addItem(item: ShopBagItem) {
            val itemFromDao = dao.loadSingle(item.id, item.size)
            if (itemFromDao != null){
                val itemAdd = itemFromDao.copy(count = itemFromDao.count + 1)
                dao.insert(itemAdd)
            } else {
                dao.insert(item)
            }
        }

        suspend fun removeItem(bagItemId: Int){
            val itemFromDao = dao.loadSingle(bagItemId)
            if (itemFromDao != null){
                val itemAdd = itemFromDao.copy(count = itemFromDao.count - 1)
                if (itemAdd.count <= 0){
                    dao.delete(bagItemId)
                } else {
                    dao.insert(itemAdd)
                }
            } else {
                dao.delete(bagItemId)
            }
        }

        suspend fun count(): Int = dao.getItemsCount()

        suspend fun nuke() = dao.nukeAll()

    }
}