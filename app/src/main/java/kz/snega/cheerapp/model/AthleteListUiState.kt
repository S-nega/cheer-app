package kz.snega.cheerapp.model

import android.os.Message

sealed interface AthleteListUiState {
    data object Loading: AthleteListUiState

    data class Success(
        val athletes: List<Athlete>
    ) : AthleteListUiState

    data class Error(
        val message: String
    ) : AthleteListUiState
}