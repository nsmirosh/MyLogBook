package nick.mirosh.logbook.data.mappers

import nick.mirosh.logbook.data.model.BmDatabaseEntry
import nick.mirosh.logbook.domain.model.BloodGlucoseEntry

// Extracting mappers to the data layer
// to avoid the domain layer knowing about the data layer

fun BmDatabaseEntry.toBmEntry() = BloodGlucoseEntry(
        type = type,
        value = value
    )

fun BloodGlucoseEntry.toBmDatabaseEntry(): BmDatabaseEntry =
    BmDatabaseEntry(
        type = type,
        value = value
    )
