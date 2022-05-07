package com.example.onlishop.ui.shop.bag

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.onlishop.R
import com.example.onlishop.base.BaseFragment
import com.example.onlishop.databinding.FragmentBagBinding
import com.example.onlishop.databinding.FragmentItemDetailBinding
import com.example.onlishop.global.*
import com.example.onlishop.models.Item
import com.example.onlishop.models.Size
import com.example.onlishop.ui.detail.FragmentDetailDirections
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class FragmentBag: BaseFragment(R.layout.fragment_bag) {

    private val binding by viewBinding(FragmentBagBinding::bind)
    private val viewModel: BagViewModel by viewModel()

    private val itemsAdapter by lazy { ItemsBagAdapter(viewModel::removeItem, viewModel::addItem) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeViewModel()
    }

    private fun setupView(){
        binding.recItems.adapter = itemsAdapter

        binding.btnBack.setOnClickListener(this)
        binding.btnMakeOrder.setOnClickListener(this)
    }

    private fun observeViewModel(){

        viewModel.items.observe(viewLifecycleOwner){
            if (it.isEmpty()){
                onBackPressed()
            } else {
                itemsAdapter.submitList(it)
            }
        }

        viewModel.fullPrice.observe(viewLifecycleOwner){
            val discountText = it.getDiscountText(viewModel.itemsInOrderCount)
            binding.discountText.text = discountText
            binding.discountText.isVisible = discountText.isNotEmpty()
            binding.btnMakeOrder.text = getString(
                R.string.text_make_order,
                it.getAppliedDiscount(viewModel.itemsInOrderCount).signed
            )
        }

    }

    override fun onClick(v: View?) {
        when (v?.id){
            binding.btnBack.id -> onBackPressed()
            binding.btnMakeOrder.id -> {
                logger.logExecution("btnMakeOrder")
                val action = FragmentBagDirections.toOrder()
                findNavController().navigate(action)
            }
            else -> super.onClick(v)
        }
    }

}