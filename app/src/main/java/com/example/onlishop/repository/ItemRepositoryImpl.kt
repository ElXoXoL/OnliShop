package com.example.onlishop.repository

import com.example.onlishop.database.daos.ShopBagItemDao
import com.example.onlishop.database.daos.ShopGroupDao
import com.example.onlishop.database.daos.ShopItemDao
import com.example.onlishop.database.models.ShopBagItem
import com.example.onlishop.database.models.ShopGroup
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.global.Logger
import com.example.onlishop.models.BagItem
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.network.groups.GroupsService
import com.example.onlishop.network.items.ItemsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ItemRepositoryImpl(
    private val logger: Logger,
    private val itemsService: ItemsService,
    private val groupsService: GroupsService,
    private val itemDao: ShopItemDao,
    private val groupDao: ShopGroupDao,
    private val bagItemDao: ShopBagItemDao,
): ItemRepository {

    // TODO: extract bag stuff to bag repo

    override suspend fun getGroups(): List<Group> {
        val groupsResponse = getGroupsOrLoad()

        return groupsResponse.map { Group.from(it) }
    }

    override suspend fun getGroupChildrenAndParent(groupId: Int): List<Group> {
        val groupsResponse = getGroupsOrLoad()

        return getGroupSiblingAndParent(groupId, groupsResponse).map { Group.from(it) }
    }

    override suspend fun getItems(): List<Item> {
        val itemsResponse = getItemsOrLoad()

        return itemsResponse.map { Item.from(it) }
    }

    override suspend fun getItemsAndSubitemsForGroup(groupId: Int): List<Item> {
        val items = getItemsOrLoad() // Load items from 'internet'
        val groups = getGroupsOrLoad()
        val parents = getGroupSiblingIds(groupId, groups)

        val resultList = mutableListOf<Item>()
        parents.forEach { groupId ->
            resultList.addAll(
                itemDao.loadForGroup(groupId).map { Item.from(it) }
            )
        }

        return resultList.sortedBy { it.id }
    }

    override suspend fun getItem(itemId: Int): Item {
        val item = itemDao.loadSingle(itemId)
            ?: getItemsOrLoad().find { it.id == itemId }
            ?: throw Exception("Can't find item")

        return Item.from(item)
    }

    override suspend fun getSearchItems(search: String): List<Item> {
        val searchLower = search.lowercase()
        val allItems = getItemsOrLoad()
        val allGroups = getGroupsOrLoad()
        val resultList = mutableListOf<ShopItem>()

        allItems.forEach { item ->
            when {
                item.name.lowercase().contains(searchLower) -> resultList.add(item)
                item.description.lowercase().contains(searchLower) -> resultList.add(item)
                item.sizes.lowercase().contains(searchLower) -> resultList.add(item)
            }
            val groupsBySearch = allGroups.filter { it.name.lowercase().contains(searchLower) }
            val isGroupFounded = groupsBySearch.firstOrNull { it.id == item.groupId } != null
            if (isGroupFounded) {
                resultList.add(item)
            }
        }

        val setOfItems = resultList.toSet()

        return setOfItems.toList().map { Item.from(it) }
    }

    override suspend fun addBagItem(item: Item, size: String) {
        val itemFromDao = bagItemDao.loadSingle(item.id, size)
        if (itemFromDao != null) {
            val itemAdd = itemFromDao.copy(count = itemFromDao.count + 1)
            bagItemDao.insert(itemAdd)
        } else {
            bagItemDao.insert(ShopBagItem(item.id, size))
        }
    }

    override suspend fun removeBagItem(bagItemId: Int) {
        val itemFromDao = bagItemDao.loadSingle(bagItemId)
        if (itemFromDao != null) {
            val itemAdd = itemFromDao.copy(count = itemFromDao.count - 1)
            if (itemAdd.count <= 0) {
                bagItemDao.delete(bagItemId)
            } else {
                bagItemDao.insert(itemAdd)
            }
        } else {
            bagItemDao.delete(bagItemId)
        }
    }

    override fun getBagItemsFlow(): Flow<List<BagItem>> {
        return bagItemDao.getAllFlow().map { bagItems ->
            bagItems.map { bagItem ->
                val item = getItem(bagItem.id)
                BagItem(
                    bagItemId = bagItem.bagItemId,
                    item = item,
                    size = bagItem.size,
                    count = bagItem.count
                )
            }
        }
    }

    override suspend fun getBagItems(): List<BagItem> {
        return bagItemDao.getAll().map { bagItem ->
            val item = getItem(bagItem.id)
            BagItem(
                bagItemId = bagItem.bagItemId,
                item = item,
                size = bagItem.size,
                count = bagItem.count
            )
        }
    }

    override suspend fun cleanBag() {
        bagItemDao.nukeAll()
    }

    override fun getBagSizeFlow(): Flow<Int> {
        return bagItemDao.getAllFlow().map { it.sumOf { it.count } }
    }

    private fun getGroupSiblingIds(groupId: Int, groups: List<ShopGroup>): List<Int> {
        val parents = mutableListOf<Int>()
        parents.add(groupId)

        var counter = 0
        while (counter < parents.size) {
            val parentId = parents[counter++]
            groups.forEach {
                if (it.parentGroupId == parentId) {
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

    private fun getGroupsOrLoad(): List<ShopGroup> {
        val isDbEmpty = groupDao.getItemsCount() <= 0

        return if (isDbEmpty) {
            val items = groupsService.getGroups()
            groupDao.insertAll(items)
            items
        } else {
            groupDao.getAll()
        }
    }

    private fun getItemsOrLoad(): List<ShopItem> {
        val isDbEmpty = itemDao.getItemsCount() <= 0

        return if (isDbEmpty) {
            val items = itemsService.getItems()
            itemDao.insertAll(items)
            items
        } else {
            itemDao.getAll()
        }
    }
}
