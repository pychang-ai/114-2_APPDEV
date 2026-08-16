package com.example.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MarineAnimalIllustration(
    key: String,
    modifier: Modifier = Modifier
) {
    // Elegant swaying & bubble swimming animations
    val infiniteTransition = rememberInfiniteTransition(label = "marine_swim")
    
    val swayOffset by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sway"
    )

    val scaleSway by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2
            val centerY = height / 2
            val radius = minOf(width, height) * 0.45f

            // Dynamic Ocean Water Background Circle
            val ambientGradient = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF0D324D), // Bio-neon teal center
                    Color(0xFF031625)  // Near-black edges
                ),
                center = Offset(centerX, centerY),
                radius = radius
            )
            drawCircle(brush = ambientGradient, radius = radius, center = Offset(centerX, centerY))
            drawCircle(
                color = Color(0xFF00E5FF).copy(alpha = 0.25f),
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 3f)
            )

            // Draw Background atmospheric micro-bubbles
            val bubbleTime = (swayOffset + 5) / 10f
            drawCircle(
                color = Color(0x3300E5FF),
                radius = 6f,
                center = Offset(centerX - radius * 0.4f, centerY - radius * 0.5f + bubbleTime * 20f)
            )
            drawCircle(
                color = Color(0x22FFFFFF),
                radius = 10f,
                center = Offset(centerX + radius * 0.5f, centerY + radius * 0.2f - bubbleTime * 15f)
            )
            drawCircle(
                color = Color(0x4400E5FF),
                radius = 4f,
                center = Offset(centerX - radius * 0.1f, centerY + radius * 0.4f + bubbleTime * 12f)
            )

            // Dynamic coordinate transformation
            val drawCenterX = centerX + swayOffset
            val drawCenterY = centerY + (swayOffset * 0.4f)
            
            // Draw specific marine target
            when (key) {
                "blue_whale" -> drawBlueWhale(drawCenterX, drawCenterY, radius, scaleSway)
                "shark" -> drawGreatWhiteShark(drawCenterX, drawCenterY, radius, scaleSway)
                "clownfish" -> drawClownfish(drawCenterX, drawCenterY, radius, scaleSway)
                "turtle" -> drawGreenTurtle(drawCenterX, drawCenterY, radius, scaleSway)
                "octopus" -> drawOctopus(drawCenterX, drawCenterY, radius, scaleSway)
                "penguin" -> drawPenguin(drawCenterX, drawCenterY, radius, scaleSway)
                "dolphin" -> drawDolphin(drawCenterX, drawCenterY, radius, scaleSway)
                "manta_ray" -> drawMantaRay(drawCenterX, drawCenterY, radius, scaleSway)
                "lionfish" -> drawLionfish(drawCenterX, drawCenterY, radius, scaleSway)
                "seahorse" -> drawSeahorse(drawCenterX, drawCenterY, radius, scaleSway)
                else -> {
                    // Default fallback: glowing shell
                    drawCircle(
                        color = Color(0xFFFF4081),
                        radius = radius * 0.3f,
                        center = Offset(drawCenterX, drawCenterY)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawBlueWhale(cx: Float, cy: Float, r: Float, scale: Float) {
    val bodyW = r * 1.1f * scale
    val bodyH = r * 0.45f
    val path = Path().apply {
        // Smooth curved body of the whale
        moveTo(cx - bodyW * 0.5f, cy + bodyH * 0.05f)
        cubicTo(
            cx - bodyW * 0.2f, cy - bodyH * 0.5f,
            cx + bodyW * 0.2f, cy - bodyH * 0.4f,
            cx + bodyW * 0.4f, cy - bodyH * 0.1f
        )
        // Tail transition
        lineTo(cx + bodyW * 0.6f, cy - bodyH * 0.2f)
        lineTo(cx + bodyW * 0.65f, cy - bodyH * 0.35f)
        lineTo(cx + bodyW * 0.7f, cy - bodyH * 0.1f)
        lineTo(cx + bodyW * 0.65f, cy + bodyH * 0.15f)
        lineTo(cx + bodyW * 0.6f, cy + bodyH * 0.05f)
        // Belly & Chin
        cubicTo(
            cx + bodyW * 0.1f, cy + bodyH * 0.45f,
            cx - bodyW * 0.3f, cy + bodyH * 0.35f,
            cx - bodyW * 0.5f, cy + bodyH * 0.05f
        )
        close()
    }

    // Gradient blue for whale body
    drawPath(
        path = path,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFF2A80B9), Color(0xFF1B4F72)),
            start = Offset(cx - bodyW * 0.5f, cy),
            end = Offset(cx + bodyW * 0.5f, cy)
        )
    )

    // Fin path
    val finPath = Path().apply {
        moveTo(cx - bodyW * 0.05f, cy + bodyH * 0.12f)
        lineTo(cx - bodyW * 0.15f, cy + bodyH * 0.45f)
        lineTo(cx + bodyW * 0.05f, cy + bodyH * 0.2f)
        close()
    }
    drawPath(finPath, Color(0xFF1B4F72))

    // Eye
    drawCircle(
        color = Color.White,
        radius = r * 0.03f,
        center = Offset(cx - bodyW * 0.38f, cy - bodyH * 0.08f)
    )
    drawCircle(
        color = Color(0xFF031625),
        radius = r * 0.015f,
        center = Offset(cx - bodyW * 0.39f, cy - bodyH * 0.08f)
    )

    // Spouting water particles
    val spoutY = cy - bodyH * 0.35f
    val spoutX = cx - bodyW * 0.15f
    drawLine(Color(0xBB00E5FF), Offset(spoutX, spoutY), Offset(spoutX - 10f, spoutY - 30f), strokeWidth = 4f)
    drawLine(Color(0xBB00E5FF), Offset(spoutX, spoutY), Offset(spoutX, spoutY - 36f), strokeWidth = 5f)
    drawLine(Color(0xBB00E5FF), Offset(spoutX, spoutY), Offset(spoutX + 10f, spoutY - 26f), strokeWidth = 4f)
}

