package com.example.onlishop.repository

import com.example.onlishop.database.RoomDatabase
import com.example.onlishop.database.models.ShopBagItem
import com.example.onlishop.database.models.ShopGroup
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.global.Logger
import com.example.onlishop.models.BagItem
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

            val groupsResponse = loadGroups()

            val resultList = groupsResponse.map { Group.from(it) }

            resultList.sortedBy { it.id }
        }.await()
    }

    override suspend fun getItems(): List<Item> {
        return externalScope.async(dispatcher + ignoreHandler) {
            val itemsResponse = loadItems()

            val resultList = itemsResponse.map { Item.from(it) }

            resultList.sortedBy { it.id }
        }.await()
    }

    override suspend fun getItemsForGroup(groupId: Int): List<Item> {
        return externalScope.async(dispatcher + ignoreHandler) {
            val items = loadItems()
            val groups = loadGroups()
            val parents = getGroupParentsId(groupId, groups)

            val resultList = mutableListOf<Item>()
            parents.forEach { groupId ->
                resultList.addAll(
                    room.shopItems.getItems(groupId).map { Item.from(it) }
                )
            }

            resultList.sortedBy { it.id }
        }.await()
    }

    override suspend fun getItem(itemId: Int): Item {
        return externalScope.async(dispatcher + ignoreHandler) {
            val item = room.shopItems.getItem(itemId)
                ?: itemsService.getItems().find { it.id == itemId }
                ?: throw Exception("Can't find item")

            Item.from(item)
        }.await()
    }

    override suspend fun getSearchItems(search: String): List<Item> {
        return externalScope.async(dispatcher + ignoreHandler) {

            val resultList = room.shopItems.getItems(search).map { Item.from(it) }

            resultList.sortedBy { it.id }
        }.await()
    }

    override suspend fun addBagItem(item: Item, size: String): Boolean {
        return externalScope.async(dispatcher + ignoreHandler) {
            room.shopBag.addItem(
                ShopBagItem(item.id, size)
            )
            true
        }.await()
    }

    override suspend fun removeBagItem(bagItemId: Int) {
        externalScope.launch(dispatcher + ignoreHandler) {
            room.shopBag.removeItem(bagItemId)
        }
    }

    override suspend fun getBagItems(): List<BagItem> {
        return externalScope.async(dispatcher + ignoreHandler) {
            val bagItems = room.shopBag.getItems()
            val resultList = mutableListOf<BagItem>()

            bagItems.forEach { bagItem ->
                room.shopItems.getItem(bagItem.id)?.let { shopItem ->
                    val item = Item.from(shopItem)
                    resultList.add(
                        BagItem(
                            bagItem.bagItemId,
                            item,
                            bagItem.size,
                            bagItem.count
                        )
                    )
                }
            }

            resultList
        }.await()
    }

    override suspend fun cleanBag() {
        externalScope.launch(dispatcher + ignoreHandler) {
            room.shopBag.nuke()
        }
    }

    override suspend fun getBagSize(): Int {
        return externalScope.async(dispatcher + ignoreHandler) {
            var size = 0
            room.shopBag.getItems().forEach {
                size += it.count
            }
            size
        }.await()
    }

    private fun getGroupParentsId(groupId: Int, groups: List<ShopGroup>): List<Int>{
        val parents = mutableListOf<Int>()
        parents.add(groupId)

        var counter = 0
        while (counter < parents.size){
            val parentId = parents[counter++]
            groups.forEach {
                if (it.parentGroupId == parentId){
                    parents.add(it.id)
                }
            }
        }
        return parents
    }

    private suspend fun loadGroups(): List<ShopGroup>{
        val isDbEmpty = room.shopGroups.count() <= 0

        return  if (isDbEmpty){
            val items = groupsService.getGroups()
            room.shopGroups.setItems(items)
            items
        } else {
            room.shopGroups.getItems()
        }
    }

    private suspend fun loadItems(): List<ShopItem> {
        val isDbEmpty = room.shopItems.count() <= 0

        return if (isDbEmpty) {
            val items = itemsService.getItems()
            room.shopItems.setItems(items)
            items
        } else {
            room.shopItems.getItems()
        }
    }

}