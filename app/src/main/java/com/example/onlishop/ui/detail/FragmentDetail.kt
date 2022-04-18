package com.example.onlishop.ui.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.onlishop.R
import com.example.onlishop.base.BaseFragment
import com.example.onlishop.databinding.FragmentItemDetailBinding
import com.example.onlishop.global.load
import com.example.onlishop.global.signed
import com.example.onlishop.global.viewBinding
import com.example.onlishop.models.Item
import com.example.onlishop.models.Size
import com.example.onlishop.ui.shop.search.FragmentSearchDirections
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import androidx.activity.OnBackPressedCallback





class FragmentDetail: BaseFragment(R.layout.fragment_item_detail) {

    private val binding by viewBinding(FragmentItemDetailBinding::bind)
    private val viewModel: DetailsViewModel by viewModel{
        parametersOf(
            if (arguments == null) {
                -1
            } else {
                FragmentDetailArgs.fromBundle(requireArguments()).itemId
            }
        )
    }

    private val sizesAdapter by lazy { SizesAdapter(viewModel::selectSize) }

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
        binding.sizesList.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
            alignItems = AlignItems.FLEX_START
        }
        binding.sizesList.adapter = sizesAdapter

        binding.btnBack.setOnClickListener(this)
        binding.btnAddItem.setOnClickListener(this)
        binding.detailImageFull.setOnClickListener(this)
        binding.detailImage.setOnClickListener(this)
        binding.bag.root.setOnClickListener(this)
    }

    private fun observeViewModel(){

        viewModel.bagCount.observe(viewLifecycleOwner){
            bagCount = it
        }

        viewModel.item.observe(viewLifecycleOwner){
            setItemContent(it)
        }

        viewModel.sizes.observe(viewLifecycleOwner){ list ->
            if (list.isNotEmpty()){
                if (list.all { !it.isSelected }){
                    viewModel.selectSize(0)
                } else {
                    sizesAdapter.submitList(list)
                    sizesAdapter.notifyDataSetChanged()
                }
            }
        }

    }

    private fun setItemContent(item: Item){
        binding.btnAddItem.text = if (item.sizes.isNotEmpty())
            "${getString(R.string.text_add_item)} ${item.price.signed}"
        else
            getString(R.string.text_not_available_items)

        binding.detailsTitle.text = item.name
        binding.detailImage.load(item.imageDrawable)
        binding.detailImageFull.load(item.imageDrawable)
        binding.detailsDescription.text = item.description
    }

    override fun onClick(v: View?) {
        when (v?.id){
            binding.btnBack.id -> onBackPressed()
            binding.btnAddItem.id -> viewModel.addItem()
            binding.bag.root.id -> {
                logger.logExecution("onBagClick")
                val action = FragmentDetailDirections.toBag()
                findNavController().navigate(action)
            }
            binding.detailImage.id -> binding.detailImageFull.visibility = View.VISIBLE
            binding.detailImageFull.id -> binding.detailImageFull.visibility = View.GONE
            else -> super.onClick(v)
        }
    }

    override fun onBackPressed() {
        if (binding.detailImageFull.visibility == View.VISIBLE){
            binding.detailImageFull.callOnClick()
            return
        }
        super.onBackPressed()
    }

}