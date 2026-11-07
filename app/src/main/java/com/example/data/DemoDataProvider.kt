package com.example.data

import com.example.data.local.AntsoirayDao
import com.example.data.model.CallLogEntity
import com.example.data.model.ContactEntity
import com.example.data.model.ConversationEntity
import com.example.data.model.MessageEntity
import com.example.data.model.TeamMemberEntity
import com.example.data.model.UserProfileEntity

object DemoDataProvider {

    fun getDefaultProfile(): UserProfileEntity {
        return UserProfileEntity(
            id = 1,
            fullName = "Franco Demo",
            phoneNumber = "+261 34 00 000 01",
            email = "franco.demo@antsoiray.test",
            company = "Antsoiray Telecom Corp",
            role = "Administrateur / Pro",
            isDemoMode = true,
            activeLine = "SIM Personnelle (+261 34 00 000 01)",
            secondaryLine = "Ligne VoIP Cloud (+261 20 22 000 01)",
            voipEnabled = true,
            cellularEnabled = true,
            callForwarding = false,
            autoReply = false,
            autoReplyMessage = "Bonjour, je suis actuellement en ligne ou en déplacement. Je vous réponds au plus vite !",
            ringtone = "Antsoiray Tone M3",
            crmIntegrationEnabled = true
        )
    }

