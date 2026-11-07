package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ActiveCallState
import com.example.ui.CallStatus
import com.example.ui.components.ContactAvatar
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CoralEndCall
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.SurfaceNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun ActiveCallScreen(
    callState: ActiveCallState,
    onAnswerCall: () -> Unit,
    onDeclineCall: () -> Unit,
    onEndCall: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onToggleMute: () -> Unit,
    onToggleHold: () -> Unit,
    onToggleKeypad: () -> Unit,
    onSendDtmf: (Char) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("active_call_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Line indicator and status
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CardNavy,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (callState.status == CallStatus.CONNECTED) EmeraldSuccess else CyanAccent)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = callState.lineUsed,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                Text(
                    text = when (callState.status) {
                        CallStatus.DIALING -> "Appel en cours de numérotation..."
                        CallStatus.INCOMING_RINGING -> "Appel entrant..."
                        CallStatus.CONNECTED -> if (callState.isOnHold) "En attente..." else "Appel connecté • Voix HD"
                        CallStatus.ENDED -> "Appel terminé"
                        else -> ""
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (callState.isOnHold) CyanAccent else TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            // Center: Contact Info & Pulse Avatar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .scale(if (callState.status == CallStatus.DIALING || callState.status == CallStatus.INCOMING_RINGING) pulseScale else 1f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(CyanAccent.copy(alpha = 0.1f))
                    )
                    ContactAvatar(
                        name = callState.contactName ?: callState.phoneNumber,
                        size = 110.dp,
                        textSize = 40
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = callState.contactName ?: "Correspondant",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = callState.phoneNumber,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = CyanAccent,
                        letterSpacing = 1.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Call Duration Counter
                if (callState.status == CallStatus.CONNECTED) {
                    val minutes = callState.durationSeconds / 60
                    val seconds = callState.durationSeconds % 60
                    Text(
                        text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                    )
                }
            }

            // In-call Keypad overlay if toggled
            AnimatedVisibility(visible = callState.showKeypad) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardNavy)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Touches DTMF: ${callState.inCallDtmfString.ifEmpty { "Tapez un chiffre" }}",
                        style = MaterialTheme.typography.bodySmall.copy(color = CyanAccent)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf('1', '2', '3', '4', '5', '6').forEach { digit ->
                            Surface(
                                shape = CircleShape,
                                color = SurfaceNavy,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { onSendDtmf(digit) }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(digit.toString(), color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Controls & Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (callState.status == CallStatus.CONNECTED || callState.status == CallStatus.DIALING) {
                    // Call in progress controls (Speaker, Mute, Hold, Keypad)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CallControlButton(
                            icon = if (callState.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            label = if (callState.isMuted) "Micro coupé" else "Muet",
                            isActive = callState.isMuted,
                            onClick = onToggleMute,
                            testTag = "call_mute_toggle"
                        )
                        CallControlButton(
                            icon = Icons.Default.Dialpad,
                            label = "Clavier",
                            isActive = callState.showKeypad,
                            onClick = onToggleKeypad,
                            testTag = "call_keypad_toggle"
                        )
                        CallControlButton(
                            icon = if (callState.isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                            label = "Haut-parleur",
                            isActive = callState.isSpeakerOn,
                            onClick = onToggleSpeaker,
                            testTag = "call_speaker_toggle"
                        )
                        CallControlButton(
                            icon = if (callState.isOnHold) Icons.Default.PlayArrow else Icons.Default.Pause,
                            label = if (callState.isOnHold) "Reprendre" else "Attente",
                            isActive = callState.isOnHold,
                            onClick = onToggleHold,
                            testTag = "call_hold_toggle"
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // End Call button
                    FloatingActionButton(
                        onClick = onEndCall,
                        containerColor = CoralEndCall,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(72.dp)
                            .testTag("end_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallEnd,
                            contentDescription = "Raccrocher",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                } else if (callState.status == CallStatus.INCOMING_RINGING) {
                    // Incoming call: Accept & Decline
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Decline Call
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            FloatingActionButton(
                                onClick = onDeclineCall,
                                containerColor = CoralEndCall,
                                contentColor = Color.White,
                                shape = CircleShape,
                                modifier = Modifier
                                    .size(68.dp)
                                    .testTag("decline_call_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CallEnd,
                                    contentDescription = "Refuser l'appel",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Refuser",
                                style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary)
                            )
                        }

                        // Accept Call
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            FloatingActionButton(
                                onClick = onAnswerCall,
                                containerColor = EmeraldSuccess,
                                contentColor = MidnightNavy,
                                shape = CircleShape,
                                modifier = Modifier
                                    .size(68.dp)
                                    .testTag("answer_call_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = "Répondre",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Accepter",
                                style = MaterialTheme.typography.labelMedium.copy(color = EmeraldSuccess)
                            )
                        }
                    }
                } else if (callState.status == CallStatus.ENDED) {
                    Text(
                        text = "Appel terminé • Enregistré dans l'historique",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                    )
                }
            }
        }
    }
}

@Composable
private fun CallControlButton(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }.testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(if (isActive) CyanAccent else CardNavy)
                .border(1.dp, if (isActive) CyanAccent else BorderSubtle, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) MidnightNavy else TextPrimary,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isActive) CyanAccent else TextSecondary,
                fontSize = 11.sp
            )
        )
    }
}
