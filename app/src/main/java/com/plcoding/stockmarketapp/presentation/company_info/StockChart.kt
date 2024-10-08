package com.plcoding.stockmarketapp.presentation.company_info

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.stockmarketapp.domain.model.IntraDayInfoModel
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun StockChart(
    modifier: Modifier = Modifier,
    graphicColor: Color,
    intraDayInfo: List<IntraDayInfoModel>
) {
    val spacing = 100f

    val transParentGraphColor = remember(graphicColor) {
        graphicColor.copy(alpha = 0.5f)
    }

    val upperValue = remember(intraDayInfo) {
        (intraDayInfo.maxOfOrNull {
            it.close
        }?.plus(1))?.roundToInt() ?: 0
    }

    val lowerValue = remember(intraDayInfo) {
        (intraDayInfo.minOfOrNull {
            it.close
        }?.minus(1))?.roundToInt() ?: 0
    }

    val density = LocalDensity.current

    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(
        modifier = modifier,
    ) {
        val spacePerHour = (size.width - spacing) / intraDayInfo.size
        (0 until  intraDayInfo.size - 1 step 2).forEach { i->
            val info = intraDayInfo[i]
            val hour = info.date.hour

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(),
                    spacing + i * spacePerHour,
                    size.height - 5,
                    textPaint
                )
            }
        }

        val priceStep = (upperValue - lowerValue)/5
        (0..4).forEach { i->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    round((lowerValue + priceStep * i).toDouble()).toString(),
                    30f,
                    size.height - spacing - i * size.height / 5f,
                    textPaint
                )
            }
        }

        var lastX = 0f

        val strokePath = Path().apply {
            val height = size.height
            for(i in intraDayInfo.indices) {
                val info = intraDayInfo[i]
                val nextInfo = intraDayInfo.getOrNull(i + 1) ?: intraDayInfo.last()
                val leftRatio = (info.close - lowerValue) / (upperValue - lowerValue)
                val rightRatio = (nextInfo.close - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (leftRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (rightRatio * height).toFloat()
                if(i == 0) {
                    moveTo(x1, y1)
                }
                lastX = (x1 + x2) /2f
                quadraticBezierTo(
                    x1, y1, (x1 + x2)/2f, (y1 + y2)/2f
                )
            }
        }

        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
            lineTo(lastX, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                listOf(
                    transParentGraphColor,
                    Color.Transparent,
                ),
                endY = size.height - spacing
            )
        )

        drawPath(
            path = strokePath,
            color = graphicColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}