package com.example.onlishop.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.StringRes
import com.example.onlishop.R
import com.example.onlishop.databinding.FragmentDialogConfirmBinding
import android.graphics.LightingColorFilter




class DialogConfirm(
    context: Context,
    private val textTitle: String,
    private val textSubtitle: String,
    private val textNo: String = context.getString(R.string.text_cancel),
    private val textYes: String = context.getString(R.string.text_confirm),
    private val isCancelable: Boolean = true,
    private val isYesImportant: Boolean = true,
    private val action: ((isAccepted: Boolean) -> Unit)? = null,
): Dialog(context) {

    private val binding = FragmentDialogConfirmBinding.inflate(LayoutInflater.from(context))

    init {
        if (textTitle.isEmpty() || textSubtitle.isEmpty()) {
            throw Exception("Texts can't be empty")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        this.window?.decorView?.setBackgroundColor(context.getColor(R.color.trans))

        setCancelable(isCancelable)

        binding.title.text = textTitle
        binding.subtitle.text = textSubtitle

        binding.acceptText.text = textYes
        binding.cancelText.text = textNo

        binding.cardAccept.isSelected = isYesImportant
        binding.cardCancel.isSelected = !isYesImportant

        binding.cardCancel.setOnClickListener {
            cancel()
        }
        binding.cardAccept.setOnClickListener {
            dismiss()
            action?.invoke(true)
        }
    }

    override fun cancel() {
        super.cancel()
        action?.invoke(false)
    }

}