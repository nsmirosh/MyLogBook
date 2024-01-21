package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.domain.model.BmType
import java.math.BigDecimal
import java.math.MathContext
import javax.inject.Inject

/**
 * Move to BmType.
 */
const val MMOL_CONVERSION_FACTOR = 18.0182

class ConvertMeasurementUseCase @Inject constructor() {
    operator fun invoke(typeToConvertTo: BmType, valueToConvert: BigDecimal): BigDecimal {
        if (valueToConvert == BigDecimal(0)) {
            return BigDecimal(0)
        }
        return if (typeToConvertTo == BmType.Mmol)
            mgToMmol(valueToConvert)
        else
            mmolToMg(valueToConvert)
    }
}

/**
 * You can have extension function like: Mmol.toMg() and Mg.toMmol()
 */
fun mmolToMg(mmol: BigDecimal): BigDecimal = mmol.multiply(BigDecimal(MMOL_CONVERSION_FACTOR))

fun mgToMmol(mg: BigDecimal): BigDecimal =
    mg.divide(BigDecimal(MMOL_CONVERSION_FACTOR), MathContext.DECIMAL128)