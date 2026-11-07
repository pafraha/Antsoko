package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContactEntity
import com.example.ui.components.ContactAvatar
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CoralEndCall
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ContactDetailSheet(
    contact: ContactEntity,
    onBack: () -> Unit,
    onCallClick: (String, String?) -> Unit,
    onVoipCallClick: (String, String?) -> Unit,
    onSmsClick: (String, String?) -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleBlock: () -> Unit,
    onDeleteContact: () -> Unit
) {
    BackHandler {
        onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Surface(color = SurfaceNavy, modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("contact_detail_back")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour",
                        tint = TextPrimary
                    )
                }

                Row {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (contact.isFavorite) Icons.Default.Star else Icons.Outlined.StarOutline,
                            contentDescription = "Favori",
                            tint = if (contact.isFavorite) Color(0xFFFFB703) else TextSecondary
                        )
                    }
                    IconButton(onClick = onDeleteContact) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Supprimer",
                            tint = CoralEndCall
                        )
                    }
                }
            }
        }

        // Profile Hero
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ContactAvatar(
                name = contact.name,
                colorHex = contact.avatarColorHex,
                size = 90.dp,
                textSize = 36
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = contact.name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            )

            if (contact.jobTitle != null || contact.company != null) {
                Spacer(modifier = Modifier.height(4.dp))
                val desc = listOfNotNull(contact.jobTitle, contact.company).joinToString(" • ")
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )
            }

            if (contact.isBlocked) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CoralEndCall.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CoralEndCall)
                ) {
                    Text(
                        text = "Contact bloqué",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CoralEndCall,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Quick Buttons (Appel SIM, Appel VoIP, SMS)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickActionButton(
                    icon = Icons.Default.Call,
                    label = "Appel SIM",
                    accentColor = EmeraldSuccess,
                    onClick = { onCallClick(contact.phoneNumber, contact.name) },
                    testTag = "contact_detail_call_sim"
                )
                QuickActionButton(
                    icon = Icons.Default.PhoneInTalk,
                    label = "Appel VoIP",
                    accentColor = CyanAccent,
                    onClick = { onVoipCallClick(contact.phoneNumber, contact.name) },
                    testTag = "contact_detail_call_voip"
                )
                QuickActionButton(
                    icon = Icons.Default.ChatBubble,
                    label = "SMS Talkyto",
                    accentColor = Color(0xFF4CC9F0),
                    onClick = { onSmsClick(contact.phoneNumber, contact.name) },
                    testTag = "contact_detail_sms"
                )
            }
        }

        // Contact Information Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Primary Phone Card
            InfoRowCard(
                icon = Icons.Default.Phone,
                title = contact.phoneNumber,
                subtitle = "${contact.label} (Principal)"
            )

            // Secondary Phone (if exists)
            if (contact.secondaryPhoneNumber != null) {
                InfoRowCard(
                    icon = Icons.Default.Phone,
                    title = contact.secondaryPhoneNumber,
                    subtitle = "Ligne secondaire"
                )
            }

            // Email Card
            if (contact.email != null) {
                InfoRowCard(
                    icon = Icons.Default.Email,
                    title = contact.email,
                    subtitle = "Adresse e-mail"
                )
            }

            // Company Card
            if (contact.company != null) {
                InfoRowCard(
                    icon = Icons.Default.Business,
                    title = contact.company,
                    subtitle = contact.jobTitle ?: "Entreprise"
                )
            }

            // Notes Card
            if (!contact.notes.isNullOrBlank()) {
                InfoRowCard(
                    icon = Icons.Default.Notes,
                    title = contact.notes,
                    subtitle = "Remarques & contexte"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Block / Unblock Contact Button
            OutlinedButton(
                onClick = onToggleBlock,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (contact.isBlocked) EmeraldSuccess else CoralEndCall
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (contact.isBlocked) EmeraldSuccess else CoralEndCall
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("contact_detail_block_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Block,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (contact.isBlocked) "Débloquer ce contact" else "Bloquer ce contact (Appels & SMS)"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(CardNavy)
                .border(1.dp, accentColor.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = accentColor,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
private fun InfoRowCard(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardNavy),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CyanAccent,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
