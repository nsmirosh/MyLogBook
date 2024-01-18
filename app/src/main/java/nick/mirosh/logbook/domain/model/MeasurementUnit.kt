package nick.mirosh.logbook.domain.model

import java.math.BigInteger

sealed class MeasurementUnit {
    data class Mmol(val value: BigInteger) : MeasurementUnit()
    data class Mg(val value: BigInteger) : MeasurementUnit()
}
