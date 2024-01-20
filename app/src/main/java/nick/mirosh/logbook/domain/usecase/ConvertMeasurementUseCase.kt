package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.domain.model.BmType
import java.math.BigDecimal
import java.math.MathContext
import javax.inject.Inject

const val MMOL_CONVERSION_FACTOR = 18.0182

class ConvertMeasurementUseCase @Inject constructor() {
    operator fun invoke(typeToConvertTo: BmType, toConvert: BigDecimal) =
        if (toConvert != BigDecimal(0)) {
            if (typeToConvertTo == BmType.Mmol)
                mgToMmol(toConvert)
            else
                mmolToMg(toConvert)
        } else {
            BigDecimal(0)
        }

}


fun mmolToMg(mmol: BigDecimal): BigDecimal = mmol.multiply(BigDecimal(MMOL_CONVERSION_FACTOR))

fun mgToMmol(mg: BigDecimal): BigDecimal =
    mg.divide(BigDecimal(MMOL_CONVERSION_FACTOR), MathContext.DECIMAL128)