private fun DrawScope.drawGreatWhiteShark(cx: Float, cy: Float, r: Float, scale: Float) {
    val w = r * 1.1f * scale
    val h = r * 0.5f
    
    // Main Body (Torpedo streamlined)
    val body = Path().apply {
        moveTo(cx - w * 0.55f, cy - h * 0.02f) // Nose
        cubicTo(
            cx - w * 0.2f, cy - h * 0.5f,
            cx + w * 0.2f, cy - h * 0.25f,
            cx + w * 0.45f, cy - h * 0.02f
        )
        // Tail fin
        lineTo(cx + w * 0.65f, cy - h * 0.4f)
        lineTo(cx + w * 0.55f, cy)
        lineTo(cx + w * 0.65f, cy + h * 0.35f)
        lineTo(cx + w * 0.4f, cy + h * 0.05f)
        // Lower belly
        cubicTo(
            cx + w * 0.1f, cy + h * 0.25f,
            cx - w * 0.2f, cy + h * 0.15f,
            cx - w * 0.55f, cy - h * 0.02f
        )
        close()
    }
    drawPath(
        path = body,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF5D6D7E), Color(0xFFBDC3C7)),
            startY = cy - h * 0.3f,
            endY = cy + h * 0.15f
        )
    )

    // Dorsal Fin (The classic triangle atop)
    val dorsal = Path().apply {
        moveTo(cx - w * 0.05f, cy - h * 0.32f)
        cubicTo(
            cx - w * 0.02f, cy - h * 0.65f,
            cx + w * 0.08f, cy - h * 0.65f,
            cx + w * 0.1f, cy - h * 0.6f
        )
        lineTo(cx + w * 0.12f, cy - h * 0.24f)
        close()
    }
    drawPath(dorsal, Color(0xFF424949))

    // Pectoral fin
    val pectoral = Path().apply {
        moveTo(cx - w * 0.15f, cy + h * 0.05f)
        lineTo(cx - w * 0.25f, cy + h * 0.5f)
        lineTo(cx - w * 0.02f, cy + h * 0.18f)
        close()
    }
    drawPath(pectoral, Color(0xFF424949))

    // Lethal eye and gills
    drawCircle(Color.Red, radius = r * 0.02f, center = Offset(cx - w * 0.4f, cy - h * 0.12f))
    drawCircle(Color.White, radius = r * 0.008f, center = Offset(cx - w * 0.395f, cy - h * 0.13f))

    // Gill slits
    val gillX = cx - w * 0.26f
    drawLine(Color(0xFF2C3E50), Offset(gillX, cy - 15f), Offset(gillX, cy + 15f), strokeWidth = 3f)
    drawLine(Color(0xFF2C3E50), Offset(gillX + 12f, cy - 12f), Offset(gillX + 12f, cy + 12f), strokeWidth = 3f)
    drawLine(Color(0xFF2C3E50), Offset(gillX + 24f, cy - 10f), Offset(gillX + 24f, cy + 10f), strokeWidth = 3f)
}