    fun getDemoContacts(): List<ContactEntity> {
        return listOf(
            ContactEntity(
                name = "Jean Rakoto",
                phoneNumber = "+261 34 00 000 02",
                secondaryPhoneNumber = "+261 20 22 123 45",
                email = "jean.rakoto@madagascar-tech.mg",
                company = "Madagascar Tech Solutions",
                jobTitle = "Directeur Technique",
                label = "Mobile",
                isFavorite = true,
                avatarColorHex = "#00B4D8",
                notes = "Contact clé pour l'infrastructure réseau"
            ),
            ContactEntity(
                name = "Marie Dupont",
                phoneNumber = "+261 32 00 000 03",
                email = "marie.dupont@orange.mg",
                company = "Orange Madagascar Partner",
                jobTitle = "Responsable Partenariats",
                label = "Travail",
                isFavorite = true,
                avatarColorHex = "#FF007F",
                notes = "Discuter des forfaits voix et data le 5"
            ),
            ContactEntity(
                name = "Paul Martin",
                phoneNumber = "+261 33 00 000 04",
                secondaryPhoneNumber = "+33 6 12 34 56 78",
                email = "p.martin@antananarivo-consult.com",
                company = "Antananarivo Consult",
                jobTitle = "Consultant Télécom",
                label = "Mobile",
                isFavorite = true,
                avatarColorHex = "#06D6A0",
                notes = "Point d'étape sur les passerelles SIP"
            ),
            ContactEntity(
                name = "Sophie Demo",
                phoneNumber = "+261 38 00 000 05",
                email = "sophie.demo@antsoiray.test",
                company = "Antsoiray Core Team",
                jobTitle = "Product Manager",
                label = "Travail",
                isFavorite = true,
                avatarColorHex = "#7209B7",
                notes = "Coordination des releases de l'application"
            ),
            ContactEntity(
                name = "Rova Andrianasolo",
                phoneNumber = "+261 34 11 223 34",
                email = "rova.andria@telma-partner.mg",
                company = "Rova Conseil",
                jobTitle = "Avocate d'affaires",
                label = "Personnel",
                isFavorite = true,
                avatarColorHex = "#F72585",
                notes = "Validation contrat de licence"
            ),
            ContactEntity(
                name = "Hery Rajaonarivelo",
                phoneNumber = "+261 32 44 556 67",
                email = "hery.raj@banque-mada.mg",
                company = "Banque Centrale Mada",
                jobTitle = "Gestionnaire de compte",
                label = "Travail",
                isFavorite = false,
                avatarColorHex = "#4361EE"
            ),
            ContactEntity(
                name = "Faly Rasolofo",
                phoneNumber = "+261 33 77 889 90",
                email = "faly.rasolofo@logistics-tana.mg",
                company = "Logistique Express Tana",
                jobTitle = "Chef des opérations",
                label = "Mobile",
                isFavorite = false,
                avatarColorHex = "#4CC9F0"
            ),
            ContactEntity(
                name = "Aina Randrianarisoa",
                phoneNumber = "+261 34 99 887 76",
                email = "aina.randria@designstudio.mg",
                company = "Studio Pixel Tana",
                jobTitle = "UI/UX Designer",
                label = "Personnel",
                isFavorite = true,
                avatarColorHex = "#9B5DE5"
            ),
            ContactEntity(
                name = "Mialy Rabemananjara",
                phoneNumber = "+261 32 55 443 32",
                email = "mialy.rabe@univ-tana.mg",
                company = "Université d'Antananarivo",
                jobTitle = "Enseignante Chercheuse",
                label = "Personnel",
                isFavorite = false,
                avatarColorHex = "#F15BB5"
            ),
            ContactEntity(
                name = "Christian Razafy",
                phoneNumber = "+261 33 22 110 09",
                email = "christian.razafy@sysadmin.io",
                company = "Cloud Africa Hosting",
                jobTitle = "DevOps Engineer",
                label = "Travail",
                isFavorite = false,
                avatarColorHex = "#00BBF9"
            ),
            ContactEntity(
                name = "Bakoly Ratsimbazafy",
                phoneNumber = "+261 34 33 667 78",
                email = "bakoly.ratsimba@commerce.mg",
                company = "Bazar Be Import",
                jobTitle = "Directrice Commerciale",
                label = "Travail",
                isFavorite = false,
                avatarColorHex = "#00F5D4"
            ),
            ContactEntity(
                name = "Sarah Robinson",
                phoneNumber = "+1 415 555 0199",
                secondaryPhoneNumber = "+1 415 555 0188",
                email = "s.robinson@siliconvalley-voip.com",
                company = "Global VoIP Solutions US",
                jobTitle = "VP Engineering",
                label = "International",
                isFavorite = true,
                avatarColorHex = "#FB5607"
            ),
            ContactEntity(
                name = "David Cohen",
                phoneNumber = "+33 6 88 99 00 11",
                email = "david.cohen@paris-telecom.fr",
                company = "Paris Telecom Cloud",
                jobTitle = "Directeur Général",
                label = "International",
                isFavorite = false,
                avatarColorHex = "#3A86FF"
            ),
            ContactEntity(
                name = "Luc Besson",
                phoneNumber = "+33 1 44 55 66 77",
                email = "luc.besson@production-cin.fr",
                company = "Europa Studio",
                jobTitle = "Réalisateur & Producteur",
                label = "Travail",
                isFavorite = false,
                avatarColorHex = "#8338EC"
            ),
            ContactEntity(
                name = "Nicole Laurent",
                phoneNumber = "+261 38 12 345 67",
                email = "nicole.laurent@clinique-tana.mg",
                company = "Clinique Medicale Ankadifotsy",
                jobTitle = "Médecin Coordinateur",
                label = "Personnel",
                isFavorite = false,
                avatarColorHex = "#FFBE0B"
            ),
            ContactEntity(
                name = "Fanja Ralambo",
                phoneNumber = "+261 34 88 991 12",
                email = "fanja.ralambo@comptabilite.mg",
                company = "Cabinet Fiduciaire Tana",
                jobTitle = "Expert Comptable",
                label = "Travail",
                isFavorite = false,
                avatarColorHex = "#06D6A0"
            ),
            ContactEntity(
                name = "Tojo Ramanantoanina",
                phoneNumber = "+261 32 77 665 54",
                email = "tojo.ram@startup-mada.io",
                company = "Startup Incubator Mada",
                jobTitle = "Directeur des Programmes",
                label = "Mobile",
                isFavorite = false,
                avatarColorHex = "#118AB2"
            ),
            ContactEntity(
                name = "Tahina Rakotomalala",
                phoneNumber = "+261 33 66 554 43",
                email = "tahina.rakoto@callcenter.mg",
                company = "BPO Contact Madagascar",
                jobTitle = "Superviseur Centre d'appels",
                label = "Travail",
                isFavorite = false,
                avatarColorHex = "#073B4C"
            ),
            ContactEntity(
                name = "Lalaina Randriamamonjy",
                phoneNumber = "+261 34 55 667 78",
                email = "lalaina.randria@tourisme-mada.org",
                company = "Office National du Tourisme",
                jobTitle = "Responsable Communication",
                label = "Personnel",
                isFavorite = false,
                avatarColorHex = "#EF476F"
            ),
            ContactEntity(
                name = "Eric Rajaona",
                phoneNumber = "+261 32 99 001 12",
                email = "eric.rajaona@imprimerie-centrale.mg",
                company = "Imprimerie Centrale Tana",
                jobTitle = "Gérant",
                label = "Mobile",
                isFavorite = false,
                avatarColorHex = "#3F37C9"
            ),
            ContactEntity(
                name = "Spam Télémarketing",
                phoneNumber = "+261 20 99 999 99",
                email = null,
                company = "Appels Automatisés Suspects",
                jobTitle = null,
                label = "Bloqué",
                isFavorite = false,
                isBlocked = true,
                avatarColorHex = "#64748B",
                notes = "Numéro bloqué automatiquement par le filtre anti-spam"
            )
        )
    }

