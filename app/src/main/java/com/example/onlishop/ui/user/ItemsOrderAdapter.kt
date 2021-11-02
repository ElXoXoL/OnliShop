package com.example.onlishop.ui.user

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.onlishop.R
import com.example.onlishop.base.BaseAdapter
import com.example.onlishop.base.BaseViewHolder
import com.example.onlishop.databinding.ItemItemBagBinding
import com.example.onlishop.databinding.ItemItemOrderBinding
import com.example.onlishop.global.inflater
import com.example.onlishop.global.load
import com.example.onlishop.global.signed
import com.example.onlishop.models.BagItem
import com.example.onlishop.models.OrderItem

class ItemsOrderAdapter : BaseAdapter<OrderItem>(Diff()) {

    class Diff: DiffUtil.ItemCallback<OrderItem>(){
        override fun areItemsTheSame(p0: OrderItem, p1: OrderItem): Boolean {
            return p0 == p1
        }

        override fun areContentsTheSame(p0: OrderItem, p1: OrderItem): Boolean {
            return p0.count == p1.count
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(ItemItemOrderBinding.inflate(parent.inflater, parent, false))
    }

    inner class ItemViewHolder(private val binding: ItemItemOrderBinding): BaseViewHolder<OrderItem>(binding){

        override fun bind(item: OrderItem){
            binding.itemImage.load(item.item.imageDrawable)
            binding.itemName.text = item.item.name
            binding.itemSize.text = item.size.uppercase()
            binding.itemCount.text = binding.root.context.getString(R.string.text_order_count, item.count)
            binding.itemPrice.text = (item.item.price * item.count).signed
        }

    }

}