private fun DrawScope.drawClownfish(cx: Float, cy: Float, r: Float, scale: Float) {
    val w = r * 0.95f * scale
    val h = r * 0.55f

    // Primary bright coral orange body
    val body = Path().apply {
        moveTo(cx - w * 0.45f, cy)
        cubicTo(
            cx - w * 0.25f, cy - h * 0.6f,
            cx + w * 0.15f, cy - h * 0.5f,
            cx + w * 0.42f, cy - h * 0.1f
        )
        // Tail peduncle
        lineTo(cx + w * 0.48f, cy - h * 0.22f)
        cubicTo(
            cx + w * 0.65f, cy - h * 0.38f,
            cx + w * 0.65f, cy + h * 0.38f,
            cx + w * 0.48f, cy + h * 0.22f
        )
        lineTo(cx + w * 0.42f, cy + h * 0.10f)
        cubicTo(
            cx + w * 0.15f, cy + h * 0.55f,
            cx - w * 0.25f, cy + h * 0.55f,
            cx - w * 0.45f, cy
        )
        close()
    }
    drawPath(body, Color(0xFFFF6F00)) // Safety Orange

    // Draw White Vertical Bands
    val bands = listOf(
        // Band 1: Head/neck
        listOf(cx - w * 0.15f, cx - w * 0.28f),
        // Band 2: Middle thick ribbon with a slight curve
        listOf(cx + w * 0.05f, cx - w * 0.05f),
        // Band 3: Near tail
        listOf(cx + w * 0.32f, cx + w * 0.42f)
    )

    for (b in bands) {
        val stripePath = Path().apply {
            moveTo(b[0], cy - h * 0.5f)
            lineTo(b[1], cy - h * 0.5f)
            lineTo(b[1], cy + h * 0.5f)
            lineTo(b[0], cy + h * 0.5f)
            close()
        }
        // Clip to fish body
        drawPath(stripePath, Color.White)
    }

    // Black outlines for stripes
    for (b in bands) {
        drawLine(Color.Black, Offset(b[0], cy - h * 0.45f), Offset(b[0], cy + h * 0.45f), strokeWidth = 5f)
        drawLine(Color.Black, Offset(b[1], cy - h * 0.45f), Offset(b[1], cy + h * 0.45f), strokeWidth = 5f)
    }

    // Outlined dorsal fin
    drawArc(
        color = Color(0xFFFFE082),
        startAngle = 180f,
        sweepAngle = 130f,
        useCenter = false,
        topLeft = Offset(cx - w * 0.25f, cy - h * 0.62f),
        size = Size(w * 0.6f, h * 0.5f)
    )

    // Eye
    drawCircle(Color.White, radius = r * 0.05f, center = Offset(cx - w * 0.32f, cy - h * 0.1f))
    drawCircle(Color.Black, radius = r * 0.025f, center = Offset(cx - w * 0.31f, cy - h * 0.1f))
}

private fun DrawScope.drawGreenTurtle(cx: Float, cy: Float, r: Float, scale: Float) {
    val shellRadius = r * 0.45f * scale

    // Draw Head
    val headX = cx + shellRadius * 1.1f
    val headY = cy - shellRadius * 0.2f
    drawCircle(Color(0xFF3B7A57), radius = shellRadius * 0.28f, center = Offset(headX, headY))
    
    // Turtle eye
    drawCircle(Color(0xFF1C2833), radius = 5f, center = Offset(headX + 10f, headY - 8f))

    // Flippers (Front and Back)
    val frontFlipper = Path().apply {
        moveTo(cx + shellRadius * 0.4f, cy - shellRadius * 0.2f)
        cubicTo(
            cx + shellRadius * 0.9f, cy - shellRadius * 0.8f,
            cx + shellRadius * 1.1f, cy - shellRadius * 0.5f,
            cx + shellRadius * 0.6f, cy + shellRadius * 0.1f
        )
        close()
    }
    drawPath(frontFlipper, Color(0xFF2D5A27))

    val backFlipper = Path().apply {
        moveTo(cx - shellRadius * 0.5f, cy + shellRadius * 0.2f)
        lineTo(cx - shellRadius * 0.9f, cy + shellRadius * 0.6f)
        lineTo(cx - shellRadius * 0.3f, cy + shellRadius * 0.5f)
        close()
    }
    drawPath(backFlipper, Color(0xFF214E1F))

    // Main carapace (shell)
    val carapaceGradient = Brush.radialGradient(
        colors = listOf(Color(0xFF2E7D32), Color(0xFF1B5E20)),
        center = Offset(cx, cy),
        radius = shellRadius
    )
    drawCircle(brush = carapaceGradient, radius = shellRadius, center = Offset(cx, cy))

    // Shell geometric plates (hexagons pattern drawing)
    drawCircle(
        color = Color(0xFFA1E73B).copy(alpha = 0.35f),
        radius = shellRadius,
        center = Offset(cx, cy),
        style = Stroke(width = 6f)
    )

    // Simple shell mosaic lines
    drawLine(Color(0xBB1B5E20), Offset(cx, cy - shellRadius), Offset(cx, cy + shellRadius), strokeWidth = 4f)
    drawLine(Color(0xBB1B5E20), Offset(cx - shellRadius, cy), Offset(cx + shellRadius, cy), strokeWidth = 4f)
}

