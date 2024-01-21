package nick.mirosh.logbook.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import nick.mirosh.logbook.MainCoroutineRule
import nick.mirosh.logbook.data.repositories.BloodMeasurementsRepository
import nick.mirosh.logbook.domain.DomainState
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
class GetEntriesUseCaseTest {


    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var bmRepository: BloodMeasurementsRepository
    private lateinit var getEntriesUseCase: GetEntriesUseCase

    @Before
    fun setUp() {
        bmRepository = mock(BloodMeasurementsRepository::class.java)
        getEntriesUseCase = GetEntriesUseCase(bmRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun executeGetEntriesUseCase_returnsSuccess() = mainCoroutineRule.runTest {
        // Arrange
        val expectedEntries = listOf(
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
        val dataStateResponse = DomainState.Success(expectedEntries)
        val flow = listOf(dataStateResponse).asFlow()
        `when`(bmRepository.getEntries()).thenReturn(flow)

        // Act
        val response = getEntriesUseCase().first()
        val actualEntries = (response as DomainState.Success).data

        // Assert
        assertEquals(expectedEntries, actualEntries)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun executeGetEntriesUseCase_returnsEmpty() = mainCoroutineRule.runTest {
        // Arrange
        val expectedDataStateResponse = DomainState.Empty
        val flow = listOf(expectedDataStateResponse).asFlow()
        `when`(bmRepository.getEntries()).thenReturn(flow)

        // Act
        val actualDataStateResponse = getEntriesUseCase().first()

        // Assert
        assertEquals(expectedDataStateResponse, actualDataStateResponse)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun executeGetEntriesUseCase_returnsError() = mainCoroutineRule.runTest {
        // Arrange
        val expectedDataStateResponse = DomainState.Error(message = "Some error")
        val flow = listOf(expectedDataStateResponse).asFlow()
        `when`(bmRepository.getEntries()).thenReturn(flow)

        // Act
        val response = getEntriesUseCase().first()
        val actualDataStateResponse = response as DomainState.Error

        // Assert
        assertEquals(expectedDataStateResponse, actualDataStateResponse)
    }
}