package nick.mirosh.logbook.ui.main

import android.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import nick.mirosh.logbook.MainCoroutineRule
import nick.mirosh.logbook.domain.model.BmType
import nick.mirosh.logbook.domain.usecase.ConvertMeasurementUseCase
import nick.mirosh.logbook.domain.usecase.GetAverageEntryValueUseCase
import nick.mirosh.logbook.domain.usecase.GetEntriesUseCase
import nick.mirosh.logbook.domain.usecase.SaveBloodMeasurementUseCase
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
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

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(
            saveBloodMeasurementUseCase,
            getEntriesUseCase,
            convertMeasurementUseCase,
            getAverageEntryValueUseCase
        )
    }


//    fun convertMeasurement(text: String, typeToConvertTo: BmType) {
//        if (text.isNotEmpty() && text != ".") {
//            val converted = convertMeasurementUseCase(typeToConvertTo, text.toBigDecimal())
//            _inputTextUIState.value = formatBigDecimal(converted)
//        }
//
//        val average = getAverageEntryValueUseCase(entries, typeToConvertTo)
//        _bloodMeasurementUIState.value = _bloodMeasurementUIState.value.copy(
//            average = formatBigDecimal(average),
//            type = typeToConvertTo
//        )
//    }

    @Test
    fun convertMeasurement_updatesBloodMeasureUIState() = mainCoroutineRule.runTest {
        // Arrange
        val expectedType = BmType.Mg
        val expectedAverage = "50"
        val expectedBigDecimalAverage = BigDecimal(50)

        whenever(getAverageEntryValueUseCase.invoke(any(), any())).thenReturn(
            expectedBigDecimalAverage
        )

        // Act
        val job: Job?
        val bloodMeasurementStateResult = mutableListOf<BloodMeasurementUIState>()

        job = launch {
            viewModel.bloodMeasurementUIState.toList(bloodMeasurementStateResult)
        }
        viewModel.convertMeasurement("", expectedType)
        job.cancel()

        // Assert
        assertThat(
            bloodMeasurementStateResult.first().average,
            equalTo("0"))

        assertThat(
            bloodMeasurementStateResult.first().type, equalTo(expectedType))

        assertThat(
            bloodMeasurementStateResult[1].average,
            equalTo(expectedAverage))

        assertThat(
            bloodMeasurementStateResult[1].type, equalTo(expectedType))
    }


//    @Test
//    fun loginCredentialsEmpty_returnError() = mainCoroutineRule.runTest {
//        // Arrange
//        whenever(loginUseCase.invoke(any())).thenReturn(DomainState.Error(""))
//
//        // Act
//        val job: Job?
//        val loginStateResult = mutableListOf<LoginScreenUIState>()
//        job = launch {
//            viewModel.uiState.toList(loginStateResult)
//        }
//        viewModel.login("", "")
//        job.cancel()
//
//        // Assert
//        assertThat(loginStateResult.first(), instanceOf(LoginScreenUIState.Input::class.java))
//        assertThat(loginStateResult[1], instanceOf(LoginScreenUIState.Loading::class.java))
//        assertThat(loginStateResult.last(), instanceOf(LoginScreenUIState.Failed::class.java))
//    }
//
//    @Test
//    fun restLoginUIState() = mainCoroutineRule.runTest {
//        // Act
//        viewModel.resetLoginUIState()
//
//        // Assert
//        assertThat(viewModel.uiState.value, instanceOf(LoginScreenUIState.Input::class.java))
//    }
}