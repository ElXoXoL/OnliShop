package com.example.onlishop.repository

import com.example.onlishop.database.RoomDatabase
import com.example.onlishop.database.models.*
import com.example.onlishop.global.Logger
import com.example.onlishop.models.*
import com.example.onlishop.utils.Crypt
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.*

class OrderRepositoryImpl(
    private val logger: Logger,
    private val room: RoomDatabase,
    private val crypt: Crypt,
    private val externalScope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : OrderRepository {

    private val ignoreHandler = CoroutineExceptionHandler { _, throwable ->
        logger.exception(throwable, this::class)
    }

    override fun getOrders(): Single<List<Order>> {
        logger.logDevWithThread("getOrdersRx initial")
        return room.shopOrder.getItemsRx().map { orders ->
            logger.logDevWithThread("getOrdersRx map")
            val ordersFormatted = orders.map { order ->
                val orderItems = room.shopOrderItems.getForOrder(order.id).map { orderItem ->
                    val item = Item.from(room.shopItems.getItem(orderItem.id)!!)
                    OrderItem(orderItem.orderItemId, item, orderItem.size, orderItem.count)
                }
                Order.from(order, orderItems, crypt)
            }
            logger.logDevWithThread("getOrdersRx map2")
            ordersFormatted
        }
    }

    override fun getLastOrder(): Single<Order> {
        logger.logDevWithThread("getLastOrderRx initial")
        return getOrders().map {
            logger.logDevWithThread("getLastOrderRx map")
            it.last()
        }
    }

    override fun addOrder(order: Order): Completable {
        logger.logDevWithThread("addOrderRx initial")
        return Single.just(order).flatMapCompletable { order ->
            logger.logDevWithThread("addOrderRx flatMapCompletable")
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


            logger.logDevWithThread("addOrderRx Completable.mergeArray")
            Completable.mergeArray(
                room.shopOrder.insertRx(shopOrder),
                room.shopOrderItems.addItemsRx(orderItems)
            )
        }
    }

    override fun addUser(user: User): Completable {
        return Single.just(user).flatMapCompletable {
            val shopUser = ShopUser(
                id = user.id,
                name = user.name,
                phone = crypt.encrypt(user.phone),
                email = crypt.encrypt(user.email),
                pass = crypt.encrypt(user.pass),
            )

            room.shopUsers.insert(shopUser)
        }
    }

    override fun getUser(): Maybe<User> {
        return room.shopUsers.getSingleRx().map {
            User.from(it, crypt)
        }
    }

    override fun removeUser(): Completable {
        return Completable.mergeArray(
            room.shopUsers.nukeRx(),
            room.shopOrder.nukeRx(),
            room.shopOrderItems.nukeRx(),
        )
    }

}