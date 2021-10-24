package com.example.onlishop.ui.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlishop.R
import com.example.onlishop.app.App
import com.example.onlishop.base.BaseAdapter
import com.example.onlishop.base.BaseViewHolder
import com.example.onlishop.databinding.ItemItemBinding
import com.example.onlishop.databinding.ItemSizeBinding
import com.example.onlishop.global.color
import com.example.onlishop.global.inflater
import com.example.onlishop.global.load
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.models.Size
import kotlin.random.Random

class SizesAdapter(
    private val onClick: (position: Int) -> Unit
): BaseAdapter<Size>(Diff()) {

    class Diff: DiffUtil.ItemCallback<Size>(){
        override fun areItemsTheSame(before: Size, new: Size): Boolean {
            return before.isSelected == new.isSelected
        }

        override fun areContentsTheSame(before: Size, new: Size): Boolean {
            return before.isSelected == new.isSelected
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SizeViewHolder(ItemSizeBinding.inflate(parent.inflater, parent, false))
    }

    inner class SizeViewHolder(private val binding: ItemSizeBinding): BaseViewHolder<Size>(binding){

        init {
            binding.root.setOnClickListener {
                onClick.invoke(bindingAdapterPosition)
            }
        }

        override fun bind(item: Size){
            binding.size.text = item.size
            binding.container.isSelected = item.isSelected
        }

    }

}