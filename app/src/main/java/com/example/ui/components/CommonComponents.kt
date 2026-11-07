package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainTab
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.SurfaceNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AntsoirayHeader(
    title: String,
    subtitle: String? = null,
    activeLineName: String? = "SIM Personnelle • +261 34 00 000 01",
    actionContent: (@Composable () -> Unit)? = null
) {
    Surface(
        color = SurfaceNavy,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CyanAccent.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "DEMO",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CyanAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary
                            )
                        )
                    }
                }

                if (actionContent != null) {
                    actionContent()
                }
            }

            if (activeLineName != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CardNavy)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(EmeraldSuccess)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = activeLineName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AntsoirayBottomNav(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    unreadMessagesCount: Int = 0,
    missedCallsCount: Int = 0
) {
    NavigationBar(
        containerColor = MidnightNavy,
        contentColor = TextSecondary,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            NavTabItem("Clavier", MainTab.DIALER, Icons.Filled.Call, Icons.Outlined.Call, "nav_dialer", 0),
            NavTabItem("Messages", MainTab.MESSAGES, Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline, "nav_messages", unreadMessagesCount),
            NavTabItem("Contacts", MainTab.CONTACTS, Icons.Filled.Contacts, Icons.Outlined.Contacts, "nav_contacts", 0),
            NavTabItem("Récents", MainTab.RECENTS, Icons.Filled.History, Icons.Outlined.History, "nav_recents", missedCallsCount),
            NavTabItem("Paramètres", MainTab.SETTINGS, Icons.Filled.Settings, Icons.Outlined.Settings, "nav_settings", 0)
        )

        items.forEach { item ->
            val isSelected = currentTab == item.tab
            NavigationBarItem(
                modifier = Modifier.testTag(item.testTag),
                selected = isSelected,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    if (item.badgeCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = CyanAccent,
                                    contentColor = MidnightNavy
                                ) {
                                    Text(item.badgeCount.toString(), fontWeight = FontWeight.Bold)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MidnightNavy,
                    selectedTextColor = CyanAccent,
                    indicatorColor = CyanAccent,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}

private data class NavTabItem(
    val label: String,
    val tab: MainTab,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String,
    val badgeCount: Int = 0
)

@Composable
fun ContactAvatar(
    name: String,
    colorHex: String? = null,
    size: Dp = 44.dp,
    textSize: Int = 16
) {
    val initial = if (name.isNotBlank()) name.take(1).uppercase() else "?"
    val bgBrush = when {
        colorHex != null -> {
            try {
                val parsedColor = Color(android.graphics.Color.parseColor(colorHex))
                Brush.linearGradient(listOf(parsedColor, parsedColor.copy(alpha = 0.7f)))
            } catch (e: Exception) {
                Brush.linearGradient(listOf(CyanAccent, Color(0xFF0077B6)))
            }
        }
        else -> Brush.linearGradient(listOf(CyanAccent, Color(0xFF0077B6)))
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(bgBrush),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = textSize.sp
            )
        )
    }
}