    fun getDemoCallLogs(): List<CallLogEntity> {
        val now = System.currentTimeMillis()
        val oneHour = 3600_000L
        val oneDay = 86400_000L

        return listOf(
            CallLogEntity(
                phoneNumber = "+261 34 00 000 02",
                contactName = "Jean Rakoto",
                callType = "INCOMING",
                timestamp = now - (15 * 60_000L),
                durationSeconds = 245,
                lineUsed = "SIM Personnelle (+261 34 00 000 01)",
                callCost = "0.00 Ar (Demo)"
            ),
            CallLogEntity(
                phoneNumber = "+261 32 00 000 03",
                contactName = "Marie Dupont",
                callType = "OUTGOING",
                timestamp = now - (2 * oneHour),
                durationSeconds = 118,
                lineUsed = "VoIP Antsoiray Cloud",
                callCost = "35.00 Ar (Demo)"
            ),
            CallLogEntity(
                phoneNumber = "+261 33 00 000 04",
                contactName = "Paul Martin",
                callType = "MISSED",
                timestamp = now - (3 * oneHour + 20 * 60_000L),
                durationSeconds = 0,
                lineUsed = "SIM Personnelle (+261 34 00 000 01)",
                callCost = "0.00 Ar (Demo)",
                isVoicemail = true,
                voicemailDuration = 32,
                voicemailTranscript = "Bonjour Franco, c'est Paul. Je voulais faire le point avec toi sur la réunion de demain à 14h30. Rappelle-moi dès que tu as un moment !",
                voicemailListened = false
            ),
            CallLogEntity(
                phoneNumber = "+261 38 00 000 05",
                contactName = "Sophie Demo",
                callType = "INCOMING",
                timestamp = now - (5 * oneHour),
                durationSeconds = 482,
                lineUsed = "VoIP Antsoiray Cloud",
                callCost = "0.00 Ar (Demo)"
            ),
            CallLogEntity(
                phoneNumber = "+261 34 11 223 34",
                contactName = "Rova Andrianasolo",
                callType = "OUTGOING",
                timestamp = now - (8 * oneHour),
                durationSeconds = 64,
                lineUsed = "SIM Personnelle (+261 34 00 000 01)",
                callCost = "20.00 Ar (Demo)"
            ),
            CallLogEntity(
                phoneNumber = "+1 415 555 0199",
                contactName = "Sarah Robinson",
                callType = "MISSED",
                timestamp = now - (12 * oneHour),
                durationSeconds = 0,
                lineUsed = "VoIP Antsoiray Cloud",
                callCost = "0.00 Ar (Demo)",
                isVoicemail = true,
                voicemailDuration = 45,
                voicemailTranscript = "Hi Franco, Sarah from Silicon Valley. We reviewed the Antsoiray prototype specs and we're ready to proceed with testing the SIP trunk.",
                voicemailListened = true
            ),
            CallLogEntity(
                phoneNumber = "+261 20 99 999 99",
                contactName = "Spam Télémarketing",
                callType = "BLOCKED",
                timestamp = now - (14 * oneHour),
                durationSeconds = 0,
                lineUsed = "Filtre Antsoiray",
                callCost = "0.00 Ar (Demo)"
            ),
            CallLogEntity(
                phoneNumber = "+261 34 99 887 76",
                contactName = "Aina Randrianarisoa",
                callType = "OUTGOING",
                timestamp = now - oneDay,
                durationSeconds = 180,
                lineUsed = "SIM Personnelle (+261 34 00 000 01)",
                callCost = "45.00 Ar (Demo)"
            ),
            CallLogEntity(
                phoneNumber = "+261 32 44 556 67",
                contactName = "Hery Rajaonarivelo",
                callType = "INCOMING",
                timestamp = now - (oneDay + 2 * oneHour),
                durationSeconds = 310,
                lineUsed = "VoIP Antsoiray Cloud",
                callCost = "0.00 Ar (Demo)"
            ),
            CallLogEntity(
                phoneNumber = "+33 6 88 99 00 11",
                contactName = "David Cohen",
                callType = "OUTGOING",
                timestamp = now - (oneDay + 5 * oneHour),
                durationSeconds = 540,
                lineUsed = "VoIP Antsoiray Cloud",
                callCost = "150.00 Ar (Demo)"
            ),
            CallLogEntity(
                phoneNumber = "+261 33 77 889 90",
                contactName = "Faly Rasolofo",
                callType = "MISSED",
                timestamp = now - (oneDay + 8 * oneHour),
                durationSeconds = 0,
                lineUsed = "SIM Personnelle (+261 34 00 000 01)",
                callCost = "0.00 Ar (Demo)"
            ),
            CallLogEntity(
                phoneNumber = "+261 34 00 000 02",
                contactName = "Jean Rakoto",
                callType = "OUTGOING",
                timestamp = now - (2 * oneDay),
                durationSeconds = 125,
                lineUsed = "SIM Personnelle (+261 34 00 000 01)",
                callCost = "30.00 Ar (Demo)"
            ),
            CallLogEntity(
                phoneNumber = "+261 38 12 345 67",
                contactName = "Nicole Laurent",
                callType = "INCOMING",
                timestamp = now - (2 * oneDay + 3 * oneHour),
                durationSeconds = 95,
                lineUsed = "SIM Personnelle (+261 34 00 000 01)",
                callCost = "0.00 Ar (Demo)"
            ),
            CallLogEntity(
                phoneNumber = "+261 32 55 443 32",
                contactName = "Mialy Rabemananjara",
                callType = "MISSED",
                timestamp = now - (3 * oneDay),
                durationSeconds = 0,
                lineUsed = "SIM Personnelle (+261 34 00 000 01)",
                callCost = "0.00 Ar (Demo)",
                isVoicemail = true,
                voicemailDuration = 18,
                voicemailTranscript = "Coucou Franco, c'est Mialy. Dis-moi si tu es libre ce weekend pour déjeuner en famille !",
                voicemailListened = true
            ),
            CallLogEntity(
                phoneNumber = "+261 34 33 667 78",
                contactName = "Bakoly Ratsimbazafy",
                callType = "OUTGOING",
                timestamp = now - (3 * oneDay + 4 * oneHour),
                durationSeconds = 420,
                lineUsed = "VoIP Antsoiray Cloud",
                callCost = "85.00 Ar (Demo)"
            )
        )
    }

