package com.example.onlishop.repository

import com.example.onlishop.database.RoomDatabase
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.network.groups.GroupsService
import com.example.onlishop.network.items.ItemsService
import kotlinx.coroutines.*

class ItemRepositoryImpl(
    private val logger: Logger,
    private val itemsService: ItemsService,
    private val groupsService: GroupsService,
    private val room: RoomDatabase,
    private val externalScope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ): ItemRepository {

    private val ignoreHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        logger.exception(throwable, this::class)
    }

    override suspend fun getGroups(): List<Group> {
        return externalScope.async(dispatcher + ignoreHandler) {
            val isDbEmpty = room.shopGroups.count() <= 0

            val groupsResponse = if (isDbEmpty){
                val items = groupsService.getGroups()
                room.shopGroups.setItems(items)
                items
            } else {
                room.shopGroups.getItems()
            }

            val resultList = groupsResponse.map {
                Group(
                    it.id, it.parentGroupId, it.name
                )
            }

            resultList.sortedBy { it.id }
        }.await()
    }

    override suspend fun getItems(): List<Item> {
        return externalScope.async(dispatcher + ignoreHandler) {
            val isDbEmpty = room.shopItems.count() <= 0

            val itemsResponse = if (isDbEmpty){
                val items = itemsService.getItems()
                room.shopItems.setItems(items)
                items
            } else {
                room.shopItems.getItems()
            }

            val resultList = itemsResponse.map {
                Item(
                    id = it.id,
                    groupId = it.groupId,
                    name = it.name,
                    description = it.description,
                    imageDrawable = it.imageDrawable,
                    price = it.price,
                    sizes = it.sizes.split(",").dropLastWhile { it.isEmpty() }
                )
            }

            resultList.sortedBy { it.id }
        }.await()
    }

}