package com.example.onlishop.repository

import com.example.onlishop.models.*

interface OrderRepository {

    suspend fun getOrders(): List<Order>

    suspend fun getLastOrder(): Order?

    suspend fun addOrder(order: Order): Boolean

    suspend fun addUser(user: User): Boolean

    suspend fun getUser(): User?
}