private fun DrawScope.drawOctopus(cx: Float, cy: Float, r: Float, scale: Float) {
    val headW = r * 0.5f * scale
    val headH = r * 0.45f

    // Tentacles (Sinuous curves under head)
    val tColor = Color(0xFF9B59B6) // Purple Octopus
    val tStroke = Stroke(width = 16f, cap = StrokeCap.Round)
    
    // Draw 6 visible waving paths
    for (i in 0..5) {
        val angle = (i * 30f + 35f) * (PI / 180f)
        val endX = cx + (r * 0.8f) * cos(angle).toFloat()
        val endY = cy + (hOffset(i, scale) + r * 0.65f) * sin(angle).toFloat()
        
        val tentacle = Path().apply {
            moveTo(cx + (headW * 0.2f * (i - 2.5f)), cy + headH * 0.1f)
            cubicTo(
                cx + (headW * 0.4f * (i - 2.5f)), cy + headH * 0.6f,
                (cx + endX) / 2f + (swayOffset(i) * 6f), (cy + endY) / 2f,
                endX, endY
            )
        }
        drawPath(tentacle, tColor, style = tStroke)
        // Little yellow suckers along tentacles
        drawCircle(Color(0xFFF1C40F), radius = 5f, center = Offset(endX, endY))
    }

    // Octopus Crown Head
    val headRect = Rect(cx - headW * 0.5f, cy - headH * 0.6f, cx + headW * 0.5f, cy + headH * 0.2f)
    val headPath = Path().apply {
        addOval(headRect)
    }
    drawPath(
        path = headPath,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFD7BDE2), Color(0xFF8E44AD)),
            startY = cy - headH * 0.6f,
            endY = cy + headH * 0.2f
        )
    )

    // Big localized alien eyes
    drawCircle(Color.White, radius = r * 0.07f, center = Offset(cx - headW * 0.2f, cy + headH * 0.1f))
    drawCircle(Color.White, radius = r * 0.07f, center = Offset(cx + headW * 0.2f, cy + headH * 0.1f))
    drawCircle(Color(0xFF1A1A54), radius = r * 0.045f, center = Offset(cx - headW * 0.18f, cy + headH * 0.12f))
    drawCircle(Color(0xFF1A1A54), radius = r * 0.045f, center = Offset(cx + headW * 0.22f, cy + headH * 0.12f))
}

private fun hOffset(i: Int, scale: Float): Float = if (i % 2 == 0) scale * 20f else -scale * 10f
private fun swayOffset(i: Int): Float = if (i % 3 == 0) 4f else -3f

