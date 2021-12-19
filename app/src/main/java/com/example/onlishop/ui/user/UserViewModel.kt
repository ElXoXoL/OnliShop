package com.example.onlishop.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.models.Order
import com.example.onlishop.models.User
import com.example.onlishop.repository.OrderRepository

class UserViewModel(private val repository: OrderRepository, private val logger: Logger): BaseViewModel() {

    private val _items = MutableLiveData<List<Order>>()
    val items: LiveData<List<Order>> = _items

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    init {
        loadOrders()
        loadUser()
    }

    fun loadUser() {
        logger.logExecution("loadUser")
        viewModelScope.launchIo {
            val user = repository.getUser() ?: return@launchIo
            _user.postValue(user)
        }
    }

    fun removeUser() {
        logger.logExecution("loadUser")
        viewModelScope.launchIo {
            repository.removeUser()
            _user.postValue(null)
            _items.postValue(emptyList())
        }
    }

    fun loadOrders() {
        viewModelScope.launchIo {
            val orders = repository.getOrders().reversed()
            _items.postValue(orders)
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launchIo {
            val orders = repository.addUser(user)
            loadUser()
        }
    }

}