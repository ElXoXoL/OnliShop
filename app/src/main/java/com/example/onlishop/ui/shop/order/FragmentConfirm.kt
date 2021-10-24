package com.example.onlishop.ui.shop.order

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.onlishop.R
import com.example.onlishop.base.BaseFragment
import com.example.onlishop.base.SimpleAdapterDataObserver
import com.example.onlishop.databinding.FragmentOrderBinding
import com.example.onlishop.databinding.FragmentSearchBinding
import com.example.onlishop.databinding.FragmentShopBinding
import com.example.onlishop.global.hideKeyboard
import com.example.onlishop.global.showKeyboard
import com.example.onlishop.global.viewBinding
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.ui.shop.FragmentShopDirections
import com.example.onlishop.ui.shop.ItemsAdapter
import com.example.onlishop.ui.shop.bag.FragmentBagDirections
import com.example.onlishop.ui.splash.FragmentSplashDirections
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentConfirm: BaseFragment(R.layout.fragment_order) {

    private val binding by viewBinding(FragmentOrderBinding::bind)
    private val viewModel: ConfirmViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeViewModel()
    }

    private fun setupView(){
        binding.btnBack.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)
        binding.btnConfirmFull.setOnClickListener(this)

        binding.dividerName.dividerTitle.text = getString(R.string.text_full_name)
        binding.dividerPhone.dividerTitle.text = getString(R.string.text_phone)
        binding.dividerDelivery.dividerTitle.text = getString(R.string.text_address_del)
    }

    private fun observeViewModel(){

        viewModel.fullPrice.observe(viewLifecycleOwner){
            binding.btnConfirm.text = "Confirm - $it grn"
        }

    }

    private fun startConfirmAnims(){
        binding.resultContainer.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progress.visibility = View.GONE
            binding.tvConfirmed.visibility = View.VISIBLE
            binding.btnConfirmFull.visibility = View.VISIBLE
        }, 2000)
    }

    private fun toLaunchScreen(){
        logger.logExecution("toLaunchScreen")
        val action = FragmentConfirmDirections.toShop()
        findNavController().navigate(action)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            binding.btnBack.id -> {
                onBackPressed()
            }
            binding.btnConfirm.id -> {
                startConfirmAnims()
            }
            binding.btnConfirmFull.id -> {
                onBackPressed()
            }
            else -> super.onClick(v)
        }
    }

    override fun onBackPressed() {
        if (binding.resultContainer.visibility == View.VISIBLE){
            viewModel.cleanBag()
            toLaunchScreen()
        } else {
            super.onBackPressed()
        }
    }

}