private fun DrawScope.drawPenguin(cx: Float, cy: Float, r: Float, scale: Float) {
    val bodyRadius = r * 0.42f * scale
    
    // Foot pads
    drawCircle(Color(0xFFF39C12), radius = 18f, center = Offset(cx - bodyRadius * 0.4f, cy + bodyRadius * 1.0f))
    drawCircle(Color(0xFFF39C12), radius = 18f, center = Offset(cx + bodyRadius * 0.4f, cy + bodyRadius * 1.0f))

    // Main Black Coat Oval
    drawOval(
        color = Color(0xFF2C3E50),
        topLeft = Offset(cx - bodyRadius * 0.8f, cy - bodyRadius * 1.0f),
        size = Size(bodyRadius * 1.6f, bodyRadius * 2.1f)
    )

    // White belly
    drawOval(
        color = Color.White,
        topLeft = Offset(cx - bodyRadius * 0.5f, cy - bodyRadius * 0.6f),
        size = Size(bodyRadius * 1.0f, bodyRadius * 1.5f)
    )

    // Warm Orange Neck Patch
    val neckPatch = Path().apply {
        moveTo(cx - bodyRadius * 0.35f, cy - bodyRadius * 0.5f)
        cubicTo(
            cx - bodyRadius * 0.1f, cy - bodyRadius * 0.5f,
            cx + bodyRadius * 0.1f, cy - bodyRadius * 0.5f,
            cx + bodyRadius * 0.35f, cy - bodyRadius * 0.5f
        )
        lineTo(cx, cy - bodyRadius * 0.1f)
        close()
    }
    drawPath(neckPatch, Color(0xFFF39C12))

    // Flipper wings
    val leftFlipper = Path().apply {
        moveTo(cx - bodyRadius * 0.72f, cy - bodyRadius * 0.4f)
        cubicTo(
            cx - bodyRadius * 1.2f, cy - bodyRadius * 0.1f,
            cx - bodyRadius * 1.1f, cy + bodyRadius * 0.4f,
            cx - bodyRadius * 0.75f, cy + bodyRadius * 0.3f
        )
        close()
    }
    drawPath(leftFlipper, Color(0xFF1F2F3D))

    val rightFlipper = Path().apply {
        moveTo(cx + bodyRadius * 0.72f, cy - bodyRadius * 0.4f)
        cubicTo(
            cx + bodyRadius * 1.2f, cy - bodyRadius * 0.1f,
            cx + bodyRadius * 1.1f, cy + bodyRadius * 0.4f,
            cx + bodyRadius * 0.75f, cy + bodyRadius * 0.3f
        )
        close()
    }
    drawPath(rightFlipper, Color(0xFF1F2F3D))

    // Yellow beak
    val beak = Path().apply {
        moveTo(cx - 10f, cy - bodyRadius * 0.75f)
        lineTo(cx + 34f, cy - bodyRadius * 0.7f)
        lineTo(cx - 10f, cy - bodyRadius * 0.65f)
        close()
    }
    drawPath(beak, Color(0xFFF1C40F))

    // Small eye
    drawCircle(Color.White, radius = 6f, center = Offset(cx - 12f, cy - bodyRadius * 0.8f))
    drawCircle(Color.Black, radius = 3f, center = Offset(cx - 10f, cy - bodyRadius * 0.8f))
}

private fun DrawScope.drawDolphin(cx: Float, cy: Float, r: Float, scale: Float) {
    val w = r * 1.15f * scale
    val h = r * 0.5f

    // Sleek jumping arc body
    val body = Path().apply {
        moveTo(cx - w * 0.45f, cy + h * 0.25f) // Tail
        cubicTo(
            cx - w * 0.2f, cy - h * 0.62f,
            cx + w * 0.2f, cy - h * 0.52f,
            cx + w * 0.48f, cy - h * 0.05f  // Head
        )
        // Beak nose
        lineTo(cx + w * 0.58f, cy - h * 0.02f)
        lineTo(cx + w * 0.45f, cy + h * 0.12f)
        // Belly back to tail
        cubicTo(
            cx + w * 0.12f, cy + h * 0.35f,
            cx - w * 0.2f, cy + h * 0.55f,
            cx - w * 0.4f, cy + h * 0.35f
        )
        close()
    }
    drawPath(
        path = body,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFF5dade2), Color(0xFF2874a6)),
            start = Offset(cx - w * 0.45f, cy),
            end = Offset(cx + w * 0.45f, cy)
        )
    )

    // Curved Tail fin
    val tail = Path().apply {
        moveTo(cx - w * 0.42f, cy + h * 0.28f)
        lineTo(cx - w * 0.58f, cy + h * 0.1f)
        lineTo(cx - w * 0.52f, cy + h * 0.36f)
        lineTo(cx - w * 0.58f, cy + h * 0.6f)
        close()
    }
    drawPath(tail, Color(0xFF1b4f72))

    // Flippy dorsal fin
    val dorsal = Path().apply {
        moveTo(cx - w * 0.02f, cy - h * 0.36f)
        cubicTo(
            cx - w * 0.05f, cy - h * 0.58f,
            cx + w * 0.06f, cy - h * 0.58f,
            cx + w * 0.04f, cy - h * 0.34f
        )
        close()
    }
    drawPath(dorsal, Color(0xFF21618c))

    // Eye
    drawCircle(Color.White, radius = 8f, center = Offset(cx + w * 0.38f, cy - h * 0.10f))
    drawCircle(Color.Black, radius = 4f, center = Offset(cx + w * 0.39f, cy - h * 0.10f))
}

