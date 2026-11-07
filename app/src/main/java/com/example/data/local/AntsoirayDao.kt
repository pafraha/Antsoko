package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CallLogEntity
import com.example.data.model.ContactEntity
import com.example.data.model.ConversationEntity
import com.example.data.model.MessageEntity
import com.example.data.model.TeamMemberEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AntsoirayDao {

    // --- Contacts ---
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE isBlocked = 1 ORDER BY name ASC")
    fun getBlockedContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    suspend fun getContactById(id: Long): ContactEntity?

    @Query("SELECT * FROM contacts WHERE phoneNumber = :phoneNumber OR secondaryPhoneNumber = :phoneNumber LIMIT 1")
    suspend fun getContactByPhone(phoneNumber: String): ContactEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<ContactEntity>)

    @Update
    suspend fun updateContact(contact: ContactEntity)

    @Delete
    suspend fun deleteContact(contact: ContactEntity)

    @Query("DELETE FROM contacts")
    suspend fun clearContacts()

    // --- Call Logs ---
    @Query("SELECT * FROM call_logs ORDER BY timestamp DESC")
    fun getAllCallLogs(): Flow<List<CallLogEntity>>

    @Query("SELECT * FROM call_logs WHERE callType = 'MISSED' ORDER BY timestamp DESC")
    fun getMissedCalls(): Flow<List<CallLogEntity>>

    @Query("SELECT * FROM call_logs WHERE isVoicemail = 1 ORDER BY timestamp DESC")
    fun getVoicemails(): Flow<List<CallLogEntity>>

    @Query("SELECT * FROM call_logs WHERE phoneNumber = :phoneNumber ORDER BY timestamp DESC LIMIT 20")
    fun getCallsForPhone(phoneNumber: String): Flow<List<CallLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLog(callLog: CallLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLogs(callLogs: List<CallLogEntity>)

    @Delete
    suspend fun deleteCallLog(callLog: CallLogEntity)

    @Query("UPDATE call_logs SET voicemailListened = 1 WHERE id = :id")
    suspend fun markVoicemailListened(id: Long)

    @Query("DELETE FROM call_logs")
    suspend fun clearCallLogs()

    // --- Messages & Conversations ---
    @Query("SELECT * FROM conversations ORDER BY isPinned DESC, lastTimestamp DESC")
    fun getAllConversations(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE phoneNumber = :phone LIMIT 1")
    suspend fun getConversation(phone: String): ConversationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<ConversationEntity>)

    @Query("UPDATE conversations SET isPinned = :isPinned WHERE phoneNumber = :phone")
    suspend fun togglePinConversation(phone: String, isPinned: Boolean)

    @Query("UPDATE conversations SET isFollowUp = :isFollowUp WHERE phoneNumber = :phone")
    suspend fun toggleFollowUpConversation(phone: String, isFollowUp: Boolean)

    @Query("UPDATE conversations SET unreadCount = 0 WHERE phoneNumber = :phone")
    suspend fun markConversationRead(phone: String)

    @Query("DELETE FROM conversations WHERE phoneNumber = :phone")
    suspend fun deleteConversation(phone: String)

    @Query("SELECT * FROM messages WHERE conversationPhone = :phone ORDER BY timestamp ASC")
    fun getMessagesForConversation(phone: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("DELETE FROM messages WHERE conversationPhone = :phone")
    suspend fun deleteMessagesForConversation(phone: String)

    @Query("DELETE FROM messages")
    suspend fun clearMessages()

    @Query("DELETE FROM conversations")
    suspend fun clearConversations()

    // --- User Profile & Settings ---
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)

    // --- Team Members ---
    @Query("SELECT * FROM team_members ORDER BY name ASC")
    fun getAllTeamMembers(): Flow<List<TeamMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamMembers(members: List<TeamMemberEntity>)

    @Update
    suspend fun updateTeamMember(member: TeamMemberEntity)

    @Delete
    suspend fun deleteTeamMember(member: TeamMemberEntity)

    @Query("DELETE FROM team_members")
    suspend fun clearTeamMembers()
}
