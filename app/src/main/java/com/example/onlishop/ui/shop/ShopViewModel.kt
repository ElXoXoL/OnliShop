package com.example.onlishop.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.repository.ItemRepository

class ShopViewModel(private val repository: ItemRepository, private val logger: Logger): BaseViewModel() {

    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>> = _groups

    val selectedGroup = MutableLiveData<Group>()


    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    init {
        loadData()
    }

    val bagCount = MutableLiveData<Int>()
    fun loadBagCount(){
        logger.logExecution("loadBagCount")
        viewModelScope.launchIo {
            bagCount.postValue(repository.getBagSize())
        }
    }

    private fun loadData(){
        logger.logExecution("loadData")
        viewModelScope.launchIo {
            val groups = repository.getGroups()
            _groups.postValue(groups)

            selectGroup(groups.first())
        }
    }

    fun selectGroup(group: Group){
        _groups.value?.forEach {
            it.isSelected = false
        }
        group.isSelected = true
        selectedGroup.postValue(group)
        loadItemsForGroup(group)
    }

    private fun loadItemsForGroup(group: Group) {
        logger.logExecution("loadItemsForGroup ${group.id}")
        viewModelScope.launchIo {
            val items = repository.getItemsForGroup(group.id)
            _items.postValue(items)
        }
    }

}