package com.example.chess.ui.avatar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.model.AvatarCategory
import com.example.chess.model.AvatarItem
import com.example.chess.model.AvatarRepository

@Composable
fun ChessAvatarView(
    avatarId: String,
    size: Dp = 56.dp,
    borderColor: Color = Color(0xFFFFD700),
    showOnlineIndicator: Boolean = false,
    isOnline: Boolean = true,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val avatar = AvatarRepository.getAvatar(avatarId)

    Box(
        modifier = modifier
            .size(size)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow/border ring
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(elevation = 6.dp, shape = CircleShape, ambientColor = borderColor, spotColor = borderColor)
                .clip(CircleShape)
                .border(2.5.dp, borderColor, CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(avatar.secondaryColor),
                            Color(avatar.primaryColor)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            AvatarGraphic(avatar = avatar, modifier = Modifier.fillMaxSize().padding(size * 0.12f))
        }

        // Online status dot
        if (showOnlineIndicator) {
            val statusColor = if (isOnline) Color(0xFF00E676) else Color(0xFF9E9E9E)
            Box(
                modifier = Modifier
                    .size(size * 0.28f)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(statusColor)
                    .border(2.dp, Color(0xFF1E1E2A), CircleShape)
            )
        }
    }
}

@Composable
fun AvatarGraphic(avatar: AvatarItem, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(avatar.secondaryColor),
                        Color(avatar.primaryColor)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        coil.compose.SubcomposeAsyncImage(
            model = avatar.imageUrl,
            contentDescription = avatar.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            error = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = avatar.name.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        )
    }
}

private fun DrawScope.drawMaleAvatar(w: Float, h: Float, avatar: AvatarItem) {
    val skinGradient = Brush.radialGradient(
        colors = listOf(Color(0xFFFFEECC), Color(0xFFFFDBAC), Color(0xFFD4A373)),
        center = Offset(w * 0.45f, h * 0.40f),
        radius = w * 0.3f
    )
    val hairColor = when (avatar.id) {
        "m_1" -> Color(0xFFF1F5F9) // Silver royal
        "m_2" -> Color(0xFF29190A) // Dark brown
        "m_3" -> Color(0xFFB45309) // Auburn
        "m_4" -> Color(0xFF0EA5E9) // Cyber blue
        else -> Color(0xFF18181B)
    }

    // Shoulders / Suit 3D gradient
    val suitBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF334155), Color(0xFF1E293B), Color(0xFF0F172A))
    )
    val suitPath = Path().apply {
        moveTo(w * 0.12f, h * 0.98f)
        lineTo(w * 0.88f, h * 0.98f)
        cubicTo(w * 0.82f, h * 0.68f, w * 0.65f, h * 0.62f, w * 0.50f, h * 0.62f)
        cubicTo(w * 0.35f, h * 0.62f, w * 0.18f, h * 0.68f, w * 0.12f, h * 0.98f)
    }
    drawPath(suitPath, suitBrush)

    // Collar & golden tie/accent
    drawLine(Color(0xFFE2E8F0), Offset(w * 0.5f, h * 0.62f), Offset(w * 0.5f, h * 0.85f), strokeWidth = w * 0.06f)
    drawCircle(Color(0xFFFFD700), radius = w * 0.04f, center = Offset(w * 0.5f, h * 0.75f))

    // Head base with 3D shadow/volume
    drawCircle(skinGradient, radius = w * 0.27f, center = Offset(w * 0.5f, h * 0.44f))

    // Hair / Crown
    if (avatar.id == "m_1") {
        // 3D Gold Crown with jewels
        val crownBrush = Brush.linearGradient(
            colors = listOf(Color(0xFFFFEE55), Color(0xFFFFD700), Color(0xFFB8860B))
        )
        val crown = Path().apply {
            moveTo(w * 0.26f, h * 0.32f)
            lineTo(w * 0.33f, h * 0.14f)
            lineTo(w * 0.50f, h * 0.23f)
            lineTo(w * 0.67f, h * 0.14f)
            lineTo(w * 0.74f, h * 0.32f)
            close()
        }
        drawPath(crown, crownBrush)
        drawCircle(Color(0xFFEF4444), radius = w * 0.04f, center = Offset(w * 0.5f, h * 0.23f))
        drawCircle(Color(0xFF3B82F6), radius = w * 0.03f, center = Offset(w * 0.33f, h * 0.22f))
        drawCircle(Color(0xFF10B981), radius = w * 0.03f, center = Offset(w * 0.67f, h * 0.22f))
    } else {
        val hairBrush = Brush.radialGradient(
            colors = listOf(hairColor.copy(alpha = 1f), hairColor.copy(alpha = 0.8f)),
            center = Offset(w * 0.4f, h * 0.28f),
            radius = w * 0.3f
        )
        drawCircle(hairBrush, radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.35f))
        drawCircle(skinGradient, radius = w * 0.23f, center = Offset(w * 0.5f, h * 0.45f))
    }

    // Realistic 3D Eyes with specular highlights
    drawCircle(Color(0xFF1E293B), radius = w * 0.04f, center = Offset(w * 0.41f, h * 0.45f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.395f, h * 0.435f))

    drawCircle(Color(0xFF1E293B), radius = w * 0.04f, center = Offset(w * 0.59f, h * 0.45f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.575f, h * 0.435f))

    // Eyebrows
    drawLine(hairColor, Offset(w * 0.37f, h * 0.39f), Offset(w * 0.45f, h * 0.40f), strokeWidth = w * 0.02f, cap = StrokeCap.Round)
    drawLine(hairColor, Offset(w * 0.55f, h * 0.40f), Offset(w * 0.63f, h * 0.39f), strokeWidth = w * 0.02f, cap = StrokeCap.Round)

    // Smile / Lips
    val smile = Path().apply {
        moveTo(w * 0.43f, h * 0.54f)
        quadraticBezierTo(w * 0.50f, h * 0.58f, w * 0.57f, h * 0.54f)
    }
    drawPath(smile, Color(0xFF991B1B), style = Stroke(w * 0.03f, cap = StrokeCap.Round))
}

