package nick.mirosh.logbook.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import nick.mirosh.logbook.MainCoroutineRule
import nick.mirosh.logbook.domain.DomainState
import nick.mirosh.logbook.domain.data.BloodMeasurementsRepository
import nick.mirosh.logbook.domain.model.BloodGlucoseEntry
import nick.mirosh.logbook.domain.model.BmType
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal


@RunWith(MockitoJUnitRunner::class)
class SaveBloodMeasurementUseCaseTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var bmRepository: BloodMeasurementsRepository
    private lateinit var saveBloodMeasurementUseCase: SaveBloodMeasurementUseCase

    @Before
    fun setUp() {
        bmRepository = mock(BloodMeasurementsRepository::class.java)
        saveBloodMeasurementUseCase = SaveBloodMeasurementUseCase(bmRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun execute_returnsSuccess() = mainCoroutineRule.runTest {
        // Arrange
        val bloodGlucoseEntry = BloodGlucoseEntry(
            value = BigDecimal(54.0546),
            type = BmType.Mg
        )
        val expectedResponse = DomainState.Success(Unit)
        val flow = listOf(expectedResponse).asFlow()
        `when`(bmRepository.saveEntry(bloodGlucoseEntry)).thenReturn(flow)

        // Act
        val response = saveBloodMeasurementUseCase(bloodGlucoseEntry).first()

        // Assert
        assertEquals(expectedResponse, response)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun executeGetEntriesUseCase_returnsError() = mainCoroutineRule.runTest {
        // Arrange
        val bloodGlucoseEntry = BloodGlucoseEntry(
            value = BigDecimal(54.0546),
            type = BmType.Mg
        )
        val expectedDataStateResponse = DomainState.Error("Error writing to the database")
        val flow = listOf(expectedDataStateResponse).asFlow()
        `when`(bmRepository.saveEntry(bloodGlucoseEntry)).thenReturn(flow)

        // Act
        val actualDataStateResponse = saveBloodMeasurementUseCase(bloodGlucoseEntry).first()

        // Assert
        assertEquals(expectedDataStateResponse, actualDataStateResponse)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun executeGetEntriesUseCase_returnsLoading() = mainCoroutineRule.runTest {
        // Arrange
        val bloodGlucoseEntry = BloodGlucoseEntry(
            value = BigDecimal(54.0546),
            type = BmType.Mg
        )
        val expectedDataStateResponse = DomainState.Loading
        val flow = listOf(expectedDataStateResponse).asFlow()
        `when`(bmRepository.saveEntry(bloodGlucoseEntry)).thenReturn(flow)

        // Act
        val response = saveBloodMeasurementUseCase(bloodGlucoseEntry).first()

        // Assert
        assertEquals(expectedDataStateResponse, response)
    }
}