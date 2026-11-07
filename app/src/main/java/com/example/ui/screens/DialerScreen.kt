package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialerScreen(
    dialerNumber: String,
    selectedLine: String,
    suggestions: List<ContactEntity>,
    onDigitClick: (Char) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onLineSelect: (String) -> Unit,
    onCallClick: (String, String) -> Unit,
    onSendSmsClick: (String) -> Unit,
    onSuggestionSelect: (ContactEntity) -> Unit
) {
    var showLineMenu by remember { mutableStateOf(false) }

    val lineOptions = listOf(
        "SIM Personnelle (+261 34 00 000 01)",
        "VoIP Cloud Pro (+261 20 22 000 01)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightNavy)
    ) {
        AntsoirayHeader(
            title = "Clavier d'appel",
            subtitle = "Appels voix SIM & VoIP haute définition",
            activeLineName = selectedLine
        )

        // Line selector toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Surface(
                    onClick = { showLineMenu = true },
                    shape = RoundedCornerShape(20.dp),
                    color = CardNavy,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.testTag("line_selector_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneInTalk,
                            contentDescription = null,
                            tint = CyanAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (selectedLine.contains("VoIP")) "Ligne VoIP Pro" else "Ligne SIM Personnelle",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Changer de ligne",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = showLineMenu,
                    onDismissRequest = { showLineMenu = false },
                    modifier = Modifier.background(SurfaceElevated)
                ) {
                    lineOptions.forEach { line ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (selectedLine == line) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = CyanAccent,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Text(
                                        text = line,
                                        color = TextPrimary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            },
                            onClick = {
                                onLineSelect(line)
                                showLineMenu = false
                            }
                        )
                    }
                }
            }
        }

        // Contact Suggestions (Search as you type)
        AnimatedVisibility(visible = suggestions.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(suggestions) { contact ->
                    Card(
                        onClick = { onSuggestionSelect(contact) },
                        colors = CardDefaults.cardColors(containerColor = CardNavy),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("suggestion_${contact.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ContactAvatar(name = contact.name, colorHex = contact.avatarColorHex, size = 28.dp, textSize = 12)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = contact.name,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = contact.phoneNumber,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = CyanAccent,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.1f))

        // Dialed number display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (dialerNumber.isEmpty()) "Composez un numéro" else dialerNumber,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = if (dialerNumber.isEmpty()) TextMuted else TextPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.testTag("dialer_display_text")
                )
                if (dialerNumber.isNotEmpty()) {
                    Text(
                        text = "Numéro personnel présenté au correspondant",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = EmeraldSuccess,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3x4 Keypad Grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val keyRows = listOf(
                listOf(KeypadKey('1', ""), KeypadKey('2', "ABC"), KeypadKey('3', "DEF")),
                listOf(KeypadKey('4', "GHI"), KeypadKey('5', "JKL"), KeypadKey('6', "MNO")),
                listOf(KeypadKey('7', "PQRS"), KeypadKey('8', "TUV"), KeypadKey('9', "WXYZ")),
                listOf(KeypadKey('*', ""), KeypadKey('0', "+"), KeypadKey('#', ""))
            )

            keyRows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { key ->
                        KeypadButton(
                            digit = key.digit,
                            letters = key.letters,
                            onClick = { onDigitClick(key.digit) },
                            onLongClick = {
                                if (key.digit == '0') {
                                    onDigitClick('+')
                                }
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom Actions: SMS Shortcut, Green Call Button, Backspace Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // SMS Quick Action
            IconButton(
                onClick = {
                    if (dialerNumber.isNotBlank()) onSendSmsClick(dialerNumber)
                },
                enabled = dialerNumber.isNotBlank(),
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(if (dialerNumber.isNotBlank()) CardNavy else Color.Transparent)
                    .testTag("dialer_sms_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "Envoyer SMS",
                    tint = if (dialerNumber.isNotBlank()) CyanAccent else TextMuted
                )
            }

            // Big Call Button
            FloatingActionButton(
                onClick = {
                    if (dialerNumber.isNotBlank()) {
                        onCallClick(dialerNumber, selectedLine)
                    }
                },
                containerColor = EmeraldSuccess,
                contentColor = MidnightNavy,
                shape = CircleShape,
                modifier = Modifier
                    .size(68.dp)
                    .testTag("dialer_call_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Lancer l'appel",
                    modifier = Modifier.size(34.dp)
                )
            }

            // Backspace / Clear Action
            IconButton(
                onClick = onBackspaceClick,
                enabled = dialerNumber.isNotEmpty(),
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(if (dialerNumber.isNotEmpty()) CardNavy else Color.Transparent)
                    .testTag("dialer_backspace_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Backspace,
                    contentDescription = "Effacer chiffre",
                    tint = if (dialerNumber.isNotEmpty()) TextSecondary else TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

private data class KeypadKey(val digit: Char, val letters: String)

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun KeypadButton(
    digit: Char,
    letters: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = CardNavy,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier
            .size(66.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .testTag("keypad_btn_$digit")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = digit.toString(),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
            if (letters.isNotEmpty()) {
                Text(
                    text = letters,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}
