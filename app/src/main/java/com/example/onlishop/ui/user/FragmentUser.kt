package com.example.onlishop.ui.user

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.text.TextUtilsCompat
import androidx.core.util.PatternsCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.onlishop.R
import com.example.onlishop.base.BaseFragment
import com.example.onlishop.base.SimpleAdapterDataObserver
import com.example.onlishop.databinding.FragmentSearchBinding
import com.example.onlishop.databinding.FragmentShopBinding
import com.example.onlishop.databinding.FragmentUserBinding
import com.example.onlishop.global.*
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.models.OrderCheck
import com.example.onlishop.models.User
import com.example.onlishop.ui.DialogConfirm
import com.example.onlishop.ui.shop.FragmentShopDirections
import com.example.onlishop.ui.shop.ItemsAdapter
import com.example.onlishop.ui.splash.FragmentSplashDirections
import com.example.onlishop.utils.IdGenerator
import com.example.onlishop.utils.LinkOpenHelper
import com.example.onlishop.utils.PhoneWatcher
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class FragmentUser: BaseFragment(R.layout.fragment_user) {

    private val binding by viewBinding(FragmentUserBinding::bind)
    private val viewModel: UserViewModel by viewModel()

    private val ordersAdapter by lazy { OrdersAdapter() }

    private var isEmptyItems: Boolean = true
        set(value) {
            binding.tvEmptyItems.visibility = if (value) View.VISIBLE else View.GONE
            field = value
        }

    private val phoneWatcher by lazy {
        PhoneWatcher(binding.editPhone)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupUserRegView()
        observeViewModel()

        showScreen(false)
    }

    private fun setupView(){
        binding.btnBack.setOnClickListener(this)
        binding.btnInfo.setOnClickListener(this)
        binding.btnPhone.setOnClickListener(this)

        binding.dividerUserData.dividerTitle.text = getString(R.string.text_user_data)
        binding.dividerUserOrders.dividerTitle.text = getString(R.string.text_user_orders)

        binding.recItems.adapter = ordersAdapter

        ordersAdapter.registerAdapterDataObserver(SimpleAdapterDataObserver{
            isEmptyItems = ordersAdapter.itemCount == 0
        })
        isEmptyItems = ordersAdapter.itemCount == 0
    }

    private fun setupUserRegView(){
        binding.btnUserLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.btnDelete.setOnClickListener(this)
        binding.checkPrivacy.setOnLongClickListener {
            LinkOpenHelper.openPrivacyPolicy(requireActivity())
            true
        }

        binding.editPhone.addTextChangedListener(phoneWatcher)
        binding.editPhone.setOnTouchListener { v, _ ->
            val editText = v as? EditText
            editText?.setSelection(editText?.length() ?: 0)
            editText?.showKeyboard()
            true
        }

        binding.dividerEmail.dividerTitle.text = getString(R.string.text_email)
        binding.dividerName.dividerTitle.text = getString(R.string.text_full_name)
        binding.dividerPass.dividerTitle.text = getString(R.string.text_pass)
        binding.dividerPassConfirm.dividerTitle.text = getString(R.string.text_pass_confirm)
        binding.dividerPhone.dividerTitle.text = getString(R.string.text_phone)

        // By default reg btn is visible, later it will be changed if user exists
        binding.btnUserLogin.visibility = View.VISIBLE
        binding.containerUserData.visibility = View.GONE
    }

    private fun observeViewModel(){

        viewModel.items.observe(viewLifecycleOwner){
            ordersAdapter.submitList(it)
        }

        viewModel.user.observe(viewLifecycleOwner){

            if (it == null) {
                binding.btnUserLogin.visibility = View.VISIBLE
                binding.containerUserData.visibility = View.GONE
                return@observe
            }

            binding.btnUserLogin.visibility = View.GONE
            binding.containerUserData.visibility = View.VISIBLE

            binding.tvUserName.text = getString(R.string.text_user_name, it.name)
            binding.tvUserPhone.text = getString(R.string.text_user_phone, it.phone.formattedPhone)
            binding.tvUserEmail.text = getString(R.string.text_user_email, it.email)
        }

    }

    private fun showScreen(isUserLogin: Boolean) {
        if (isUserLogin) {
            binding.userRegContainer.visibility = View.VISIBLE
            binding.containerUserOrders.visibility = View.GONE
        } else {
            binding.userRegContainer.visibility = View.GONE
            binding.containerUserOrders.visibility = View.VISIBLE
        }
    }

    private fun getUserCheck(): User {
        return User(
            id = IdGenerator.randomId,
            name = binding.editName.text?.toString() ?: "",
            phone = phoneWatcher.phone,
            email = binding.editEmail.text?.toString() ?: "",
            pass = binding.editPass.text?.toString() ?: "",
            confirmPass = binding.editPassConfirm.text?.toString() ?: "",
        )
    }

    private fun checkInfo(user: User): Boolean {
        return when {
            user.name.isEmpty() -> false
            user.phone.length < 10 -> false
            user.email.isEmpty() -> false
            !PatternsCompat.EMAIL_ADDRESS.matcher(user.email).matches() -> false
            user.pass.isEmpty() -> false
            user.confirmPass.isEmpty() -> false
            user.pass.replace("[0-9]".toRegex(), "").isEmpty() -> false
            !user.pass.matches(".*\\d.*".toRegex()) -> false
            user.pass != user.confirmPass -> false
            !binding.checkPrivacy.isChecked -> false
            else -> true
        }
    }

    override fun onBackPressed() {
        if (binding.userRegContainer.visibility == View.VISIBLE){
            showScreen(false)
            return
        }
        super.onBackPressed()
    }

    override fun onClick(v: View?) {
        when (v?.id){
            binding.btnBack.id -> {
                onBackPressed()
            }
            binding.btnUserLogin.id -> {
                showScreen(true)
            }
            binding.btnRegister.id -> {
                val user = getUserCheck()
                if (checkInfo(user)) {
                    viewModel.saveUser(user)
                    showScreen(false)
                } else {
                    getString(R.string.error_order_info).toast()
                }
            }
            binding.btnInfo.id -> {
                LinkOpenHelper.openPrivacyPolicy(requireActivity())
            }
            binding.btnPhone.id -> {
                LinkOpenHelper.openHelpPhone(requireActivity())
            }
            binding.btnDelete.id -> {
                DialogConfirm(
                    requireContext(),
                    "Deleting your user",
                    "Deleting will erase all your data, do you want to continue?",
                    "No",
                    "Yes",
                    true,
                    false
                ) {
                    if (it) {
                        viewModel.removeUser()
                    }
                }.show()
            }
            else -> super.onClick(v)
        }
    }

}