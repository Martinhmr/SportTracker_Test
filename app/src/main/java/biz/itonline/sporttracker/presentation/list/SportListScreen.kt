package biz.itonline.sporttracker.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Delete // Nová ikona
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import biz.itonline.sporttracker.domain.model.SportRecord
import biz.itonline.sporttracker.domain.model.StorageType
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportListScreen(
    state: SportListUiState,
    currentFilter: FilterType,
    onFilterChange: (FilterType) -> Unit,
    onAddClick: () -> Unit,
    onDeleteClick: (SportRecord) -> Unit
) {
    var sportToDelete by remember { mutableStateOf<SportRecord?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Můj Sport Tracker") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Přidat sport")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FilterSection(
                currentFilter = currentFilter,
                onFilterChange = onFilterChange
            )

            when (state) {
                is SportListUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is SportListUiState.Success -> {
                    if (state.sports.isEmpty()) {
                        EmptyState()
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.sports) { sport ->
                                SportItem(
                                    sport = sport,
                                    onDeleteRequest = { sportToDelete = sport }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (sportToDelete != null) {
            AlertDialog(
                onDismissRequest = { sportToDelete = null },
                title = { Text("Smazat výkon?") },
                text = { Text("Opravdu chceš smazat záznam '${sportToDelete?.name}'? Tato akce je nevratná.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            sportToDelete?.let { onDeleteClick(it) }
                            sportToDelete = null
                        }
                    ) {
                        Text("Smazat", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { sportToDelete = null }) {
                        Text("Zrušit")
                    }
                }
            )
        }
    }
}

@Composable
fun FilterSection(
    currentFilter: FilterType,
    onFilterChange: (FilterType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChipItem("Vše", FilterType.ALL, currentFilter, onFilterChange)
        FilterChipItem("Lokální", FilterType.LOCAL, currentFilter, onFilterChange)
        FilterChipItem("Remote", FilterType.REMOTE, currentFilter, onFilterChange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipItem(
    label: String,
    type: FilterType,
    current: FilterType,
    onClick: (FilterType) -> Unit
) {
    FilterChip(
        selected = (type == current),
        onClick = { onClick(type) },
        label = { Text(label) }
    )
}

@Composable
fun SportItem(
    sport: SportRecord,
    onDeleteRequest: () -> Unit
) {
    val containerColor = if (sport.type == StorageType.LOCAL) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
    }

    val icon: ImageVector =
        if (sport.type == StorageType.LOCAL) Icons.Default.Smartphone else Icons.Default.Cloud

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sport.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${sport.place} • ${sport.durationMinutes} min",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(onClick = onDeleteRequest) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Smazat",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            val dateFormat = SimpleDateFormat("dd.MM. HH:mm", Locale.getDefault())
            Text(
                text = dateFormat.format(sport.date),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Zatím žádné výkony.", style = MaterialTheme.typography.headlineSmall)
            Text("Jdi si zaběhat!", style = MaterialTheme.typography.bodyMedium)
        }
    }
}