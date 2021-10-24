package com.example.onlishop.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.onlishop.R
import com.example.onlishop.base.BaseFragment
import com.example.onlishop.databinding.FragmentSplashBinding
import com.example.onlishop.global.viewBinding


class FragmentSplash: BaseFragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            openNextFragment()
        }, 1000)
    }

    private fun openNextFragment(){
        logger.logExecution("openNextFragment")
        val action = FragmentSplashDirections.toShop()
        findNavController().navigate(action)
    }

}