package com.example.onlishop.global

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.example.onlishop.app.App
import com.example.onlishop.app.glide.GlideApp
import com.google.android.play.core.tasks.Task
import com.google.android.play.core.tasks.Tasks
import java.util.concurrent.TimeUnit
import kotlin.Exception

/**
 * My favourite extensions for easy work
 */

fun ImageView.load(@DrawableRes resId: Int?){
    GlideApp.with(App.instance)
        .load(resId)
        .into(this)
}

fun Context.drawable(@DrawableRes resId: Int): Drawable?{
    return ContextCompat.getDrawable(this, resId)
}

fun Context.color(@ColorRes resId: Int): Int{
    return ContextCompat.getColor(this, resId)
}

fun <T> Task<T>.await(): T{
    return Tasks.await(this, 10, TimeUnit.SECONDS)
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.dp: Float
    get() = this / Resources.getSystem().displayMetrics.density

val Float.px: Float
    get() = this * Resources.getSystem().displayMetrics.density

fun View.hideKeyboard(){
    try {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
        this.requestFocus()
    } catch (e: Exception) {
        Log.d("Keyboard", e.toString())
    }
}

fun EditText.hideKeyboard(){
    try {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
        this.clearFocus()
    } catch (e: Exception) {
        Log.d("Keyboard", e.toString())
    }
}

fun EditText.showKeyboard(){
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

val View.inflater: LayoutInflater
    get() = LayoutInflater.from(context)

// Fast toast short
fun String?.toast(){
    Toast.makeText(App.instance, this, Toast.LENGTH_SHORT).show()
}

// Fast toast long
fun String?.toastLong(){
    Toast.makeText(App.instance, this, Toast.LENGTH_LONG).show()
}

val String?.formattedPhone: String
    get() {
        if (this.isNullOrEmpty()) return ""
        var formattedPhone = ""
        var counter = 0
        this.forEach {
            formattedPhone += it
            when (counter) {
                2, 5, 7 -> {
                    formattedPhone += " "
                }
            }
            counter++
        }
        formattedPhone = formattedPhone.trimEnd()
        return formattedPhone
    }

val String?.cardMasked: String
    get() {
        if (this.isNullOrEmpty()) return ""
        return try {
            "**** ${substring(length - 4)}"
        } catch (e: Exception) {
            ""
        }
    }

val String?.signed: String
    get() {
        if (this.isNullOrEmpty()) return ""
        return "$this ₴"
    }

val Int?.signed: String
    get() {
        if (this == null) return ""
        return "$this ₴"
    }

fun Int.getDiscountText(itemsCount: Int): String {
    if (itemsCount <= 1) return ""

    val discount = when (itemsCount) {
        0, 1 -> 1.0
        2 -> 0.05
        3 -> 0.1
        4 -> 0.15
        else -> 0.2
    }

    val discountValue = (this * discount).toInt()

    return "Full price - ${this.signed}; Discount - ${(discount * 100).toInt()} % (-${discountValue.signed})"
}

fun Int.getAppliedDiscount(itemsCount: Int): Int {
    if (itemsCount <= 1) return this

    val discount = when (itemsCount) {
        0, 1 -> 1.0
        2 -> 0.05
        3 -> 0.1
        4 -> 0.15
        else -> 0.2
    }

    val discountValue = (this * discount).toInt()

    return this - discountValue
}