private fun DrawScope.drawFemaleAvatar(w: Float, h: Float, avatar: AvatarItem) {
    val skinGradient = Brush.radialGradient(
        colors = listOf(Color(0xFFFFF2E6), Color(0xFFFFE0BD), Color(0xFFE5B299)),
        center = Offset(w * 0.45f, h * 0.40f),
        radius = w * 0.3f
    )
    val hairColor = when (avatar.id) {
        "f_1" -> Color(0xFF9333EA) // Royal violet
        "f_2" -> Color(0xFF06B6D4) // Cyber cyan
        "f_3" -> Color(0xFFB45309) // Auburn
        "f_4" -> Color(0xFFF43F5E) // Rose pink
        else -> Color(0xFF312E81)
    }

    // Long hair 3D backdrop
    drawCircle(hairColor, radius = w * 0.36f, center = Offset(w * 0.5f, h * 0.46f))

    // Elegant dress 3D gradient
    val dressBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFDB2777), Color(0xFF9D174D), Color(0xFF500724))
    )
    val dress = Path().apply {
        moveTo(w * 0.14f, h * 0.98f)
        lineTo(w * 0.86f, h * 0.98f)
        cubicTo(w * 0.80f, h * 0.70f, w * 0.65f, h * 0.65f, w * 0.50f, h * 0.65f)
        cubicTo(w * 0.35f, h * 0.65f, w * 0.20f, h * 0.70f, w * 0.14f, h * 0.98f)
    }
    drawPath(dress, dressBrush)

    // Face
    drawCircle(skinGradient, radius = w * 0.25f, center = Offset(w * 0.5f, h * 0.44f))

    // Stylish 3D Hair bangs
    val bangsBrush = Brush.verticalGradient(
        colors = listOf(hairColor, hairColor.copy(alpha = 0.85f))
    )
    val bangs = Path().apply {
        moveTo(w * 0.26f, h * 0.35f)
        quadraticBezierTo(w * 0.40f, h * 0.44f, w * 0.50f, h * 0.35f)
        quadraticBezierTo(w * 0.60f, h * 0.44f, w * 0.74f, h * 0.35f)
        quadraticBezierTo(w * 0.50f, h * 0.16f, w * 0.26f, h * 0.35f)
    }
    drawPath(bangs, bangsBrush)

    // Tiara / Crown jewel for Queen/Princess
    if (avatar.id == "f_1" || avatar.id == "f_4") {
        drawCircle(Color(0xFFFFD700), radius = w * 0.06f, center = Offset(w * 0.5f, h * 0.20f))
        drawCircle(Color(0xFFE11D48), radius = w * 0.03f, center = Offset(w * 0.5f, h * 0.20f))
    }

    // Realistic glossy 3D Eyes
    drawCircle(Color(0xFF0F172A), radius = w * 0.05f, center = Offset(w * 0.41f, h * 0.45f))
    drawCircle(Color.White, radius = w * 0.02f, center = Offset(w * 0.395f, h * 0.435f))
    drawCircle(Color.White, radius = w * 0.008f, center = Offset(w * 0.425f, h * 0.465f))

    drawCircle(Color(0xFF0F172A), radius = w * 0.05f, center = Offset(w * 0.59f, h * 0.45f))
    drawCircle(Color.White, radius = w * 0.02f, center = Offset(w * 0.575f, h * 0.435f))
    drawCircle(Color.White, radius = w * 0.008f, center = Offset(w * 0.605f, h * 0.465f))

    // Soft 3D Blush
    drawCircle(Color(0xFFFF80AB).copy(alpha = 0.5f), radius = w * 0.06f, center = Offset(w * 0.34f, h * 0.53f))
    drawCircle(Color(0xFFFF80AB).copy(alpha = 0.5f), radius = w * 0.06f, center = Offset(w * 0.66f, h * 0.53f))

    // Glossy lips
    val lips = Path().apply {
        moveTo(w * 0.43f, h * 0.55f)
        quadraticBezierTo(w * 0.50f, h * 0.59f, w * 0.57f, h * 0.55f)
        quadraticBezierTo(w * 0.50f, h * 0.57f, w * 0.43f, h * 0.55f)
    }
    drawPath(lips, Color(0xFFE11D48))
}

