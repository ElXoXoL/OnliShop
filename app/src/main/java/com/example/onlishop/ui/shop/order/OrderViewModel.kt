package com.example.onlishop.ui.shop.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.*
import com.example.onlishop.repository.ItemRepository
import com.example.onlishop.repository.OrderRepository
import com.example.onlishop.utils.IdGenerator
import java.text.SimpleDateFormat
import java.util.*

class OrderViewModel(
    private val repository: ItemRepository,
    private val orderRepository: OrderRepository,
    private val logger: Logger
): BaseViewModel() {

    val fullPrice = MutableLiveData<Int>()
    val lastOrder = MutableLiveData<Order>()

    init {
        loadFullPrice()
        loadLastOrder()
    }

    private fun loadLastOrder(){
        logger.logExecution("loadLastOrder")
        viewModelScope.launchIo {
            val order = orderRepository.getLastOrder() ?: return@launchIo
            lastOrder.postValue(order)
        }
    }

    private fun loadFullPrice(){
        logger.logExecution("loadFullPrice")
        viewModelScope.launchIo {
            var price = 0
            repository.getBagItems().forEach {
                price += it.count * it.item.price
            }
            fullPrice.postValue(price)
        }
    }

    fun saveOrder(orderCheck: OrderCheck){
        logger.logExecution("cleanBag")
        viewModelScope.launchIo {
            val bagItems = repository.getBagItems()
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

            cleanBag()
        }
    }

    fun cleanBag(){
        logger.logExecution("cleanBag")
        viewModelScope.launchIo {
            repository.cleanBag()
        }
    }

}