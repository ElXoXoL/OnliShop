package com.example.onlishop.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.repository.ItemRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class ShopViewModel(private val repository: ItemRepository, private val logger: Logger): BaseViewModel() {

    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>> = _groups

    private val subject = PublishSubject.create<Group>()

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    init {
        subscribeItemsForGroup()
        subscribeGroupsForGroup()

        loadData()
        loadBagCount()
    }

    val bagCount = MutableLiveData<Int>()

    private fun loadBagCount() {
        logger.logExecution("ShopViewModel loadBagCount")

        repository.getBagSize()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    logger.logDevWithThread("ShopViewModel loadBagCount onSuccess")
                    bagCount.postValue(it)
                }, this::baseHandler
            ).toCache()

    }

    private fun getGroupsObservable(): Observable<Group> {
        return subject
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .distinctUntilChanged { first, second ->
                first.id == second.id
            }
    }

    private fun loadData() {
        logger.logExecution("ShopViewModel loadData")

        repository.getGroups()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { groups ->
                    logger.logDevWithThread("ShopViewModel loadData onSuccess")
                    selectGroup(groups.first())
                }, this::baseHandler
            ).toCache()
    }

    fun getParentOfSelected(): Group? {
        val parentId = _groups.value?.firstOrNull { it.isSelected }?.parentGroupId
        return _groups.value?.firstOrNull { it.id == parentId }
    }

    fun selectGroup(group: Group) {
        subject.onNext(group)
    }

    private fun subscribeItemsForGroup() {
        logger.logExecution("ShopViewModel subscribeItemsForGroup")

        getGroupsObservable().flatMapSingle {
            logger.logDevWithThread("ShopViewModel subscribeItemsForGroup getGroupsObservable")
            repository.getItemsForGroup(it.id)
        }.subscribe(
            { items ->
                logger.logDevWithThread("ShopViewModel subscribeItemsForGroup onSuccess")
                _items.postValue(items)
            }, this::baseHandler).toCache()

    }

    private fun subscribeGroupsForGroup() {
        logger.logExecution("ShopViewModel subscribeGroupsForGroup")

        var groupId: Int? = null
        getGroupsObservable().flatMapSingle {
            logger.logDevWithThread("ShopViewModel subscribeGroupsForGroup getGroupsObservable")
            groupId = it.id
            repository.getGroups(it.id)
        }.subscribe(
            { items ->
                logger.logDevWithThread("ShopViewModel subscribeGroupsForGroup onSuccess")
                _groups.postValue(
                    items.map {
                        it.isSelected = it.id == groupId
                        it
                    }
                )
            }, this::baseHandler
        ).toCache()
    }

}