package com.example.onlishop.ui.shop

import androidx.lifecycle.*
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class ShopViewModel(private val repository: ItemRepository, private val logger: Logger): BaseViewModel() {

    private val _selectedGroupState = MutableStateFlow<Group?>(null)
    val selectedGroup = _selectedGroupState.asStateFlow()

    private val groupIdTrigger = MutableLiveData<Int>()

    val groups: LiveData<List<Group>> = groupIdTrigger.switchMap { id ->
        logger.logExecution("loadItemsForGroup ${id}")

        liveData(mainDispatcherHandled) {
            withContext(Dispatchers.IO) {
                val groups = repository.getGroupChildrenAndParent(id)
                groups.forEach { group ->
                    if (group.id == id) {
                        group.isSelected = true
                        _selectedGroupState.update {
                            group
                        }
                    }
                }
                emit(groups)
            }
        }
    }

    val items: LiveData<List<Item>> = groupIdTrigger.switchMap { id ->
        logger.logExecution("loadGroupsForGroup ${id}")

        liveData(mainDispatcherHandled) {
            withContext(Dispatchers.IO) {
                emit(repository.getItemsAndSubitemsForGroup(id))
            }
        }
    }

    val bagCount: LiveData<Int> = repository.getBagSizeFlow()
        .flowOn(Dispatchers.IO)
        .asLiveData(mainDispatcherHandled)

    init {
        loadData()
    }

    private fun loadData() {
        logger.logExecution("loadData")
        viewModelScope.launchIo {
            val groups = repository.getGroups()

            selectGroup(groups.first().id)
        }
    }

    fun selectGroup(groupId: Int) {
        if (groupIdTrigger.value == groupId) return
        groupIdTrigger.postValue(groupId)
        logger.logExecution("selectGroup ${groupId}")
    }

}