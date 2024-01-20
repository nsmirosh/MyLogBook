package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.domain.model.BmEntry
import nick.mirosh.logbook.domain.model.BmType
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject


class GetAverageEntryValueUseCase @Inject constructor() {
//    operator fun invoke(entries: List<BmEntry>, averageOnType: BmType): BigDecimal {
//
//        with(entries) {
//            if (entries.isEmpty()) return BigDecimal.ZERO
//
//            val mMolEntries = this.filter { it.type == BmType.Mmol }
//            val mMgEntries = this.filter { it.type == BmType.Mg }
//
//            var sum = BigDecimal.ZERO
//
//            if (averageOnType == BmType.Mmol) {
//                sum = mMolEntries.fold(BigDecimal.ZERO) { sum, entry -> sum.add(entry.value) }
//                val convertedMgToMol = mMgEntries.map { mgToMmol(it.value) }
//                convertedMgToMol.forEach {
//                    sum = sum.add(it)
//                }
//            } else {
//                sum = mMgEntries.fold(BigDecimal.ZERO) { sum, entry -> sum.add(entry.value) }
//                val convertedMolToMg = mMolEntries.map { mmolToMg(it.value) }
//                convertedMolToMg.forEach {
//                    sum = sum.add(it)
//                }
//            }
//
//            return sum.divide(
//                BigDecimal(this.size),
//                5,
//                RoundingMode.HALF_UP
//            )
//        }
//    }
     operator fun invoke(entries: List<BmEntry>, averageOnType: BmType): BigDecimal {
        if (entries.isEmpty()) return BigDecimal.ZERO

        val (matchingEntries, otherEntries) = entries.partition { it.type == averageOnType }
        val sumOfMatchingEntries = matchingEntries.sumOf { it.value }

        val conversionFunction = if (averageOnType == BmType.Mmol) ::mgToMmol else ::mmolToMg
        val sumOfConvertedEntries = otherEntries.sumOf { conversionFunction(it.value) }

        val totalSum = sumOfMatchingEntries + sumOfConvertedEntries
        return totalSum.divide(BigDecimal(entries.size), 5, RoundingMode.HALF_UP)
    }
}

