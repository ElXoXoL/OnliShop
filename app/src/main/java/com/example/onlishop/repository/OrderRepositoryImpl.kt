package com.example.onlishop.repository

import com.example.onlishop.database.RoomDatabase
import com.example.onlishop.database.models.*
import com.example.onlishop.global.Logger
import com.example.onlishop.models.*
import com.example.onlishop.network.groups.GroupsService
import com.example.onlishop.network.items.ItemsService
import kotlinx.coroutines.*
import java.util.*

class OrderRepositoryImpl(
    private val logger: Logger,
    private val room: RoomDatabase,
    private val externalScope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : OrderRepository {

    private val ignoreHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        logger.exception(throwable, this::class)
    }

    override suspend fun getOrders(): List<Order> {
        return externalScope.async(dispatcher + ignoreHandler) {
            val orders = room.shopOrder.getItems()
            val ordersFormatted = orders.map { order ->
                val orderItems = room.shopOrderItems.getForOrder(order.id).map { orderItem ->
                    val item = Item.from(room.shopItems.getItem(orderItem.id)!!)
                    OrderItem(orderItem.orderItemId, item, orderItem.size, orderItem.count)
                }
                Order.from(order, orderItems)
            }
            ordersFormatted
        }.await()
    }

    override suspend fun getLastOrder(): Order? = getOrders().lastOrNull()

    override suspend fun addOrder(order: Order): Boolean {
        return externalScope.async(dispatcher + ignoreHandler) {
            val shopOrder = ShopOrder(
                id = order.id,
                userId = order.userId,
                name = order.name,
                phone = order.phone,
                email = order.email,
                orderType = order.orderType,
                delivery = order.delivery,
                cardNum = order.cardNum,
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
            room.shopOrder.insert(shopOrder)
            room.shopOrderItems.addItems(orderItems)
            true
        }.await()
    }

    override suspend fun addUser(user: User): Boolean {
        return externalScope.async(dispatcher + ignoreHandler) {
            val shopUser = ShopUser(
                id = user.id,
                name = user.name,
                phone = user.phone,
                email = user.email,
                pass = user.pass,
            )
            room.shopUsers.insert(shopUser)
            true
        }.await()
    }

    override suspend fun getUser(): User? {
        return externalScope.async(dispatcher + ignoreHandler) {
            User.from(room.shopUsers.getSingle())
        }.await()
    }

}