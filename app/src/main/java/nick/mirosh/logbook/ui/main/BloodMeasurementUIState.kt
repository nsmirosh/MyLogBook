package nick.mirosh.logbook.ui.main

import nick.mirosh.logbook.domain.model.BmType

data class BloodMeasurementUIState(
    val average: String = "0",
    val type: BmType = BmType.Mg
)
