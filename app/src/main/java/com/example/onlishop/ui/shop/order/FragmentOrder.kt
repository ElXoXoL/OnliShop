package com.example.onlishop.ui.shop.order

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import androidx.core.util.PatternsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.onlishop.R
import com.example.onlishop.base.BaseFragment
import com.example.onlishop.databinding.FragmentOrderBinding
import com.example.onlishop.global.*
import com.example.onlishop.models.OrderCheck
import com.example.onlishop.utils.Crypt
import com.example.onlishop.utils.LinkOpenHelper
import com.example.onlishop.utils.PhoneWatcher
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentOrder : BaseFragment(R.layout.fragment_order),
    RadioGroup.OnCheckedChangeListener,
    View.OnTouchListener {

    private val binding by viewBinding(FragmentOrderBinding::bind)
    private val viewModel: OrderViewModel by viewModel()

    private var checkedId: Int = 0

    private var cardNum: String = ""
    private var cardDate: String = ""

    private val phoneWatcher by lazy {
        PhoneWatcher(binding.editPhone)
    }

    private val cardWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val textInside = s?.toString()?.replace("\\s+".toRegex(), "") ?: ""
            if (cardNum == textInside) return
            cardNum = textInside
            var counter = 0
            var formatted = ""
            cardNum.forEach {
                formatted += it
                when (counter) {
                    3, 7, 11 -> {
                        formatted += " "
                    }
                }
                counter++
            }
            formatted = formatted.trimEnd()
            binding.editCardNumber.setText(formatted)
            binding.editCardNumber.setSelection(binding.editCardNumber.length())
        }

        override fun afterTextChanged(s: Editable?) {}

    }

    private val cardDateWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val textInside = s?.toString()?.replace("/", "") ?: ""
            if (cardDate == textInside) return
            cardDate = textInside
            var counter = 0
            var formattedPhone = ""
            cardDate.forEach {
                formattedPhone += it
                if (counter == 1) {
                    formattedPhone += "/"
                }
                counter++
            }
            formattedPhone = if (formattedPhone.lastOrNull() == '/') {
                formattedPhone.replace("/", "")
            } else {
                formattedPhone
            }
            binding.editCardDate.setText(formattedPhone)
            binding.editCardDate.setSelection(binding.editCardDate.length())
        }

        override fun afterTextChanged(s: Editable?) {}

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeViewModel()
    }

    private fun setupView() {
        binding.btnBack.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)
        binding.btnConfirmFull.setOnClickListener(this)
        binding.checkPrivacy.setOnLongClickListener {
            LinkOpenHelper.openPrivacyPolicy(requireActivity())
            true
        }

        binding.editPhone.setOnTouchListener(this)
        binding.editCardNumber.setOnTouchListener(this)
        binding.editCardDate.setOnTouchListener(this)

        binding.editPhone.addTextChangedListener(phoneWatcher)
        binding.editCardNumber.addTextChangedListener(cardWatcher)
        binding.editCardDate.addTextChangedListener(cardDateWatcher)

        binding.radioContainer.setOnCheckedChangeListener(this)

        binding.dividerName.dividerTitle.text = getString(R.string.text_full_name)
        binding.dividerPhone.dividerTitle.text = getString(R.string.text_phone)
        binding.dividerDelivery.dividerTitle.text = getString(R.string.text_address_del)
        binding.dividerEmail.dividerTitle.text = getString(R.string.text_email)
        binding.dividerPayment.dividerTitle.text = getString(R.string.text_payment)
    }

    private fun observeViewModel() {

        viewModel.fullPrice.observe(viewLifecycleOwner) {
            val discountText = it.getDiscountText(viewModel.itemsInOrderCount)
            binding.discountText.text = discountText
            binding.discountText.isVisible = discountText.isNotEmpty()
            binding.btnConfirm.text = getString(
                R.string.text_confirm_compound,
                it.getAppliedDiscount(viewModel.itemsInOrderCount).signed
            )
        }

        viewModel.lastOrder.observe(viewLifecycleOwner) {
            binding.editName.setText(it.name)
            binding.editPhone.setText(it.phone)
            binding.editDelivery.setText(it.delivery)
            binding.editEmail.setText(it.email)
            binding.editCardNumber.setText(it.cardNum)
            binding.editCardDate.setText(it.cardDate)

            logger.logDev("ORder type : ${it.orderType}")
            if (it.orderType == "card"){
                binding.radioCard.isChecked = true
            }
        }

    }

    private fun startConfirmAnims() {
        binding.resultContainer.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progress.visibility = View.GONE
            binding.tvConfirmed.visibility = View.VISIBLE
            binding.btnConfirmFull.visibility = View.VISIBLE
        }, 2000)
    }

    private fun getOrderCheck(): OrderCheck = OrderCheck(
        name = binding.editName.text?.toString() ?: "",
        phone = phoneWatcher.phone,
        email = binding.editEmail.text?.toString() ?: "",
        orderType = if (checkedId == binding.radioCard.id) "card" else "cash",
        delivery = binding.editDelivery.text?.toString() ?: "",
        cardNum = cardNum,
        cardDate = cardDate,
        cardCvc = binding.editCardCvc.text?.toString() ?: "",
        isPrivacyAccepted = binding.checkPrivacy.isChecked
    )

    private fun checkInfo(orderCheck: OrderCheck): Boolean {
        logger.logDev("Check: $orderCheck")
        return when {
            orderCheck.name.isEmpty() -> false
            orderCheck.phone.length < 10 -> false
            orderCheck.email.isEmpty() -> false
            !PatternsCompat.EMAIL_ADDRESS.matcher(orderCheck.email).matches() -> false
            orderCheck.delivery.isEmpty() -> false
            !orderCheck.isPrivacyAccepted -> false
            orderCheck.orderType == "card" -> {
                when {
                    orderCheck.cardNum.length < 16 -> false
                    orderCheck.cardDate.length < 4 -> false
                    orderCheck.cardCvc.length < 3 -> false
                    else -> true
                }
            }
            else -> true
        }
    }

    private fun toLaunchScreen() {
        logger.logExecution("toLaunchScreen")
        val action = FragmentOrderDirections.toShop()
        findNavController().navigate(action)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnBack.id -> {
                onBackPressed()
            }
            binding.btnConfirm.id -> {
                val orderCheck = getOrderCheck()
                if (checkInfo(orderCheck)) {
                    viewModel.saveOrder(orderCheck)
                    startConfirmAnims()
                } else {
                    getString(R.string.error_order_info).toast()
                }
            }
            binding.btnConfirmFull.id -> {
                onBackPressed()
            }
            else -> super.onClick(v)
        }
        v?.hideKeyboard()
    }

    override fun onBackPressed() {
        if (binding.resultContainer.visibility == View.VISIBLE) {
            viewModel.cleanBag()
            toLaunchScreen()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        if (checkedId == this.checkedId) return
        when (checkedId) {
            binding.radioCash.id -> {
                binding.paymentCardContainer.visibility = View.GONE
            }
            binding.radioCard.id -> {
                binding.paymentCardContainer.visibility = View.VISIBLE
            }
        }
        this.checkedId = checkedId
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v?.id) {
            binding.editPhone.id,
            binding.editCardNumber.id,
            binding.editCardDate.id -> {
                val editText = v as? EditText
                editText?.setSelection(editText?.length() ?: 0)
                editText?.showKeyboard()
            }
        }
        return true
    }
}