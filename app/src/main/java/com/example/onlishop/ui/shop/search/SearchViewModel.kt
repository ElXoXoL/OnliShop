package com.example.onlishop.ui.shop.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class SearchViewModel(private val repository: ItemRepository, private val logger: Logger): BaseViewModel() {

    private val selectedSearch = MutableStateFlow("")

    @ExperimentalCoroutinesApi
    @FlowPreview
    val items: LiveData<List<Item>> = selectedSearch
        .debounce(500)
        .mapLatest {
            logger.logExecution("loadItemsSearch $it")
            if (it.isEmpty()) {
                emptyList()
            } else {
                repository.getSearchItems(it)
            }
        }
        .flowOn(Dispatchers.IO)
        .asLiveData(mainDispatcherHandled)

    val bagCount: LiveData<Int> = repository.getBagSizeFlow()
        .flowOn(Dispatchers.IO)
        .asLiveData(mainDispatcherHandled)

    fun setSearch(search: String){
        selectedSearch.value = search
    }

}