package com.example.onlishop.ui.shop.bag

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.onlishop.R
import com.example.onlishop.base.BaseAdapter
import com.example.onlishop.base.BaseViewHolder
import com.example.onlishop.databinding.ItemItemBagBinding
import com.example.onlishop.global.inflater
import com.example.onlishop.global.load
import com.example.onlishop.global.signed
import com.example.onlishop.models.BagItem

class ItemsBagAdapter(
    private val onMinus: (bagItem: BagItem) -> Unit,
    private val onPlus: (bagItem: BagItem) -> Unit,
): BaseAdapter<BagItem>(Diff()) {

    class Diff: DiffUtil.ItemCallback<BagItem>(){
        override fun areItemsTheSame(first: BagItem, second: BagItem): Boolean {
            return first.bagItemId == second.bagItemId && first.size == second.size
        }

        override fun areContentsTheSame(first: BagItem, second: BagItem): Boolean {
            return first == second
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(ItemItemBagBinding.inflate(parent.inflater, parent, false))
    }

    private inner class ItemViewHolder(private val binding: ItemItemBagBinding): BaseViewHolder<BagItem>(binding){

        init {
            binding.btnMinus.setOnClickListener{
                onMinus.invoke(getItem(bindingAdapterPosition))
            }
            binding.btnPlus.setOnClickListener{
                onPlus.invoke(getItem(bindingAdapterPosition))
            }
        }

        override fun bind(item: BagItem){
            binding.itemImage.load(item.item.imageDrawable)
            binding.itemName.text = item.item.name
            binding.itemSize.text = item.size.uppercase()
            val countString = binding.root.context.getString(R.string.text_order_count, item.count)
            val priceString = binding.root.context.getString(R.string.text_price, (item.item.price * item.count).signed)
            binding.itemPrice.text = "$countString\n$priceString"
        }

    }

}