package com.example.onlishop.ui.composables.general

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlishop.ui.composables.BackgroundColor

@Composable
fun LineTextSpacer(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    backgroundColor: Color = BackgroundColor,
) {
    BoxWithConstraints(
        modifier = modifier,
    ) {
        val centerHeight = remember {
            constraints.maxHeight * 0.5f
        }
        val strokeWidth = remember {
            1.dp
        }
        val circleRadius = remember {
            constraints.maxHeight * 0.2f
        }
        Canvas(modifier = modifier) {
            drawLine(
                color = color,
                start = Offset(0f, centerHeight),
                end = Offset(constraints.maxWidth.toFloat(), centerHeight),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
            drawCircle(
                color = backgroundColor,
                radius = circleRadius,
                center = Offset(constraints.maxWidth * 0.2f, centerHeight),
            )
            drawCircle(
                color = color,
                radius = circleRadius,
                center = Offset(constraints.maxWidth * 0.2f, centerHeight),
                style = Stroke(
                    width = strokeWidth.toPx()
                )
            )
            drawCircle(
                color = backgroundColor,
                radius = circleRadius,
                center = Offset(constraints.maxWidth * 0.8f, centerHeight),
            )
            drawCircle(
                color = color,
                radius = circleRadius,
                center = Offset(constraints.maxWidth * 0.8f, centerHeight),
                style = Stroke(
                    width = strokeWidth.toPx()
                )
            )
        }

        Text(
            text = text,
            modifier = Modifier
                .fillMaxHeight()
                .background(color = backgroundColor)
                .align(Alignment.Center)
                .padding(horizontal = 10.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }

}