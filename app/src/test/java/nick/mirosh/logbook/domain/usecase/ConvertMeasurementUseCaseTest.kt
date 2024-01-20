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


    @Test
    fun testConvertMeasurementUseCase() {
        //Arrange
        val usecase = ConvertMeasurementUseCase()
        val type = BmType.Mmol
        val toConvert = BigDecimal(18.0182)
        val expected = BigDecimal(1)
        val delta = BigDecimal(0.000000000000001)

        //Act
        val result = usecase.invoke(type, toConvert)

        //Assert
        assertThat(
            result, closeTo(expected, delta)
        )
    }

}