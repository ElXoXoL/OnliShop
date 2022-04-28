package com.example.onlishop.ui.shop

import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.onlishop.R
import com.example.onlishop.app.App
import com.example.onlishop.base.BaseAdapter
import com.example.onlishop.base.BaseViewHolder
import com.example.onlishop.databinding.ItemItemBinding
import com.example.onlishop.global.inflater
import com.example.onlishop.global.load
import com.example.onlishop.global.signed
import com.example.onlishop.models.Item
import kotlin.math.sign

class ItemsAdapter(
    private val onClick: (item: Item) -> Unit
): BaseAdapter<Item>(Diff()) {

    class Diff: DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(p0: Item, p1: Item): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: Item, p1: Item): Boolean {
            return p0.name == p1.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(ItemItemBinding.inflate(parent.inflater, parent, false))
    }

    inner class ItemViewHolder(private val binding: ItemItemBinding): BaseViewHolder<Item>(binding){

        init {
            binding.root.setOnClickListener {
                onClick.invoke(getItem(bindingAdapterPosition))
            }
        }

        override fun bind(item: Item){
            binding.itemImage.load(item.imageDrawable)
            binding.itemName.text = item.name
            binding.itemDescr.text = item.description
            binding.itemPrice.text = item.price.signed
        }

    }

}