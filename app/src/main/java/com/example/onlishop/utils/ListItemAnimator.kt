package com.example.onlishop.utils

import android.content.Context
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.onlishop.R
import com.example.onlishop.global.Logger

class ListItemAnimator(private val logger: Logger, private val context: Context) : SimpleItemAnimator() {

    private val animation = AnimationUtils.loadAnimation(context, R.anim.item_anim_fall_from_right_small)

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder?,
        newHolder: RecyclerView.ViewHolder?,
        fromLeft: Int,
        fromTop: Int,
        toLeft: Int,
        toTop: Int
    ): Boolean {
        logger.logDev("animateChange")
        return true
    }

    override fun runPendingAnimations() {
        logger.logDev("runPendingAnimations")
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        item.itemView.clearAnimation()
        logger.logDev("endAnimation")
    }

    override fun endAnimations() {
        logger.logDev("endAnimations")
    }

    override fun isRunning(): Boolean {
        logger.logDev("isRunning")
        return false
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        logger.logDev("animateRemove")
        return true
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        logger.logDev("animateAdd")
        holder?.itemView?.startAnimation(animation)
        return true
    }

    override fun animateMove(
        holder: RecyclerView.ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        logger.logDev("animateMove")
        return true
    }

}