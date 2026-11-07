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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContactEntity
import com.example.ui.components.AntsoirayHeader
import com.example.ui.components.ContactAvatar
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
fun ContactsScreen(
    contacts: List<ContactEntity>,
    favoriteContacts: List<ContactEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onContactClick: (ContactEntity) -> Unit,
    onCallClick: (String, String?) -> Unit,
    onSmsClick: (String, String?) -> Unit,
    onToggleFavorite: (ContactEntity) -> Unit,
    onAddContactClick: () -> Unit
) {
    Scaffold(
        containerColor = MidnightNavy,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddContactClick,
                containerColor = CyanAccent,
                contentColor = MidnightNavy,
                shape = CircleShape,
                modifier = Modifier.testTag("add_contact_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter un contact",
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
                title = "Contacts",
                subtitle = "${contacts.size} contacts enregistrés • Synchronisation SIM"
            )

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        "Rechercher un nom, numéro, société...",
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
                    .testTag("contacts_search_field")
            )

            // Favorites Bar (if available)
            if (favoriteContacts.isNotEmpty() && searchQuery.isBlank()) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "FAVORIS RAPIDES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CyanAccent,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(favoriteContacts, key = { it.id }) { fav ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { onContactClick(fav) }
                                    .padding(vertical = 4.dp)
                            ) {
                                ContactAvatar(
                                    name = fav.name,
                                    colorHex = fav.avatarColorHex,
                                    size = 52.dp,
                                    textSize = 20
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = fav.name.split(" ").firstOrNull() ?: fav.name,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Grouped Contacts List
            val groupedContacts = contacts.groupBy { it.name.firstOrNull()?.uppercaseChar() ?: '#' }
                .toSortedMap()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                groupedContacts.forEach { (initial, contactList) ->
                    item(key = "header_$initial") {
                        Text(
                            text = initial.toString(),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = CyanAccent,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)
                        )
                    }

                    items(contactList, key = { it.id }) { contact ->
                        ContactListItem(
                            contact = contact,
                            onClick = { onContactClick(contact) },
                            onCallClick = { onCallClick(contact.phoneNumber, contact.name) },
                            onSmsClick = { onSmsClick(contact.phoneNumber, contact.name) },
                            onToggleFavorite = { onToggleFavorite(contact) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactListItem(
    contact: ContactEntity,
    onClick: () -> Unit,
    onCallClick: () -> Unit,
    onSmsClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardNavy),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("contact_item_${contact.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContactAvatar(
                name = contact.name,
                colorHex = contact.avatarColorHex,
                size = 44.dp,
                textSize = 17
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (contact.isBlocked) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Block,
                            contentDescription = "Bloqué",
                            tint = Color(0xFFEF476F),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = contact.phoneNumber,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = CyanAccent,
                            fontSize = 12.sp
                        )
                    )
                    if (contact.company != null) {
                        Text(
                            text = " • ${contact.company}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Quick actions
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (contact.isFavorite) Icons.Default.Star else Icons.Outlined.StarOutline,
                    contentDescription = "Favori",
                    tint = if (contact.isFavorite) Color(0xFFFFB703) else TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = onSmsClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "SMS",
                    tint = CyanAccent,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = onCallClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Appeler",
                    tint = EmeraldSuccess,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