    fun getDemoConversations(): List<ConversationEntity> {
        val now = System.currentTimeMillis()
        val oneHour = 3600_000L
        val oneDay = 86400_000L

        return listOf(
            ConversationEntity(
                phoneNumber = "+261 34 00 000 02",
                contactName = "Jean Rakoto",
                lastMessage = "Parfait, je prépare le rapport technique pour la présentation.",
                lastTimestamp = now - (8 * 60_000L),
                unreadCount = 1,
                isPinned = true,
                isFollowUp = false
            ),
            ConversationEntity(
                phoneNumber = "+261 38 00 000 05",
                contactName = "Sophie Demo",
                lastMessage = "Merci pour ton retour ! Le sprint 4 commence lundi.",
                lastTimestamp = now - (45 * 60_000L),
                unreadCount = 0,
                isPinned = true,
                isFollowUp = true
            ),
            ConversationEntity(
                phoneNumber = "+261 32 00 000 03",
                contactName = "Marie Dupont",
                lastMessage = "Je te rappelle demain matin pour valider les quotas SMS.",
                lastTimestamp = now - (2 * oneHour),
                unreadCount = 0,
                isPinned = false,
                isFollowUp = true
            ),
            ConversationEntity(
                phoneNumber = "+261 33 00 000 04",
                contactName = "Paul Martin",
                lastMessage = "Réunion confirmée à 14h30 en salle de conférence.",
                lastTimestamp = now - (3 * oneHour),
                unreadCount = 2,
                isPinned = false,
                isFollowUp = false
            ),
            ConversationEntity(
                phoneNumber = "+261 34 11 223 34",
                contactName = "Rova Andrianasolo",
                lastMessage = "J'ai bien reçu les clauses juridiques, tout est en ordre.",
                lastTimestamp = now - (7 * oneHour),
                unreadCount = 0,
                isPinned = false,
                isFollowUp = false
            ),
            ConversationEntity(
                phoneNumber = "+261 34 99 887 76",
                contactName = "Aina Randrianarisoa",
                lastMessage = "Voici la maquette finale de l'interface Talkyto / Antsoiray 🎨",
                lastTimestamp = now - oneDay,
                unreadCount = 0,
                isPinned = false,
                isFollowUp = false
            ),
            ConversationEntity(
                phoneNumber = "+1 415 555 0199",
                contactName = "Sarah Robinson",
                lastMessage = "The latency tests on Madagascar SIP routes look very solid!",
                lastTimestamp = now - (oneDay + 4 * oneHour),
                unreadCount = 0,
                isPinned = false,
                isFollowUp = false
            ),
            ConversationEntity(
                phoneNumber = "+33 6 88 99 00 11",
                contactName = "David Cohen",
                lastMessage = "Bien reçu ! On synchronise nos équipes la semaine prochaine.",
                lastTimestamp = now - (2 * oneDay),
                unreadCount = 0,
                isPinned = false,
                isFollowUp = false
            )
        )
    }

