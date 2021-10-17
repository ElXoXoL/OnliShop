package com.example.onlishop.base


import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.onlishop.app.App
import com.example.onlishop.global.Logger

abstract class BaseAdapter<T: Model>: ListAdapter<T, RecyclerView.ViewHolder>(DiffCallback()) {

    protected val logger: Logger = App.logger

    private class DiffCallback<T: Model> : DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.contentIdentical(newItem)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BaseViewHolder<T>).bind(getItem(position))
    }

}

abstract class BaseViewHolder<T: Model>(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: T)
}