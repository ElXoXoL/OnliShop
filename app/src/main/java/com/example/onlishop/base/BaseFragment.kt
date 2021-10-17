package com.example.onlishop.base

import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.onlishop.global.Logger
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.ext.android.inject
import java.util.*

abstract class BaseFragment(@LayoutRes resource: Int): Fragment(resource), View.OnClickListener{

    val logger: Logger by inject()

    // Fixes meizu phones error on editText hints
    protected fun haxMeizu(vararg editTexts: TextInputEditText) {
        val manufacturer = Build.MANUFACTURER.uppercase(Locale.US)
        if (manufacturer.contains("MEIZU")) {
            for (editText in editTexts) {
                editText.setHintTextColor(Color.TRANSPARENT)
                editText.hint = editText.hint
            }
        }
    }

    override fun onClick(v: View?) {

    }

    open fun onBackPressed(){
        logger.logExecution("onBackPressedFragment")
        findNavController().popBackStack()
    }

}