private fun DrawScope.drawAnimalAvatar(w: Float, h: Float, avatar: AvatarItem) {
    val furColor = when (avatar.id) {
        "a_1" -> Color(0xFFD97706) // Lion
        "a_2" -> Color(0xFFEA580C) // Fox
        "a_3" -> Color(0xFFC2410C) // Tiger
        "a_4" -> Color(0xFF64748B) // Wolf
        else -> Color(0xFFF1F5F9)  // Panda
    }
    val furBrush = Brush.radialGradient(
        colors = listOf(furColor, furColor.copy(alpha = 0.8f)),
        center = Offset(w * 0.45f, h * 0.45f),
        radius = w * 0.35f
    )

    // 3D Ears
    drawCircle(furBrush, radius = w * 0.13f, center = Offset(w * 0.28f, h * 0.25f))
    drawCircle(furBrush, radius = w * 0.13f, center = Offset(w * 0.72f, h * 0.25f))
    drawCircle(Color(0xFFFDA4AF), radius = w * 0.07f, center = Offset(w * 0.28f, h * 0.25f))
    drawCircle(Color(0xFFFDA4AF), radius = w * 0.07f, center = Offset(w * 0.72f, h * 0.25f))

    // Lion Mane 3D gradient
    if (avatar.id == "a_1") {
        val maneBrush = Brush.radialGradient(
            colors = listOf(Color(0xFFB45309), Color(0xFF78350F), Color(0xFF451A03)),
            center = Offset(w * 0.5f, h * 0.5f),
            radius = w * 0.4f
        )
        drawCircle(maneBrush, radius = w * 0.39f, center = Offset(w * 0.5f, h * 0.52f))
    }

    // Main head volume
    drawCircle(furBrush, radius = w * 0.29f, center = Offset(w * 0.5f, h * 0.52f))

    if (avatar.id == "a_5") {
        // Panda black eye patches
        drawCircle(Color(0xFF0F172A), radius = w * 0.10f, center = Offset(w * 0.39f, h * 0.50f))
        drawCircle(Color(0xFF0F172A), radius = w * 0.10f, center = Offset(w * 0.61f, h * 0.50f))
    }

    // Glossy 3D Animal Eyes
    drawCircle(Color.White, radius = w * 0.05f, center = Offset(w * 0.39f, h * 0.49f))
    drawCircle(Color(0xFF0F172A), radius = w * 0.03f, center = Offset(w * 0.39f, h * 0.49f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.38f, h * 0.48f))

    drawCircle(Color.White, radius = w * 0.05f, center = Offset(w * 0.61f, h * 0.49f))
    drawCircle(Color(0xFF0F172A), radius = w * 0.03f, center = Offset(w * 0.61f, h * 0.49f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.60f, h * 0.48f))

    // Snout / Nose
    drawCircle(Color(0xFF0F172A), radius = w * 0.045f, center = Offset(w * 0.50f, h * 0.61f))
    val snout = Path().apply {
        moveTo(w * 0.50f, h * 0.63f)
        lineTo(w * 0.50f, h * 0.70f)
        moveTo(w * 0.42f, h * 0.70f)
        quadraticBezierTo(w * 0.50f, h * 0.75f, w * 0.58f, h * 0.70f)
    }
    drawPath(snout, Color(0xFF0F172A), style = Stroke(w * 0.03f, cap = StrokeCap.Round))
}

