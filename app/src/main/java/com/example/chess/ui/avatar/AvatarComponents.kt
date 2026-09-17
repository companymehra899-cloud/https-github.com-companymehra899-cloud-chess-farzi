package com.example.chess.ui.avatar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
            AvatarGraphic(
                avatar = avatar,
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (avatar.drawableResId != null) Modifier else Modifier.padding(size * 0.06f))
            )
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
        if (avatar.drawableResId != null) {
            Image(
                painter = painterResource(id = avatar.drawableResId),
                contentDescription = avatar.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                when (avatar.category) {
                    AvatarCategory.MALE -> drawMaleAvatar(w, h, avatar)
                    AvatarCategory.FEMALE -> drawFemaleAvatar(w, h, avatar)
                    AvatarCategory.ANIMAL -> drawAnimalAvatar(w, h, avatar)
                    AvatarCategory.BIRD -> drawBirdAvatar(w, h, avatar)
                    AvatarCategory.ROBOT -> drawRobotAvatar(w, h, avatar)
                }
            }
        }
    }
}

private fun DrawScope.drawMaleAvatar(w: Float, h: Float, avatar: AvatarItem) {
    // Determine skin tone based on culture/country
    val skinGradient = when (avatar.id) {
        "m_ind", "m_pun", "m_pak" -> Brush.radialGradient(
            colors = listOf(Color(0xFFFFE0B2), Color(0xFFDEB887), Color(0xFFC68B59)),
            center = Offset(w * 0.45f, h * 0.40f), radius = w * 0.3f
        )
        "m_afr" -> Brush.radialGradient(
            colors = listOf(Color(0xFF8D5524), Color(0xFF6A3B14), Color(0xFF4A2508)),
            center = Offset(w * 0.45f, h * 0.40f), radius = w * 0.3f
        )
        "m_bra" -> Brush.radialGradient(
            colors = listOf(Color(0xFFFFE0BD), Color(0xFFD4A373), Color(0xFFB07A4B)),
            center = Offset(w * 0.45f, h * 0.40f), radius = w * 0.3f
        )
        "m_jpn" -> Brush.radialGradient(
            colors = listOf(Color(0xFFFFF7ED), Color(0xFFFFEDD5), Color(0xFFE2C9B0)),
            center = Offset(w * 0.45f, h * 0.40f), radius = w * 0.3f
        )
        else -> Brush.radialGradient(
            colors = listOf(Color(0xFFFFEECC), Color(0xFFFFDBAC), Color(0xFFD4A373)),
            center = Offset(w * 0.45f, h * 0.40f), radius = w * 0.3f
        )
    }

    val hairColor = when (avatar.id) {
        "m_1" -> Color(0xFFF1F5F9)
        "m_2", "m_uk" -> Color(0xFF3E2723)
        "m_3", "m_usa" -> Color(0xFFB45309)
        "m_4" -> Color(0xFF0EA5E9)
        "m_rus" -> Color(0xFF64748B)
        else -> Color(0xFF18181B)
    }

    // Cultural Attire / Shoulders
    when (avatar.id) {
        "m_ind" -> {
            // Saffron/Maroon Kurta with golden embroidery
            val kurtaBrush = Brush.verticalGradient(listOf(Color(0xFFEA580C), Color(0xFFC2410C), Color(0xFF7C2D12)))
            val kurta = Path().apply {
                moveTo(w * 0.12f, h * 0.98f); lineTo(w * 0.88f, h * 0.98f)
                cubicTo(w * 0.82f, h * 0.68f, w * 0.65f, h * 0.62f, w * 0.50f, h * 0.62f)
                cubicTo(w * 0.35f, h * 0.62f, w * 0.18f, h * 0.68f, w * 0.12f, h * 0.98f)
            }
            drawPath(kurta, kurtaBrush)
            drawLine(Color(0xFFFFD700), Offset(w * 0.5f, h * 0.62f), Offset(w * 0.5f, h * 0.88f), strokeWidth = w * 0.04f)
        }
        "m_pun" -> {
            // Royal blue Kurta with gold trim
            val kurtaBrush = Brush.verticalGradient(listOf(Color(0xFF1E3A8A), Color(0xFF172554)))
            val kurta = Path().apply {
                moveTo(w * 0.12f, h * 0.98f); lineTo(w * 0.88f, h * 0.98f)
                cubicTo(w * 0.82f, h * 0.68f, w * 0.65f, h * 0.62f, w * 0.50f, h * 0.62f)
                cubicTo(w * 0.35f, h * 0.62f, w * 0.18f, h * 0.68f, w * 0.12f, h * 0.98f)
            }
            drawPath(kurta, kurtaBrush)
            drawCircle(Color(0xFFFFD700), radius = w * 0.035f, center = Offset(w * 0.5f, h * 0.72f))
        }
        "m_pak" -> {
            // Emerald Sherwani
            val sherwaniBrush = Brush.verticalGradient(listOf(Color(0xFF064E3B), Color(0xFF022C22)))
            val sherwani = Path().apply {
                moveTo(w * 0.12f, h * 0.98f); lineTo(w * 0.88f, h * 0.98f)
                cubicTo(w * 0.82f, h * 0.68f, w * 0.65f, h * 0.62f, w * 0.50f, h * 0.62f)
                cubicTo(w * 0.35f, h * 0.62f, w * 0.18f, h * 0.68f, w * 0.12f, h * 0.98f)
            }
            drawPath(sherwani, sherwaniBrush)
            drawLine(Color(0xFFE2E8F0), Offset(w * 0.5f, h * 0.62f), Offset(w * 0.5f, h * 0.88f), strokeWidth = w * 0.03f)
        }
        "m_usa" -> {
            // Varsity Jacket (Navy & Crimson with white shoulders)
            val varsity = Path().apply {
                moveTo(w * 0.12f, h * 0.98f); lineTo(w * 0.88f, h * 0.98f)
                cubicTo(w * 0.82f, h * 0.68f, w * 0.65f, h * 0.62f, w * 0.50f, h * 0.62f)
                cubicTo(w * 0.35f, h * 0.62f, w * 0.18f, h * 0.68f, w * 0.12f, h * 0.98f)
            }
            drawPath(varsity, Color(0xFF1E3A8A))
            drawCircle(Color(0xFFEF4444), radius = w * 0.14f, center = Offset(w * 0.50f, h * 0.82f))
            drawCircle(Color.White, radius = w * 0.05f, center = Offset(w * 0.50f, h * 0.82f))
        }
        "m_afr" -> {
            // African Kente patterned shawl
            val kente = Path().apply {
                moveTo(w * 0.12f, h * 0.98f); lineTo(w * 0.88f, h * 0.98f)
                cubicTo(w * 0.82f, h * 0.68f, w * 0.65f, h * 0.62f, w * 0.50f, h * 0.62f)
                cubicTo(w * 0.35f, h * 0.62f, w * 0.18f, h * 0.68f, w * 0.12f, h * 0.98f)
            }
            drawPath(kente, Color(0xFFD97706))
            drawLine(Color(0xFF15803D), Offset(w * 0.3f, h * 0.7f), Offset(w * 0.7f, h * 0.7f), strokeWidth = w * 0.04f)
            drawCircle(Color(0xFFFFD700), radius = w * 0.05f, center = Offset(w * 0.5f, h * 0.72f))
        }
        else -> {
            val suitBrush = Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B), Color(0xFF0F172A)))
            val suitPath = Path().apply {
                moveTo(w * 0.12f, h * 0.98f); lineTo(w * 0.88f, h * 0.98f)
                cubicTo(w * 0.82f, h * 0.68f, w * 0.65f, h * 0.62f, w * 0.50f, h * 0.62f)
                cubicTo(w * 0.35f, h * 0.62f, w * 0.18f, h * 0.68f, w * 0.12f, h * 0.98f)
            }
            drawPath(suitPath, suitBrush)
            drawLine(Color(0xFFE2E8F0), Offset(w * 0.5f, h * 0.62f), Offset(w * 0.5f, h * 0.85f), strokeWidth = w * 0.06f)
            drawCircle(Color(0xFFFFD700), radius = w * 0.04f, center = Offset(w * 0.5f, h * 0.75f))
        }
    }

    // Head base with 3D shadow/volume
    drawCircle(skinGradient, radius = w * 0.27f, center = Offset(w * 0.5f, h * 0.44f))

    // Headwear / Hair
    when (avatar.id) {
        "m_pun" -> {
            // Royal Punjabi Turban (Dastar) with 3D wraps
            val turbanBrush = Brush.radialGradient(listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8), Color(0xFF1E3A8A)))
            drawCircle(turbanBrush, radius = w * 0.33f, center = Offset(w * 0.5f, h * 0.30f))
            // Turban wrap folds
            drawLine(Color(0xFFFFD700), Offset(w * 0.26f, h * 0.32f), Offset(w * 0.74f, h * 0.26f), strokeWidth = w * 0.04f, cap = StrokeCap.Round)
            drawLine(Color(0xFF60A5FA), Offset(w * 0.30f, h * 0.24f), Offset(w * 0.70f, h * 0.20f), strokeWidth = w * 0.04f, cap = StrokeCap.Round)
            // Golden Kalgi Brooch
            drawCircle(Color(0xFFFFD700), radius = w * 0.045f, center = Offset(w * 0.5f, h * 0.16f))
            drawCircle(Color(0xFFEF4444), radius = w * 0.02f, center = Offset(w * 0.5f, h * 0.16f))
            // Neat beard & mustache
            val beard = Path().apply {
                moveTo(w * 0.30f, h * 0.48f)
                cubicTo(w * 0.34f, h * 0.68f, w * 0.66f, h * 0.68f, w * 0.70f, h * 0.48f)
                cubicTo(w * 0.64f, h * 0.62f, w * 0.36f, h * 0.62f, w * 0.30f, h * 0.48f)
            }
            drawPath(beard, Color(0xFF0F172A))
        }
        "m_rus" -> {
            // Ushanka Winter Fur Hat
            val furBrush = Brush.verticalGradient(listOf(Color(0xFF94A3B8), Color(0xFF475569)))
            drawRoundRect(
                brush = furBrush,
                topLeft = Offset(w * 0.22f, h * 0.16f),
                size = Size(w * 0.56f, h * 0.24f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.08f)
            )
            // Ear flaps
            drawRoundRect(furBrush, Offset(w * 0.20f, h * 0.24f), Size(w * 0.12f, h * 0.22f), androidx.compose.ui.geometry.CornerRadius(w * 0.05f))
            drawRoundRect(furBrush, Offset(w * 0.68f, h * 0.24f), Size(w * 0.12f, h * 0.22f), androidx.compose.ui.geometry.CornerRadius(w * 0.05f))
            // Star badge
            drawCircle(Color(0xFFFFD700), radius = w * 0.04f, center = Offset(w * 0.50f, h * 0.24f))
        }
        "m_ara" -> {
            // White Ghutra / Keffiyeh with black Agal
            val ghutra = Path().apply {
                moveTo(w * 0.20f, h * 0.75f)
                lineTo(w * 0.24f, h * 0.24f)
                cubicTo(w * 0.36f, h * 0.14f, w * 0.64f, h * 0.14f, w * 0.76f, h * 0.24f)
                lineTo(w * 0.80f, h * 0.75f)
                cubicTo(w * 0.72f, h * 0.45f, w * 0.28f, h * 0.45f, w * 0.20f, h * 0.75f)
            }
            drawPath(ghutra, Color(0xFFF8FAFC))
            // Black Agal circlet
            drawRoundRect(Color(0xFF0F172A), Offset(w * 0.28f, h * 0.22f), Size(w * 0.44f, h * 0.05f), androidx.compose.ui.geometry.CornerRadius(w * 0.02f))
            drawRoundRect(Color(0xFF0F172A), Offset(w * 0.30f, h * 0.27f), Size(w * 0.40f, h * 0.04f), androidx.compose.ui.geometry.CornerRadius(w * 0.02f))
        }
        "m_usa" -> {
            // Backwards Baseball Cap
            val capBrush = Brush.verticalGradient(listOf(Color(0xFFDC2626), Color(0xFF991B1B)))
            drawCircle(capBrush, radius = w * 0.29f, center = Offset(w * 0.5f, h * 0.32f))
            drawRoundRect(Color(0xFF1E3A8A), Offset(w * 0.32f, h * 0.35f), Size(w * 0.36f, h * 0.06f), androidx.compose.ui.geometry.CornerRadius(w * 0.03f))
        }
        "m_ind" -> {
            // Indian slick black hair with small golden tilak
            val hairBrush = Brush.radialGradient(listOf(Color(0xFF27272A), Color(0xFF09090B)), center = Offset(w * 0.4f, h * 0.26f), radius = w * 0.3f)
            drawCircle(hairBrush, radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.34f))
            drawCircle(skinGradient, radius = w * 0.23f, center = Offset(w * 0.5f, h * 0.45f))
            // Royal Tilak
            drawCircle(Color(0xFFDC2626), radius = w * 0.02f, center = Offset(w * 0.50f, h * 0.37f))
        }
        else -> {
            val hairBrush = Brush.radialGradient(
                colors = listOf(hairColor.copy(alpha = 1f), hairColor.copy(alpha = 0.8f)),
                center = Offset(w * 0.4f, h * 0.28f),
                radius = w * 0.3f
            )
            drawCircle(hairBrush, radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.35f))
            drawCircle(skinGradient, radius = w * 0.23f, center = Offset(w * 0.5f, h * 0.45f))
        }
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
    // Cultural Skin Tone
    val skinGradient = when (avatar.id) {
        "f_ind", "f_pun", "f_pak" -> Brush.radialGradient(
            colors = listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2), Color(0xFFD4A373)),
            center = Offset(w * 0.45f, h * 0.40f), radius = w * 0.3f
        )
        "f_afr" -> Brush.radialGradient(
            colors = listOf(Color(0xFF9C6644), Color(0xFF7F4F24), Color(0xFF582F0E)),
            center = Offset(w * 0.45f, h * 0.40f), radius = w * 0.3f
        )
        "f_jpn", "f_kor" -> Brush.radialGradient(
            colors = listOf(Color(0xFFFFF7ED), Color(0xFFFFEDD5), Color(0xFFFED7AA)),
            center = Offset(w * 0.45f, h * 0.40f), radius = w * 0.3f
        )
        else -> Brush.radialGradient(
            colors = listOf(Color(0xFFFFF2E6), Color(0xFFFFE0BD), Color(0xFFE5B299)),
            center = Offset(w * 0.45f, h * 0.40f), radius = w * 0.3f
        )
    }

    val hairColor = when (avatar.id) {
        "f_1" -> Color(0xFF9333EA)
        "f_2" -> Color(0xFF06B6D4)
        "f_3", "f_fra" -> Color(0xFF3E2723)
        "f_4" -> Color(0xFFF43F5E)
        "f_usa", "f_rus" -> Color(0xFFFDE047)
        "f_kor" -> Color(0xFFA855F7)
        else -> Color(0xFF18181B)
    }

    // Long hair 3D backdrop
    drawCircle(hairColor, radius = w * 0.36f, center = Offset(w * 0.5f, h * 0.46f))

    // Cultural Dress
    when (avatar.id) {
        "f_ind" -> {
            // Royal Red & Gold Saree
            val saree = Path().apply {
                moveTo(w * 0.14f, h * 0.98f); lineTo(w * 0.86f, h * 0.98f)
                cubicTo(w * 0.80f, h * 0.70f, w * 0.65f, h * 0.65f, w * 0.50f, h * 0.65f)
                cubicTo(w * 0.35f, h * 0.65f, w * 0.20f, h * 0.70f, w * 0.14f, h * 0.98f)
            }
            drawPath(saree, Color(0xFFDC2626))
            // Gold Zari diagonal drape
            drawLine(Color(0xFFFFD700), Offset(w * 0.25f, h * 0.98f), Offset(w * 0.75f, h * 0.65f), strokeWidth = w * 0.07f)
        }
        "f_pun" -> {
            // Magenta & Gold Phulkari Suit
            val suit = Path().apply {
                moveTo(w * 0.14f, h * 0.98f); lineTo(w * 0.86f, h * 0.98f)
                cubicTo(w * 0.80f, h * 0.70f, w * 0.65f, h * 0.65f, w * 0.50f, h * 0.65f)
                cubicTo(w * 0.35f, h * 0.65f, w * 0.20f, h * 0.70f, w * 0.14f, h * 0.98f)
            }
            drawPath(suit, Color(0xFFC026D3))
            drawLine(Color(0xFFFACC15), Offset(w * 0.20f, h * 0.75f), Offset(w * 0.80f, h * 0.75f), strokeWidth = w * 0.04f)
        }
        "f_pak" -> {
            // Mint Green & Silver Shalwar Kameez
            val suit = Path().apply {
                moveTo(w * 0.14f, h * 0.98f); lineTo(w * 0.86f, h * 0.98f)
                cubicTo(w * 0.80f, h * 0.70f, w * 0.65f, h * 0.65f, w * 0.50f, h * 0.65f)
                cubicTo(w * 0.35f, h * 0.65f, w * 0.20f, h * 0.70f, w * 0.14f, h * 0.98f)
            }
            drawPath(suit, Color(0xFF059669))
            drawLine(Color(0xFFE2E8F0), Offset(w * 0.5f, h * 0.65f), Offset(w * 0.5f, h * 0.95f), strokeWidth = w * 0.04f)
        }
        "f_jpn" -> {
            // Sakura Pink Kimono
            val kimono = Path().apply {
                moveTo(w * 0.14f, h * 0.98f); lineTo(w * 0.86f, h * 0.98f)
                cubicTo(w * 0.80f, h * 0.70f, w * 0.65f, h * 0.65f, w * 0.50f, h * 0.65f)
                cubicTo(w * 0.35f, h * 0.65f, w * 0.20f, h * 0.70f, w * 0.14f, h * 0.98f)
            }
            drawPath(kimono, Color(0xFFFB7185))
            drawLine(Color(0xFFBE185D), Offset(w * 0.25f, h * 0.80f), Offset(w * 0.75f, h * 0.80f), strokeWidth = w * 0.06f)
        }
        else -> {
            val dressBrush = Brush.verticalGradient(listOf(Color(0xFFDB2777), Color(0xFF9D174D), Color(0xFF500724)))
            val dress = Path().apply {
                moveTo(w * 0.14f, h * 0.98f); lineTo(w * 0.86f, h * 0.98f)
                cubicTo(w * 0.80f, h * 0.70f, w * 0.65f, h * 0.65f, w * 0.50f, h * 0.65f)
                cubicTo(w * 0.35f, h * 0.65f, w * 0.20f, h * 0.70f, w * 0.14f, h * 0.98f)
            }
            drawPath(dress, dressBrush)
        }
    }

    // Face
    drawCircle(skinGradient, radius = w * 0.25f, center = Offset(w * 0.5f, h * 0.44f))

    // Cultural Ornaments & Headwear
    when (avatar.id) {
        "f_ind" -> {
            // Golden Maang Tikka
            drawLine(Color(0xFFFFD700), Offset(w * 0.50f, h * 0.22f), Offset(w * 0.50f, h * 0.34f), strokeWidth = w * 0.02f)
            drawCircle(Color(0xFFFFD700), radius = w * 0.035f, center = Offset(w * 0.50f, h * 0.34f))
            drawCircle(Color(0xFFDC2626), radius = w * 0.015f, center = Offset(w * 0.50f, h * 0.34f))
            // Red Bindi
            drawCircle(Color(0xFFDC2626), radius = w * 0.018f, center = Offset(w * 0.50f, h * 0.39f))
            // Golden Jhumkas
            drawCircle(Color(0xFFFFD700), radius = w * 0.03f, center = Offset(w * 0.24f, h * 0.52f))
            drawCircle(Color(0xFFFFD700), radius = w * 0.03f, center = Offset(w * 0.76f, h * 0.52f))
        }
        "f_pun" -> {
            // Golden Turban/Parandi jewel
            drawCircle(Color(0xFFFFD700), radius = w * 0.035f, center = Offset(w * 0.50f, h * 0.33f))
            // Sparkling earrings
            drawCircle(Color(0xFFFACC15), radius = w * 0.035f, center = Offset(w * 0.23f, h * 0.50f))
            drawCircle(Color(0xFFFACC15), radius = w * 0.035f, center = Offset(w * 0.77f, h * 0.50f))
        }
        "f_pak" -> {
            // Elegant Sheer Dupatta drape around head
            val dupatta = Path().apply {
                moveTo(w * 0.20f, h * 0.65f)
                cubicTo(w * 0.22f, h * 0.16f, w * 0.78f, h * 0.16f, w * 0.80f, h * 0.65f)
                lineTo(w * 0.72f, h * 0.65f)
                cubicTo(w * 0.70f, h * 0.22f, w * 0.30f, h * 0.22f, w * 0.28f, h * 0.65f)
                close()
            }
            drawPath(dupatta, Color(0xFF6EE7B7).copy(alpha = 0.85f))
            // Pearl earrings
            drawCircle(Color.White, radius = w * 0.03f, center = Offset(w * 0.24f, h * 0.51f))
            drawCircle(Color.White, radius = w * 0.03f, center = Offset(w * 0.76f, h * 0.51f))
        }
        "f_rus" -> {
            // Ornate Russian Kokoshnik Headdress
            val kokoshnik = Path().apply {
                moveTo(w * 0.20f, h * 0.36f)
                cubicTo(w * 0.25f, h * 0.08f, w * 0.75f, h * 0.08f, w * 0.80f, h * 0.36f)
                cubicTo(w * 0.65f, h * 0.24f, w * 0.35f, h * 0.24f, w * 0.20f, h * 0.36f)
            }
            drawPath(kokoshnik, Brush.verticalGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7))))
            drawCircle(Color(0xFFFFD700), radius = w * 0.03f, center = Offset(w * 0.50f, h * 0.16f))
            drawCircle(Color.White, radius = w * 0.02f, center = Offset(w * 0.35f, h * 0.20f))
            drawCircle(Color.White, radius = w * 0.02f, center = Offset(w * 0.65f, h * 0.20f))
        }
        "f_fra" -> {
            // Parisian Black Beret
            val beret = Path().apply {
                moveTo(w * 0.20f, h * 0.30f)
                cubicTo(w * 0.28f, h * 0.10f, w * 0.78f, h * 0.10f, w * 0.84f, h * 0.28f)
                cubicTo(w * 0.70f, h * 0.36f, w * 0.30f, h * 0.36f, w * 0.20f, h * 0.30f)
            }
            drawPath(beret, Color(0xFF18181B))
            drawCircle(Color(0xFF18181B), radius = w * 0.03f, center = Offset(w * 0.50f, h * 0.14f))
        }
        "f_jpn" -> {
            // Sakura Flower Hair Pin
            drawCircle(Color(0xFFF472B6), radius = w * 0.045f, center = Offset(w * 0.72f, h * 0.30f))
            drawCircle(Color(0xFFFFD700), radius = w * 0.02f, center = Offset(w * 0.72f, h * 0.30f))
        }
        "f_ara" -> {
            // Royal Silk Hijab with Golden Tiara
            val hijab = Path().apply {
                moveTo(w * 0.18f, h * 0.70f)
                cubicTo(w * 0.22f, h * 0.14f, w * 0.78f, h * 0.14f, w * 0.82f, h * 0.70f)
                lineTo(w * 0.72f, h * 0.70f)
                cubicTo(w * 0.70f, h * 0.24f, w * 0.30f, h * 0.24f, w * 0.28f, h * 0.70f)
                close()
            }
            drawPath(hijab, Color(0xFF0F766E))
            drawCircle(Color(0xFFFFD700), radius = w * 0.04f, center = Offset(w * 0.50f, h * 0.20f))
        }
        "f_afr" -> {
            // Majestic Gele Headwrap
            val gele = Path().apply {
                moveTo(w * 0.12f, h * 0.32f)
                cubicTo(w * 0.20f, h * 0.06f, w * 0.80f, h * 0.06f, w * 0.88f, h * 0.32f)
                cubicTo(w * 0.70f, h * 0.24f, w * 0.30f, h * 0.24f, w * 0.12f, h * 0.32f)
            }
            drawPath(gele, Brush.horizontalGradient(listOf(Color(0xFF0D9488), Color(0xFFF59E0B), Color(0xFF0D9488))))
            // Gold neck rings
            drawLine(Color(0xFFFFD700), Offset(w * 0.35f, h * 0.64f), Offset(w * 0.65f, h * 0.64f), strokeWidth = w * 0.03f)
        }
        else -> {
            // Stylish 3D Hair bangs
            val bangsBrush = Brush.verticalGradient(listOf(hairColor, hairColor.copy(alpha = 0.85f)))
            val bangs = Path().apply {
                moveTo(w * 0.26f, h * 0.35f)
                quadraticBezierTo(w * 0.40f, h * 0.44f, w * 0.50f, h * 0.35f)
                quadraticBezierTo(w * 0.60f, h * 0.44f, w * 0.74f, h * 0.35f)
                quadraticBezierTo(w * 0.50f, h * 0.16f, w * 0.26f, h * 0.35f)
            }
            drawPath(bangs, bangsBrush)
        }
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
        "a_1" -> Color(0xFFD97706)
        "a_2" -> Color(0xFFEA580C)
        "a_3", "a_bengal" -> Color(0xFFEA580C)
        "a_4" -> Color(0xFF64748B)
        "a_5" -> Color(0xFFF1F5F9)
        "a_snow_leopard" -> Color(0xFFE2E8F0)
        "a_bear" -> Color(0xFF582F0E)
        "a_stallion" -> Color(0xFF0F172A)
        "a_cheetah" -> Color(0xFFF59E0B)
        "a_polar" -> Color(0xFFF8FAFC)
        "a_eagle" -> Color(0xFF78350F)
        "a_kangaroo" -> Color(0xFFC2410C)
        "a_cobra" -> Color(0xFF065F46)
        "a_dragon" -> Color(0xFF047857)
        else -> Color(0xFFF1F5F9)
    }

    val furBrush = Brush.radialGradient(
        colors = listOf(furColor, furColor.copy(alpha = 0.85f)),
        center = Offset(w * 0.45f, h * 0.45f),
        radius = w * 0.38f
    )

    if (avatar.id == "a_dragon") {
        // Mystic Oriental Dragon with golden horns
        drawCircle(furBrush, radius = w * 0.32f, center = Offset(w * 0.5f, h * 0.50f))
        // Horns
        drawLine(Color(0xFFFFD700), Offset(w * 0.35f, h * 0.32f), Offset(w * 0.20f, h * 0.12f), strokeWidth = w * 0.05f, cap = StrokeCap.Round)
        drawLine(Color(0xFFFFD700), Offset(w * 0.65f, h * 0.32f), Offset(w * 0.80f, h * 0.12f), strokeWidth = w * 0.05f, cap = StrokeCap.Round)
        // Glowing Ruby Eyes
        drawCircle(Color(0xFFEF4444), radius = w * 0.055f, center = Offset(w * 0.38f, h * 0.46f))
        drawCircle(Color(0xFFEF4444), radius = w * 0.055f, center = Offset(w * 0.62f, h * 0.46f))
        drawCircle(Color.White, radius = w * 0.02f, center = Offset(w * 0.37f, h * 0.45f))
        drawCircle(Color.White, radius = w * 0.02f, center = Offset(w * 0.61f, h * 0.45f))
        // Whiskers
        drawLine(Color(0xFFF8FAFC), Offset(w * 0.42f, h * 0.60f), Offset(w * 0.15f, h * 0.70f), strokeWidth = w * 0.025f, cap = StrokeCap.Round)
        drawLine(Color(0xFFF8FAFC), Offset(w * 0.58f, h * 0.60f), Offset(w * 0.85f, h * 0.70f), strokeWidth = w * 0.025f, cap = StrokeCap.Round)
        return
    }

    if (avatar.id == "a_cobra") {
        // King Cobra Flared Hood
        val hood = Path().apply {
            moveTo(w * 0.15f, h * 0.70f)
            cubicTo(w * 0.12f, h * 0.28f, w * 0.88f, h * 0.28f, w * 0.85f, h * 0.70f)
            cubicTo(w * 0.65f, h * 0.85f, w * 0.35f, h * 0.85f, w * 0.15f, h * 0.70f)
        }
        drawPath(hood, furBrush)
        drawCircle(Color(0xFF047857), radius = w * 0.22f, center = Offset(w * 0.50f, h * 0.50f))
        // Crown
        drawCircle(Color(0xFFFFD700), radius = w * 0.05f, center = Offset(w * 0.50f, h * 0.25f))
        // Glowing Eyes
        drawCircle(Color(0xFFEF4444), radius = w * 0.04f, center = Offset(w * 0.40f, h * 0.48f))
        drawCircle(Color(0xFFEF4444), radius = w * 0.04f, center = Offset(w * 0.60f, h * 0.48f))
        return
    }

    // Standard 3D Ears
    drawCircle(furBrush, radius = w * 0.13f, center = Offset(w * 0.28f, h * 0.25f))
    drawCircle(furBrush, radius = w * 0.13f, center = Offset(w * 0.72f, h * 0.25f))
    drawCircle(Color(0xFFFDA4AF), radius = w * 0.07f, center = Offset(w * 0.28f, h * 0.25f))
    drawCircle(Color(0xFFFDA4AF), radius = w * 0.07f, center = Offset(w * 0.72f, h * 0.25f))

    // Lion Mane
    if (avatar.id == "a_1") {
        val maneBrush = Brush.radialGradient(
            colors = listOf(Color(0xFFB45309), Color(0xFF78350F), Color(0xFF451A03)),
            center = Offset(w * 0.5f, h * 0.5f), radius = w * 0.4f
        )
        drawCircle(maneBrush, radius = w * 0.39f, center = Offset(w * 0.5f, h * 0.52f))
    }

    // Polar Bear Cozy Scarf
    if (avatar.id == "a_polar") {
        drawRoundRect(Color(0xFFDC2626), Offset(w * 0.25f, h * 0.72f), Size(w * 0.50f, h * 0.14f), androidx.compose.ui.geometry.CornerRadius(w * 0.06f))
    }

    // Main head volume
    drawCircle(furBrush, radius = w * 0.29f, center = Offset(w * 0.5f, h * 0.52f))

    // Animal special markings
    if (avatar.id == "a_5") {
        // Panda eye patches
        drawCircle(Color(0xFF0F172A), radius = w * 0.10f, center = Offset(w * 0.39f, h * 0.50f))
        drawCircle(Color(0xFF0F172A), radius = w * 0.10f, center = Offset(w * 0.61f, h * 0.50f))
    }
    if (avatar.id == "a_3" || avatar.id == "a_bengal") {
        // Tiger Stripes
        drawLine(Color(0xFF0F172A), Offset(w * 0.50f, h * 0.30f), Offset(w * 0.50f, h * 0.40f), strokeWidth = w * 0.035f, cap = StrokeCap.Round)
        drawLine(Color(0xFF0F172A), Offset(w * 0.28f, h * 0.42f), Offset(w * 0.36f, h * 0.45f), strokeWidth = w * 0.03f, cap = StrokeCap.Round)
        drawLine(Color(0xFF0F172A), Offset(w * 0.72f, h * 0.42f), Offset(w * 0.64f, h * 0.45f), strokeWidth = w * 0.03f, cap = StrokeCap.Round)
    }
    if (avatar.id == "a_snow_leopard" || avatar.id == "a_cheetah") {
        // Spotted rosettes
        drawCircle(Color(0xFF1E293B), radius = w * 0.025f, center = Offset(w * 0.48f, h * 0.36f))
        drawCircle(Color(0xFF1E293B), radius = w * 0.025f, center = Offset(w * 0.32f, h * 0.42f))
        drawCircle(Color(0xFF1E293B), radius = w * 0.025f, center = Offset(w * 0.68f, h * 0.42f))
        if (avatar.id == "a_cheetah") {
            // Cheetah tear stripes
            drawLine(Color(0xFF0F172A), Offset(w * 0.39f, h * 0.51f), Offset(w * 0.45f, h * 0.62f), strokeWidth = w * 0.025f, cap = StrokeCap.Round)
            drawLine(Color(0xFF0F172A), Offset(w * 0.61f, h * 0.51f), Offset(w * 0.55f, h * 0.62f), strokeWidth = w * 0.025f, cap = StrokeCap.Round)
        }
    }

    // Glossy 3D Animal Eyes
    val eyeColor = when (avatar.id) {
        "a_bengal" -> Color(0xFF10B981)
        "a_snow_leopard" -> Color(0xFF38BDF8)
        "a_cheetah" -> Color(0xFFFBBF24)
        else -> Color(0xFF0F172A)
    }
    drawCircle(Color.White, radius = w * 0.05f, center = Offset(w * 0.39f, h * 0.49f))
    drawCircle(eyeColor, radius = w * 0.032f, center = Offset(w * 0.39f, h * 0.49f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.38f, h * 0.48f))

    drawCircle(Color.White, radius = w * 0.05f, center = Offset(w * 0.61f, h * 0.49f))
    drawCircle(eyeColor, radius = w * 0.032f, center = Offset(w * 0.61f, h * 0.49f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.60f, h * 0.48f))

    // Snout / Nose
    drawCircle(Color(0xFF0F172A), radius = w * 0.045f, center = Offset(w * 0.50f, h * 0.61f))
    val snout = Path().apply {
        moveTo(w * 0.50f, h * 0.63f); lineTo(w * 0.50f, h * 0.70f)
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
