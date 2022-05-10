package com.example.onlishop.repository

import com.example.onlishop.models.*
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    fun getOrdersFlow(): Flow<List<Order>>

    suspend fun getLastOrder(): Order?

    suspend fun addOrder(order: Order)

    suspend fun addUser(user: User)

    fun getUserFlow(): Flow<User?>

    suspend fun removeUser()
}