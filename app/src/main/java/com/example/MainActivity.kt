package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AntsoirayViewModel
import com.example.ui.CallStatus
import com.example.ui.MainTab
import com.example.ui.components.AntsoirayBottomNav
import com.example.ui.dialogs.AddContactDialog
import com.example.ui.dialogs.NewMessageDialog
import com.example.ui.dialogs.ScheduleSmsDialog
import com.example.ui.screens.ActiveCallScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ContactDetailSheet
import com.example.ui.screens.ContactsScreen
import com.example.ui.screens.ConversationDetailScreen
import com.example.ui.screens.DialerScreen
import com.example.ui.screens.MessagesScreen
import com.example.ui.screens.RecentsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.AntsoirayTheme
import com.example.ui.theme.MidnightNavy

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AntsoirayTheme(darkTheme = true) {
                AntsoirayApp()
            }
        }
    }
}

@Composable
fun AntsoirayApp(viewModel: AntsoirayViewModel = viewModel()) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val callState by viewModel.callState.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()

    val activeConversationPhone by viewModel.activeConversationPhone.collectAsStateWithLifecycle()
    val activeContactDetail by viewModel.activeContactDetail.collectAsStateWithLifecycle()

    val showAddContactDialog by viewModel.showAddContactDialog.collectAsStateWithLifecycle()
    val showNewMessageDialog by viewModel.showNewMessageDialog.collectAsStateWithLifecycle()
    val showScheduleSmsDialog by viewModel.showScheduleSmsDialog.collectAsStateWithLifecycle()

    // 1. Authentication Check
    if (!authState.isLoggedIn) {
        AuthScreen(
            authState = authState,
            onPhoneChange = viewModel::setAuthPhone,
            onOtpChange = viewModel::setAuthOtp,
            onNameChange = viewModel::setAuthName,
            onEmailChange = viewModel::setAuthEmail,
            onCountryCodeChange = viewModel::setAuthCountryCode,
            onProceedToOtp = viewModel::proceedToOtp,
            onVerifyOtp = viewModel::verifyOtp,
            onFillDemoOtp = viewModel::fillDemoOtp,
            onCompleteRegistration = viewModel::completeRegistration,
            onQuickDemoLogin = viewModel::quickDemoLogin
        )
        return
    }

    // 2. Full-screen active call overlay if in a call
    if (callState.status != CallStatus.IDLE) {
        ActiveCallScreen(
            callState = callState,
            onAnswerCall = viewModel::answerCall,
            onDeclineCall = viewModel::declineCall,
            onEndCall = viewModel::endCall,
            onToggleSpeaker = viewModel::toggleSpeaker,
            onToggleMute = viewModel::toggleMute,
            onToggleHold = viewModel::toggleHold,
            onToggleKeypad = viewModel::toggleInCallKeypad,
            onSendDtmf = viewModel::appendInCallDtmf
        )
        return
    }

    // 3. Conversation Detail Screen
    if (activeConversationPhone != null) {
        val phone = activeConversationPhone!!
        val activeMessages by viewModel.getActiveMessages(phone).collectAsStateWithLifecycle()
        val allConvs by viewModel.allConversations.collectAsStateWithLifecycle()
        val currentConv = allConvs.firstOrNull { it.phoneNumber == phone }

        ConversationDetailScreen(
            contactPhone = phone,
            contactName = currentConv?.contactName,
            messages = activeMessages,
            onBack = viewModel::closeConversation,
            onCallClick = { targetPhone ->
                viewModel.startCall(targetPhone, currentConv?.contactName)
            },
            onSendMessage = { text ->
                viewModel.sendMessage(phone, text, currentConv?.contactName)
            },
            onSendMms = { caption, url ->
                viewModel.sendMms(phone, caption, url, currentConv?.contactName)
            },
            onOpenScheduleDialog = viewModel::openScheduleSmsDialog,
            onDeleteConversation = {
                viewModel.deleteConversation(phone)
            }
        )

        if (showScheduleSmsDialog) {
            ScheduleSmsDialog(
                contactPhone = phone,
                onDismiss = viewModel::closeScheduleSmsDialog,
                onSchedule = { minutes, text ->
                    viewModel.scheduleMessage(phone, text, minutes, currentConv?.contactName)
                    viewModel.closeScheduleSmsDialog()
                }
            )
        }
        return
    }

    // 4. Contact Detail Screen
    if (activeContactDetail != null) {
        val contact = activeContactDetail!!
        ContactDetailSheet(
            contact = contact,
            onBack = viewModel::closeContactDetail,
            onCallClick = { targetPhone, name ->
                viewModel.startCall(targetPhone, name, "SIM Personnelle (+261 34 00 000 01)")
            },
            onVoipCallClick = { targetPhone, name ->
                viewModel.startCall(targetPhone, name, "VoIP Cloud Pro (+261 20 22 000 01)")
            },
            onSmsClick = { targetPhone, _ ->
                viewModel.closeContactDetail()
                viewModel.openConversation(targetPhone)
            },
            onToggleFavorite = { viewModel.toggleContactFavorite(contact) },
            onToggleBlock = { viewModel.toggleContactBlock(contact) },
            onDeleteContact = { viewModel.deleteContact(contact) }
        )
        return
    }

    // 5. Main Screen with Bottom Navigation
    val allConversations by viewModel.allConversations.collectAsStateWithLifecycle()
    val missedCalls by viewModel.missedCalls.collectAsStateWithLifecycle()
    val totalUnreadSms = allConversations.sumOf { it.unreadCount }

    Scaffold(
        containerColor = MidnightNavy,
        bottomBar = {
            AntsoirayBottomNav(
                currentTab = currentTab,
                onTabSelected = viewModel::selectTab,
                unreadMessagesCount = totalUnreadSms,
                missedCallsCount = missedCalls.size
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MidnightNavy)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tabTransition"
            ) { tab ->
                when (tab) {
                    MainTab.DIALER -> {
                        val dialerNumber by viewModel.dialerNumber.collectAsStateWithLifecycle()
                        val selectedLine by viewModel.selectedLine.collectAsStateWithLifecycle()
                        val suggestions by viewModel.dialerSuggestions.collectAsStateWithLifecycle()

                        DialerScreen(
                            dialerNumber = dialerNumber,
                            selectedLine = selectedLine,
                            suggestions = suggestions,
                            onDigitClick = viewModel::appendDialerDigit,
                            onBackspaceClick = viewModel::deleteDialerDigit,
                            onClearClick = viewModel::clearDialer,
                            onLineSelect = viewModel::setSelectedLine,
                            onCallClick = { number, line ->
                                viewModel.startCall(number, null, line)
                            },
                            onSendSmsClick = { number ->
                                viewModel.openConversation(number)
                            },
                            onSuggestionSelect = { contact ->
                                viewModel.setDialerNumber(contact.phoneNumber)
                            }
                        )
                    }

                    MainTab.MESSAGES -> {
                        val conversations by viewModel.filteredConversations.collectAsStateWithLifecycle()
                        val searchQuery by viewModel.messagesSearchQuery.collectAsStateWithLifecycle()
                        val filter by viewModel.messagesFilter.collectAsStateWithLifecycle()

                        MessagesScreen(
                            conversations = conversations,
                            searchQuery = searchQuery,
                            currentFilter = filter,
                            onSearchQueryChange = viewModel::setMessagesSearchQuery,
                            onFilterChange = viewModel::setMessagesFilter,
                            onConversationClick = viewModel::openConversation,
                            onNewMessageClick = viewModel::openNewMessageDialog,
                            onTogglePin = viewModel::togglePinConversation,
                            onToggleFollowUp = viewModel::toggleFollowUpConversation,
                            onDeleteConversation = viewModel::deleteConversation
                        )
                    }

                    MainTab.CONTACTS -> {
                        val contacts by viewModel.filteredContacts.collectAsStateWithLifecycle()
                        val favorites by viewModel.favoriteContacts.collectAsStateWithLifecycle()
                        val searchQuery by viewModel.contactsSearchQuery.collectAsStateWithLifecycle()

                        ContactsScreen(
                            contacts = contacts,
                            favoriteContacts = favorites,
                            searchQuery = searchQuery,
                            onSearchQueryChange = viewModel::setContactsSearchQuery,
                            onContactClick = viewModel::openContactDetail,
                            onCallClick = { phone, name ->
                                viewModel.startCall(phone, name)
                            },
                            onSmsClick = { phone, _ ->
                                viewModel.openConversation(phone)
                            },
                            onToggleFavorite = viewModel::toggleContactFavorite,
                            onAddContactClick = viewModel::openAddContactDialog
                        )
                    }

                    MainTab.RECENTS -> {
                        val allCalls by viewModel.allCallLogs.collectAsStateWithLifecycle()
                        val missed by viewModel.missedCalls.collectAsStateWithLifecycle()
                        val voicemails by viewModel.voicemails.collectAsStateWithLifecycle()
                        val playingVoicemailId by viewModel.playingVoicemailId.collectAsStateWithLifecycle()
                        val voicemailProgress by viewModel.voicemailProgress.collectAsStateWithLifecycle()

                        RecentsScreen(
                            allCalls = allCalls,
                            missedCalls = missed,
                            voicemails = voicemails,
                            playingVoicemailId = playingVoicemailId,
                            voicemailProgress = voicemailProgress,
                            onCallClick = { phone, name ->
                                viewModel.startCall(phone, name)
                            },
                            onSmsClick = { phone, _ ->
                                viewModel.openConversation(phone)
                            },
                            onTogglePlayVoicemail = viewModel::togglePlayVoicemail
                        )
                    }

                    MainTab.SETTINGS -> {
                        val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
                        val teamMembers by viewModel.teamMembers.collectAsStateWithLifecycle()

                        SettingsScreen(
                            userProfile = userProfile,
                            teamMembers = teamMembers,
                            onUpdateProfile = viewModel::updateProfile,
                            onResetDemoData = viewModel::resetDemoData,
                            onSimulateIncomingCall = {
                                viewModel.simulateIncomingCall("+261 34 00 000 02", "Jean Rakoto")
                            },
                            onSimulateIncomingSms = {
                                viewModel.simulateIncomingSms(
                                    phone = "+261 38 00 000 05",
                                    name = "Sophie Demo",
                                    text = "Coucou Franco ! Nouveau message reçu sur ta ligne personnelle Antsoiray 🚀"
                                )
                            },
                            onLogout = viewModel::logout
                        )
                    }
                }
            }
        }
    }

    // Modal Dialogs
    if (showAddContactDialog) {
        AddContactDialog(
            onDismiss = viewModel::closeAddContactDialog,
            onSaveContact = { name, phone, secPhone, email, company, job, label, notes ->
                viewModel.saveContact(
                    name = name,
                    phone = phone,
                    secondaryPhone = secPhone,
                    email = email,
                    company = company,
                    jobTitle = job,
                    label = label,
                    notes = notes
                )
            }
        )
    }

    if (showNewMessageDialog) {
        val allContactsList by viewModel.allContacts.collectAsStateWithLifecycle()
        NewMessageDialog(
            contacts = allContactsList,
            onDismiss = viewModel::closeNewMessageDialog,
            onStartConversation = { phone, name, msg ->
                viewModel.closeNewMessageDialog()
                viewModel.sendMessage(phone, msg, name)
                viewModel.openConversation(phone)
            }
        )
    }
}
