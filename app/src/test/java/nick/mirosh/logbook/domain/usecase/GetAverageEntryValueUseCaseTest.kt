package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.domain.model.BloodGlucoseEntry
import nick.mirosh.logbook.domain.model.BmType
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.closeTo
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal


@RunWith(JUnit4::class)
class GetAverageEntryValueUseCaseTest {

    private val allowedDelta = BigDecimal(0.00001)

    @Test
    fun execute_withMixedEntries_returnsMmolAverage() {
        //Arrange
        val entries = listOf(
            BloodGlucoseEntry(
                value = BigDecimal(54.0546),
                type = BmType.Mg
            ),
            BloodGlucoseEntry(
                value = BigDecimal(3),
                type = BmType.Mmol
            ),
            BloodGlucoseEntry(
                value = BigDecimal(6),
                type = BmType.Mmol
            )
        )
        val convertMeasurementUseCase = ConvertMeasurementUseCase()
        val usecase = GetAverageEntryValueUseCase(convertMeasurementUseCase)
        val expected = BigDecimal(4)
        val averageOnType = BmType.Mmol

        //Act
        val result = usecase.invoke(entries, averageOnType)

        //Assert
        assertThat(
            result, closeTo(expected, allowedDelta)
        )
    }

    @Test
    fun execute_withMixedEntries_returnsMgAverage() {
        //Arrange
        val entries = listOf(
            BloodGlucoseEntry(
                value = BigDecimal(54.0546),
                type = BmType.Mg
            ),
            BloodGlucoseEntry(
                value = BigDecimal(5),
                type = BmType.Mmol
            ),
            BloodGlucoseEntry(
                value = BigDecimal(99.8434),
                type = BmType.Mg
            )
        )
        val convertMeasurementUseCase = ConvertMeasurementUseCase()
        val usecase = GetAverageEntryValueUseCase(convertMeasurementUseCase)
        val expected = BigDecimal(81.3296666667)
        val averageOnType = BmType.Mg

        //Act
        val result = usecase.invoke(entries, averageOnType)

        //Assert
        assertThat(
            result, closeTo(expected, allowedDelta)
        )
    }

    @Test
    fun execute_withEmptyEntriesList_returnsZero() {
        //Arrange
        val entries = listOf<BloodGlucoseEntry>()
        val convertMeasurementUseCase = ConvertMeasurementUseCase()
        val usecase = GetAverageEntryValueUseCase(convertMeasurementUseCase)
        val expected = BigDecimal(0)
        val averageOnType = BmType.Mmol

        //Act
        val result = usecase.invoke(entries, averageOnType)

        //Assert
        assertThat(
            result, equalTo(expected)
        )
    }
}