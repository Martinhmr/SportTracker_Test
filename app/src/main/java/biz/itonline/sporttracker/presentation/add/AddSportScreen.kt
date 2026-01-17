package biz.itonline.sporttracker.presentation.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import biz.itonline.sporttracker.domain.model.StorageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSportScreen(
    state: AddSportUiState,
    onEvent: (AddSportEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nový výkon") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zpět")
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    value = state.name,
                    onValueChange = { onEvent(AddSportEvent.NameChanged(it)) },
                    label = { Text("Název sportu (např. Běh)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                )

                OutlinedTextField(
                    value = state.place,
                    onValueChange = { onEvent(AddSportEvent.PlaceChanged(it)) },
                    label = { Text("Místo konání") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                )

                OutlinedTextField(
                    value = state.duration,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            onEvent(AddSportEvent.DurationChanged(it))
                        }
                    },
                    label = { Text("Délka trvání (minuty)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    suffix = { Text("min") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Kam uložit?", style = MaterialTheme.typography.titleMedium)

                StorageOptionRow(
                    text = "Lokálně (jen v telefonu)",
                    type = StorageType.LOCAL,
                    selectedType = state.storageType,
                    icon = Icons.Filled.Smartphone,
                    onSelect = { onEvent(AddSportEvent.TypeChanged(StorageType.LOCAL)) }
                )

                StorageOptionRow(
                    text = "Remote (Firebase)",
                    type = StorageType.REMOTE,
                    selectedType = state.storageType,
                    icon = Icons.Filled.Cloud,
                    onSelect = { onEvent(AddSportEvent.TypeChanged(StorageType.REMOTE)) }
                )

                Spacer(modifier = Modifier.weight(1f)) // Tlačítko odtlačíme dolů

                Button(
                    onClick = { onEvent(AddSportEvent.SaveClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = state.name.isNotBlank() && state.place.isNotBlank() && state.duration.isNotBlank() && !state.isSaving
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Uložit výkon")
                    }
                }
            }
        }
    }
}

@Composable
fun StorageOptionRow(
    text: String,
    type: StorageType,
    selectedType: StorageType,
    icon: ImageVector,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = (type == selectedType),
                onClick = onSelect,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (type == selectedType),
            onClick = null // null, protože klik řeší celý řádek (selectable)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}