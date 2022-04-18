package com.example.onlishop.ui.shop.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Item
import com.example.onlishop.repository.ItemRepository
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchViewModel(private val repository: ItemRepository, private val logger: Logger): BaseViewModel() {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items
    private var lastSearch = ""

    private var searchDisposable: Disposable? = null

    val bagCount = MutableLiveData<Int>()

    init {
        loadBagCount()
    }

    private fun loadBagCount() {
        logger.logExecution("SearchViewModel loadBagCount")

        repository.getBagSize()
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribe({
                logger.logDevWithThread("SearchViewModel loadBagCount onSuccess ")
                bagCount.postValue(it)
            }, {
                logger.logDevWithThread("SearchViewModel loadBagCount error $it")
            }).toCache()
    }

    fun setSearch(search: String) {
        if (search.isEmpty()) {
            _items.postValue(emptyList())
            return
        } else if (lastSearch == search) {
            return
        }

        lastSearch = search
        loadItemsSearch(search)
    }

    private fun loadItemsSearch(search: String) {
        logger.logExecution("SearchViewModel loadItemsSearch")

        repository.getSearchItems(search)
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribeWith(object : SingleObserver<List<Item>> {
                override fun onSubscribe(d: Disposable) {
                    if (searchDisposable != null && searchDisposable?.isDisposed == false) {
                        searchDisposable?.dispose()
                    }
                    searchDisposable = d
                    logger.logDevWithThread("SearchViewModel loadItemsSearch onSubscribe")
                }

                override fun onSuccess(items: List<Item>) {
                    logger.logDevWithThread("SearchViewModel loadItemsSearch onSuccess")
                    _items.postValue(items)
                }

                override fun onError(e: Throwable) {
                    baseHandler(e)
                }

            })
    }

    override fun onCleared() {
        super.onCleared()
        searchDisposable?.dispose()
        searchDisposable = null
    }

}