package com.example.onlishop.ui.user

import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.onlishop.R
import com.example.onlishop.app.App
import com.example.onlishop.base.BaseAdapter
import com.example.onlishop.base.BaseViewHolder
import com.example.onlishop.databinding.ItemItemBinding
import com.example.onlishop.databinding.ItemItemOrderBinding
import com.example.onlishop.databinding.ItemOrderBinding
import com.example.onlishop.global.*
import com.example.onlishop.models.Item
import com.example.onlishop.models.Order

class OrdersAdapter: BaseAdapter<Order>(Diff()) {

    class Diff: DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(p0: Order, p1: Order): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: Order, p1: Order): Boolean {
            return p0 == p1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(ItemOrderBinding.inflate(parent.inflater, parent, false))
    }

    private inner class ItemViewHolder(private val binding: ItemOrderBinding): BaseViewHolder<Order>(binding){

        override fun bind(item: Order){
            val context = binding.root.context

            var fullPrice = 0
            var itemsCount = 0
            item.orderItems.forEach {
                fullPrice += it.item.price * it.count
                itemsCount += it.count
            }
            binding.tvOrderName.text = context.getString(R.string.text_user_name, item.name)
            binding.tvOrderId.text = context.getString(R.string.text_order_id, item.id)
            binding.tvOrderCardNum.text = if (item.cardNum.cardMasked.isNotEmpty()) {
                context.getString(R.string.text_order_card, item.cardNum.cardMasked)
            } else {
                context.getString(R.string.text_order_cash)
            }
            binding.tvOrderDelivery.text = context.getString(R.string.text_order_delivery, item.delivery)
            binding.tvOrderEmail.text = context.getString(R.string.text_user_email, item.email)
            binding.tvOrderPhone.text = context.getString(R.string.text_user_phone, item.phone.formattedPhone)
            binding.tvOrderDate.text = context.getString(R.string.text_order_date, item.date)

            var finalPriceText = ""
            val discountTexts = fullPrice.getDiscountText(itemsCount).split("; ").filter { it.isNotEmpty() }
            discountTexts.forEach {
                finalPriceText += "$it\n"
            }

            finalPriceText += context.getString(R.string.text_order_price, fullPrice.getAppliedDiscount(itemsCount).signed)
            binding.tvOrderFullPrice.text = finalPriceText

            val adapter = ItemsOrderAdapter()
            binding.recItems.adapter = adapter
            adapter.submitList(item.orderItems)
        }

    }

}