package com.example.onlishop.repository

import com.example.onlishop.models.BagItem
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    suspend fun getGroups(): List<Group>

    suspend fun getGroups(groupId: Int): List<Group>

    suspend fun getItems(): List<Item>

    suspend fun getItemsForGroup(groupId: Int): List<Item>

    suspend fun getItem(itemId: Int): Item

    suspend fun getSearchItems(search: String): List<Item>

    suspend fun addBagItem(item: Item, size: String)

    suspend fun removeBagItem(bagItemId: Int)

    fun getBagSizeFlow(): Flow<Int>

    suspend fun getBagItems(): List<BagItem>

    fun getBagItemsFlow(): Flow<List<BagItem>>

    suspend fun cleanBag()

}