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

    var selectedGroupId: Int? = null
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

            selectGroup(groups.first().id)
        }
    }

    fun selectGroup(groupId: Int){
        if (selectedGroupId == groupId) return
        logger.logExecution("selectGroup $groupId")
        loadItemsForGroup(groupId)
        loadGroupsForGroup(groupId)
    }

    fun setSelected(){
        val groupId = selectedGroupId ?: return
        _groups.value?.forEach {
            it.isSelected = false
        }
        val group = _groups.value?.find { it.id == groupId }
        group?.let {
            it.isSelected = true
            selectedGroup.postValue(it)
        }
    }

    private fun loadItemsForGroup(groupId: Int) {
        logger.logExecution("loadItemsForGroup $groupId")
        viewModelScope.launchIo {
            val items = repository.getItemsForGroup(groupId)
            _items.postValue(items)
        }
    }

    private fun loadGroupsForGroup(groupId: Int) {
        logger.logExecution("loadGroupsForGroup $groupId")
        viewModelScope.launchIo {
            val groups = repository.getGroups(groupId)
            selectedGroupId = groupId
            _groups.postValue(groups)
        }
    }

}