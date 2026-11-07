package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val AntsoirayDarkColorScheme = darkColorScheme(
    primary = CyanAccent,
    onPrimary = MidnightNavy,
    primaryContainer = MidnightNavyVariant,
    onPrimaryContainer = CyanAccent,
    secondary = EmeraldSuccess,
    onSecondary = Color.Black,
    secondaryContainer = CardNavy,
    onSecondaryContainer = TextPrimary,
    tertiary = AmberWarning,
    onTertiary = Color.Black,
    background = MidnightNavy,
    onBackground = TextPrimary,
    surface = SurfaceNavy,
    onSurface = TextPrimary,
    surfaceVariant = CardNavy,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    error = CoralEndCall,
    onError = Color.White
)

private val AntsoirayLightColorScheme = lightColorScheme(
    primary = Color(0xFF0077B6),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCAF0F8),
    onPrimaryContainer = Color(0xFF03045E),
    secondary = Color(0xFF059669),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1FAE5),
    onSecondaryContainer = Color(0xFF065F46),
    tertiary = Color(0xFFD97706),
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    error = Color(0xFFDC2626),
    onError = Color.White
)

@Composable
fun AntsoirayTheme(
    darkTheme: Boolean = true, // Default to sleek dark telecom theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> AntsoirayDarkColorScheme
        else -> AntsoirayLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
