package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class CountryItem(
    val name: String,
    val dialCode: String,
    val flagEmoji: String,
    val isoCode: String,
    val exampleNumber: String
)

val SupportedCountries = listOf(
    CountryItem("Madagascar", "+261", "🇲🇬", "MG", "34 00 000 01"),
    CountryItem("France", "+33", "🇫🇷", "FR", "6 12 34 56 78"),
    CountryItem("États-Unis", "+1", "🇺🇸", "US", "415 555 0199"),
    CountryItem("Canada", "+1", "🇨🇦", "CA", "514 555 0123"),
    CountryItem("La Réunion", "+262", "🇷🇪", "RE", "692 12 34 56"),
    CountryItem("Maurice", "+230", "🇲🇺", "MU", "57 12 34 56"),
    CountryItem("Comores", "+269", "🇰🇲", "KM", "32 000 00"),
    CountryItem("Belgique", "+32", "🇧🇪", "BE", "470 12 34 56"),
    CountryItem("Suisse", "+41", "🇨🇭", "CH", "79 123 45 67"),
    CountryItem("Royaume-Uni", "+44", "🇬🇧", "GB", "7911 123456"),
    CountryItem("Allemagne", "+49", "🇩🇪", "DE", "151 12345678"),
    CountryItem("Côte d'Ivoire", "+225", "🇨🇮", "CI", "07 01 02 03 04"),
    CountryItem("Sénégal", "+221", "🇸🇳", "SN", "77 123 45 67"),
    CountryItem("Cameroun", "+237", "🇨🇲", "CM", "6 70 00 00 00"),
    CountryItem("Afrique du Sud", "+27", "🇿🇦", "ZA", "82 123 4567")
)

