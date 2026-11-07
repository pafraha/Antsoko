package com.example.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContactEntity
import com.example.ui.components.ContactAvatar
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AddContactDialog(
    onDismiss: () -> Unit,
    onSaveContact: (name: String, phone: String, secondaryPhone: String?, email: String?, company: String?, jobTitle: String?, label: String, notes: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+261 ") }
    var secondaryPhone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("Mobile") }
    var notes by remember { mutableStateOf("") }

    val labels = listOf("Mobile", "Travail", "Personnel", "Autre")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceElevated,
        title = {
            Text(
                text = "Nouveau contact",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom complet *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CyanAccent) },
                    singleLine = true,
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().testTag("add_contact_name_input")
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Numéro principal *") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = CyanAccent) },
                    singleLine = true,
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().testTag("add_contact_phone_input")
                )

                // Label selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    labels.forEach { l ->
                        FilterChip(
                            selected = label == l,
                            onClick = { label = l },
                            label = { Text(l, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyanAccent,
                                selectedLabelColor = MidnightNavy,
                                containerColor = CardNavy,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }

                OutlinedTextField(
                    value = secondaryPhone,
                    onValueChange = { secondaryPhone = it },
                    label = { Text("Numéro secondaire (facultatif)") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = TextMuted) },
                    singleLine = true,
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail (facultatif)") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TextMuted) },
                    singleLine = true,
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("Société / Organisation") },
                    leadingIcon = { Icon(Icons.Default.Business, contentDescription = null, tint = TextMuted) },
                    singleLine = true,
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes personnelles") },
                    leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null, tint = TextMuted) },
                    maxLines = 2,
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        onSaveContact(
                            name,
                            phone,
                            secondaryPhone.ifBlank { null },
                            email.ifBlank { null },
                            company.ifBlank { null },
                            jobTitle.ifBlank { null },
                            label,
                            notes.ifBlank { null }
                        )
                    }
                },
                enabled = name.isNotBlank() && phone.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyanAccent,
                    contentColor = MidnightNavy
                ),
                modifier = Modifier.testTag("save_contact_button")
            ) {
                Text("Enregistrer", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
            ) {
                Text("Annuler")
            }
        }
    )
}

@Composable
fun NewMessageDialog(
    contacts: List<ContactEntity>,
    onDismiss: () -> Unit,
    onStartConversation: (phone: String, name: String?, message: String) -> Unit
) {
    var recipientInput by remember { mutableStateOf("") }
    var messageBody by remember { mutableStateOf("") }
    var selectedContact by remember { mutableStateOf<ContactEntity?>(null) }

    val filtered = remember(recipientInput, contacts) {
        if (recipientInput.isBlank()) contacts.take(6)
        else contacts.filter {
            it.name.contains(recipientInput, ignoreCase = true) ||
                    it.phoneNumber.contains(recipientInput)
        }.take(6)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceElevated,
        title = {
            Text(
                text = "Nouveau message SMS",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = recipientInput,
                    onValueChange = {
                        recipientInput = it
                        selectedContact = null
                    },
                    label = { Text("Destinataire (Nom ou numéro)") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = CyanAccent) },
                    singleLine = true,
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().testTag("new_message_recipient_input")
                )

                // Contact suggestions
                if (selectedContact == null && filtered.isNotEmpty()) {
                    Text(
                        text = "Suggestions de contacts :",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                    )
                    LazyColumn(
                        modifier = Modifier.height(140.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(filtered) { c ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = CardNavy,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedContact = c
                                        recipientInput = "${c.name} (${c.phoneNumber})"
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ContactAvatar(name = c.name, size = 28.dp, textSize = 11)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(c.name, color = TextPrimary, style = MaterialTheme.typography.labelMedium)
                                        Text(c.phoneNumber, color = CyanAccent, style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = messageBody,
                    onValueChange = { messageBody = it },
                    label = { Text("Votre message...") },
                    maxLines = 3,
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().testTag("new_message_body_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val phone = selectedContact?.phoneNumber ?: recipientInput
                    if (phone.isNotBlank() && messageBody.isNotBlank()) {
                        onStartConversation(phone, selectedContact?.name, messageBody)
                    }
                },
                enabled = recipientInput.isNotBlank() && messageBody.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyanAccent,
                    contentColor = MidnightNavy
                ),
                modifier = Modifier.testTag("new_message_send_button")
            ) {
                Text("Envoyer", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
            ) {
                Text("Annuler")
            }
        }
    )
}

@Composable
fun ScheduleSmsDialog(
    contactPhone: String,
    onDismiss: () -> Unit,
    onSchedule: (delayMinutes: Int, message: String) -> Unit
) {
    var selectedOption by remember { mutableIntStateOf(5) }
    var message by remember { mutableStateOf("Bonjour, ceci est un rappel automatique programmé.") }

    val options = listOf(
        5 to "Dans 5 minutes (Test rapide)",
        60 to "Dans 1 heure",
        1440 to "Demain matin (24h)"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceElevated,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = CyanAccent)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Programmer un SMS",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Choisissez l'horaire de transmission vers $contactPhone :",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )

                options.forEach { (minutes, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedOption = minutes }
                    ) {
                        RadioButton(
                            selected = selectedOption == minutes,
                            onClick = { selectedOption = minutes },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = CyanAccent,
                                unselectedColor = TextMuted
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = label, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Texte du SMS programmé") },
                    maxLines = 3,
                    colors = dialogTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (message.isNotBlank()) {
                        onSchedule(selectedOption, message)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyanAccent,
                    contentColor = MidnightNavy
                )
            ) {
                Text("Valider la programmation", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
            ) {
                Text("Annuler")
            }
        }
    )
}

@Composable
private fun dialogTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = CyanAccent,
    unfocusedBorderColor = BorderSubtle,
    focusedContainerColor = CardNavy,
    unfocusedContainerColor = CardNavy,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedLabelColor = CyanAccent,
    unfocusedLabelColor = TextSecondary
)
