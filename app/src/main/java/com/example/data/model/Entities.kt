package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phoneNumber: String,
    val secondaryPhoneNumber: String? = null,
    val email: String? = null,
    val company: String? = null,
    val jobTitle: String? = null,
    val label: String = "Mobile", // "Mobile", "Travail", "Personnel"
    val isFavorite: Boolean = false,
    val isBlocked: Boolean = false,
    val notes: String? = null,
    val avatarColorHex: String = "#00B4D8"
)

@Entity(tableName = "call_logs")
data class CallLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val phoneNumber: String,
    val contactName: String?,
    val callType: String, // "INCOMING", "OUTGOING", "MISSED", "REJECTED", "BLOCKED"
    val timestamp: Long = System.currentTimeMillis(),
    val durationSeconds: Int = 0,
    val lineUsed: String = "SIM Personnelle (+261 34 00 000 01)",
    val callCost: String = "0.00 Ar (Demo)",
    val isVoicemail: Boolean = false,
    val voicemailDuration: Int = 0,
    val voicemailTranscript: String? = null,
    val voicemailListened: Boolean = false
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val conversationPhone: String,
    val senderPhone: String,
    val senderName: String?,
    val body: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromMe: Boolean = false,
    val status: String = "RECEIVED", // "SENDING", "SENT", "DELIVERED", "READ", "RECEIVED"
    val attachmentUrl: String? = null,
    val attachmentType: String? = null, // "IMAGE", "DOCUMENT", "AUDIO"
    val isScheduled: Boolean = false,
    val scheduledTime: Long = 0
)

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey
    val phoneNumber: String,
    val contactName: String?,
    val lastMessage: String,
    val lastTimestamp: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isFollowUp: Boolean = false
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val fullName: String = "Franco Demo",
    val phoneNumber: String = "+261 34 00 000 01",
    val email: String = "franco.demo@antsoiray.test",
    val company: String = "Antsoiray Telecom",
    val role: String = "Administrateur",
    val isDemoMode: Boolean = true,
    val activeLine: String = "SIM Personnelle (+261 34 00 000 01)",
    val secondaryLine: String? = "VoIP Antsoiray Pro (+261 20 22 000 01)",
    val voipEnabled: Boolean = true,
    val cellularEnabled: Boolean = true,
    val callForwarding: Boolean = false,
    val autoReply: Boolean = false,
    val autoReplyMessage: String = "Bonjour, je suis actuellement indisponible. Je vous recontacte dès que possible.",
    val ringtone: String = "Antsoiray Pulse",
    val crmIntegrationEnabled: Boolean = true
)

@Entity(tableName = "team_members")
data class TeamMemberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val role: String, // "Admin", "Agent Commercial", "Support Client"
    val phoneNumber: String,
    val email: String,
    val canMakeCalls: Boolean = true,
    val canSendSms: Boolean = true,
    val assignedNumber: String,
    val callsToday: Int = 0,
    val smsToday: Int = 0
)
