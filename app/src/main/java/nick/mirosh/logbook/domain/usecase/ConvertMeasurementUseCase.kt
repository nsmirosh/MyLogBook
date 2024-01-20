package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.domain.model.BmType
import java.math.BigDecimal
import java.math.MathContext
import javax.inject.Inject

class ConvertMeasurementUseCase @Inject constructor() {

    operator fun invoke(bloodMeasurementType: BmType, toConvert: BigDecimal) =
        if (toConvert != BigDecimal(0)) {
            if (bloodMeasurementType == BmType.Mmol)
                mgToMmol(toConvert)
            else
                mmolToMg(toConvert)
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