private fun DrawScope.drawMantaRay(cx: Float, cy: Float, r: Float, scale: Float) {
    val wingW = r * 1.15f * scale
    val wingH = r * 0.65f

    // Master Diamond Wings Path
    val body = Path().apply {
        moveTo(cx, cy - wingH * 0.35f) // Front Head
        // Left Wingtip
        cubicTo(
            cx - wingW * 0.5f, cy - wingH * 0.3f,
            cx - wingW * 0.55f, cy - wingH * 0.1f,
            cx - wingW * 0.58f, cy + wingH * 0.05f
        )
        // Back left boundary
        cubicTo(
            cx - wingW * 0.32f, cy + wingH * 0.28f,
            cx - wingW * 0.15f, cy + wingH * 0.35f,
            cx, cy + wingH * 0.25f // Tail base
        )
        // Back right boundary
        cubicTo(
            cx + wingW * 0.15f, cy + wingH * 0.35f,
            cx + wingW * 0.32f, cy + wingH * 0.28f,
            cx + wingW * 0.58f, cy + wingH * 0.05f
        )
        // Right Wingtip
        cubicTo(
            cx + wingW * 0.55f, cy - wingH * 0.1f,
            cx + wingW * 0.50f, cy - wingH * 0.3f,
            cx, cy - wingH * 0.35f // Head
        )
        close()
    }
    
    // Elegant Dark Slate Blue to Ocean Teal Gradient Brush
    drawPath(
        path = body,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF2C3E50), Color(0xFF111E2E)),
            startY = cy - wingH * 0.3f,
            endY = cy + wingH * 0.3f
        )
    )

    // Distinct cephalic horns (two feelers at front of head)
    val leftHorn = Path().apply {
        moveTo(cx - wingW * 0.08f, cy - wingH * 0.3f)
        cubicTo(
            cx - wingW * 0.12f, cy - wingH * 0.5f,
            cx - wingW * 0.05f, cy - wingH * 0.5f,
            cx - wingW * 0.03f, cy - wingH * 0.34f
        )
        close()
    }
    val rightHorn = Path().apply {
        moveTo(cx + wingW * 0.08f, cy - wingH * 0.3f)
        cubicTo(
            cx + wingW * 0.12f, cy - wingH * 0.5f,
            cx + wingW * 0.05f, cy - wingH * 0.5f,
            cx + wingW * 0.03f, cy - wingH * 0.34f
        )
        close()
    }
    drawPath(leftHorn, Color(0xFF1F2E3E))
    drawPath(rightHorn, Color(0xFF1F2E3E))

    // Whiplash thin tail
    drawLine(
        color = Color(0xFF0E1A24),
        start = Offset(cx, cy + wingH * 0.25f),
        end = Offset(cx - 20f, cy + wingH * 0.85f),
        strokeWidth = 4f
    )
}