/**
 * RegisterScreen - Inscription par numéro personnel pour Antsoiray.
 * Comporte le champ de saisie avec sélecteur d'indicatif pays et le bouton Continuer
 * qui déclenche la simulation de réception du code OTP.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    currentPhone: String,
    currentCountryCode: String,
    onPhoneChange: (String) -> Unit,
    onCountryCodeChange: (String) -> Unit,
    onContinue: () -> Unit,
    onQuickDemoLogin: () -> Unit
) {
    var selectedCountry by remember(currentCountryCode) {
        mutableStateOf(
            SupportedCountries.find { it.dialCode == currentCountryCode }
                ?: SupportedCountries.first()
        )
    }

    // Extract national number if currentPhone already contains dialCode
    var nationalNumber by remember(currentPhone, selectedCountry) {
        val cleaned = if (currentPhone.startsWith(selectedCountry.dialCode)) {
            currentPhone.removePrefix(selectedCountry.dialCode).trim()
        } else {
            currentPhone.trim()
        }
        mutableStateOf(cleaned)
    }

    var showCountryPicker by remember { mutableStateOf(false) }
    var simulationNotice by remember { mutableStateOf<String?>(null) }
    var inputError by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val fullPhoneNumber by remember(selectedCountry, nationalNumber) {
        derivedStateOf {
            val clean = nationalNumber.trim()
            if (clean.isEmpty()) "" else "${selectedCountry.dialCode} $clean"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Logo & Branding
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(CyanAccent.copy(alpha = 0.15f))
                    .border(2.dp, CyanAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = CyanAccent,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ANTSOIRAY",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
            )

            Text(
                text = "Communication • Appels • SMS • Contacts",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = CyanAccent,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fundamental Rule Banner (Talkyto with personal number)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardNavy,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(EmeraldSuccess.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Votre numéro réel est votre identité",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Aucun numéro virtuel de remplacement. Vos correspondants vous identifient directement.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Registration Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardNavy),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Inscription par téléphone",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Sélectionnez votre indicatif et entrez votre numéro de mobile personnel.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Phone Number Input Row with Country Picker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Country Selector Button
                        Surface(
                            onClick = { showCountryPicker = true },
                            shape = RoundedCornerShape(12.dp),
                            color = SurfaceNavy,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("country_code_selector")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedCountry.flagEmoji,
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = selectedCountry.dialCode,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Choisir pays",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // National Number Text Field
                        OutlinedTextField(
                            value = nationalNumber,
                            onValueChange = { input ->
                                nationalNumber = input
                                inputError = null
                                onPhoneChange("${selectedCountry.dialCode} ${input.trim()}")
                            },
                            placeholder = {
                                Text(
                                    selectedCountry.exampleNumber,
                                    style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (nationalNumber.trim().isNotEmpty()) {
                                        val combined = "${selectedCountry.dialCode} ${nationalNumber.trim()}"
                                        onPhoneChange(combined)
                                        onContinue()
                                    }
                                }
                            ),
                            trailingIcon = {
                                if (nationalNumber.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            nationalNumber = ""
                                            onPhoneChange("")
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Effacer",
                                            tint = TextSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyanAccent,
                                unfocusedBorderColor = BorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = SurfaceNavy,
                                unfocusedContainerColor = SurfaceNavy
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .testTag("register_phone_input")
                        )
                    }

                    // Validation / Error display
                    if (inputError != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = inputError!!,
                            style = MaterialTheme.typography.bodySmall.copy(color = CoralEndCall),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Full formatted number display preview
                    if (fullPhoneNumber.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceNavy)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Numéro complet :",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = fullPhoneNumber,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = CyanAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Demo Helper Quick Fill
                    Surface(
                        onClick = {
                            selectedCountry = SupportedCountries.first() // Madagascar +261
                            onCountryCodeChange("+261")
                            nationalNumber = "34 00 000 01"
                            onPhoneChange("+261 34 00 000 01")
                            inputError = null
                        },
                        shape = RoundedCornerShape(8.dp),
                        color = SurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("fill_demo_number_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "🇲🇬", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Utiliser le numéro démo : +261 34 00 000 01",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CyanAccent,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Bouton Continuer (Déclenche la simulation de réception du code OTP)
                    Button(
                        onClick = {
                            if (nationalNumber.trim().isEmpty()) {
                                inputError = "Veuillez saisir votre numéro de téléphone."
                            } else if (nationalNumber.trim().length < 6) {
                                inputError = "Numéro trop court. Veuillez vérifier votre saisie."
                            } else {
                                inputError = null
                                val finalNumber = "${selectedCountry.dialCode} ${nationalNumber.trim()}"
                                onPhoneChange(finalNumber)

                                simulationNotice = "Simulation : Code OTP envoyé vers $finalNumber"
                                coroutineScope.launch {
                                    delay(800)
                                    onContinue()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyanAccent,
                            contentColor = MidnightNavy
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("register_continue_button")
                    ) {
                        Text(
                            text = "Continuer",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Simulation Feedback Banner
                    AnimatedVisibility(
                        visible = simulationNotice != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmeraldSuccess.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = EmeraldSuccess,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = simulationNotice ?: "",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = EmeraldSuccess,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Mode Prototype Notice
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SurfaceElevated.copy(alpha = 0.8f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "MODE 1 — PROTOTYPE :",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CyanAccent,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Le code OTP est simulé localement (Code fixe : 123456). Aucun véritable SMS payant n'est débité.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Direct Demo Login shortcut
            OutlinedButton(
                onClick = onQuickDemoLogin,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanAccent),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quick_demo_login_button")
            ) {
                Text(
                    text = "🚀 Accès direct Démo : Franco Demo",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }

        // Country Picker ModalBottomSheet
        if (showCountryPicker) {
            CountryPickerBottomSheet(
                selected = selectedCountry,
                onDismiss = { showCountryPicker = false },
                onSelectCountry = { country ->
                    selectedCountry = country
                    onCountryCodeChange(country.dialCode)
                    onPhoneChange("${country.dialCode} ${nationalNumber.trim()}")
                    showCountryPicker = false
                }
            )
        }
    }
}

/**
 * BottomSheet pour rechercher et sélectionner le pays et l'indicatif téléphonique.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountryPickerBottomSheet(
    selected: CountryItem,
    onDismiss: () -> Unit,
    onSelectCountry: (CountryItem) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchQuery by remember { mutableStateOf("") }

    val filteredCountries = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            SupportedCountries
        } else {
            val q = searchQuery.trim().lowercase()
            SupportedCountries.filter {
                it.name.lowercase().contains(q) ||
                        it.dialCode.contains(q) ||
                        it.isoCode.lowercase().contains(q)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceElevated,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Sélectionnez votre pays",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fermer",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search in countries
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        "Rechercher un pays ou indicatif...",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Effacer",
                                tint = TextSecondary
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyanAccent,
                    unfocusedBorderColor = BorderSubtle,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = CardNavy,
                    unfocusedContainerColor = CardNavy
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("country_search_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Country List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(filteredCountries, key = { "${it.isoCode}_${it.dialCode}" }) { country ->
                    val isCurrent = country.dialCode == selected.dialCode && country.isoCode == selected.isoCode
                    Surface(
                        onClick = { onSelectCountry(country) },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isCurrent) CardNavy else Color.Transparent,
                        border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.dp, CyanAccent) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("country_item_${country.isoCode}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = country.flagEmoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = country.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextPrimary,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Ex: ${country.exampleNumber}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                            Text(
                                text = country.dialCode,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = if (isCurrent) CyanAccent else TextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            if (isCurrent) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = CyanAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