    fun getDemoMessages(): List<MessageEntity> {
        val now = System.currentTimeMillis()
        val oneHour = 3600_000L

        return listOf(
            // --- Jean Rakoto Conversation ---
            MessageEntity(
                conversationPhone = "+261 34 00 000 02",
                senderPhone = "+261 34 00 000 02",
                senderName = "Jean Rakoto",
                body = "Bonjour Franco, comment vas-tu ?",
                timestamp = now - (4 * oneHour),
                isFromMe = false,
                status = "READ"
            ),
            MessageEntity(
                conversationPhone = "+261 34 00 000 02",
                senderPhone = "+261 34 00 000 01",
                senderName = "Franco Demo",
                body = "Bonjour Jean ! Très bien merci. On avance à grand pas sur le prototype Antsoiray.",
                timestamp = now - (3 * oneHour + 45 * 60_000L),
                isFromMe = true,
                status = "READ"
            ),
            MessageEntity(
                conversationPhone = "+261 34 00 000 02",
                senderPhone = "+261 34 00 000 02",
                senderName = "Jean Rakoto",
                body = "Excellent ! Est-ce que le numéro personnel sert bien d'identifiant unique sans attribuer de numéro virtuel ?",
                timestamp = now - (2 * oneHour),
                isFromMe = false,
                status = "READ"
            ),
            MessageEntity(
                conversationPhone = "+261 34 00 000 02",
                senderPhone = "+261 34 00 000 01",
                senderName = "Franco Demo",
                body = "Exactement ! Règle fondamentale respectée : chaque utilisateur s'inscrit avec son numéro réel. Aucun numéro virtuel de remplacement.",
                timestamp = now - (1 * oneHour),
                isFromMe = true,
                status = "DELIVERED"
            ),
            MessageEntity(
                conversationPhone = "+261 34 00 000 02",
                senderPhone = "+261 34 00 000 02",
                senderName = "Jean Rakoto",
                body = "Parfait, je prépare le rapport technique pour la présentation.",
                timestamp = now - (8 * 60_000L),
                isFromMe = false,
                status = "RECEIVED"
            ),

            // --- Sophie Demo Conversation ---
            MessageEntity(
                conversationPhone = "+261 38 00 000 05",
                senderPhone = "+261 34 00 000 01",
                senderName = "Franco Demo",
                body = "Sophie, as-tu pu vérifier les maquettes du dialer et de la gestion des appels VoIP ?",
                timestamp = now - (2 * oneHour),
                isFromMe = true,
                status = "READ"
            ),
            MessageEntity(
                conversationPhone = "+261 38 00 000 05",
                senderPhone = "+261 38 00 000 05",
                senderName = "Sophie Demo",
                body = "Oui ! L'écran d'appel actif avec haut-parleur, mute et mise en attente est super fluide.",
                timestamp = now - (1 * oneHour + 10 * 60_000L),
                isFromMe = false,
                status = "READ"
            ),
            MessageEntity(
                conversationPhone = "+261 38 00 000 05",
                senderPhone = "+261 38 00 000 05",
                senderName = "Sophie Demo",
                body = "Merci pour ton retour ! Le sprint 4 commence lundi.",
                timestamp = now - (45 * 60_000L),
                isFromMe = false,
                status = "READ"
            ),

            // --- Marie Dupont Conversation ---
            MessageEntity(
                conversationPhone = "+261 32 00 000 03",
                senderPhone = "+261 34 00 000 01",
                senderName = "Franco Demo",
                body = "Marie, pour les forfaits SMS groupés pro, on a défini les autorisations par utilisateur.",
                timestamp = now - (3 * oneHour),
                isFromMe = true,
                status = "READ"
            ),
            MessageEntity(
                conversationPhone = "+261 32 00 000 03",
                senderPhone = "+261 32 00 000 03",
                senderName = "Marie Dupont",
                body = "Je te rappelle demain matin pour valider les quotas SMS.",
                timestamp = now - (2 * oneHour),
                isFromMe = false,
                status = "READ"
            ),

            // --- Paul Martin Conversation ---
            MessageEntity(
                conversationPhone = "+261 33 00 000 04",
                senderPhone = "+261 33 00 000 04",
                senderName = "Paul Martin",
                body = "As-tu reçu mon message vocal concernant la réunion ?",
                timestamp = now - (3 * oneHour + 10 * 60_000L),
                isFromMe = false,
                status = "READ"
            ),
            MessageEntity(
                conversationPhone = "+261 33 00 000 04",
                senderPhone = "+261 33 00 000 04",
                senderName = "Paul Martin",
                body = "Réunion confirmée à 14h30 en salle de conférence.",
                timestamp = now - (3 * oneHour),
                isFromMe = false,
                status = "RECEIVED"
            ),

            // --- Aina (MMS & Images) ---
            MessageEntity(
                conversationPhone = "+261 34 99 887 76",
                senderPhone = "+261 34 99 887 76",
                senderName = "Aina Randrianarisoa",
                body = "Voici la maquette finale de l'interface Talkyto / Antsoiray 🎨",
                timestamp = now - (24 * oneHour),
                isFromMe = false,
                status = "READ",
                attachmentType = "IMAGE",
                attachmentUrl = "https://images.unsplash.com/photo-1551650975-87deedd944c3?w=600"
            )
        )
    }

