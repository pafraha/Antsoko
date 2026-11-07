package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Webhook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TeamMemberEntity
import com.example.data.model.UserProfileEntity
import com.example.ui.components.AntsoirayHeader
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
fun SettingsScreen(
    userProfile: UserProfileEntity?,
    teamMembers: List<TeamMemberEntity>,
    onUpdateProfile: (UserProfileEntity) -> Unit,
    onResetDemoData: () -> Unit,
    onSimulateIncomingCall: () -> Unit,
    onSimulateIncomingSms: () -> Unit,
    onLogout: () -> Unit
) {
    val profile = userProfile ?: UserProfileEntity()
    var showTeamManagement by remember { mutableStateOf(false) }
    var showAnalytics by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .verticalScroll(rememberScrollState())
    ) {
        AntsoirayHeader(
            title = "Paramètres & Entreprise",
            subtitle = "Identité SIM, lignes Talkyto Pro et gestion d'équipe"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card (Franco Demo)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardNavy),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_profile_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ContactAvatar(name = profile.fullName, size = 56.dp, textSize = 22)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = profile.fullName,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = EmeraldSuccess.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "Actif",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = EmeraldSuccess,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text(
                                text = profile.phoneNumber,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = CyanAccent,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )

                            Text(
                                text = "${profile.company} • ${profile.role}",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = BorderSubtle)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Règle Antsoiray : Le numéro personnel constitue l'identité téléphonique. Aucun numéro virtuel de remplacement n'est imposé.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // SIM & Telecom Lines Management
            SettingsSectionHeader(title = "LIGNES TÉLÉPHONIQUES")
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CardNavy),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    SettingsRow(
                        icon = Icons.Default.SimCard,
                        title = "Ligne Principale (SIM)",
                        subtitle = profile.phoneNumber,
                        trailing = {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = EmeraldSuccess.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "SIM 1 Active",
                                    color = EmeraldSuccess,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    )
                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(start = 54.dp))
                    SettingsRow(
                        icon = Icons.Default.Phone,
                        title = "Ligne VoIP Cloud Pro",
                        subtitle = profile.secondaryLine ?: "+261 20 22 000 01",
                        trailing = {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = CyanAccent.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "VoIP Connectée",
                                    color = CyanAccent,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    )
                }
            }

            // Call & SMS Configuration
            SettingsSectionHeader(title = "CONFIGURATION DES APPELS & SMS")
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CardNavy),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    SettingsSwitchRow(
                        icon = Icons.Default.Call,
                        title = "Appels VoIP via Internet (WiFi/4G)",
                        subtitle = "Qualité audio HD Opus et réduction des coûts",
                        isChecked = profile.voipEnabled,
                        onCheckedChange = { onUpdateProfile(profile.copy(voipEnabled = it)) }
                    )
                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(start = 54.dp))
                    SettingsSwitchRow(
                        icon = Icons.Default.ChatBubble,
                        title = "Répondeur SMS automatique",
                        subtitle = "Envoi automatique d'un SMS en cas d'indisponibilité",
                        isChecked = profile.autoReply,
                        onCheckedChange = { onUpdateProfile(profile.copy(autoReply = it)) }
                    )
                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(start = 54.dp))
                    SettingsSwitchRow(
                        icon = Icons.Default.Sync,
                        title = "Renvoi d'appel vers ligne secondaire",
                        subtitle = "Bascule automatique en cas de non-réponse",
                        isChecked = profile.callForwarding,
                        onCheckedChange = { onUpdateProfile(profile.copy(callForwarding = it)) }
                    )
                }
            }

            // Professional Talkyto Pro Features
            SettingsSectionHeader(title = "FONCTIONNALITÉS PROFESSIONNELLES")
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CardNavy),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Team members row
                    SettingsRow(
                        icon = Icons.Default.Group,
                        title = "Gestion d'équipe & Droits d'accès",
                        subtitle = "${teamMembers.size} collaborateurs (Admin, Agents, Support)",
                        trailing = {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = null,
                                tint = TextSecondary
                            )
                        },
                        onClick = { showTeamManagement = !showTeamManagement }
                    )

                    // Expandable team list
                    AnimatedVisibility(visible = showTeamManagement) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceNavy)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            teamMembers.forEach { member ->
                                TeamMemberCard(member = member)
                            }
                        }
                    }

                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(start = 54.dp))

                    // Analytics Dashboard row
                    SettingsRow(
                        icon = Icons.Default.Analytics,
                        title = "Tableau de bord communications",
                        subtitle = "Statistiques d'appels, durée et quotas SMS",
                        trailing = {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = null,
                                tint = TextSecondary
                            )
                        },
                        onClick = { showAnalytics = !showAnalytics }
                    )

                    // Expandable Analytics
                    AnimatedVisibility(visible = showAnalytics) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceNavy)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Statistiques de l'entreprise (Aujourd'hui)",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = CyanAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                StatCard(title = "Appels passés", value = "87", icon = Icons.Default.Call)
                                StatCard(title = "Durée totale", value = "4h 12m", icon = Icons.Default.Speed)
                                StatCard(title = "SMS envoyés", value = "114", icon = Icons.Default.ChatBubble)
                            }
                        }
                    }

                    HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(start = 54.dp))

                    // CRM & Webhooks
                    SettingsSwitchRow(
                        icon = Icons.Default.Webhook,
                        title = "Intégration CRM & Webhooks",
                        subtitle = "Synchronisation automatique des appels avec le CRM",
                        isChecked = profile.crmIntegrationEnabled,
                        onCheckedChange = { onUpdateProfile(profile.copy(crmIntegrationEnabled = it)) }
                    )
                }
            }

            // Sandbox & Prototype Actions (Specification §8)
            SettingsSectionHeader(title = "SIMULATEUR DE PROTOTYPE & TESTS")
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Outils de démonstration Talkyto / Antsoiray :",
                        style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onSimulateIncomingCall,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess, contentColor = MidnightNavy),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("simulate_incoming_call_button")
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Appel entrant", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onSimulateIncomingSms,
                            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = MidnightNavy),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("simulate_incoming_sms_button")
                        ) {
                            Icon(Icons.Default.ChatBubble, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("SMS entrant", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    OutlinedButton(
                        onClick = onResetDemoData,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFFB703)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB703)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reset_demo_data_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Réinitialiser les données de démonstration")
                    }
                }
            }

            // Logout & Account recovery
            OutlinedButton(
                onClick = onLogout,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CoralEndCall),
                border = androidx.compose.foundation.BorderStroke(1.dp, CoralEndCall),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("logout_button")
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Se déconnecter (Tester l'inscription par numéro)")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
            color = CyanAccent,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        ),
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailing: @Composable () -> Unit,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = CyanAccent,
            modifier = Modifier.size(24.dp)
        )
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
        trailing()
    }
}

@Composable
private fun SettingsSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = CyanAccent,
            modifier = Modifier.size(24.dp)
        )
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
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MidnightNavy,
                checkedTrackColor = CyanAccent,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = SurfaceNavy
            )
        )
    }
}

@Composable
private fun TeamMemberCard(member: TeamMemberEntity) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = CardNavy),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContactAvatar(name = member.name, size = 36.dp, textSize = 14)
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "${member.role} • ${member.assignedNumber}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = CyanAccent,
                        fontSize = 10.sp
                    )
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${member.callsToday} appels • ${member.smsToday} SMS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, icon: ImageVector) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = CardNavy),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
            Text(text = title, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 9.sp))
        }
    }
}
