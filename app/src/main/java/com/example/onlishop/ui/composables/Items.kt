package com.example.onlishop.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.onlishop.global.signed
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemsColumn(
    listItems: List<Item>,
    modifier: Modifier = Modifier,
    onItemClicked: (item: Item) -> Unit = {},
) {
    if (listItems.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.empty_items),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        items(listItems, key = { it.id }) {
            ItemItem(
                item = it,
                modifier = Modifier.animateItemPlacement(),
                onItemClicked = onItemClicked
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemItem(
    item: Item,
    modifier: Modifier = Modifier,
    onItemClicked: (item: Item) -> Unit = {},
) {

    Row(
        modifier = modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .clipToBounds()
            .background(DarkColor)
            .clickable {
                onItemClicked(item)
            }
    ) {
        GlideImage(
            model = item.imageDrawable,
            modifier = Modifier
                .width(100.dp)
                .fillMaxHeight(),
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = item.description,
                color = WhiteTransColor,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.align(Alignment.End),
                text = item.price.signed,
                color = LightMainColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }

}