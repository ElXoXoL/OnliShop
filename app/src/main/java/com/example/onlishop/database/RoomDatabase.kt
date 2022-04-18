package com.example.onlishop.database

import com.example.onlishop.app.App
import com.example.onlishop.app.App.Companion.logger
import com.example.onlishop.database.daos.*
import com.example.onlishop.database.models.*
import io.reactivex.rxjava3.core.*


class RoomDatabase(val database: AppDatabase) {

    val shopItems = ShopItems()

    inner class ShopItems {

        private val dao: ShopItemDao = database.itemDao()

        fun getItems(): List<ShopItem> = dao.getAll()

        fun getItemsRx(): Single<List<ShopItem>> {
            App.logger.logDevWithThread("getItemsRx DB")
            return dao.getAllRx()
        }

        fun getItems(groupId: Int): List<ShopItem> = dao.loadForGroup(groupId)

        fun getItemsRx(groupId: Int): Observable<List<ShopItem>> = dao.loadForGroupRx(groupId)

        fun getItems(name: String): List<ShopItem> = dao.loadForSearchName(name)

        fun getItem(itemId: Int): ShopItem? = dao.loadSingle(itemId)

        fun getItemRx(itemId: Int): Maybe<ShopItem> = dao.loadSingleRx(itemId)

        fun setItemsRx(list: List<ShopItem>) = dao.insertAllRx(list)

        fun count(): Int = dao.getItemsCount()

        fun countRx(): Single<Int> {
            App.logger.logDevWithThread("countRx DB")
            return dao.getItemsCountRx()
        }

        fun nuke() = dao.nukeAll()

    }

    val shopGroups = ShopGroups()

    inner class ShopGroups {

        private val dao: ShopGroupDao = database.groupDao()

        fun getItems(): List<ShopGroup> = dao.getAll()

        fun getItemsRx(): Single<List<ShopGroup>> = dao.getAllRx()

        fun getByName(search: String): List<ShopGroup> = dao.loadForSearchName(search)

        fun setItemsRx(list: List<ShopGroup>) = dao.insertAllRx(list)

        fun countRx(): Single<Int> = dao.getItemsCountRx()

        fun nuke() = dao.nukeAll()

    }

    val shopBag = ShopBag()

    inner class ShopBag {

        private val dao: ShopBagItemDao = database.bagDao()

        fun getItemsRx(): Flowable<List<ShopBagItem>> = dao.getAllRx()

        fun setItems(list: List<ShopBagItem>) = dao.insertAll(list)

        fun addItemRx(item: ShopBagItem): Completable {
            App.logger.logDevWithThread("addItemRx item")

            return Single.just(item).flatMapCompletable {
                App.logger.logDevWithThread("addItemRx item2")
                val itemFromDao = dao.loadSingle(it.id, it.size)

                if (itemFromDao != null) {
                    val itemAdd = itemFromDao.copy(count = itemFromDao.count + 1)
                    dao.insertRx(itemAdd)
                } else {
                    dao.insertRx(it)
                }
            }
        }

        fun removeItemRx(bagItemId: Int): Completable {
            App.logger.logDevWithThread("removeItemRx item")

            return Single.just(bagItemId).flatMapCompletable {
                App.logger.logDevWithThread("removeItemRx item2")

                val itemFromDao = dao.loadSingle(it)
                if (itemFromDao != null) {
                    val itemAdd = itemFromDao.copy(count = itemFromDao.count - 1)
                    if (itemAdd.count <= 0) {
                        dao.deleteRx(it)
                    } else {
                        dao.insertRx(itemAdd)
                    }
                } else {
                    dao.deleteRx(it)
                }
            }
        }


        fun count(): Int = dao.getItemsCount()

        fun nuke() = dao.nukeAll()

    }

    val shopOrder = ShopOrders()

    inner class ShopOrders {

        private val dao: ShopOrderDao = database.orderDao()

        fun getItemsRx(): Single<List<ShopOrder>> = dao.getAllRx()

        fun insertRx(order: ShopOrder): Completable {
            logger.logDevWithThread("insertRx from Completable.mergeArray")
            return dao.insertRx(order)
        }

        fun nukeRx() = dao.nukeAllRx()


    }

    val shopOrderItems = ShopOrderItems()

    inner class ShopOrderItems {

        private val dao: ShopOrderItemDao = database.orderItemDao()

        fun getItems(): List<ShopOrderItem> = dao.getAll()

        fun getForOrder(orderId: String) = dao.loadForOrder(orderId)

        fun addItem(item: ShopOrderItem) = dao.insert(item)

        fun addItemsRx(items: List<ShopOrderItem>): Completable {
            logger.logDevWithThread("addItemsRx from Completable.mergeArray")
            return dao.insertAllRx(items)
        }

        fun nukeRx() = dao.nukeAllRx()

    }

    val shopUsers = ShopUsers()

    inner class ShopUsers {

        private val dao: ShopUserDao = database.userDao()

        fun getUsers(): List<ShopUser> = dao.getAll()

        fun getSingleRx(): Maybe<ShopUser> = dao.loadSingleRx()

        fun insert(item: ShopUser) = dao.insert(item)

        fun nukeRx() = dao.nukeAllRx()

    }
}