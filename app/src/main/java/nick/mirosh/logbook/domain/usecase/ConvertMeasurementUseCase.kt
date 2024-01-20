package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.domain.model.BmType
import java.math.BigDecimal
import java.math.MathContext
import javax.inject.Inject

class ConvertMeasurementUseCase @Inject constructor() {

    operator fun invoke(bloodMeasurementType: BmType, input: BigDecimal) =
        if (input != BigDecimal(0)) {
            if (bloodMeasurementType == BmType.Mmol)
                mgToMmol(input)
            else
                mmolToMg(input)
        } else {
            BigDecimal(0)
        }

}


fun mmolToMg(mmol: BigDecimal): BigDecimal {
    val conversionFactor = BigDecimal("18.0182")
    return mmol.multiply(conversionFactor)
}

fun mgToMmol(mg: BigDecimal): BigDecimal {
    val conversionFactor = BigDecimal("18.0182")
    return mg.divide(conversionFactor, MathContext.DECIMAL128)
}