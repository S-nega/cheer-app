package kz.snega.cheerapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kz.snega.cheerapp.R
import kz.snega.cheerapp.model.Athlete
import kz.snega.cheerapp.model.AthleteListUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AthleteCard(
    athlete: Athlete,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = athlete.name + " " + athlete.surname,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Group: " + athlete.groupName,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = attendanceLabel(athlete.isAttendance),
                style = MaterialTheme.typography.labelSmall,
                color = if (athlete.isAttendance)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun AthleteList(
    athletes: List<Athlete>,
    onAthleteClick: (Athlete) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            items = athletes,
            key = { athlete -> athlete.id }
        ) { athlete ->
            AthleteCard(
                athlete = athlete,
                onClick = { onAthleteClick(athlete) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AthleteListScreen(
    uiState: AthleteListUiState,
    onAddAthlete: (String, String, String, Boolean) -> Unit,
    onUpdateAthlete: (Athlete) -> Unit,
    onDeleteAthlete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedAthleteId by remember { mutableStateOf<String?>(null) }
    var athleteToEdit by remember { mutableStateOf<Athlete?>(null) }
    var athleteToDelete by remember { mutableStateOf<Athlete?>(null) }
    val athletes = (uiState as? AthleteListUiState.Success)?.athletes.orEmpty()
    val selectedAthlete = athletes.find { it.id == selectedAthleteId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Group composition") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is AthleteListUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is AthleteListUiState.Success -> {
                    AthleteList(
                        athletes = uiState.athletes,
                        onAthleteClick = { athlete -> selectedAthleteId = athlete.id }
                    )
                }
                is AthleteListUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            selectedAthlete?.let { athlete ->
                AthleteDetailsDialog(
                    athlete = athlete,
                    onDismiss = { selectedAthleteId = null },
                    onEdit = { athleteToEdit = athlete },
                    onDelete = { athleteToDelete = athlete }
                )
            }

            if (showAddDialog) {
                AthleteFormDialog(
                    title = "Add athlete",
                    confirmLabel = "Add",
                    onDismiss = { showAddDialog = false },
                    onConfirm = { name, surname, group, isAttendance ->
                        onAddAthlete(name, surname, group, isAttendance)
                        showAddDialog = false
                    }
                )
            }

            athleteToEdit?.let { athlete ->
                AthleteFormDialog(
                    title = "Edit athlete",
                    confirmLabel = "Save",
                    initialName = athlete.name,
                    initialSurname = athlete.surname,
                    initialGroup = athlete.groupName,
                    initialIsAttendance = athlete.isAttendance,
                    onDismiss = { athleteToEdit = null },
                    onConfirm = { name, surname, group, isAttendance ->
                        val updated = athlete.copy(
                            name = name,
                            surname = surname,
                            groupName = group,
                            isAttendance = isAttendance
                        )
                        onUpdateAthlete(updated)
                        athleteToEdit = null
                    }
                )
            }

            athleteToDelete?.let { athlete ->
                AlertDialog(
                    onDismissRequest = { athleteToDelete = null },
                    title = { Text("Delete athlete") },
                    text = {
                        Text("Delete ${athlete.name} ${athlete.surname} from the list?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onDeleteAthlete(athlete.id)
                                athleteToDelete = null
                                selectedAthleteId = null
                                athleteToEdit = null
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { athleteToDelete = null }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AthleteDetailsDialog(
    athlete: Athlete,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Athlete",
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onEdit) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "Edit",
                        modifier = Modifier.size(22.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "Delete",
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        text = {
            Column {
                DetailRow(label = "Name", value = athlete.name)
                DetailRow(label = "Surname", value = athlete.surname)
                DetailRow(label = "Group", value = athlete.groupName)
                DetailRow(label = "Status", value = attendanceLabel(athlete.isAttendance))
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun AthleteFormDialog(
    title: String,
    confirmLabel: String,
    initialName: String = "",
    initialSurname: String = "",
    initialGroup: String = "",
    initialIsAttendance: Boolean = true,
    onDismiss: () -> Unit,
    onConfirm: (name: String, surname: String, group: String, isAttendance: Boolean) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var surname by remember { mutableStateOf(initialSurname) }
    var group by remember { mutableStateOf(initialGroup) }
    var isAttendance by remember { mutableStateOf(initialIsAttendance) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Surname") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                OutlinedTextField(
                    value = group,
                    onValueChange = { group = it },
                    label = { Text("Group") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Active (currently training)",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = isAttendance,
                        onCheckedChange = { isAttendance = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(name.trim(), surname.trim(), group.trim(), isAttendance)
                },
                enabled = name.isNotBlank() && surname.isNotBlank() && group.isNotBlank()
            ) {
                Text(confirmLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun attendanceLabel(isAttendance: Boolean): String {
    return if (isAttendance) "Active" else "Pause"
}
