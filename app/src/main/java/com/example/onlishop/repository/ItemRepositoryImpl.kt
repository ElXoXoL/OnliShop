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
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
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

    override fun getGroups(): Single<List<Group>> {
        logger.logDevWithThread("getGroupsRx initial")
        return loadGroupsRx().map {
            logger.logDevWithThread("getGroupsRx map")
            it.map { Group.from(it) }.sortedBy { it.id }
        }
    }

    override fun getGroups(groupId: Int): Single<List<Group>> {
        logger.logDevWithThread("getGroupsRx-groupId initial")
        return loadGroupsRx().map { groups ->
            logger.logDevWithThread("getGroupsRx-groupId map")
            getGroupSiblingAndParent(groupId, groups)
                .map { Group.from(it) }
                .sortedBy { it.id }
        }
    }

    override fun getItemsForGroup(groupId: Int): Single<List<Item>> {
        logger.logDevWithThread("getItemsForGroupRx initial")

        // Loads items first to save them to DB
        return loadItemsRx().flatMap {
            logger.logDevWithThread("getItemsForGroupRx flatMap 1")
            loadGroupsRx()
        }.map { groups ->
            logger.logDevWithThread("getItemsForGroupRx map 2")
            val parents = getGroupSiblingIds(groupId, groups)

            val resultList = mutableListOf<ShopItem>()
            parents.forEach { groupId ->
//                logger.logDevWithThread("parents.forEach $groupId ")
                val items = room.shopItems.getItems(groupId)
//                logger.logDevWithThread("parents.forEach itemsCount ${items.size}")

                resultList.addAll(items)
            }

            logger.logDevWithThread("getItemsForGroupRx map 2 result")

            resultList.map {
//                    logger.logDevWithThread("Thread map - ")
                Item.from(it)
            }.sortedBy { it.id }
        }
    }

    override fun getItem(itemId: Int): Single<Item> {
        logger.logDevWithThread("getItemRx initial")
        return room.shopItems.getItemRx(itemId)
            .switchIfEmpty(
                itemsService.getItemsRx().map {
                    it.first { it.id == itemId }
                }
            ).map {
                Item.from(it)
            }
    }

    override fun getSearchItems(search: String): Single<List<Item>> {
        logger.logDevWithThread("getSearchItemsRx initial")
        val searchLower = search.lowercase()
        return room.shopItems.getItemsRx().map { items ->
            logger.logDevWithThread("getSearchItemsRx map")

            val allGroups = room.shopGroups.getItems()
            val groupsBySearch = allGroups.filter { it.name.lowercase().contains(searchLower) }
            val resultList = mutableListOf<ShopItem>()


            items.forEach { item ->
                when {
                    item.name.lowercase().contains(searchLower) -> resultList.add(item)
                    item.description.lowercase().contains(searchLower) -> resultList.add(item)
                    item.sizes.lowercase().contains(searchLower) -> resultList.add(item)
                    else -> {
                        val isGroupFounded =
                            groupsBySearch.firstOrNull { it.id == item.groupId } != null
                        if (isGroupFounded) {
                            resultList.add(item)
                        }
                    }
                }
            }

            resultList.map { Item.from(it) }.sortedBy { it.id }
        }
    }

    override fun addBagItem(item: Item, size: String): Completable {
        logger.logDevWithThread("addBagItemRx initial")
        return room.shopBag.addItemRx(ShopBagItem(item.id, size))
    }

    override fun removeBagItem(bagItemId: Int): Completable {
        logger.logDevWithThread("removeBagItemRx initial")
        return room.shopBag.removeItemRx(bagItemId)
    }

    override fun getBagItems(): Flowable<List<BagItem>> {
        logger.logDevWithThread("getBagItemsRx initial")
        return room.shopBag.getItemsRx().map { bagItems ->
            logger.logDevWithThread("getBagItemsRx map")

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

    override fun cleanBag(): Completable {
        logger.logDevWithThread("cleanBag initial")
        return room.shopBag.nuke()
    }

    override fun getBagSize(): Flowable<Int> {
        logger.logDevWithThread("getBagSizeRx initial")
        return room.shopBag.getItemsRx().map {
            it.sumOf { it.count }
        }
    }

    private fun getGroupSiblingIds(groupId: Int, groups: List<ShopGroup>): List<Int> {
        logger.logDevWithThread("getGroupSiblingIds initial")
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
        logger.logDevWithThread("getGroupSiblingAndParent initial")
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

    private fun loadGroupsRx(): Single<List<ShopGroup>> {
        logger.logDevWithThread("loadGroupsRx initial")
        return room.shopGroups.countRx()
            .subscribeOn(Schedulers.io())
            .flatMap {
                logger.logDevWithThread("loadGroupsRx flatMap")
                val isDbEmpty = it <= 0

                if (isDbEmpty) {
                    groupsService.getGroupsRx()
                        .map { groups ->
                            logger.logDevWithThread("groupsService.getGroupsRx() flatMap 1")
                            room.shopGroups.setItemsRx(groups)
                            logger.logDevWithThread("groupsService.getGroupsRx() flatMap 2")
                            groups
                        }
                } else {
                    logger.logDevWithThread("room.shopGroups.getItemsRx()")
                    room.shopGroups.getItemsRx()
                }.subscribeOn(Schedulers.computation())
            }
    }

    private fun loadItemsRx(): Single<List<ShopItem>> {
        logger.logDevWithThread("loadItemsRx initial")
        return room.shopItems.countRx()
            .subscribeOn(Schedulers.io())
            .flatMap {
                logger.logDevWithThread("loadItemsRx flatMap")
                val isDbEmpty = it <= 0

                if (isDbEmpty) {
                    itemsService.getItemsRx()
                        .flatMap { items ->
                            logger.logDevWithThread("itemsService.getItemsRx() flatMap 1")
                            room.shopItems.setItemsRx(items)
                            logger.logDevWithThread("itemsService.getItemsRx() flatMap 2")
                            room.shopItems.getItemsRx()
                        }
                } else {
                    logger.logDevWithThread("room.shopItems.getItemsRx()")
                    room.shopItems.getItemsRx()
                }.subscribeOn(Schedulers.computation())
            }
    }

}