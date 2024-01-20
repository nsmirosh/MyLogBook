package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.domain.model.BmEntry
import nick.mirosh.logbook.domain.model.BmType
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject


//class GetAverageEntryValueUseCase @Inject constructor() {
//
//     operator fun invoke(entries: List<BmEntry>, averageOnType: BmType): BigDecimal {
//        if (entries.isEmpty()) return BigDecimal.ZERO
//
//        val (matchingEntries, otherEntries) = entries.partition { it.type == averageOnType }
//        val sumOfMatchingEntries = matchingEntries.sumOf { it.value }
//
//        val conversionFunction = if (averageOnType == BmType.Mmol) ::mgToMmol else ::mmolToMg
//        val sumOfConvertedEntries = otherEntries.sumOf { conversionFunction(it.value) }
//
//        val totalSum = sumOfMatchingEntries + sumOfConvertedEntries
//        return totalSum.divide(BigDecimal(entries.size), 5, RoundingMode.HALF_UP)
//    }
//}
class GetAverageEntryValueUseCase @Inject constructor(
    private val convertMeasurementUseCase: ConvertMeasurementUseCase
) {

    operator fun invoke(entries: List<BmEntry>, averageOnType: BmType): BigDecimal {
        if (entries.isEmpty()) return BigDecimal.ZERO

        val totalSum = entries.sumOf { entry ->
            val convertedValue = if (entry.type != averageOnType) {
                convertMeasurementUseCase(averageOnType, entry.value)
            } else {
                entry.value
            }

            convertedValue
        }

        return totalSum.divide(BigDecimal(entries.size), 5, RoundingMode.HALF_UP)
    }
}

