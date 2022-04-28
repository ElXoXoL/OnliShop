package com.example.onlishop.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Item
import com.example.onlishop.models.Size
import com.example.onlishop.repository.ItemRepository
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailsViewModel(
    private val repository: ItemRepository,
    private val logger: Logger,
    private val itemId: Int,
    ): BaseViewModel() {

    val sizes = MutableLiveData<List<Size>>()

    private val _item = MutableLiveData<Item>()
    val item: LiveData<Item> = _item

    val bagCount = MutableLiveData<Int>()

    init {
        loadItem()
        loadBagCount()
    }

    override fun initOnFragmentCreated() {
        super.initOnFragmentCreated()
    }

    private fun loadItem() {
        logger.logExecution("DetailsViewModel loadItem")

        repository.getItem(itemId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    _item.postValue(it)
                    sizes.postValue(it.sizes)
                    logger.logDevWithThread("DetailsViewModel loadItem onSuccess")
                }, this::baseHandler
            ).toCache()

    }

    private fun loadBagCount() {
        logger.logExecution("DetailsViewModel loadBagCount")

        repository.getBagSize()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    logger.logDevWithThread("DetailsViewModel loadBagCount onSuccess")
                    bagCount.postValue(it)
                }, this::baseHandler
            ).toCache()

    }

    fun selectSize(pos: Int) {
        logger.logExecution("selectSize")
        viewModelScope.launchIo {
            val currentSizes = sizes.value ?: return@launchIo
            if (currentSizes[pos].isSelected) return@launchIo

            currentSizes.forEach {
                it.isSelected = false
            }
            currentSizes[pos].isSelected = true
            sizes.postValue(currentSizes)
        }
    }

    fun addItem() {
        logger.logExecution("addItem")

        val item = _item.value ?: return
        val size = sizes.value?.firstOrNull { it.isSelected } ?: return

        repository.addBagItem(item, size.size)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    logger.logDevWithThread("addItem onSuccess")
                }, this::baseHandler
            ).toCache()

    }

}