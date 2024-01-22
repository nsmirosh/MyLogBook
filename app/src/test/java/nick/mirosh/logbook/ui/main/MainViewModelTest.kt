package nick.mirosh.logbook.ui.main

import android.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
import java.math.BigDecimal


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var saveBloodMeasurementUseCase: SaveBloodMeasurementUseCase
    private lateinit var getEntriesUseCase: GetEntriesUseCase
    private lateinit var convertMeasurementUseCase: ConvertMeasurementUseCase
    private lateinit var getAverageEntryValueUseCase: GetAverageEntryValueUseCase


    @Before
    fun setUp() {
        saveBloodMeasurementUseCase = mock(SaveBloodMeasurementUseCase::class.java)
        getEntriesUseCase = mock(GetEntriesUseCase::class.java)
        convertMeasurementUseCase = mock(ConvertMeasurementUseCase::class.java)
        getAverageEntryValueUseCase = mock(GetAverageEntryValueUseCase::class.java)
    }

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
    fun saveBloodMeasurement_emitsExpectedEntries() = mainCoroutineRule.runTest {
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

        `when`(saveBloodMeasurementUseCase.invoke(any())).thenReturn(
            flow {
                emit(DomainState.Success(Unit))
            }
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

        viewModel.saveBloodMeasurements("50")
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

    @Test
    fun saveBloodMeasurement_emitsErrorState() = mainCoroutineRule.runTest {
        // Arrange

        `when`(getEntriesUseCase.invoke()).thenReturn(
            flow {
                emit(DomainState.Empty)
            }
        )
        `when`(saveBloodMeasurementUseCase.invoke(any())).thenReturn(
            flow {
                emit(DomainState.Error("Something went wrong"))
            }
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

        viewModel.saveBloodMeasurements("50")
        job.cancel()

        // Assert
        assertThat(
            bloodEntriesUIState.first(),
            instanceOf(BloodEntriesUIState.Empty::class.java)
        )

        assertThat(
            bloodEntriesUIState[1],
            instanceOf(BloodEntriesUIState.Error::class.java)
        )
    }
}