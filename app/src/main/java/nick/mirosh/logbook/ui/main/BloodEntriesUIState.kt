package nick.mirosh.logbook.ui.main

import nick.mirosh.logbook.domain.model.BloodGlucoseEntry

sealed class BloodEntriesUIState {
    data class Success(val entries: List<BloodGlucoseEntry>) : BloodEntriesUIState()
    data object Empty : BloodEntriesUIState()

    data object Error : BloodEntriesUIState()
    data object Loading : BloodEntriesUIState()
}