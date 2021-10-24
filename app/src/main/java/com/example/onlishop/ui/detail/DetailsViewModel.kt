package com.example.onlishop.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Item
import com.example.onlishop.models.Size
import com.example.onlishop.repository.ItemRepository

class DetailsViewModel(
    private val repository: ItemRepository,
    private val logger: Logger,
    private val itemId: Int,
    ): BaseViewModel() {

    val sizes = MutableLiveData<List<Size>>()

    private val _item = MutableLiveData<Item>()
    val item: LiveData<Item> = _item

    init {
        loadItem()
    }

    private fun loadItem(){
        logger.logExecution("loadItem")
        viewModelScope.launchIo {
            if (itemId == -1) throw Exception("Can't find item")

            val item = repository.getItem(itemId)

            _item.postValue(item)
            sizes.postValue(item.sizes)
        }
    }

    val bagCount = MutableLiveData<Int>()
    fun loadBagCount(){
        logger.logExecution("loadBagCount")
        viewModelScope.launchIo {
            bagCount.postValue(repository.getBagSize())
        }
    }

    fun selectSize(pos: Int){
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

    fun addItem(){
        logger.logExecution("addItem")
        viewModelScope.launchIo {
            val item = _item.value ?: return@launchIo
            val size = sizes.value?.firstOrNull { it.isSelected } ?: return@launchIo
            repository.addBagItem(item, size.size)
            loadBagCount()
        }
    }

}