package com.example.onlishop.ui.shop.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.onlishop.R
import com.example.onlishop.base.BaseFragment
import com.example.onlishop.base.SimpleAdapterDataObserver
import com.example.onlishop.databinding.FragmentSearchBinding
import com.example.onlishop.global.viewBinding
import com.example.onlishop.models.Item
import com.example.onlishop.ui.shop.ItemsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentSearch: BaseFragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModel()

    private val itemsAdapter by lazy { ItemsAdapter(this::onItemClick) }

    private var isEmptyItems: Boolean = true
        set(value) {
            binding.tvEmptyItems.visibility = if (value) View.VISIBLE else View.GONE
            field = value
        }

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
        binding.btnBack.setOnClickListener(this)
        binding.bag.root.setOnClickListener(this)

        binding.editSearch.addTextChangedListener(onTextChanged = { text, _, _, _ ->
            viewModel.setSearch(text?.toString() ?: "")
        })

        binding.dividerItems.dividerTitle.text = getString(R.string.text_items_title)

        binding.recItems.adapter = itemsAdapter

        itemsAdapter.registerAdapterDataObserver(SimpleAdapterDataObserver{
            isEmptyItems = itemsAdapter.itemCount == 0
        })
        isEmptyItems = itemsAdapter.itemCount == 0
    }

    private fun observeViewModel(){

        viewModel.items.observe(viewLifecycleOwner){
            itemsAdapter.submitList(it)
        }

        viewModel.bagCount.observe(viewLifecycleOwner){
            bagCount = it
        }

    }

    private fun onItemClick(item: Item) {
//        binding.editSearch.hideKeyboard()
        logger.logExecution("onItemClick")
        val action = FragmentSearchDirections.toDetailFromSearch()
        action.itemId = item.id
        findNavController().navigate(action)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            binding.btnBack.id -> {
                onBackPressed()
            }
            binding.bag.root.id -> {
                logger.logExecution("onBagClick")
                val action = FragmentSearchDirections.toBag()
                findNavController().navigate(action)
            }
            else -> super.onClick(v)
        }
    }

}