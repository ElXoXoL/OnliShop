package com.example.onlishop.ui.shop

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlishop.app.App
import com.example.onlishop.base.BaseAdapter
import com.example.onlishop.base.BaseViewHolder
import com.example.onlishop.databinding.ItemGroupBinding
import com.example.onlishop.databinding.ItemItemBinding
import com.example.onlishop.global.inflater
import com.example.onlishop.global.load
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.models.Size
import kotlin.random.Random

class GroupAdapter(
    private val onClick: (item: Group) -> Unit
): BaseAdapter<Group>(Diff()) {

    class Diff: DiffUtil.ItemCallback<Group>(){
        override fun areItemsTheSame(p0: Group, p1: Group): Boolean {
            return p0 == p1
        }

        override fun areContentsTheSame(p0: Group, p1: Group): Boolean {
            return p0.isSelected == p1.isSelected
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(ItemGroupBinding.inflate(parent.inflater, parent, false))
    }

    inner class ItemViewHolder(private val binding: ItemGroupBinding): BaseViewHolder<Group>(binding){

        init {
            binding.root.setOnClickListener {
                onClick.invoke(getItem(bindingAdapterPosition))
            }
        }

        override fun bind(item: Group){
            binding.groupName.text = item.name
            binding.card.isSelected = item.isSelected
        }

    }

}