package nick.mirosh.logbook.ui.main

import android.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import nick.mirosh.logbook.MainCoroutineRule
import nick.mirosh.logbook.domain.DomainState
import nick.mirosh.logbook.domain.model.BloodGlucoseEntry
import nick.mirosh.logbook.domain.model.BmType
import nick.mirosh.logbook.domain.usecase.ConvertMeasurementUseCase
import nick.mirosh.logbook.domain.usecase.GetAverageEntryValueUseCase
import nick.mirosh.logbook.domain.usecase.GetEntriesUseCase
import nick.mirosh.logbook.domain.usecase.SaveBloodMeasurementUseCase
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.math.BigDecimal


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val saveBloodMeasurementUseCase = mock(SaveBloodMeasurementUseCase::class.java)
    private val getEntriesUseCase = mock(GetEntriesUseCase::class.java)
    private val convertMeasurementUseCase = mock(ConvertMeasurementUseCase::class.java)
    private val getAverageEntryValueUseCase = mock(GetAverageEntryValueUseCase::class.java)

    @Test
    fun convertMeasurement_updatesTypeAndAverageUiState() = mainCoroutineRule.runTest {
        // Arrange
        val expectedType = BmType.Mg
        val expectedSecondAverage = "50"

        val entries = mutableListOf<BloodGlucoseEntry>()

        `when`(getEntriesUseCase.invoke()).thenReturn(
            flow {
                emit(DomainState.Success(entries))
            }
        )

        `when`(getAverageEntryValueUseCase.invoke(any(), any())).thenReturn(
            BigDecimal(50)
        )

        val viewModel = MainViewModel(
            saveBloodMeasurementUseCase,
            getEntriesUseCase,
            convertMeasurementUseCase,
            getAverageEntryValueUseCase
        )

        // Act
        val job: Job?
        val bloodMeasurementUiStateResult = mutableListOf<BloodMeasurementUIState>()

        job = launch {
            viewModel.bloodMeasurementUIState.toList(bloodMeasurementUiStateResult)
        }
        viewModel.convertMeasurement("", expectedType)
        job.cancel()

        // Assert
        assertThat(
            bloodMeasurementUiStateResult.first().average,
            equalTo(expectedSecondAverage)
        )

        assertThat(
            bloodMeasurementUiStateResult.first().type, equalTo(expectedType)
        )
    }


    @Test
    fun saveBloodMeasurement_emitsEmptyStateAndSuccess() = mainCoroutineRule.runTest {
        // Arrange

        val expectedEntries = listOf(
            BloodGlucoseEntry(
                value = BigDecimal(54.0546),
                type = BmType.Mg
            ),
            BloodGlucoseEntry(
                value = BigDecimal(3),
                type = BmType.Mmol
            )
        )

        `when`(getEntriesUseCase.invoke()).thenReturn(
            flow {
                emit(DomainState.Success(expectedEntries))
            }
        )

        `when`(getAverageEntryValueUseCase.invoke(any(), any())).thenReturn(
            BigDecimal(50)
        )

        val viewModel = MainViewModel(
            saveBloodMeasurementUseCase,
            getEntriesUseCase,
            convertMeasurementUseCase,
            getAverageEntryValueUseCase
        )

        // Act
        val job: Job?
        val bloodEntriesUIState = mutableListOf<BloodEntriesUIState>()

        job = launch {
            viewModel.entriesUIState.toList(bloodEntriesUIState)
        }
        job.cancel()

        // Assert
        assertThat(
            bloodEntriesUIState.first(),
            instanceOf(BloodEntriesUIState.Success::class.java)
        )

        assertThat(
            (bloodEntriesUIState.first() as BloodEntriesUIState.Success).entries,
            equalTo(expectedEntries)
        )
    }
}