package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.domain.model.BloodMeasurementType
import java.math.BigDecimal
import java.math.MathContext

class ConvertMeasurementUseCase {

    operator fun invoke(bloodMeasurementType: BloodMeasurementType, value: BigDecimal) =
        if (bloodMeasurementType == BloodMeasurementType.Mmol)
            mmolToMg(value)
        else
            mgToMmol(value)

}


fun mmolToMg(mmol: BigDecimal): BigDecimal {
    val conversionFactor = BigDecimal("18.0182")
    return mmol.multiply(conversionFactor)
}

fun mgToMmol(mg: BigDecimal): BigDecimal {
    val conversionFactor = BigDecimal("18.0182")
    return mg.divide(conversionFactor, MathContext.DECIMAL128)
}