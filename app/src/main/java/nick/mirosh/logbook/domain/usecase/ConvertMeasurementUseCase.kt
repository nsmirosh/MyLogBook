package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.domain.model.BmType
import java.math.BigDecimal
import java.math.MathContext
import javax.inject.Inject

class ConvertMeasurementUseCase @Inject constructor() {

    operator fun invoke(bloodMeasurementType: BmType, value: BigDecimal) =
        if (bloodMeasurementType == BmType.Mmol)
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