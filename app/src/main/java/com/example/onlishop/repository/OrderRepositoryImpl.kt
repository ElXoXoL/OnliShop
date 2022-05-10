package com.example.onlishop.repository

import com.example.onlishop.database.daos.ShopItemDao
import com.example.onlishop.database.daos.ShopOrderDao
import com.example.onlishop.database.daos.ShopOrderItemDao
import com.example.onlishop.database.daos.ShopUserDao
import com.example.onlishop.database.models.*
import com.example.onlishop.global.Logger
import com.example.onlishop.models.*
import com.example.onlishop.utils.Crypt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepositoryImpl(
    private val logger: Logger,
    private val shopOrderDao: ShopOrderDao,
    private val shopOrderItemDao: ShopOrderItemDao,
    private val shopUserDao: ShopUserDao,
    private val shopItemDao: ShopItemDao,
    private val crypt: Crypt,
) : OrderRepository {

    //TODO : extract user stuff to user repo

    /**
     * Get orders flow, then get items for order one by one, map everything to Order.kt
     * @return list of orders combined with their items
     */
    override fun getOrdersFlow(): Flow<List<Order>> {
        return shopOrderDao.getAllFlow().map { orderList ->
            orderList.map {
                getOrderWithItems(it)
            }
        }
    }

    override suspend fun getLastOrder(): Order? {
        val lastOrder = shopOrderDao.getLast() ?: return null
        return getOrderWithItems(lastOrder)
    }

    private fun getOrderWithItems(order: ShopOrder): Order {
        val orderItems = shopOrderItemDao.loadForOrder(order.id).map { orderItem ->
            val item = Item.from(shopItemDao.loadSingle(orderItem.id)!!)
            OrderItem(
                item = item,
                size = orderItem.size,
                count = orderItem.count
            )
        }
        return Order.from(order, orderItems, crypt)
    }

    override suspend fun addOrder(order: Order) {
        val shopOrder = ShopOrder(
            id = order.id,
            userId = order.userId,
            name = order.name,
            phone = crypt.encrypt(order.phone),
            email = crypt.encrypt(order.email),
            orderType = order.orderType,
            delivery = crypt.encrypt(order.delivery),
            cardNum = crypt.encrypt(order.cardNum),
            cardDate = crypt.encrypt(order.cardDate),
            date = order.date,
        )
        val orderItems = order.orderItems.map {
            ShopOrderItem(
                id = it.item.id,
                size = it.size,
                count = it.count,
                orderId = order.id
            )
        }
        shopOrderItemDao.insertAll(orderItems)
        shopOrderDao.insert(shopOrder)
    }

    override suspend fun addUser(user: User) {
        val shopUser = ShopUser(
            id = user.id,
            name = user.name,
            phone = crypt.encrypt(user.phone),
            email = crypt.encrypt(user.email),
            pass = crypt.encrypt(user.pass),
        )
        shopUserDao.insert(shopUser)
    }

    override fun getUserFlow(): Flow<User?> {
        return shopUserDao
            .getAllFlow()
            .map {
                User.from(it.firstOrNull(), crypt)
            }
    }

    override suspend fun removeUser() {
        shopUserDao.nukeAll()
        shopOrderDao.nukeAll()
        shopOrderItemDao.nukeAll()
    }

}
