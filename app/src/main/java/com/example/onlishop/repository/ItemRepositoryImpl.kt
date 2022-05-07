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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ItemRepositoryImpl(
    private val logger: Logger,
    private val itemsService: ItemsService,
    private val groupsService: GroupsService,
    private val room: RoomDatabase,
    ): ItemRepository {

    override suspend fun getGroups(): List<Group> {
        val groupsResponse = loadGroups()

        val resultList = groupsResponse.map { Group.from(it) }

        return resultList.sortedBy { it.id }
    }

    override suspend fun getGroups(groupId: Int): List<Group> {
        val groupsResponse = loadGroups()

        val resultList = getGroupSiblingAndParent(groupId, groupsResponse).map { Group.from(it) }

        return resultList.sortedBy { it.id }
    }

    override suspend fun getItems(): List<Item> {
        val itemsResponse = loadItems()

        val resultList = itemsResponse.map { Item.from(it) }

        return resultList.sortedBy { it.id }
    }

    override suspend fun getItemsForGroup(groupId: Int): List<Item> {
        val items = loadItems()
        val groups = loadGroups()
        val parents = getGroupSiblingIds(groupId, groups)

        val resultList = mutableListOf<Item>()
        parents.forEach { groupId ->
            resultList.addAll(
                room.shopItems.getItems(groupId).map { Item.from(it) }
            )
        }

        return resultList.sortedBy { it.id }
    }

    override suspend fun getItem(itemId: Int): Item {
        val item = room.shopItems.getItem(itemId)
            ?: itemsService.getItems().find { it.id == itemId }
            ?: throw Exception("Can't find item")

        return Item.from(item)
    }

    override suspend fun getSearchItems(search: String): List<Item> {
        val searchLower = search.lowercase()
        val allItems = room.shopItems.getItems()
        val allGroups = room.shopGroups.getItems()
        val resultList = mutableListOf<ShopItem>()

        allItems.forEach { item ->
            when {
                item.name.lowercase().contains(searchLower) -> resultList.add(item)
                item.description.lowercase().contains(searchLower) -> resultList.add(item)
                item.sizes.lowercase().contains(searchLower) -> resultList.add(item)
            }
            val groupsBySearch = allGroups.filter { it.name.lowercase().contains(searchLower) }
            val isGroupFounded = groupsBySearch.firstOrNull { it.id == item.groupId } != null
            if (isGroupFounded){
                resultList.add(item)
            }
        }

        val setOfItems = resultList.toSet()

        return setOfItems.toList().map { Item.from(it) }.sortedBy { it.id }
    }

    override suspend fun addBagItem(item: Item, size: String) {
        room.shopBag.addItem(
            ShopBagItem(item.id, size)
        )
    }

    override suspend fun removeBagItem(bagItemId: Int) {
        room.shopBag.removeItem(bagItemId)
    }

    override fun getBagItemsFlow(): Flow<List<BagItem>> {
        return room.shopBag.getItemsFlow().map { bagItems ->
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
        }
    }

    override suspend fun getBagItems(): List<BagItem> {
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

        return resultList
    }

    override suspend fun cleanBag() {
        room.shopBag.nuke()
    }

    override fun getBagSizeFlow(): Flow<Int> {
        return room.shopBag.getItemsFlow().map { it.sumOf { it.count } }
    }

    private fun getGroupSiblingIds(groupId: Int, groups: List<ShopGroup>): List<Int>{
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

    private fun getGroupSiblingAndParent(groupId: Int, groups: List<ShopGroup>): List<ShopGroup> {
        val list = mutableListOf<ShopGroup>()
        val initialGroup = groups.find { it.id == groupId } ?: return emptyList()
        list.add(initialGroup)

        groups.forEach {
            if (it.parentGroupId == initialGroup.id) {
                list.add(it)
            }
        }

        groups.find { it.id == initialGroup.parentGroupId }?.let {
            list.add(0, it)
        }
        return list
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