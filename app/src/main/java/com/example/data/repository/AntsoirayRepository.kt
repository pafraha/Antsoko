package com.example.data.repository

import com.example.data.DemoDataProvider
import com.example.data.local.AntsoirayDao
import com.example.data.model.CallLogEntity
import com.example.data.model.ContactEntity
import com.example.data.model.ConversationEntity
import com.example.data.model.MessageEntity
import com.example.data.model.TeamMemberEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AntsoirayRepository(private val dao: AntsoirayDao) {

    val allContacts: Flow<List<ContactEntity>> = dao.getAllContacts()
    val favoriteContacts: Flow<List<ContactEntity>> = dao.getFavoriteContacts()
    val blockedContacts: Flow<List<ContactEntity>> = dao.getBlockedContacts()
    val allCallLogs: Flow<List<CallLogEntity>> = dao.getAllCallLogs()
    val missedCalls: Flow<List<CallLogEntity>> = dao.getMissedCalls()
    val voicemails: Flow<List<CallLogEntity>> = dao.getVoicemails()
    val allConversations: Flow<List<ConversationEntity>> = dao.getAllConversations()
    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()
    val teamMembers: Flow<List<TeamMemberEntity>> = dao.getAllTeamMembers()

    suspend fun initializeDatabase() {
        val existingContacts = dao.getAllContacts().firstOrNull()
        if (existingContacts.isNullOrEmpty()) {
            DemoDataProvider.seedInitialData(dao, forceReset = false)
        }
    }

    suspend fun resetDemoData() {
        DemoDataProvider.seedInitialData(dao, forceReset = true)
    }

    // --- Contacts ---
    suspend fun addContact(contact: ContactEntity): Long = dao.insertContact(contact)

    suspend fun updateContact(contact: ContactEntity) = dao.updateContact(contact)

    suspend fun deleteContact(contact: ContactEntity) = dao.deleteContact(contact)

    suspend fun toggleFavorite(contact: ContactEntity) {
        dao.updateContact(contact.copy(isFavorite = !contact.isFavorite))
    }

    suspend fun toggleBlock(contact: ContactEntity) {
        dao.updateContact(contact.copy(isBlocked = !contact.isBlocked))
    }

    suspend fun findContactByPhone(phone: String): ContactEntity? = dao.getContactByPhone(phone)

    // --- Call Logs ---
    suspend fun logCall(
        phoneNumber: String,
        contactName: String?,
        callType: String,
        durationSeconds: Int = 0,
        lineUsed: String = "SIM Personnelle (+261 34 00 000 01)",
        callCost: String = "0.00 Ar (Demo)"
    ): Long {
        return dao.insertCallLog(
            CallLogEntity(
                phoneNumber = phoneNumber,
                contactName = contactName,
                callType = callType,
                timestamp = System.currentTimeMillis(),
                durationSeconds = durationSeconds,
                lineUsed = lineUsed,
                callCost = callCost
            )
        )
    }

    suspend fun deleteCallLog(callLog: CallLogEntity) = dao.deleteCallLog(callLog)

    suspend fun markVoicemailListened(id: Long) = dao.markVoicemailListened(id)

    fun getCallsForPhone(phone: String): Flow<List<CallLogEntity>> = dao.getCallsForPhone(phone)

    // --- Messages & Conversations ---
    fun getMessagesForConversation(phone: String): Flow<List<MessageEntity>> =
        dao.getMessagesForConversation(phone)

    suspend fun sendMessage(
        toPhone: String,
        body: String,
        contactName: String?,
        scope: CoroutineScope,
        attachmentUrl: String? = null,
        attachmentType: String? = null
    ) {
        val timestamp = System.currentTimeMillis()

        // 1. Insert our outgoing message
        dao.insertMessage(
            MessageEntity(
                conversationPhone = toPhone,
                senderPhone = "+261 34 00 000 01",
                senderName = "Franco Demo",
                body = body,
                timestamp = timestamp,
                isFromMe = true,
                status = "SENT",
                attachmentUrl = attachmentUrl,
                attachmentType = attachmentType
            )
        )

        // 2. Upsert conversation
        val existing = dao.getConversation(toPhone)
        val conversation = ConversationEntity(
            phoneNumber = toPhone,
            contactName = contactName ?: existing?.contactName,
            lastMessage = body,
            lastTimestamp = timestamp,
            unreadCount = 0,
            isPinned = existing?.isPinned ?: false,
            isFollowUp = existing?.isFollowUp ?: false
        )
        dao.insertConversation(conversation)

        // 3. Simulate automated contact reply for realism in prototype mode!
        scope.launch(Dispatchers.IO) {
            delay(2500)
            val replyText = generateSimulatedReply(body, contactName ?: toPhone)
            val replyTimestamp = System.currentTimeMillis()

            dao.insertMessage(
                MessageEntity(
                    conversationPhone = toPhone,
                    senderPhone = toPhone,
                    senderName = contactName,
                    body = replyText,
                    timestamp = replyTimestamp,
                    isFromMe = false,
                    status = "RECEIVED"
                )
            )

            dao.insertConversation(
                conversation.copy(
                    lastMessage = replyText,
                    lastTimestamp = replyTimestamp,
                    unreadCount = (existing?.unreadCount ?: 0) + 1
                )
            )
        }
    }

    private fun generateSimulatedReply(originalMessage: String, contactName: String): String {
        val lower = originalMessage.lowercase()
        return when {
            lower.contains("bonjour") || lower.contains("salut") ->
                "Bonjour Franco ! J'ai bien reçu ton message sur Antsoiray, tout fonctionne parfaitement."
            lower.contains("reunion") || lower.contains("rdv") || lower.contains("heure") ->
                "C'est noté pour le créneau ! Je réserve la salle et je te confirme par SMS."
            lower.contains("voip") || lower.contains("appel") ->
                "La qualité audio VoIP de l'application est impressionnante, aucun lag !"
            lower.contains("devis") || lower.contains("contrat") || lower.contains("prix") ->
                "Je t'envoie les documents signés d'ici cet après-midi."
            else ->
                "Merci pour l'information Franco ! Bien reçu via Antsoiray."
        }
    }

    suspend fun togglePinConversation(phone: String, isPinned: Boolean) {
        dao.togglePinConversation(phone, isPinned)
    }

    suspend fun toggleFollowUpConversation(phone: String, isFollowUp: Boolean) {
        dao.toggleFollowUpConversation(phone, isFollowUp)
    }

    suspend fun markConversationRead(phone: String) {
        dao.markConversationRead(phone)
    }

    suspend fun deleteConversation(phone: String) {
        dao.deleteConversation(phone)
        dao.deleteMessagesForConversation(phone)
    }

    // --- Profile & Settings ---
    suspend fun updateProfile(profile: UserProfileEntity) {
        dao.saveUserProfile(profile)
    }

    // --- Simulation Triggers ---
    suspend fun simulateIncomingCall(
        phone: String = "+261 34 00 000 02",
        name: String = "Jean Rakoto"
    ) {
        // Will be picked up by in-call state machine in ViewModel
    }

    suspend fun simulateIncomingSms(
        phone: String = "+261 38 00 000 05",
        name: String = "Sophie Demo",
        body: String = "Coucou Franco ! Nouveau test SMS entrant simulé sur le numéro personnel Antsoiray 🚀"
    ) {
        val timestamp = System.currentTimeMillis()
        dao.insertMessage(
            MessageEntity(
                conversationPhone = phone,
                senderPhone = phone,
                senderName = name,
                body = body,
                timestamp = timestamp,
                isFromMe = false,
                status = "RECEIVED"
            )
        )
        val existing = dao.getConversation(phone)
        dao.insertConversation(
            ConversationEntity(
                phoneNumber = phone,
                contactName = name,
                lastMessage = body,
                lastTimestamp = timestamp,
                unreadCount = (existing?.unreadCount ?: 0) + 1,
                isPinned = existing?.isPinned ?: false,
                isFollowUp = existing?.isFollowUp ?: false
            )
        )
    }
}
