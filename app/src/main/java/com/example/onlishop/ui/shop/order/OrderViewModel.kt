package com.example.onlishop.ui.shop.order

import androidx.lifecycle.MutableLiveData
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.*
import com.example.onlishop.repository.ItemRepository
import com.example.onlishop.repository.OrderRepository
import com.example.onlishop.utils.IdGenerator
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class OrderViewModel(
    private val repository: ItemRepository,
    private val orderRepository: OrderRepository,
    private val logger: Logger
): BaseViewModel() {

    val fullPrice = MutableLiveData<Int>()
    var itemsInOrderCount = 0
    val lastOrder = MutableLiveData<Order>()

    init {
        loadFullPrice()
        loadLastOrder()
    }

    private fun loadLastOrder() {
        logger.logExecution("loadLastOrder")

        orderRepository.getLastOrder()
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribe(
                {
                    logger.logDevWithThread("loadLastOrder onSuccess")
                    lastOrder.postValue(it)
                }, this::baseHandler
            )
            .toCache()
    }

    private fun loadFullPrice() {
        logger.logExecution("loadFullPrice")

        repository.getBagItems()
            .firstOrError()
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribe(
                {
                    logger.logDevWithThread("OrderViewModel loadFullPrice onSuccess")
                    itemsInOrderCount = it.sumOf { it.count }
                    this.fullPrice.postValue(
                        it.sumOf { it.count * it.item.price }
                    )
                }, {
                    logger.logDevWithThread("OrderViewModel loadFullPrice error $it")
                    itemsInOrderCount = 0
                    this.fullPrice.postValue(0)
                }).toCache()

    }

    fun saveOrder(orderCheck: OrderCheck) {
        logger.logExecution("saveOrder")

        repository.getBagItems()
            .firstOrError()
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .flatMapCompletable { bagItems ->

                val orderItems = bagItems.map {
                    OrderItem(it.bagItemId, it.item, it.size, it.count)
                }
                val order = Order(
                    id = IdGenerator.randomId,
                    name = orderCheck.name,
                    phone = orderCheck.phone,
                    email = orderCheck.email,
                    orderType = orderCheck.orderType,
                    delivery = orderCheck.delivery,
                    cardNum = orderCheck.cardNum,
                    cardDate = orderCheck.cardDate,
                    date = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date()),
                    orderItems = orderItems,
                )

                orderRepository.addOrder(order)
            }
            .subscribe(
                {
                    logger.logDevWithThread("OrderViewModel saveOrder onSuccess")
                    cleanBag()
                }, this::baseHandler
            ).toCache()

    }

    fun cleanBag() {
        logger.logExecution("cleanBag")

        repository.cleanBag()
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribe({}, this::baseHandler)
            .toCache()
    }

}