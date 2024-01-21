package nick.mirosh.logbook.domain.model

import nick.mirosh.logbook.data.model.BmDatabaseEntry
import java.math.BigDecimal

/**
 * Naming: BloodGlucoseEntry
 */
data class BmEntry(
    val type: BmType,
    val value: BigDecimal
) {

    /**
     * Data class, no need to have a toString.
     */
    override fun toString(): String {
        return "BmEntry(type=$type, value=$value)"
    }
}


/**
 * Put into it's own file, this should also be in data layer to follow clean architecture.
 */
fun BmEntry.toBmDatabaseEntry(): BmDatabaseEntry =
    BmDatabaseEntry(
        type = type,
        value = value
    )
