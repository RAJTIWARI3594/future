package com.example.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Ambient Frosted Glass Background Container
 * Draws soft glowing radial ambient gradient orbs behind content to maximize the frosted glass refraction effect.
 */
@Composable
fun GlassCanvasBackground(
    modifier: Modifier = Modifier,
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable BoxScope.() -> Unit
) {
    val baseBg = if (isDark) GlassBgDark else GlassBgLight
    val orb1Color = if (isDark) Color(0x334F46E5) else Color(0x40C7D2FE) // Indigo orb
    val orb2Color = if (isDark) Color(0x33A855F7) else Color(0x35E0E7FF) // Purple orb
    val orb3Color = if (isDark) Color(0x2206B6D4) else Color(0x30CFFAFE) // Cyan orb

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(baseBg)
    ) {
        // Glowing Background Ambient Orbs
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-60).dp, y = (-40).dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(orb1Color, Color.Transparent)
                    )
                )
                .blur(40.dp)
        )
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 80.dp, y = (-120).dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(orb2Color, Color.Transparent)
                    )
                )
                .blur(45.dp)
        )
        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-40).dp, y = 100.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(orb3Color, Color.Transparent)
                    )
                )
                .blur(50.dp)
        )

        // Main Screen Content
        content()
    }
}

/**
 * Reusable Frosted Glass Card Container
 */
@Composable
fun FrostedGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    isDark: Boolean = isSystemInDarkTheme(),
    elevation: Dp = 4.dp,
    containerColor: Color? = null,
    borderColor: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val bg = containerColor ?: if (isDark) GlassSurfaceDark else GlassSurfaceLight
    val border = borderColor ?: if (isDark) GlassBorderDark else GlassBorderLight

    Card(
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.15f)
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = bg,
            contentColor = if (isDark) DarkTextPrimary else Slate900
        ),
        border = BorderStroke(1.dp, border)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

/**
 * Frosted Glass Chip / Pill Badge
 */
@Composable
fun GlassChip(
    text: String,
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    isDark: Boolean = isSystemInDarkTheme(),
    accentColor: Color = Indigo600
) {
    val chipBg = if (isDark) accentColor.copy(alpha = 0.25f) else accentColor.copy(alpha = 0.12f)
    val chipBorder = if (isDark) accentColor.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.8f)

    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = chipBg,
        border = BorderStroke(1.dp, chipBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
            }
            Text(
                text = text,
                color = if (isDark) DarkTextPrimary else Slate900,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Frosted Glass Action Button
 */
@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isDark: Boolean = isSystemInDarkTheme(),
    gradientColors: List<Color> = listOf(Indigo600, Indigo500),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .border(
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(gradientColors)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                content = content
            )
        }
    }
}
