package com.example.onlishop.ui.shop.bag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.BagItem
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.repository.ItemRepository

class BagViewModel(private val repository: ItemRepository, private val logger: Logger): BaseViewModel() {

    private val _items = MutableLiveData<List<BagItem>>()
    val items: LiveData<List<BagItem>> = _items

    val changedPos = MutableLiveData<Int>()
    val fullPrice = MutableLiveData<Int>()

    init {
        loadData()
    }

    private fun loadData(){
        logger.logExecution("loadData")
        viewModelScope.launchIo {
            val items = repository.getBagItems()
            _items.postValue(items)
        }
    }

    fun removeItem(bagItem: BagItem, pos: Int){
        logger.logExecution("removeItem")
        viewModelScope.launchIo {
            repository.removeBagItem(bagItem.bagItemId)
            val item = _items.value?.get(pos)
            item?.let {
                it.count = it.count - 1
            }

            if (item != null && item.count <= 0){
                val currentItems = _items.value?.toMutableList()
                currentItems?.removeAt(pos)
                _items.postValue(currentItems ?: emptyList())
            } else {
                changedPos.postValue(pos)
            }
        }
    }

    fun addItem(bagItem: BagItem, pos: Int){
        logger.logExecution("addItem")
        viewModelScope.launchIo {
            repository.addBagItem(bagItem.item, bagItem.size)
            val item = _items.value?.get(pos)
            item?.let {
                it.count = it.count + 1
            }
            changedPos.postValue(pos)
        }
    }

    fun countFullPrice(){
        logger.logExecution("countFullPrice")
        viewModelScope.launchIo {
            var price = 0
            _items.value?.forEach {
                price += it.count * it.item.price
            }
            fullPrice.postValue(price)
        }
    }

}