private fun DrawScope.drawLionfish(cx: Float, cy: Float, r: Float, scale: Float) {
    val sizeW = r * 0.72f * scale
    val sizeH = r * 0.45f
    
    // Draw 14 radiating venomous long needle spines from center (back and side)
    val spineColor = Color(0xFFE74C3C)
    for (i in 0..11) {
        val sweepAngle = (i * 30f) * (PI / 180f)
        val spineLength = r * (0.6f + (i % 3) * 0.1f)
        val endX = cx + spineLength * cos(sweepAngle).toFloat()
        val endY = cy + spineLength * sin(sweepAngle).toFloat()
        
        drawLine(
            color = spineColor,
            start = Offset(cx, cy),
            end = Offset(endX, endY),
            strokeWidth = 4f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f))
        )
        // Little white toxin drops
        drawCircle(Color.White, radius = 3.5f, center = Offset(endX, endY))
    }

    // Gilded striped central fish body
    val bodyRect = Rect(cx - sizeW * 0.5f, cy - sizeH * 0.5f, cx + sizeW * 0.5f, cy + sizeH * 0.5f)
    val bodyPath = Path().apply {
        addOval(bodyRect)
    }
    drawPath(
        path = bodyPath,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFFE59866), Color(0xFFB03A2E)),
            start = Offset(cx - sizeW * 0.5f, cy),
            end = Offset(cx + sizeW * 0.5f, cy)
        )
    )

    // Zebra cross stripes
    for (step in -2..2) {
        val stripeX = cx + step * (sizeW * 0.15f)
        drawLine(
            color = Color.White,
            start = Offset(stripeX, cy - sizeH * 0.38f),
            end = Offset(stripeX, cy + sizeH * 0.38f),
            strokeWidth = 8f
        )
    }

    // Wide peacock fans
    drawCircle(
        color = Color(0xFFF1C40F).copy(alpha = 0.5f),
        radius = r * 0.28f,
        center = Offset(cx - sizeW * 0.1f, cy + sizeH * 0.1f),
        style = Stroke(width = 6f)
    )

    // Big predatory eye
    drawCircle(Color.Black, radius = r * 0.05f, center = Offset(cx + sizeW * 0.28f, cy - sizeH * 0.08f))
    drawCircle(Color.White, radius = r * 0.015f, center = Offset(cx + sizeW * 0.29f, cy - sizeH * 0.1f))
}

private fun DrawScope.drawSeahorse(cx: Float, cy: Float, r: Float, scale: Float) {
    val h = r * 0.95f
    val w = r * 0.35f * scale

    // Golden seahorse curvy path (Horse head, bloated belly, spiral tail)
    val color = Color(0xFFF39C12) // Golden Yellow Seahorse
    
    val seahorseBody = Path().apply {
        // Upper Crown head
        moveTo(cx - w * 0.1f, cy - h * 0.45f)
        // Snout nozzle
        lineTo(cx + w * 0.65f, cy - h * 0.35f)
        lineTo(cx + w * 0.55f, cy - h * 0.28f)
        lineTo(cx + w * 0.15f, cy - h * 0.28f)
        // Neck bend
        cubicTo(
            cx - w * 0.38f, cy - h * 0.15f,
            cx - w * 0.22f, cy,
            cx + w * 0.1f, cy + h * 0.1f // Bloating belly
        )
        // Tail curls inward in spiral arcs
        cubicTo(
            cx + w * 0.38f, cy + h * 0.25f,
            cx + w * 0.1f, cy + h * 0.5f,
            cx - w * 0.2f, cy + h * 0.38f
        )
        cubicTo(
            cx - w * 0.28f, cy + h * 0.25f,
            cx, cy + h * 0.22f,
            cx + w * 0.1f, cy + h * 0.28f
        )
        // Inner spiral line end
        lineTo(cx + w * 0.08f, cy + h * 0.3f)
        // Outer belly boundary
        cubicTo(
            cx - w * 0.15f, cy + h * 0.18f,
            cx - w * 0.48f, cy - h * 0.05f,
            cx - w * 0.1f, cy - h * 0.45f
        )
        close()
    }
    drawPath(
        path = seahorseBody,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFF5B041), Color(0xFFD35400)),
            startY = cy - h * 0.45f,
            endY = cy + h * 0.45f
        )
    )

    // Segmented ring ridges along body back
    val ridgeStroke = Stroke(width = 4f)
    for (step in -5..5) {
        val ry = cy + step * (h * 0.07f)
        drawArc(
            color = Color(0xFFF39C12),
            startAngle = 120f,
            sweepAngle = 120f,
            useCenter = false,
            topLeft = Offset(cx - w * 0.32f, ry),
            size = Size(w * 0.48f, 15f),
            style = ridgeStroke
        )
    }

    // Little transparent dorsal fin
    val fin = Path().apply {
        moveTo(cx - w * 0.22f, cy - h * 0.08f)
        lineTo(cx - w * 0.52f, cy - h * 0.02f)
        lineTo(cx - w * 0.22f, cy + h * 0.12f)
        close()
    }
    drawPath(fin, Color(0x9900E5FF))

    // Tiny shiny eye
    drawCircle(Color.White, radius = 6f, center = Offset(cx + w * 0.15f, cy - h * 0.38f))
    drawCircle(Color.Black, radius = 3.5f, center = Offset(cx + w * 0.17f, cy - h * 0.38f))
}
