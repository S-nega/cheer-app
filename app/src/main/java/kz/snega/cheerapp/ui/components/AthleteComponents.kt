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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.snega.cheerapp.model.Athlete
import kz.snega.cheerapp.model.AthleteListUiState

@Composable
fun AthleteCard(
    athlete: Athlete,
    modifier: Modifier = Modifier
){
    Card(
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
                text = if (athlete.isAttendance) "Active" else "Pause",
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
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier) {
        items(
            items = athletes,
            key = { athlete -> athlete.id}
        ) { athlete ->
            AthleteCard(athlete = athlete)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AthleteListScreen(
    uiState: AthleteListUiState,
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Group composition")}
            )
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
                    AthleteList(athletes = uiState.athletes)
                }
                is AthleteListUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier  = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}