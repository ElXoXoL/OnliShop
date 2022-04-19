package com.example.onlishop.ui.shop

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.onlishop.R
import com.example.onlishop.app.App
import com.example.onlishop.base.BaseFragment
import com.example.onlishop.databinding.FragmentShopBinding
import com.example.onlishop.global.viewBinding
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.ui.splash.FragmentSplashDirections
import com.example.onlishop.utils.ListItemAnimator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
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
    }

    private fun setupView(){
        binding.btnSearch.setOnClickListener(this::onSearchClick)
        binding.btnAccount.setOnClickListener(this::onUserClick)
        binding.bag.root.setOnClickListener(this::onBagClick)

        binding.dividerGroups.dividerTitle.text = getString(R.string.text_groups_title)
        binding.dividerItems.dividerTitle.text = getString(R.string.text_items_title)

        binding.recGroups.adapter = groupAdapter
        binding.recItems.adapter = itemsAdapter
    }

    private fun observeViewModel(){
        viewModel.groups.observe(viewLifecycleOwner){
            groupAdapter.submitList(it)
            binding.groupName.text = it.firstOrNull { it.isSelected }?.name
        }
        viewModel.items.observe(viewLifecycleOwner){
            itemsAdapter.submitList(it)
        }

        viewModel.bagCount.observe(viewLifecycleOwner){
            bagCount = it
        }
    }

    private fun onGroupClick(item: Group) = viewModel.selectGroup(item)

    private fun onItemClick(item: Item) {
        logger.logExecution("onItemClick")
        val action = FragmentShopDirections.toDetail()
        action.itemId = item.id
        findNavController().navigate(action)
    }

    private fun onSearchClick(v: View? = null){
        logger.logExecution("onSearchClick")
        val action = FragmentShopDirections.toSearch()
        findNavController().navigate(action)
    }

    private fun onUserClick(v: View? = null){
        logger.logExecution("onUserClick")
        val action = FragmentShopDirections.toUser()
        findNavController().navigate(action)
    }

    private fun onBagClick(v: View? = null){
        logger.logExecution("onBagClick")
        val action = FragmentShopDirections.toBag()
        findNavController().navigate(action)
    }

    override fun onBackPressed() {
        val parentGroup = viewModel.getParentOfSelected()

        if (parentGroup != null){
            viewModel.selectGroup(parentGroup)
            return
        }
        super.onBackPressed()

    }
}