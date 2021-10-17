package com.example.onlishop.ui.shop

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlishop.base.BaseViewHolder
import com.example.onlishop.databinding.ItemItemBinding
import com.example.onlishop.global.inflater
import com.example.onlishop.global.load
import com.example.onlishop.models.Item
import kotlin.random.Random

class ItemsAdapter(): ListAdapter<Item, RecyclerView.ViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<Item>() {

        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BaseViewHolder<Item>).bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(ItemItemBinding.inflate(parent.inflater, parent, false))
    }

    inner class ItemViewHolder(private val binding: ItemItemBinding): BaseViewHolder<Item>(binding){

        override fun bind(item: Item){
            binding.itemImage.load(item.imageDrawable)
            binding.itemName.text = item.name
            binding.itemDescr.text = item.description
            binding.itemPrice.text = "${item.price} грн"
        }

    }

}