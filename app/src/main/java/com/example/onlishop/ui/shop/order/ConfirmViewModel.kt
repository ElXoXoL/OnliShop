package com.example.onlishop.ui.shop.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.repository.ItemRepository

class ConfirmViewModel(private val repository: ItemRepository, private val logger: Logger): BaseViewModel() {

    val fullPrice = MutableLiveData<Int>()

    init {
        loadFullPrice()
    }

    private fun loadFullPrice(){
        logger.logExecution("loadFullPrice")
        viewModelScope.launchIo {
            var price = 0
            repository.getBagItems().forEach {
                price += it.count * it.item.price
            }
            fullPrice.postValue(price)
        }
    }

    fun cleanBag(){
        logger.logExecution("cleanBag")
        viewModelScope.launchIo {
            repository.cleanBag()
        }
    }

}