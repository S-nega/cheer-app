package kz.snega.cheerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kz.snega.cheerapp.model.Athlete
import kz.snega.cheerapp.model.AthleteListUiState
import kz.snega.cheerapp.model.MockData
import kz.snega.cheerapp.ui.components.AthleteListScreen
import kz.snega.cheerapp.ui.theme.CheerAppTheme
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheerAppTheme {
                var athletes by remember { mutableStateOf(MockData.athletesList) }
                AthleteListScreen(
                    uiState = AthleteListUiState.Success(athletes = athletes),
                    onAddAthlete = { name, surname, group, isAttendance ->
                        athletes = athletes + Athlete(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            surname = surname,
                            groupName = group,
                            isAttendance = isAttendance
                        )
                    },
                    onUpdateAthlete = { updated ->
                        athletes = athletes.map { athlete ->
                            if (athlete.id == updated.id) updated else athlete
                        }
                    },
                    onDeleteAthlete = { id ->
                        athletes = athletes.filter { it.id != id }
                    }
                )
            }
        }
    }
}
