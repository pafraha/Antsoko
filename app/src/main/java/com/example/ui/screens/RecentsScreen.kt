package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallMissed
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Voicemail
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CallLogEntity
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecentsScreen(
    allCalls: List<CallLogEntity>,
    missedCalls: List<CallLogEntity>,
    voicemails: List<CallLogEntity>,
    playingVoicemailId: Long?,
    voicemailProgress: Float,
    onCallClick: (String, String?) -> Unit,
    onSmsClick: (String, String?) -> Unit,
    onTogglePlayVoicemail: (CallLogEntity) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Tous les appels", "Manqués", "Messagerie vocale")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightNavy)
    ) {
        AntsoirayHeader(
            title = "Historique & Appels",
            subtitle = "Journal des communications voix et VoIP Talkyto"
        )

        // Custom TabRow
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = SurfaceNavy,
            contentColor = CyanAccent,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = CyanAccent,
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) CyanAccent else TextSecondary
                            )
                        )
                    },
                    modifier = Modifier.testTag("recents_tab_$index")
                )
            }
        }

        // Active List according to selected tab
        when (selectedTabIndex) {
            0 -> CallsList(
                calls = allCalls,
                emptyMessage = "Aucun historique d'appel",
                onCallClick = onCallClick,
                onSmsClick = onSmsClick
            )
            1 -> CallsList(
                calls = missedCalls,
                emptyMessage = "Aucun appel manqué",
                onCallClick = onCallClick,
                onSmsClick = onSmsClick
            )
            2 -> VoicemailList(
                voicemails = voicemails,
                playingVoicemailId = playingVoicemailId,
                voicemailProgress = voicemailProgress,
                onTogglePlay = onTogglePlayVoicemail,
                onCallClick = onCallClick
            )
        }
    }
}

@Composable
private fun CallsList(
    calls: List<CallLogEntity>,
    emptyMessage: String,
    onCallClick: (String, String?) -> Unit,
    onSmsClick: (String, String?) -> Unit
) {
    if (calls.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emptyMessage,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(calls, key = { it.id }) { call ->
                CallLogCard(
                    call = call,
                    onCallClick = { onCallClick(call.phoneNumber, call.contactName) },
                    onSmsClick = { onSmsClick(call.phoneNumber, call.contactName) }
                )
            }
        }
    }
}

@Composable
private fun CallLogCard(
    call: CallLogEntity,
    onCallClick: () -> Unit,
    onSmsClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardNavy),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("call_log_${call.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Direction icon
            val (icon, tint) = when (call.callType) {
                "INCOMING" -> Icons.AutoMirrored.Filled.CallReceived to EmeraldSuccess
                "OUTGOING" -> Icons.AutoMirrored.Filled.CallMade to CyanAccent
                "MISSED" -> Icons.AutoMirrored.Filled.CallMissed to CoralEndCall
                "BLOCKED" -> Icons.Default.Block to Color(0xFF64748B)
                else -> Icons.AutoMirrored.Filled.CallMade to TextSecondary
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(tint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = call.callType,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = call.contactName ?: call.phoneNumber,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = if (call.callType == "MISSED") CoralEndCall else TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = formatCallTimestamp(call.timestamp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    )
                    if (call.durationSeconds > 0) {
                        Text(
                            text = " • ${formatDuration(call.durationSeconds)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                    Text(
                        text = " • ${call.callCost}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = CyanAccent.copy(alpha = 0.8f),
                            fontSize = 10.sp
                        )
                    )
                }

                Text(
                    text = call.lineUsed,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                )
            }

            // Quick actions
            IconButton(
                onClick = onSmsClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "SMS",
                    tint = CyanAccent,
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(
                onClick = onCallClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Rappeler",
                    tint = EmeraldSuccess,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun VoicemailList(
    voicemails: List<CallLogEntity>,
    playingVoicemailId: Long?,
    voicemailProgress: Float,
    onTogglePlay: (CallLogEntity) -> Unit,
    onCallClick: (String, String?) -> Unit
) {
    if (voicemails.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Voicemail,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Aucun message vocal enregistré",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(voicemails, key = { it.id }) { vm ->
                val isPlaying = playingVoicemailId == vm.id

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CardNavy),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isPlaying) CyanAccent else BorderSubtle
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("voicemail_${vm.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ContactAvatar(
                                name = vm.contactName ?: vm.phoneNumber,
                                size = 42.dp,
                                textSize = 16
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = vm.contactName ?: vm.phoneNumber,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "${formatCallTimestamp(vm.timestamp)} • Durée: ${vm.voicemailDuration}s",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            IconButton(
                                onClick = { onCallClick(vm.phoneNumber, vm.contactName) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = "Rappeler",
                                    tint = EmeraldSuccess,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Audio Player Widget
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceElevated)
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { onTogglePlay(vm) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Écouter",
                                    tint = CyanAccent,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                LinearProgressIndicator(
                                    progress = { if (isPlaying) voicemailProgress else 0f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = CyanAccent,
                                    trackColor = MidnightNavy
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = if (isPlaying) {
                                    val curSec = (voicemailProgress * vm.voicemailDuration).toInt()
                                    "${curSec}s"
                                } else {
                                    "${vm.voicemailDuration}s"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        // Transcript preview
                        if (!vm.voicemailTranscript.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MidnightNavy.copy(alpha = 0.5f)
                            ) {
                                Text(
                                    text = "Transcription : \"${vm.voicemailTranscript}\"",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontStyle = FontStyle.Italic,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    ),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatCallTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val now = Date()
    val isToday = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date) ==
            SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(now)

    return if (isToday) {
        "Aujourd'hui ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)}"
    } else {
        SimpleDateFormat("dd MMM • HH:mm", Locale.FRENCH).format(date)
    }
}

private fun formatDuration(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return if (m > 0) "${m}m ${s}s" else "${s}s"
}
