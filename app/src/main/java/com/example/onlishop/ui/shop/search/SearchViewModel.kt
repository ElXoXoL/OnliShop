package com.example.onlishop.ui.shop.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.onlishop.base.BaseViewModel
import com.example.onlishop.global.Logger
import com.example.onlishop.models.Item
import com.example.onlishop.repository.ItemRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchViewModel(private val repository: ItemRepository, private val logger: Logger): BaseViewModel() {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items
    private val lastSearchSubject: PublishSubject<String> = PublishSubject.create()

    private var searchDisposable: Disposable? = null

    val bagCount = MutableLiveData<Int>()

    init {
        loadBagCount()
        loadItemsSearch()
    }

    private fun loadBagCount() {
        logger.logExecution("SearchViewModel loadBagCount")

        repository.getBagSize()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                logger.logDevWithThread("SearchViewModel loadBagCount onSuccess ")
                bagCount.postValue(it)
            }, {
                logger.logDevWithThread("SearchViewModel loadBagCount error $it")
            }).toCache()
    }

    fun setSearch(search: String) {
        lastSearchSubject.onNext(search)
    }

    private fun loadItemsSearch() {
        logger.logExecution("SearchViewModel loadItemsSearch")

        lastSearchSubject
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .flatMapSingle {
                if (!it.isNullOrEmpty()) {
                    repository.getSearchItems(it)
                } else {
                    Single.just(emptyList())
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeWith(object : Observer<List<Item>> {

                override fun onSubscribe(d: Disposable) {
                    if (searchDisposable != null && searchDisposable?.isDisposed == false) {
                        searchDisposable?.dispose()
                    }
                    searchDisposable = d
                    logger.logDevWithThread("SearchViewModel loadItemsSearch onSubscribe")
                }

                override fun onError(e: Throwable) {
                    baseHandler(e)
                }

                override fun onNext(items: List<Item>) {
                    logger.logDevWithThread("SearchViewModel loadItemsSearch onNext")
                    _items.postValue(items)
                }

                override fun onComplete() {
                    logger.logDevWithThread("SearchViewModel loadItemsSearch onComplete")
                }

            })
    }

    override fun onCleared() {
        super.onCleared()
        searchDisposable?.dispose()
        searchDisposable = null
    }

}