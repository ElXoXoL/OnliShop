package com.example.onlishop.ui.shop.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.repository.ItemRepository

class SearchViewModel(private val repository: ItemRepository, private val logger: Logger): BaseViewModel() {

    val selectedSearch = MutableLiveData<String>()

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    val bagCount = MutableLiveData<Int>()
    fun loadBagCount(){
        logger.logExecution("loadBagCount")
        viewModelScope.launchIo {
            bagCount.postValue(repository.getBagSize())
        }
    }

    fun setSearch(search: String){
        if (search.isEmpty()) {
            _items.postValue(emptyList())
            return
        }

        selectedSearch.postValue(search)
        loadItemsSearch(search)
    }

    private fun loadItemsSearch(search: String) {
        logger.logExecution("loadItemsSearch $search")

        viewModelScope.launchIo {
            val items = repository.getSearchItems(search)
            _items.postValue(items)
        }
    }

}