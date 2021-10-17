package com.example.onlishop.global

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.bumptech.glide.request.RequestOptions
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