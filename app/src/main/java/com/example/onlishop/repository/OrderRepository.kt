package com.example.onlishop.repository

import com.example.onlishop.models.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface OrderRepository {

    fun getOrders(): Single<List<Order>>

    fun getLastOrder(): Single<Order>

    fun addOrder(order: Order): Completable

    fun addUser(user: User): Completable

    fun getUser(): Maybe<User>

    fun removeUser(): Completable
}