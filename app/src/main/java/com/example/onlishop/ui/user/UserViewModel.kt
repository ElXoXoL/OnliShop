package com.example.onlishop.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Order
import com.example.onlishop.models.User
import com.example.onlishop.repository.OrderRepository
import io.reactivex.rxjava3.schedulers.Schedulers

class UserViewModel(private val repository: OrderRepository, private val logger: Logger): BaseViewModel() {

    private val _items = MutableLiveData<List<Order>>()
    val items: LiveData<List<Order>> = _items

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    init {
        loadOrders()
        loadUser()
    }

    private fun loadUser() {
        logger.logExecution("loadUser")

        repository.getUser()
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribe({
                logger.logDevWithThread("UserViewModel loadUser onSuccess")
                _user.postValue(it)
            }, {
                logger.logDevWithThread("UserViewModel loadUser error $it")
            }).toCache()

    }

    fun removeUser() {
        logger.logExecution("removeUser")

        repository.removeUser()
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribe({
                logger.logDevWithThread("UserViewModel removeUser onSuccess")
                _user.postValue(null)
                _items.postValue(emptyList())
            }, {
                logger.logDevWithThread("UserViewModel removeUser error $it")
            }).toCache()

    }

    private fun loadOrders() {
        logger.logExecution("loadOrders")
        repository.getOrders()
            .map {
                logger.logDevWithThread("UserViewModel loadOrders map")
                it.reversed()
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribe({ orders ->
                logger.logDevWithThread("UserViewModel loadOrders onSuccess")
                _items.postValue(orders)
            }, {
                logger.logDevWithThread("UserViewModel loadOrders error $it")
            }).toCache()
    }

    fun saveUser(user: User) {
        logger.logExecution("saveUser")
        repository.addUser(user)
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribe({
                logger.logDevWithThread("UserViewModel saveUser onSuccess")
                loadUser()
            }, {
                logger.logDevWithThread("UserViewModel saveUser error $it")
            }).toCache()

    }

}