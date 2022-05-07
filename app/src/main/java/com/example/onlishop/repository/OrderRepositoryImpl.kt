package com.example.onlishop.repository

import com.example.onlishop.database.RoomDatabase
import com.example.onlishop.database.models.*
import com.example.onlishop.global.Logger
import com.example.onlishop.models.*
import com.example.onlishop.utils.Crypt
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class OrderRepositoryImpl(
    private val logger: Logger,
    private val room: RoomDatabase,
    private val crypt: Crypt,
) : OrderRepository {

    /**
     * Get orders flow, then get items for order one by one, map everything to Order.kt
     * @return list of orders combined with their items
     */
    override fun getOrdersFlow(): Flow<List<Order>> {
        return room.shopOrder.getItemsFlow().map { orderList ->
            orderList.map { order ->
                val orderItems = room.shopOrderItems.getForOrder(order.id).map { orderItem ->
                    val item = Item.from(room.shopItems.getItem(orderItem.id)!!)
                    OrderItem(orderItem.orderItemId, item, orderItem.size, orderItem.count)
                }
                Order.from(order, orderItems, crypt)
            }
        }
    }

    override suspend fun getOrders(): List<Order> {
        val orders = room.shopOrder.getItems()
        return orders.map { order ->
            val orderItems = room.shopOrderItems.getForOrder(order.id).map { orderItem ->
                val item = Item.from(room.shopItems.getItem(orderItem.id)!!)
                OrderItem(orderItem.orderItemId, item, orderItem.size, orderItem.count)
            }
            Order.from(order, orderItems, crypt)
        }
    }

    override suspend fun getLastOrder(): Order? = getOrders().lastOrNull()

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
        room.shopOrder.insert(shopOrder)
        room.shopOrderItems.addItems(orderItems)
    }

    override suspend fun addUser(user: User) {
        val shopUser = ShopUser(
            id = user.id,
            name = user.name,
            phone = crypt.encrypt(user.phone),
            email = crypt.encrypt(user.email),
            pass = crypt.encrypt(user.pass),
        )
        room.shopUsers.insert(shopUser)
    }

    override suspend fun getUser(): User? {
        return User.from(room.shopUsers.getSingle(), crypt)
    }

    override fun getUserFlow(): Flow<User?> {
        return room.shopUsers
            .getUsersFlow()
            .map {
                User.from(it.firstOrNull(), crypt)
            }
    }

    override suspend fun removeUser() {
        room.shopUsers.nuke()
        room.shopOrder.nuke()
        room.shopOrderItems.nuke()
    }

}
