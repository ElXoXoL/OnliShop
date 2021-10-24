package com.example.onlishop.ui.shop.bag

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.onlishop.base.BaseAdapter
import com.example.onlishop.base.BaseViewHolder
import com.example.onlishop.databinding.ItemItemBagBinding
import com.example.onlishop.global.inflater
import com.example.onlishop.global.load
import com.example.onlishop.models.BagItem

class ItemsBagAdapter(
    private val onMinus: (bagItem: BagItem, pos: Int) -> Unit,
    private val onPlus: (bagItem: BagItem, pos: Int) -> Unit,
): BaseAdapter<BagItem>(Diff()) {

    class Diff: DiffUtil.ItemCallback<BagItem>(){
        override fun areItemsTheSame(p0: BagItem, p1: BagItem): Boolean {
            return p0 == p1
        }

        override fun areContentsTheSame(p0: BagItem, p1: BagItem): Boolean {
            return p0.count == p1.count
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(ItemItemBagBinding.inflate(parent.inflater, parent, false))
    }

    inner class ItemViewHolder(private val binding: ItemItemBagBinding): BaseViewHolder<BagItem>(binding){

        init {
            binding.btnMinus.setOnClickListener{
                onMinus.invoke(getItem(bindingAdapterPosition), bindingAdapterPosition)
            }
            binding.btnPlus.setOnClickListener{
                onPlus.invoke(getItem(bindingAdapterPosition), bindingAdapterPosition)
            }
        }

        override fun bind(item: BagItem){
            binding.itemImage.load(item.item.imageDrawable)
            binding.itemName.text = item.item.name
            binding.itemSize.text = item.size
            binding.itemPrice.text = "Count: ${item.count}\n Price: ${item.item.price * item.count} grn"
        }

    }

}