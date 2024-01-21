package nick.mirosh.logbook.domain.model

import java.math.BigDecimal

data class BloodGlucoseEntry(
    val type: BmType,
    val value: BigDecimal
) {
    override fun toString(): String {
        return "BmEntry ( $value $type)"
    }
}



