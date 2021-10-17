package com.example.onlishop.ui.shop

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.onlishop.R
import com.example.onlishop.base.BaseFragment
import com.example.onlishop.databinding.FragmentShopBinding
import com.example.onlishop.global.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentShop: BaseFragment(R.layout.fragment_shop) {

    private val binding by viewBinding(FragmentShopBinding::bind)
    private val viewModel: ShopViewModel by viewModel()

    private val itemsAdapter by lazy { ItemsAdapter() }
    private val groupAdapter by lazy { GroupAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeViewModel()
    }

    private fun setupView(){
        binding.groupName.text = "Все"
        binding.dividerGroups.dividerTitle.text = "Группы"
        binding.dividerItems.dividerTitle.text = "Товары"

        binding.recGroups.adapter = groupAdapter
        binding.recItems.adapter = itemsAdapter
    }

    private fun observeViewModel(){
        viewModel.groups.observe(viewLifecycleOwner){
            groupAdapter.submitList(it)
        }
        viewModel.items.observe(viewLifecycleOwner){
            itemsAdapter.submitList(it)
        }
    }

    private fun openNextFragment(){
        logger.logExecution("openNextFragment")
//        val action = FragmentSp.fromMainToPickPack()
//        findNavController().navigate(action)
    }

}