    fun getDemoTeamMembers(): List<TeamMemberEntity> {
        return listOf(
            TeamMemberEntity(
                name = "Franco Demo",
                role = "Administrateur",
                phoneNumber = "+261 34 00 000 01",
                email = "franco.demo@antsoiray.test",
                canMakeCalls = true,
                canSendSms = true,
                assignedNumber = "Ligne Principale (+261 34 00 000 01)",
                callsToday = 14,
                smsToday = 28
            ),
            TeamMemberEntity(
                name = "Sophie Demo",
                role = "Responsable Produit",
                phoneNumber = "+261 38 00 000 05",
                email = "sophie.demo@antsoiray.test",
                canMakeCalls = true,
                canSendSms = true,
                assignedNumber = "Ligne VoIP Produit (+261 38 00 000 05)",
                callsToday = 9,
                smsToday = 19
            ),
            TeamMemberEntity(
                name = "Tahina Rakoto",
                role = "Superviseur Support",
                phoneNumber = "+261 33 66 554 43",
                email = "tahina.rakoto@callcenter.mg",
                canMakeCalls = true,
                canSendSms = true,
                assignedNumber = "Ligne Helpdesk (+261 20 22 555 00)",
                callsToday = 42,
                smsToday = 67
            ),
            TeamMemberEntity(
                name = "Marie Dupont",
                role = "Agent Commercial",
                phoneNumber = "+261 32 00 000 03",
                email = "marie.dupont@orange.mg",
                canMakeCalls = true,
                canSendSms = false,
                assignedNumber = "Ligne Commerciale (+261 32 00 000 03)",
                callsToday = 22,
                smsToday = 0
            )
        )
    }

    suspend fun seedInitialData(dao: AntsoirayDao, forceReset: Boolean = false) {
        if (forceReset) {
            dao.clearContacts()
            dao.clearCallLogs()
            dao.clearConversations()
            dao.clearMessages()
            dao.clearTeamMembers()
        }

        // Always check profile
        dao.saveUserProfile(getDefaultProfile())
        dao.insertContacts(getDemoContacts())
        dao.insertCallLogs(getDemoCallLogs())
        dao.insertConversations(getDemoConversations())
        dao.insertMessages(getDemoMessages())
        dao.insertTeamMembers(getDemoTeamMembers())
    }
}
