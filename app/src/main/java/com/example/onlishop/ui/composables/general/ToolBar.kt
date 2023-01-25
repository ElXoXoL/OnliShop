package com.example.onlishop.ui.composables.general

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.onlishop.models.IconAction
import com.example.onlishop.ui.composables.BackgroundColor

@Composable
fun ToolBar(
    title: String,
    modifier: Modifier = Modifier,
    contentColor: Color = Color.Black,
    navigationAction: IconAction? = null,
    actions: List<IconAction> = emptyList(),
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
        backgroundColor = BackgroundColor,
        contentColor = contentColor,
        elevation = 0.dp,
        navigationIcon = if (navigationAction == null) null else {
            {
                IconActionButton(action = navigationAction)
            }
        },
        actions = {
            actions.forEach { action ->
                IconActionButton(action = action)
            }
        }
    )
}

@Composable
fun IconActionButton(
    action: IconAction,
    modifier: Modifier = Modifier,
    tintColor: Color = Color.Black
) {
    IconButton(onClick = action.action, modifier = modifier) {
        Icon(
            painter = painterResource(id = action.icon),
            contentDescription = "",
            tint = tintColor
        )
    }
}