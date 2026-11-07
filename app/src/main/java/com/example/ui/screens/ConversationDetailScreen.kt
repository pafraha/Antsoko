package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MessageEntity
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ConversationDetailScreen(
    contactPhone: String,
    contactName: String?,
    messages: List<MessageEntity>,
    onBack: () -> Unit,
    onCallClick: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onSendMms: (String, String) -> Unit,
    onOpenScheduleDialog: () -> Unit,
    onDeleteConversation: () -> Unit
) {
    BackHandler {
        onBack()
    }

    var textInput by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }
    var showAttachmentSheet by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Top App Bar
        Surface(
            color = SurfaceNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("conversation_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour",
                        tint = TextPrimary
                    )
                }

                ContactAvatar(
                    name = contactName ?: contactPhone,
                    size = 40.dp,
                    textSize = 15
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = contactName ?: contactPhone,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = contactPhone,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = CyanAccent,
                            fontSize = 11.sp
                        )
                    )
                }

                // Call direct button
                IconButton(
                    onClick = { onCallClick(contactPhone) },
                    modifier = Modifier.testTag("conversation_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Appeler",
                        tint = EmeraldSuccess
                    )
                }

                // Menu
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = TextSecondary
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(SurfaceElevated)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Programmer un SMS...", color = TextPrimary) },
                            onClick = {
                                showMenu = false
                                onOpenScheduleDialog()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Supprimer la conversation", color = Color(0xFFEF476F)) },
                            onClick = {
                                showMenu = false
                                onDeleteConversation()
                            }
                        )
                    }
                }
            }
        }

        // Sub-header notice
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardNavy.copy(alpha = 0.6f))
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "💬 Expéditeur : Numéro personnel (+261 34 00 000 01) • SMS/MMS Pro",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            )
        }

        // Messages Bubble Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                MessageBubble(message = msg)
            }
        }

        // MMS quick picker bar if expanded
        if (showAttachmentSheet) {
            Card(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Joindre un fichier MMS simulé",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        AttachmentOptionItem(
                            icon = Icons.Default.Image,
                            label = "Maquette UI",
                            onClick = {
                                onSendMms(
                                    "Voici la maquette de l'écran Antsoiray",
                                    "https://images.unsplash.com/photo-1551650975-87deedd944c3?w=600"
                                )
                                showAttachmentSheet = false
                            }
                        )
                        AttachmentOptionItem(
                            icon = Icons.Default.AttachFile,
                            label = "Devis PDF",
                            onClick = {
                                onSendMessage("📎 Pièce jointe : Devis_Antsoiray_2026.pdf (145 Ko)")
                                showAttachmentSheet = false
                            }
                        )
                        AttachmentOptionItem(
                            icon = Icons.Default.Schedule,
                            label = "Programmer",
                            onClick = {
                                showAttachmentSheet = false
                                onOpenScheduleDialog()
                            }
                        )
                    }
                }
            }
        }

        // Bottom Input Row
        Surface(
            color = SurfaceNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showAttachmentSheet = !showAttachmentSheet },
                    modifier = Modifier.testTag("attach_mms_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Joindre pièce",
                        tint = if (showAttachmentSheet) CyanAccent else TextSecondary
                    )
                }

                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = {
                        Text(
                            "Message SMS...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("message_input_field"),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = BorderSubtle,
                        focusedContainerColor = CardNavy,
                        unfocusedContainerColor = CardNavy,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = {
                        if (textInput.isNotBlank()) {
                            onSendMessage(textInput)
                            textInput = ""
                        }
                    },
                    containerColor = if (textInput.isNotBlank()) CyanAccent else CardNavy,
                    contentColor = if (textInput.isNotBlank()) MidnightNavy else TextMuted,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(46.dp)
                        .testTag("send_message_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Envoyer",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: MessageEntity) {
    val isMe = message.isFromMe

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isMe) 16.dp else 4.dp,
                            bottomEnd = if (isMe) 4.dp else 16.dp
                        )
                    )
                    .background(
                        if (isMe) {
                            Brush.linearGradient(listOf(CyanAccent, Color(0xFF00B4D8)))
                        } else {
                            Brush.linearGradient(listOf(CardNavy, SurfaceElevated))
                        }
                    )
                    .border(
                        1.dp,
                        if (isMe) CyanAccent.copy(alpha = 0.5f) else BorderSubtle,
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isMe) 16.dp else 4.dp,
                            bottomEnd = if (isMe) 4.dp else 16.dp
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Column {
                    if (message.attachmentType == "IMAGE" && message.attachmentUrl != null) {
                        AsyncImage(
                            model = message.attachmentUrl,
                            contentDescription = "Pièce jointe MMS",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .padding(bottom = 6.dp)
                        )
                    }

                    Text(
                        text = message.body,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isMe) MidnightNavy else TextPrimary,
                            lineHeight = 20.sp,
                            fontWeight = if (isMe) FontWeight.SemiBold else FontWeight.Normal
                        )
                    )
                }
            }

            // Timestamp and read receipt ticks
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp)),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                )

                if (isMe) {
                    Spacer(modifier = Modifier.width(4.dp))
                    val isRead = message.status == "READ"
                    Icon(
                        imageVector = if (isRead) Icons.Default.DoneAll else Icons.Default.Done,
                        contentDescription = message.status,
                        tint = if (isRead) CyanAccent else TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AttachmentOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(CardNavy),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = CyanAccent,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
        )
    }
}
