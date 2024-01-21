package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.domain.model.BmType
import nick.mirosh.logbook.domain.model.MMOL_CONVERSION_FACTOR
import java.math.BigDecimal
import java.math.MathContext
import javax.inject.Inject

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


fun mmolToMg(mmol: BigDecimal): BigDecimal = mmol.multiply(BigDecimal(MMOL_CONVERSION_FACTOR))

fun mgToMmol(mg: BigDecimal): BigDecimal =
    mg.divide(BigDecimal(MMOL_CONVERSION_FACTOR), MathContext.DECIMAL128)