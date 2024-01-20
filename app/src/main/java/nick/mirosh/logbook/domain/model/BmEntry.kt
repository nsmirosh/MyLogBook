package nick.mirosh.logbook.domain.model

import nick.mirosh.logbook.data.model.BmDatabaseEntry
import java.math.BigDecimal

data class BmEntry(
    val type: BmType,
    val value: BigDecimal
) {
    override fun toString(): String {
        return "BmEntry(type=$type, value=$value)"
    }
}



fun BmEntry.toBmDatabaseEntry(): BmDatabaseEntry =
    BmDatabaseEntry(
        type = type,
        value = value
    )
