package com.example.onlishop.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Item
import com.example.onlishop.models.Size
import com.example.onlishop.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class DetailsViewModel(
    private val repository: ItemRepository,
    private val logger: Logger,
    private val itemId: Int,
): BaseViewModel() {

    val sizes = MutableLiveData<List<Size>>()

    private val _item = MutableLiveData<Item>()
    val item: LiveData<Item> = _item

    val bagCount: LiveData<Int> = repository.getBagSizeFlow()
        .flowOn(Dispatchers.IO)
        .asLiveData(mainDispatcherHandled)

    init {
        loadItem()
    }

    private fun loadItem() {
        logger.logExecution("loadItem")
        viewModelScope.launchIo {
            if (itemId == -1) throw Exception("Can't find item")

            val item = repository.getItem(itemId)

            _item.postValue(item)

            val sizes = item.sizes.also {
                it[0].isSelected = true
            }
            this@DetailsViewModel.sizes.postValue(sizes)
        }
    }

    fun selectSize(size: Size) {
        logger.logExecution("selectSize")
        viewModelScope.launchIo {
            val currentSizes = sizes.value ?: emptyList()

            sizes.postValue(
                currentSizes.map {
                    logger.logExecution("selectSize ${it.size == size.size}")
                    it.copy(isSelected = it.size == size.size)
                }
            )
        }
    }

    fun addItem() {
        logger.logExecution("addItem")
        viewModelScope.launchIo {
            val item = _item.value ?: return@launchIo
            val size = sizes.value?.firstOrNull { it.isSelected } ?: return@launchIo

            repository.addBagItem(item, size.size)

        }
    }
}
