package nick.mirosh.logbook.ui.main

import nick.mirosh.logbook.domain.model.BmEntry

sealed class BloodEntriesUIState {
    data class Success(val entries: List<BmEntry>) : BloodEntriesUIState()
    data object Empty : BloodEntriesUIState()
    data object Loading : BloodEntriesUIState()
}