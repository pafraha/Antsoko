package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AntsoirayDatabase
import com.example.data.model.CallLogEntity
import com.example.data.model.ContactEntity
import com.example.data.model.ConversationEntity
import com.example.data.model.MessageEntity
import com.example.data.model.TeamMemberEntity
import com.example.data.model.UserProfileEntity
import com.example.data.repository.AntsoirayRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class MainTab {
    DIALER,
    MESSAGES,
    CONTACTS,
    RECENTS,
    SETTINGS
}

enum class CallStatus {
    IDLE,
    DIALING,          // Outgoing ringing
    INCOMING_RINGING, // Incoming ring
    CONNECTED,        // In communication
    ENDED             // Call finished summary
}

data class ActiveCallState(
    val status: CallStatus = CallStatus.IDLE,
    val phoneNumber: String = "",
    val contactName: String? = null,
    val durationSeconds: Int = 0,
    val isSpeakerOn: Boolean = false,
    val isMuted: Boolean = false,
    val isOnHold: Boolean = false,
    val showKeypad: Boolean = false,
    val lineUsed: String = "SIM Personnelle (+261 34 00 000 01)",
    val inCallDtmfString: String = ""
)

data class AuthState(
    val isLoggedIn: Boolean = true,
    val registrationStep: Int = 1, // 1: Phone, 2: OTP, 3: Profile
    val inputPhone: String = "+261 34 00 000 01",
    val inputCountryCode: String = "+261",
    val inputOtp: String = "",
    val inputName: String = "",
    val inputEmail: String = "",
    val otpError: String? = null
)

class AntsoirayViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AntsoirayRepository

    init {
        val db = AntsoirayDatabase.getDatabase(application)
        repository = AntsoirayRepository(db.antsoirayDao())
        viewModelScope.launch {
            repository.initializeDatabase()
        }
    }

    // --- Tab Navigation ---
    private val _currentTab = MutableStateFlow(MainTab.DIALER)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    fun selectTab(tab: MainTab) {
        _currentTab.value = tab
    }

    // --- Sub-screen state ---
    private val _activeConversationPhone = MutableStateFlow<String?>(null)
    val activeConversationPhone: StateFlow<String?> = _activeConversationPhone.asStateFlow()

    private val _activeContactDetail = MutableStateFlow<ContactEntity?>(null)
    val activeContactDetail: StateFlow<ContactEntity?> = _activeContactDetail.asStateFlow()

    private val _showAddContactDialog = MutableStateFlow(false)
    val showAddContactDialog: StateFlow<Boolean> = _showAddContactDialog.asStateFlow()

    private val _showNewMessageDialog = MutableStateFlow(false)
    val showNewMessageDialog: StateFlow<Boolean> = _showNewMessageDialog.asStateFlow()

    private val _showScheduleSmsDialog = MutableStateFlow(false)
    val showScheduleSmsDialog: StateFlow<Boolean> = _showScheduleSmsDialog.asStateFlow()

    // --- Authentication State ---
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // --- Active Call State ---
    private val _callState = MutableStateFlow(ActiveCallState())
    val callState: StateFlow<ActiveCallState> = _callState.asStateFlow()
    private var callTimerJob: Job? = null

    // --- Voicemail Player State ---
    private val _playingVoicemailId = MutableStateFlow<Long?>(null)
    val playingVoicemailId: StateFlow<Long?> = _playingVoicemailId.asStateFlow()
    private val _voicemailProgress = MutableStateFlow(0f)
    val voicemailProgress: StateFlow<Float> = _voicemailProgress.asStateFlow()
    private var voicemailJob: Job? = null

    // --- Data Streams ---
    val allContacts: StateFlow<List<ContactEntity>> = repository.allContacts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteContacts: StateFlow<List<ContactEntity>> = repository.favoriteContacts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allCallLogs: StateFlow<List<CallLogEntity>> = repository.allCallLogs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val missedCalls: StateFlow<List<CallLogEntity>> = repository.missedCalls.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val voicemails: StateFlow<List<CallLogEntity>> = repository.voicemails.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allConversations: StateFlow<List<ConversationEntity>> = repository.allConversations.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val teamMembers: StateFlow<List<TeamMemberEntity>> = repository.teamMembers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- Dialer State ---
    private val _dialerNumber = MutableStateFlow("")
    val dialerNumber: StateFlow<String> = _dialerNumber.asStateFlow()

    private val _selectedLine = MutableStateFlow("SIM Personnelle (+261 34 00 000 01)")
    val selectedLine: StateFlow<String> = _selectedLine.asStateFlow()

    fun setSelectedLine(line: String) {
        _selectedLine.value = line
    }

    fun appendDialerDigit(char: Char) {
        _dialerNumber.update { it + char }
    }

    fun deleteDialerDigit() {
        _dialerNumber.update { if (it.isNotEmpty()) it.dropLast(1) else "" }
    }

    fun clearDialer() {
        _dialerNumber.value = ""
    }

    fun setDialerNumber(number: String) {
        _dialerNumber.value = number
    }

    // Matching contacts for dialer search-as-you-type
    val dialerSuggestions: StateFlow<List<ContactEntity>> = combine(
        allContacts,
        _dialerNumber
    ) { contacts, query ->
        if (query.isBlank()) {
            emptyList()
        } else {
            val cleanQuery = query.filter { it.isDigit() }
            contacts.filter { contact ->
                contact.phoneNumber.filter { it.isDigit() }.contains(cleanQuery) ||
                        contact.name.lowercase().contains(query.lowercase())
            }.take(3)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Call Management ---
    fun startCall(
        phoneNumber: String,
        contactName: String? = null,
        line: String = _selectedLine.value
    ) {
        if (phoneNumber.isBlank()) return

        // Resolve contact name if null
        val resolvedName = contactName ?: allContacts.value.firstOrNull {
            it.phoneNumber.filter { c -> c.isDigit() } == phoneNumber.filter { c -> c.isDigit() }
        }?.name

        _callState.value = ActiveCallState(
            status = CallStatus.DIALING,
            phoneNumber = phoneNumber,
            contactName = resolvedName,
            durationSeconds = 0,
            lineUsed = line
        )

        // Simulate connecting after 2.5 seconds
        viewModelScope.launch {
            delay(2500)
            if (_callState.value.status == CallStatus.DIALING) {
                _callState.update { it.copy(status = CallStatus.CONNECTED) }
                startCallTimer()
            }
        }
    }

    fun simulateIncomingCall(
        phone: String = "+261 34 00 000 02",
        name: String = "Jean Rakoto"
    ) {
        _callState.value = ActiveCallState(
            status = CallStatus.INCOMING_RINGING,
            phoneNumber = phone,
            contactName = name,
            durationSeconds = 0,
            lineUsed = "Ligne Personnelle (+261 34 00 000 01)"
        )
    }

    fun answerCall() {
        _callState.update { it.copy(status = CallStatus.CONNECTED) }
        startCallTimer()
    }

    fun declineCall() {
        val state = _callState.value
        callTimerJob?.cancel()
        _callState.update { it.copy(status = CallStatus.ENDED) }

        viewModelScope.launch {
            repository.logCall(
                phoneNumber = state.phoneNumber,
                contactName = state.contactName,
                callType = "REJECTED",
                durationSeconds = 0,
                lineUsed = state.lineUsed
            )
            delay(1200)
            _callState.value = ActiveCallState()
        }
    }

    fun endCall() {
        val state = _callState.value
        callTimerJob?.cancel()
        _callState.update { it.copy(status = CallStatus.ENDED) }

        viewModelScope.launch {
            val type = if (state.status == CallStatus.DIALING) "CANCELLED" else "OUTGOING"
            repository.logCall(
                phoneNumber = state.phoneNumber,
                contactName = state.contactName,
                callType = type,
                durationSeconds = state.durationSeconds,
                lineUsed = state.lineUsed,
                callCost = if (state.lineUsed.contains("VoIP")) "0.00 Ar (Demo)" else "${(state.durationSeconds / 60 + 1) * 30} Ar"
            )
            delay(1200)
            _callState.value = ActiveCallState()
        }
    }

    private fun startCallTimer() {
        callTimerJob?.cancel()
        callTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _callState.update { it.copy(durationSeconds = it.durationSeconds + 1) }
            }
        }
    }

    fun toggleSpeaker() {
        _callState.update { it.copy(isSpeakerOn = !it.isSpeakerOn) }
    }

    fun toggleMute() {
        _callState.update { it.copy(isMuted = !it.isMuted) }
    }

    fun toggleHold() {
        _callState.update { it.copy(isOnHold = !it.isOnHold) }
    }

    fun toggleInCallKeypad() {
        _callState.update { it.copy(showKeypad = !it.showKeypad) }
    }

    fun appendInCallDtmf(digit: Char) {
        _callState.update { it.copy(inCallDtmfString = it.inCallDtmfString + digit) }
    }

    // --- Messages Management ---
    private val _messagesFilter = MutableStateFlow("ALL") // "ALL", "UNREAD", "PINNED", "FOLLOW_UP"
    val messagesFilter: StateFlow<String> = _messagesFilter.asStateFlow()

    private val _messagesSearchQuery = MutableStateFlow("")
    val messagesSearchQuery: StateFlow<String> = _messagesSearchQuery.asStateFlow()

    fun setMessagesFilter(filter: String) {
        _messagesFilter.value = filter
    }

    fun setMessagesSearchQuery(query: String) {
        _messagesSearchQuery.value = query
    }

    val filteredConversations: StateFlow<List<ConversationEntity>> = combine(
        allConversations,
        _messagesFilter,
        _messagesSearchQuery
    ) { conversations, filter, query ->
        conversations.filter { conv ->
            val matchesFilter = when (filter) {
                "UNREAD" -> conv.unreadCount > 0
                "PINNED" -> conv.isPinned
                "FOLLOW_UP" -> conv.isFollowUp
                else -> true
            }
            val matchesQuery = query.isBlank() ||
                    (conv.contactName?.lowercase()?.contains(query.lowercase()) == true) ||
                    conv.phoneNumber.contains(query) ||
                    conv.lastMessage.lowercase().contains(query.lowercase())

            matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun openConversation(phone: String) {
        _activeConversationPhone.value = phone
        viewModelScope.launch {
            repository.markConversationRead(phone)
        }
    }

    fun closeConversation() {
        _activeConversationPhone.value = null
    }

    fun getActiveMessages(phone: String): StateFlow<List<MessageEntity>> {
        return repository.getMessagesForConversation(phone).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun sendMessage(toPhone: String, body: String, contactName: String? = null) {
        if (body.isBlank()) return
        viewModelScope.launch {
            repository.sendMessage(
                toPhone = toPhone,
                body = body,
                contactName = contactName,
                scope = viewModelScope
            )
        }
    }

    fun sendMms(toPhone: String, caption: String, imageUrl: String, contactName: String? = null) {
        viewModelScope.launch {
            repository.sendMessage(
                toPhone = toPhone,
                body = caption.ifBlank { "📷 Photo / MMS" },
                contactName = contactName,
                scope = viewModelScope,
                attachmentUrl = imageUrl,
                attachmentType = "IMAGE"
            )
        }
    }

    fun scheduleMessage(toPhone: String, body: String, delayMinutes: Int, contactName: String? = null) {
        viewModelScope.launch {
            // Instant feedback + delayed sending in background
            delay(1000)
            repository.sendMessage(
                toPhone = toPhone,
                body = "[Programmé] $body",
                contactName = contactName,
                scope = viewModelScope
            )
        }
    }

    fun togglePinConversation(phone: String, isPinned: Boolean) {
        viewModelScope.launch {
            repository.togglePinConversation(phone, isPinned)
        }
    }

    fun toggleFollowUpConversation(phone: String, isFollowUp: Boolean) {
        viewModelScope.launch {
            repository.toggleFollowUpConversation(phone, isFollowUp)
        }
    }

    fun deleteConversation(phone: String) {
        viewModelScope.launch {
            repository.deleteConversation(phone)
            if (_activeConversationPhone.value == phone) {
                _activeConversationPhone.value = null
            }
        }
    }

    fun simulateIncomingSms(phone: String, name: String, text: String) {
        viewModelScope.launch {
            repository.simulateIncomingSms(phone, name, text)
        }
    }

    // --- Contacts Management ---
    private val _contactsSearchQuery = MutableStateFlow("")
    val contactsSearchQuery: StateFlow<String> = _contactsSearchQuery.asStateFlow()

    fun setContactsSearchQuery(query: String) {
        _contactsSearchQuery.value = query
    }

    val filteredContacts: StateFlow<List<ContactEntity>> = combine(
        allContacts,
        _contactsSearchQuery
    ) { contacts, query ->
        if (query.isBlank()) {
            contacts
        } else {
            contacts.filter {
                it.name.lowercase().contains(query.lowercase()) ||
                        it.phoneNumber.contains(query) ||
                        (it.company?.lowercase()?.contains(query.lowercase()) == true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun openContactDetail(contact: ContactEntity) {
        _activeContactDetail.value = contact
    }

    fun closeContactDetail() {
        _activeContactDetail.value = null
    }

    fun openAddContactDialog() {
        _showAddContactDialog.value = true
    }

    fun closeAddContactDialog() {
        _showAddContactDialog.value = false
    }

    fun openNewMessageDialog() {
        _showNewMessageDialog.value = true
    }

    fun closeNewMessageDialog() {
        _showNewMessageDialog.value = false
    }

    fun openScheduleSmsDialog() {
        _showScheduleSmsDialog.value = true
    }

    fun closeScheduleSmsDialog() {
        _showScheduleSmsDialog.value = false
    }

    fun saveContact(
        name: String,
        phone: String,
        secondaryPhone: String? = null,
        email: String? = null,
        company: String? = null,
        jobTitle: String? = null,
        label: String = "Mobile",
        notes: String? = null,
        isFavorite: Boolean = false
    ) {
        viewModelScope.launch {
            val colors = listOf("#00B4D8", "#06D6A0", "#F72585", "#7209B7", "#4361EE", "#FB5607", "#3A86FF")
            val randomColor = colors.random()
            repository.addContact(
                ContactEntity(
                    name = name,
                    phoneNumber = phone,
                    secondaryPhoneNumber = secondaryPhone,
                    email = email,
                    company = company,
                    jobTitle = jobTitle,
                    label = label,
                    notes = notes,
                    isFavorite = isFavorite,
                    avatarColorHex = randomColor
                )
            )
            _showAddContactDialog.value = false
        }
    }

    fun deleteContact(contact: ContactEntity) {
        viewModelScope.launch {
            repository.deleteContact(contact)
            if (_activeContactDetail.value?.id == contact.id) {
                _activeContactDetail.value = null
            }
        }
    }

    fun toggleContactFavorite(contact: ContactEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(contact)
            if (_activeContactDetail.value?.id == contact.id) {
                _activeContactDetail.value = contact.copy(isFavorite = !contact.isFavorite)
            }
        }
    }

    fun toggleContactBlock(contact: ContactEntity) {
        viewModelScope.launch {
            repository.toggleBlock(contact)
            if (_activeContactDetail.value?.id == contact.id) {
                _activeContactDetail.value = contact.copy(isBlocked = !contact.isBlocked)
            }
        }
    }

    // --- Voicemail Player ---
    fun togglePlayVoicemail(log: CallLogEntity) {
        if (_playingVoicemailId.value == log.id) {
            // Stop
            voicemailJob?.cancel()
            _playingVoicemailId.value = null
            _voicemailProgress.value = 0f
        } else {
            // Start play
            voicemailJob?.cancel()
            _playingVoicemailId.value = log.id
            _voicemailProgress.value = 0f
            viewModelScope.launch {
                repository.markVoicemailListened(log.id)
            }
            voicemailJob = viewModelScope.launch {
                val totalDuration = if (log.voicemailDuration > 0) log.voicemailDuration else 15
                val steps = totalDuration * 10
                for (i in 1..steps) {
                    delay(100)
                    _voicemailProgress.value = i.toFloat() / steps.toFloat()
                }
                _playingVoicemailId.value = null
                _voicemailProgress.value = 0f
            }
        }
    }

    // --- Settings & Pro Administration ---
    fun resetDemoData() {
        viewModelScope.launch {
            repository.resetDemoData()
            _activeConversationPhone.value = null
            _activeContactDetail.value = null
        }
    }

    fun updateProfile(profile: UserProfileEntity) {
        viewModelScope.launch {
            repository.updateProfile(profile)
        }
    }

    // --- Auth Flow for Prototype ---
    fun logout() {
        _authState.value = AuthState(
            isLoggedIn = false,
            registrationStep = 1,
            inputPhone = "+261 34 00 000 01",
            inputOtp = ""
        )
    }

    fun setAuthPhone(phone: String) {
        _authState.update { it.copy(inputPhone = phone) }
    }

    fun setAuthOtp(otp: String) {
        _authState.update { it.copy(inputOtp = otp, otpError = null) }
    }

    fun setAuthCountryCode(code: String) {
        _authState.update { it.copy(inputCountryCode = code) }
    }

    fun setAuthName(name: String) {
        _authState.update { it.copy(inputName = name) }
    }

    fun setAuthEmail(email: String) {
        _authState.update { it.copy(inputEmail = email) }
    }

    fun proceedToOtp() {
        _authState.update { it.copy(registrationStep = 2) }
    }

    fun verifyOtp() {
        if (_authState.value.inputOtp == "123456") {
            _authState.update { it.copy(registrationStep = 3, otpError = null) }
        } else {
            _authState.update { it.copy(otpError = "Code invalide. Utilisez le code démo fixe 123456.") }
        }
    }

    fun fillDemoOtp() {
        _authState.update { it.copy(inputOtp = "123456", otpError = null) }
    }

    fun completeRegistration() {
        val name = _authState.value.inputName.ifBlank { "Franco Demo" }
        val phone = _authState.value.inputPhone.ifBlank { "+261 34 00 000 01" }
        val email = _authState.value.inputEmail.ifBlank { "franco.demo@antsoiray.test" }

        viewModelScope.launch {
            userProfile.value?.let { current ->
                repository.updateProfile(
                    current.copy(
                        fullName = name,
                        phoneNumber = phone,
                        email = email
                    )
                )
            }
            _authState.value = AuthState(isLoggedIn = true)
        }
    }

    fun quickDemoLogin() {
        _authState.value = AuthState(isLoggedIn = true)
    }
}
