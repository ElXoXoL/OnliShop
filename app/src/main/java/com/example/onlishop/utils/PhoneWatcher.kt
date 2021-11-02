package com.example.onlishop.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.example.onlishop.global.formattedPhone

class PhoneWatcher(
    private val editText: EditText
): TextWatcher {

    var phone: String = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val textInside = s?.toString()?.replace("\\s+".toRegex(), "") ?: ""
        if (phone == textInside) return
        phone = textInside
        val formattedPhone = phone.formattedPhone
        editText.setText(formattedPhone)
        editText.setSelection(editText.length())
    }

    override fun afterTextChanged(s: Editable?) {}

}