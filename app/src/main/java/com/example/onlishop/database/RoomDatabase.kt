package com.example.onlishop.database

import com.example.onlishop.database.daos.*
import com.example.onlishop.database.models.*
import kotlinx.coroutines.flow.Flow


class RoomDatabase(private val database: AppDatabase) {

    val shopItems = ShopItems()

    inner class ShopItems{

        private val dao: ShopItemDao = database.itemDao()

        suspend fun getItems(): List<ShopItem> = dao.getAll()

        suspend fun getItems(groupId: Int): List<ShopItem> = dao.loadForGroup(groupId)

        suspend fun getItems(name: String): List<ShopItem> = dao.loadForSearchName(name)

        suspend fun getItem(itemId: Int): ShopItem? = dao.loadSingle(itemId)

        suspend fun setItems(list: List<ShopItem>) = dao.insertAll(list)

        suspend fun count(): Int = dao.getItemsCount()

        suspend fun nuke() = dao.nukeAll()

    }

    val shopGroups = ShopGroups()

    inner class ShopGroups{

        private val dao: ShopGroupDao = database.groupDao()

        suspend fun getItems(): List<ShopGroup> = dao.getAll()

        suspend fun getByName(search: String): List<ShopGroup> = dao.loadForSearchName(search)

        suspend fun setItems(list: List<ShopGroup>) = dao.insertAll(list)

        suspend fun count(): Int = dao.getItemsCount()

        suspend fun nuke() = dao.nukeAll()

    }

    val shopBag = ShopBag()

    inner class ShopBag{

        private val dao: ShopBagItemDao = database.bagDao()

        suspend fun getItems(): List<ShopBagItem> = dao.getAll()

        fun getItemsFlow(): Flow<List<ShopBagItem>> = dao.getAllFlow()

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

    val shopOrder = ShopOrders()

    inner class ShopOrders{

        private val dao: ShopOrderDao = database.orderDao()

        suspend fun getItems(): List<ShopOrder> = dao.getAll()

        fun getItemsFlow(): Flow<List<ShopOrder>> = dao.getAllFlow()

        suspend fun insert(order: ShopOrder) = dao.insert(order)

        suspend fun nuke() = dao.nukeAll()

    }

    val shopOrderItems = ShopOrderItems()

    inner class ShopOrderItems{

        private val dao: ShopOrderItemDao = database.orderItemDao()

        suspend fun getItems(): List<ShopOrderItem> = dao.getAll()

        suspend fun getForOrder(orderId: String) = dao.loadForOrder(orderId)

        suspend fun addItem(item: ShopOrderItem) = dao.insert(item)

        suspend fun addItems(items: List<ShopOrderItem>) = dao.insertAll(items)

        suspend fun nuke() = dao.nukeAll()

    }

    val shopUsers = ShopUsers()

    inner class ShopUsers{

        private val dao: ShopUserDao = database.userDao()

        suspend fun getUsers(): List<ShopUser> = dao.getAll()

        fun getUsersFlow(): Flow<List<ShopUser>> = dao.getAllFlow()

        suspend fun getSingle(): ShopUser? = dao.loadSingle()

        suspend fun insert(item: ShopUser) = dao.insert(item)

        suspend fun nuke() = dao.nukeAll()

    }
}