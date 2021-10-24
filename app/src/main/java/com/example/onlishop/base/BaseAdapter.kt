package com.example.onlishop.base


import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.onlishop.app.App
import com.example.onlishop.global.Logger

abstract class BaseAdapter<T: Any>(diffCallback: DiffUtil.ItemCallback<T>): ListAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    protected val logger: Logger = App.logger

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BaseViewHolder<T>).bind(getItem(position))
    }
}

abstract class BaseViewHolder<T: Any>(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: T)
}

class SimpleAdapterDataObserver(
    private val observer: () -> Unit
) : RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        observer()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        observer()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        observer()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        observer()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        observer()
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        observer()
    }
}