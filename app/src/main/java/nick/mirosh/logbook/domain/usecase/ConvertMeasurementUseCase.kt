package nick.mirosh.logbook.domain.usecase

import java.math.BigDecimal
import java.math.MathContext

class ConvertMeasurementUseCase {

    operator fun invoke(convertToMol: Boolean, value: BigDecimal) =
        if (convertToMol)
            mgToMmol(value)
        else
            mmolToMg(value)

}


fun mmolToMg(mmol: BigDecimal): BigDecimal {
    val conversionFactor = BigDecimal("18.0182")
    return mmol.multiply(conversionFactor)
}

fun mgToMmol(mg: BigDecimal): BigDecimal {
    val conversionFactor = BigDecimal("18.0182")
    return mg.divide(conversionFactor, MathContext.DECIMAL128)
}