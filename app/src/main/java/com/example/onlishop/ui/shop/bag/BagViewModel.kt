package com.example.onlishop.ui.shop.bag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.BagItem
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class BagViewModel(private val repository: ItemRepository, private val logger: Logger): BaseViewModel() {

    private val itemsFlow: Flow<List<BagItem>> = repository.getBagItemsFlow().flowOn(Dispatchers.IO)
    val items: LiveData<List<BagItem>> = itemsFlow.asLiveData(mainDispatcherHandled)

    val fullPrice: LiveData<Int> = itemsFlow.map {
        itemsInOrderCount = it.sumOf { it.count }
        it.sumOf { it.count * it.item.price }
    }.asLiveData(mainDispatcherHandled)

    var itemsInOrderCount = 0

    fun removeItem(bagItem: BagItem){
        logger.logExecution("removeItem")
        viewModelScope.launchIo {
            repository.removeBagItem(bagItem.bagItemId)
        }
    }

    fun addItem(bagItem: BagItem){
        logger.logExecution("addItem")
        viewModelScope.launchIo {
            repository.addBagItem(bagItem.item, bagItem.size)
        }
    }

}