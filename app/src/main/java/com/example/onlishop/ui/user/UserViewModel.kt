package com.example.onlishop.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Order
import com.example.onlishop.models.User
import com.example.onlishop.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class UserViewModel(private val repository: OrderRepository, private val logger: Logger): BaseViewModel() {

    val items: LiveData<List<Order>> = repository.getOrdersFlow()
        .map { it.reversed() }
        .flowOn(Dispatchers.IO)
        .asLiveData(mainDispatcherHandled)

    val user: LiveData<User?> get() = repository.getUserFlow().flowOn(Dispatchers.IO).asLiveData(mainDispatcherHandled)

    fun removeUser() {
        logger.logExecution("loadUser")
        viewModelScope.launchIo {
            repository.removeUser()
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launchIo {
            repository.addUser(user)
        }
    }

}