package com.example.onlishop.ui.shop.bag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.BagItem
import com.example.onlishop.repository.ItemRepository
import io.reactivex.rxjava3.schedulers.Schedulers

class BagViewModel(private val repository: ItemRepository, private val logger: Logger): BaseViewModel() {

    private val _items = MutableLiveData<List<BagItem>>()
    val items: LiveData<List<BagItem>> = _items

    val fullPrice = MutableLiveData<Int>()
    var itemsInOrderCount = 0
        private set

    init {
        loadData()
    }

    private fun loadData() {
        logger.logExecution("BagViewModel loadData")

        repository.getBagItems()
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .map {
                logger.logDevWithThread("BagViewModel loadData map")
                itemsInOrderCount = it.sumOf { it.count }
                this.fullPrice.postValue(
                    it.sumOf { it.count * it.item.price }
                )

                it
            }.subscribe(
                {
                    logger.logDevWithThread("BagViewModel loadData onSuccess")
                    _items.postValue(it)
                }, this::baseHandler
            ).toCache()

    }

    fun removeItem(bagItem: BagItem){
        logger.logExecution("removeItem")

        repository.removeBagItem(bagItem.bagItemId)
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribe({}, this::baseHandler)
            .toCache()

    }

    fun addItem(bagItem: BagItem){
        logger.logExecution("addItem")

        repository.addBagItem(bagItem.item, bagItem.size)
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribe({}, this::baseHandler)
            .toCache()
    }

}