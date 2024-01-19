package nick.mirosh.logbook.domain.model

import nick.mirosh.logbook.data.model.BmDatabaseEntry
import java.math.BigDecimal

data class BmEntry(
    val type: BmType,
    val value: BigDecimal
)


fun BmEntry.toBmDatabaseEntry(): BmDatabaseEntry =
    BmDatabaseEntry(
        type = type,
        value = value
    )
