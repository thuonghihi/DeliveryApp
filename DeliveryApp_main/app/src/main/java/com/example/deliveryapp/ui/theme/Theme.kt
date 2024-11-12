package com.example.deliveryapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.deliveryapp.R

private val DarkColorScheme = darkColorScheme(
    primary = Color.Black,
    onPrimary = Color.White, //text tren background
    onSecondary = Color.LightGray, //textnote
    tertiary = Color(0xFF3569CC) // link
)

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    onPrimary = Color.Black, //text tren background
    onSecondary = Color.Gray, //text note
    tertiary = Color(0xFF3569CC)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val LightColorPalette = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    // Đặt màu nền cho ứng dụng
    background = Color.White,
    surface = Color.White,
    // Bạn có thể thêm các màu khác ở đây
)

val customFontFamily = FontFamily(
    Font(R.font.mulish, FontWeight.Normal)
)

val customTypography = Typography(
     displayLarge = TextStyle(
        fontFamily = customFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 27.sp
    ))

@Composable
fun DeliveryAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme, // Sử dụng colorScheme đã tính toán
        typography = customTypography, // Sử dụng typography tùy chỉnh
        content = content
    )
}
