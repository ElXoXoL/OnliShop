package com.example.onlishop.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class ShopViewModel(private val repository: ItemRepository, private val logger: Logger): BaseViewModel() {

    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>> = _groups

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    init {
        loadData()
    }

    private fun loadData(){
        logger.logExecution("loadData")
        viewModelScope.launchIo {
            val groups = repository.getGroups()
            val items = repository.getItems()

            _groups.postValue(groups)
            _items.postValue(items)
        }
    }

}