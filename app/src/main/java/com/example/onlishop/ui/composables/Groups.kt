package com.example.onlishop.ui.composables

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlishop.models.Group

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GroupsRow(
    listItems: List<Group>,
    modifier: Modifier = Modifier,
    cardColorDefault: Color = MainColor,
    cardColorSelected: Color = DarkColor,
    textColorDefault: Color = Color.Black,
    textColorSelected: Color = BackgroundColor,
    onItemClicked: (item: Group) -> Unit = {},
) {
    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(end = 20.dp)
    ) {
        items(listItems, key = {
            it.id
        }) {
            ItemGroup(
                item = it,
                modifier = Modifier.animateItemPlacement(),
                cardColorDefault = cardColorDefault,
                cardColorSelected = cardColorSelected,
                textColorDefault = textColorDefault,
                textColorSelected = textColorSelected,
                onItemClicked = onItemClicked
            )
        }
    }
}

@Composable
fun ItemGroup(
    item: Group,
    modifier: Modifier = Modifier,
    cardColorDefault: Color = MainColor,
    cardColorSelected: Color = DarkColor,
    textColorDefault: Color = Color.Black,
    textColorSelected: Color = BackgroundColor,
    onItemClicked: (item: Group) -> Unit = {},
) {
    val colorCardState by animateColorAsState(
        targetValue = if (item.isSelected) cardColorSelected else cardColorDefault,
        animationSpec = tween(
            durationMillis = 500
        )
    )
    val textColorState by animateColorAsState(
        targetValue = if (item.isSelected) textColorSelected else textColorDefault,
        animationSpec = tween(
            durationMillis = 500
        )
    )

    Column(
        modifier = modifier
            .size(width = 120.dp, height = 80.dp)
            .padding(start = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorCardState)
            .clickable {
                onItemClicked(item)
            },
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = item.name,
            color = textColorState,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }

}