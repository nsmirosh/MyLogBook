package nick.mirosh.logbook.domain.usecase

import nick.mirosh.logbook.domain.model.BmType
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.closeTo
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal


@RunWith(JUnit4::class)
class ConvertMeasurementUseCaseTest {

    val allowedDelta = BigDecimal(0.00000000000001)

    @Test
    fun executeConvertMeasurementUseCase_convertsMmgToMol() {

        //Arrange
        val usecase = ConvertMeasurementUseCase()
        val typeToConvertTo = BmType.Mmol
        val toConvert = BigDecimal(90.091)
        val expected = BigDecimal(5)

        //Act
        val result = usecase.invoke(typeToConvertTo, toConvert)

        //Assert
        assertThat(
            result, closeTo(expected, allowedDelta)
        )
    }

    @Test
    fun executeConvertMeasurementUseCase_convertsMolToMg() {

        //Arrange
        val usecase = ConvertMeasurementUseCase()
        val typeToConvertTo = BmType.Mg
        val toConvert = BigDecimal(5)
        val expected = BigDecimal(90.091)

        //Act
        val result = usecase.invoke(typeToConvertTo, toConvert)

        //Assert
        assertThat(
            result, closeTo(expected, allowedDelta)
        )
    }

    @Test
    fun executeConvertMeasurementUseCase_doesNotConvert_returnsZero() {

        //Arrange
        val usecase = ConvertMeasurementUseCase()
        val typeToConvertTo = BmType.Mg
        val toConvert = BigDecimal(0)
        val expected = BigDecimal(0)

        //Act
        val result = usecase.invoke(typeToConvertTo, toConvert)

        //Assert
        assertThat(
            result, closeTo(expected, allowedDelta)
        )
    }

}