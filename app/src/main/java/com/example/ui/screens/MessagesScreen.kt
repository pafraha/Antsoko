package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ConversationEntity
import com.example.ui.components.AntsoirayHeader
import com.example.ui.components.ContactAvatar
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessagesScreen(
    conversations: List<ConversationEntity>,
    searchQuery: String,
    currentFilter: String,
    onSearchQueryChange: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    onConversationClick: (String) -> Unit,
    onNewMessageClick: () -> Unit,
    onTogglePin: (String, Boolean) -> Unit,
    onToggleFollowUp: (String, Boolean) -> Unit,
    onDeleteConversation: (String) -> Unit
) {
    Scaffold(
        containerColor = MidnightNavy,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewMessageClick,
                containerColor = CyanAccent,
                contentColor = MidnightNavy,
                shape = CircleShape,
                modifier = Modifier.testTag("compose_sms_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nouveau SMS",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AntsoirayHeader(
                title = "Messages & SMS",
                subtitle = "Boîte de réception Talkyto synchronisée"
            )

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        "Rechercher un message ou contact...",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Recherche",
                        tint = TextSecondary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Effacer",
                                tint = TextSecondary
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyanAccent,
                    unfocusedBorderColor = BorderSubtle,
                    focusedContainerColor = CardNavy,
                    unfocusedContainerColor = CardNavy,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("messages_search_field")
            )

            // Filter Chips (Tous, Non lus, Épinglés, À suivre)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf(
                    "ALL" to "Tous",
                    "UNREAD" to "Non lus",
                    "PINNED" to "Épinglés",
                    "FOLLOW_UP" to "À suivre"
                )

                items(filters) { (key, label) ->
                    val isSelected = currentFilter == key
                    FilterChip(
                        selected = isSelected,
                        onClick = { onFilterChange(key) },
                        label = {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = CardNavy,
                            labelColor = TextSecondary,
                            selectedContainerColor = CyanAccent,
                            selectedLabelColor = MidnightNavy
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = BorderSubtle,
                            selectedBorderColor = CyanAccent,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            // Conversations List
            if (conversations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Aucune conversation",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Appuyez sur + pour envoyer un SMS à un contact",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(conversations, key = { it.phoneNumber }) { conv ->
                        ConversationCard(
                            conversation = conv,
                            onClick = { onConversationClick(conv.phoneNumber) },
                            onTogglePin = { onTogglePin(conv.phoneNumber, !conv.isPinned) },
                            onToggleFollowUp = { onToggleFollowUp(conv.phoneNumber, !conv.isFollowUp) },
                            onDelete = { onDeleteConversation(conv.phoneNumber) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationCard(
    conversation: ConversationEntity,
    onClick: () -> Unit,
    onTogglePin: () -> Unit,
    onToggleFollowUp: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (conversation.isPinned) SurfaceElevated else CardNavy
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (conversation.isPinned) CyanAccent.copy(alpha = 0.3f) else BorderSubtle
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("conversation_item_${conversation.phoneNumber}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContactAvatar(
                name = conversation.contactName ?: conversation.phoneNumber,
                size = 48.dp,
                textSize = 18
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = conversation.contactName ?: conversation.phoneNumber,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TextPrimary,
                                fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (conversation.isPinned) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.PushPin,
                                contentDescription = "Épinglé",
                                tint = CyanAccent,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        if (conversation.isFollowUp) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "À suivre",
                                tint = Color(0xFFFFB703),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Text(
                        text = formatTimestamp(conversation.lastTimestamp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (conversation.unreadCount > 0) CyanAccent else TextMuted,
                            fontSize = 11.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.lastMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (conversation.unreadCount > 0) TextPrimary else TextSecondary,
                            fontWeight = if (conversation.unreadCount > 0) FontWeight.Medium else FontWeight.Normal
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (conversation.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Badge(
                            containerColor = CyanAccent,
                            contentColor = MidnightNavy
                        ) {
                            Text(
                                text = conversation.unreadCount.toString(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Options conversation",
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(SurfaceElevated)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (conversation.isPinned) "Détacher de l'accueil" else "Épingler en haut",
                                        color = TextPrimary
                                    )
                                },
                                onClick = {
                                    onTogglePin()
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (conversation.isFollowUp) "Retirer du suivi" else "Marquer à suivre",
                                        color = TextPrimary
                                    )
                                },
                                onClick = {
                                    onToggleFollowUp()
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text("Supprimer la conversation", color = Color(0xFFEF476F))
                                },
                                onClick = {
                                    onDelete()
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val now = Date()
    val isToday = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date) ==
            SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(now)

    return if (isToday) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
    } else {
        SimpleDateFormat("dd MMM", Locale.FRENCH).format(date)
    }
}
