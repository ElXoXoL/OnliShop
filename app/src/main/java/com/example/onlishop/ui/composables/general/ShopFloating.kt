package com.example.onlishop.ui.composables.general

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlishop.R
import com.example.onlishop.models.IconAction
import com.example.onlishop.ui.composables.MainColor

@Composable
fun ShopFloating(
    bagSize: Int,
    modifier: Modifier = Modifier,
    onShopClick: () -> Unit = {}
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = bagSize > 0,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            IconActionButton(
                action = IconAction(R.drawable.ic_bag, onShopClick),
                modifier = Modifier
                    .size(65.dp)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background(MainColor)
            )

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .padding(end = 2.dp, top = 2.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(MainColor)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = bagSize.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
            }

        }
    }
}