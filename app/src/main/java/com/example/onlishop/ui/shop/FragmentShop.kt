package com.example.onlishop.ui.shop

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.onlishop.R
import com.example.onlishop.base.BaseFragment
import com.example.onlishop.databinding.FragmentShopBinding
import com.example.onlishop.global.viewBinding
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.ui.splash.FragmentSplashDirections
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentShop: BaseFragment(R.layout.fragment_shop) {

    private val binding by viewBinding(FragmentShopBinding::bind)
    private val viewModel: ShopViewModel by viewModel()

    private val itemsAdapter by lazy { ItemsAdapter(this::onItemClick) }
    private val groupAdapter by lazy { GroupAdapter(this::onGroupClick) }

    private var bagCount: Int = 0
        set(value) {
            if (value > 0){
                binding.bag.root.visibility = View.VISIBLE
                binding.bag.bagSize.text = value.toString()
            } else {
                binding.bag.root.visibility = View.GONE
            }
            field = value
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeViewModel()

        viewModel.loadBagCount()
    }

    private fun setupView(){
        binding.btnSearch.setOnClickListener(this::onSearchClick)
        binding.bag.root.setOnClickListener(this::onBagClick)

        binding.dividerGroups.dividerTitle.text = getString(R.string.text_groups_title)
        binding.dividerItems.dividerTitle.text = getString(R.string.text_items_title)

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

        viewModel.selectedGroup.observe(viewLifecycleOwner){
            binding.groupName.text = it.name
            groupAdapter.notifyDataSetChanged()
        }

        viewModel.bagCount.observe(viewLifecycleOwner){
            bagCount = it
        }
    }

    private fun onGroupClick(item: Group) = viewModel.selectGroup(item)

    private fun onItemClick(item: Item) {
        logger.logExecution("onItemClick")
        val action = FragmentShopDirections.toDetail(itemId = item.id)
        findNavController().navigate(action)
    }

    private fun onSearchClick(v: View? = null){
        logger.logExecution("onSearchClick")
        val action = FragmentShopDirections.toSearch()
        findNavController().navigate(action)
    }

    private fun onBagClick(v: View? = null){
        logger.logExecution("onBagClick")
        val action = FragmentShopDirections.toBag()
        findNavController().navigate(action)
    }
}