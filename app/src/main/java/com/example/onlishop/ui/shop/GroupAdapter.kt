package com.example.onlishop.ui.shop

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlishop.base.BaseViewHolder
import com.example.onlishop.databinding.ItemGroupBinding
import com.example.onlishop.databinding.ItemItemBinding
import com.example.onlishop.global.inflater
import com.example.onlishop.global.load
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import kotlin.random.Random

class GroupAdapter(): ListAdapter<Group, RecyclerView.ViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<Group>() {

        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BaseViewHolder<Group>).bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(ItemGroupBinding.inflate(parent.inflater, parent, false))
    }

    inner class ItemViewHolder(private val binding: ItemGroupBinding): BaseViewHolder<Group>(binding){

        override fun bind(item: Group){
            binding.groupName.text = item.name
        }

    }

}