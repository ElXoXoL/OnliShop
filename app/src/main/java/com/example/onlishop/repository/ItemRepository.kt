package com.example.onlishop.repository

import com.example.onlishop.models.BagItem
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ItemRepository {

    fun getGroups(): Single<List<Group>>

    fun getGroups(groupId: Int): Single<List<Group>>

    fun getItemsForGroup(groupId: Int): Single<List<Item>>

    fun getItem(itemId: Int): Single<Item>

    fun getSearchItems(search: String): Single<List<Item>>

    fun addBagItem(item: Item, size: String): Completable

    fun removeBagItem(bagItemId: Int): Completable

    fun getBagSize(): Flowable<Int>

    fun getBagItems(): Flowable<List<BagItem>>

    fun cleanBag(): Completable

}