private fun DrawScope.drawBirdAvatar(w: Float, h: Float, avatar: AvatarItem) {
    val featherColor = when (avatar.id) {
        "b_1" -> Color(0xFF78350F) // Eagle
        "b_2" -> Color(0xFF1D4ED8) // Falcon
        "b_3" -> Color(0xFF475569) // Owl
        "b_4" -> Color(0xFF0F172A) // Raven
        else -> Color(0xFFDC2626)  // Phoenix
    }
    val birdBrush = Brush.radialGradient(
        colors = listOf(featherColor.copy(alpha = 1f), featherColor.copy(alpha = 0.8f)),
        center = Offset(w * 0.45f, h * 0.45f),
        radius = w * 0.35f
    )

    // Crest
    val crest = Path().apply {
        moveTo(w * 0.34f, h * 0.32f)
        lineTo(w * 0.50f, h * 0.10f)
        lineTo(w * 0.66f, h * 0.32f)
        close()
    }
    drawPath(crest, birdBrush)

    // Head volume
    drawCircle(birdBrush, radius = w * 0.31f, center = Offset(w * 0.5f, h * 0.50f))

    if (avatar.id == "b_1") {
        drawCircle(Color(0xFFF8FAFC), radius = w * 0.29f, center = Offset(w * 0.5f, h * 0.48f))
    }

    // 3D Piercing Eyes with gold/amber glow
    val eyeColor = if (avatar.id == "b_5") Color(0xFFFACC15) else Color(0xFFFFD700)
    drawCircle(eyeColor, radius = w * 0.075f, center = Offset(w * 0.37f, h * 0.46f))
    drawCircle(Color.Black, radius = w * 0.04f, center = Offset(w * 0.38f, h * 0.46f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.37f, h * 0.45f))

    drawCircle(eyeColor, radius = w * 0.075f, center = Offset(w * 0.63f, h * 0.46f))
    drawCircle(Color.Black, radius = w * 0.04f, center = Offset(w * 0.62f, h * 0.46f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.63f, h * 0.45f))

    // 3D Beak with gradient shading
    val beakBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFBBF24), Color(0xFFD97706), Color(0xFFB45309))
    )
    val beak = Path().apply {
        moveTo(w * 0.41f, h * 0.52f)
        lineTo(w * 0.59f, h * 0.52f)
        lineTo(w * 0.50f, h * 0.74f)
        close()
    }
    drawPath(beak, beakBrush)
}

private fun DrawScope.drawRobotAvatar(w: Float, h: Float, avatar: AvatarItem) {
    val metalColor = when (avatar.id) {
        "r_1" -> Color(0xFF0284C7)
        "r_2" -> Color(0xFF475569)
        "r_3" -> Color(0xFF7C3AED)
        "r_4" -> Color(0xFF059669)
        else -> Color(0xFFD97706)
    }
    val metalBrush = Brush.verticalGradient(
        colors = listOf(metalColor.copy(alpha = 1f), metalColor.copy(alpha = 0.7f), Color(0xFF0F172A))
    )

    // Antenna & 3D glowing top
    drawLine(metalColor, Offset(w * 0.5f, h * 0.26f), Offset(w * 0.5f, h * 0.10f), strokeWidth = w * 0.045f, cap = StrokeCap.Round)
    drawCircle(Color(0xFF38BDF8), radius = w * 0.055f, center = Offset(w * 0.5f, h * 0.08f))

    // Robot Head 3D Box
    drawRoundRect(
        brush = metalBrush,
        topLeft = Offset(w * 0.22f, h * 0.26f),
        size = Size(w * 0.56f, h * 0.46f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.10f)
    )

    // Side bolts
    drawCircle(Color(0xFF1E293B), radius = w * 0.055f, center = Offset(w * 0.20f, h * 0.49f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.055f, center = Offset(w * 0.80f, h * 0.49f))

    // Visor screen with glossy reflection
    drawRoundRect(
        color = Color(0xFF090D16),
        topLeft = Offset(w * 0.28f, h * 0.36f),
        size = Size(w * 0.44f, h * 0.20f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.05f)
    )

    // Neon glowing visor eyes
    if (avatar.id == "r_1" || avatar.id == "r_3") {
        val glow = if (avatar.id == "r_1") Color(0xFF22D3EE) else Color(0xFFA855F7)
        drawRoundRect(
            color = glow,
            topLeft = Offset(w * 0.32f, h * 0.42f),
            size = Size(w * 0.36f, h * 0.07f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.035f)
        )
        // Specular highlight on visor
        drawRoundRect(
            color = Color.White.copy(alpha = 0.5f),
            topLeft = Offset(w * 0.34f, h * 0.43f),
            size = Size(w * 0.16f, h * 0.02f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.01f)
        )
    } else {
        drawCircle(Color(0xFF4ADE80), radius = w * 0.04f, center = Offset(w * 0.39f, h * 0.46f))
        drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.38f, h * 0.45f))

        drawCircle(Color(0xFF4ADE80), radius = w * 0.04f, center = Offset(w * 0.61f, h * 0.46f))
        drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.60f, h * 0.45f))
    }

    // Mouth Grill
    for (i in 0..4) {
        val x = w * (0.36f + i * 0.07f)
        drawLine(Color(0xFF1E293B), Offset(x, h * 0.61f), Offset(x, h * 0.67f), strokeWidth = w * 0.025f, cap = StrokeCap.Round)
    }
}
