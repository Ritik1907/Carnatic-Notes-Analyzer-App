package com.example.mlapp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


// 🔹 Primary brand (Blue shades)
val Blue50 = Color(0xFFE3F2FD)
val Blue100 = Color(0xFFBBDEFB)
val Blue200 = Color(0xFF90CAF9)
val Blue300 = Color(0xFF64B5F6)
val Blue400 = Color(0xFF42A5F5)
val Blue500 = Color(0xFF2196F3)   // Primary
val Blue600 = Color(0xFF1E88E5)
val Blue700 = Color(0xFF1976D2)
val Blue800 = Color(0xFF1565C0)
val Blue900 = Color(0xFF0D47A1)

// 🔹 Secondary brand (Teal shades)
val Teal50 = Color(0xFFE0F2F1)
val Teal100 = Color(0xFFB2DFDB)
val Teal200 = Color(0xFF80CBC4)
val Teal300 = Color(0xFF4DB6AC)
val Teal400 = Color(0xFF26A69A)
val Teal500 = Color(0xFF009688)   // Secondary
val Teal600 = Color(0xFF00897B)
val Teal700 = Color(0xFF00796B)
val Teal800 = Color(0xFF00695C)
val Teal900 = Color(0xFF004D40)

// 🔹 Accent (Amber shades)
val Amber50 = Color(0xFFFFF8E1)
val Amber100 = Color(0xFFFFECB3)
val Amber200 = Color(0xFFFFE082)
val Amber300 = Color(0xFFFFD54F)
val Amber400 = Color(0xFFFFCA28)
val Amber500 = Color(0xFFFFC107)  // Accent
val Amber600 = Color(0xFFFFB300)
val Amber700 = Color(0xFFFFA000)
val Amber800 = Color(0xFFFF8F00)
val Amber900 = Color(0xFFFF6F00)

// 🔹 Status colors
val GreenSuccess = Color(0xFF4CAF50)
val GreenDark = Color(0xFF2E7D32)
val RedError = Color(0xFFF44336)
val RedDark = Color(0xFFC62828)
val OrangeWarning = Color(0xFFFF9800)
val OrangeDark = Color(0xFFEF6C00)
val BlueInfo = Color(0xFF2196F3)
val BlueInfoDark = Color(0xFF1565C0)

// 🔹 Neutral Grays
val Gray50 = Color(0xFFFAFAFA)
val Gray100 = Color(0xFFF5F5F5)
val Gray200 = Color(0xFFEEEEEE)
val Gray300 = Color(0xFFE0E0E0)
val Gray400 = Color(0xFFBDBDBD)
val Gray500 = Color(0xFF9E9E9E)
val Gray600 = Color(0xFF757575)
val Gray700 = Color(0xFF616161)
val Gray800 = Color(0xFF424242)
val Gray900 = Color(0xFF212121)

// 🔹 Extra professional tones
val Indigo500 = Color(0xFF3F51B5)
val Indigo700 = Color(0xFF303F9F)
val Purple500 = Color(0xFF9C27B0)
val Purple700 = Color(0xFF7B1FA2)
val Cyan500 = Color(0xFF00BCD4)
val Cyan700 = Color(0xFF0097A7)
val Pink500 = Color(0xFFE91E63)
val Pink700 = Color(0xFFC2185B)
val Lime500 = Color(0xFFCDDC39)
val Lime700 = Color(0xFFAFB42B)
val Brown500 = Color(0xFF795548)
val Brown700 = Color(0xFF5D4037)


// 🔹 Blue gradients
val BlueGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF2196F3), // Blue500
        Color(0xFF1565C0)  // Blue800
    )
)

val TealGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF80CBC4), // Teal200
        Color(0xFF004D40)  // Teal900
    )
)

// 🔹 Warm gradients
val SunsetGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFF8A65), // Orange-ish
        Color(0xFFD84315)  // Deep Orange
    )
)

val PinkPurpleGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFE91E63), // Pink500
        Color(0xFF9C27B0)  // Purple500
    )
)

// 🔹 Neutral gradients
val GrayGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFEEEEEE), // Light Gray
        Color(0xFF424242)  // Dark Gray
    )
)

// 🔹 Fancy gradients
val RainbowGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFE91E63), // Pink
        Color(0xFFFFC107), // Amber
        Color(0xFF4CAF50), // Green
        Color(0xFF2196F3), // Blue
        Color(0xFF9C27B0)  // Purple
    )
)

// 🔹 Soft pastel pink gradient
val PinkPastelGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFDEE9), // very light pink
        Color(0xFFB5FFFC)  // soft minty blue
    )
)

// 🔹 Warm rosy gradient
val RoseGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFF9A9E), // pink
        Color(0xFFFAD0C4)  // light peach
    )
)

// 🔹 Elegant pink–purple gradient
val PinkPurpleGradient2 = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFBC2EB), // soft pink
        Color(0xFFA6C1EE)  // lavender purple
    )
)

// 🔹 Peachy morning gradient
val PeachGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFECD2), // peach cream
        Color(0xFFFCB69F)  // warm pinkish orange
    )
)

