package nick.mirosh.logbook.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import nick.mirosh.logbook.R
import nick.mirosh.logbook.domain.DomainState
import nick.mirosh.logbook.domain.model.BloodGlucoseEntry
import nick.mirosh.logbook.domain.model.BmType
import nick.mirosh.logbook.domain.usecase.GetAverageEntryValueUseCase
import nick.mirosh.logbook.domain.usecase.ConvertMeasurementUseCase
import nick.mirosh.logbook.domain.usecase.GetEntriesUseCase
import nick.mirosh.logbook.domain.usecase.SaveBloodMeasurementUseCase
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveBloodMeasurementUseCase: SaveBloodMeasurementUseCase,
    private val getEntriesUseCase: GetEntriesUseCase,
    private val convertMeasurementUseCase: ConvertMeasurementUseCase,
    private val getAverageEntryValueUseCase: GetAverageEntryValueUseCase

) : ViewModel() {

    private val _bloodMeasurementUIState =
        MutableStateFlow(BloodMeasurementUIState())
    val bloodMeasurementUIState: StateFlow<BloodMeasurementUIState> = _bloodMeasurementUIState
    private val _entriesUIState =
        MutableStateFlow<BloodEntriesUIState>(BloodEntriesUIState.Empty)
    val entriesUIState: StateFlow<BloodEntriesUIState> = _entriesUIState

    //Exposing a separate flow to avoid unneccessary recomposition on the UI
    private val _inputTextUIState =
        MutableStateFlow("")
    val inputTextUIState: StateFlow<String> = _inputTextUIState

    private var inputBigDecimal = BigDecimal(0)
    private var entries = mutableListOf<BloodGlucoseEntry>()

    init {
        getEntries()
    }

    fun convertTo(bloodMeasurementType: BmType) {
        inputBigDecimal = convertMeasurementUseCase(bloodMeasurementType, inputBigDecimal)
        val average = getAverageEntryValueUseCase(entries, bloodMeasurementType)
        _inputTextUIState.value =
            if (inputBigDecimal == BigDecimal(0)) "" else formatBigDecimal(inputBigDecimal)
        _bloodMeasurementUIState.value = _bloodMeasurementUIState.value.copy(
            average = formatBigDecimal(average),
            type = bloodMeasurementType
        )
    }

    fun onTextChanged(inputText: String) {
        if (inputText.isEmpty()) {
            resetInputState()
            return
        }

        if (isValidNumber(inputText)) {
            inputBigDecimal = BigDecimal(inputText)
            _inputTextUIState.value = inputText
        } else {
            showInvalidInputError()
        }
    }

    private fun resetInputState() {
        _inputTextUIState.value = ""
        inputBigDecimal = BigDecimal(0)
    }

    private fun isValidNumber(text: String): Boolean {
        val numberRegex = "^-?\\d*(\\.\\d+)?$".toRegex()
        return text.matches(numberRegex)
    }

    private fun showInvalidInputError() {
        _inputTextUIState.value = ""
        _entriesUIState.value = BloodEntriesUIState.Error(R.string.please_enter_valid_input)
    }

    fun saveBloodMeasurements() {
            viewModelScope.launch {
                val entry = BloodGlucoseEntry(
                    type = _bloodMeasurementUIState.value.type,
                    value = inputBigDecimal
                )
                saveBloodMeasurementUseCase(entry).collect {
                    when (it) {
                        is DomainState.Success -> {
                            getEntries()
                        }

                        is DomainState.Error -> {

                        }

                        is DomainState.Loading -> {
                            _entriesUIState.value = BloodEntriesUIState.Loading
                        }

                        else -> {

                        }
                    }
                }
            }
    }

    private fun getEntries() {
        viewModelScope.launch {
            getEntriesUseCase()
                .distinctUntilChanged() // you ensure that EntriesList recomposes only when there is a new state different from the current one.
                .collect { domainState ->
                    when (domainState) {
                        is DomainState.Success -> {
                            handleSuccess(domainState.data)
                        }

                        is DomainState.Empty -> BloodEntriesUIState.Empty

                        else -> {
                            // no op
                        }
                    }
                }
        }
    }

    private fun handleSuccess(data: List<BloodGlucoseEntry>) {

        val type = bloodMeasurementUIState.value.type
        val average = calculateAverage(data, type)
        updateUIState(data, average)
    }

    private fun calculateAverage(data: List<BloodGlucoseEntry>, type: BmType): BigDecimal {
        return getAverageEntryValueUseCase(data, type)
    }

    private fun updateUIState(data: List<BloodGlucoseEntry>, average: BigDecimal) {
        _entriesUIState.value = BloodEntriesUIState.Success(data)
        _inputTextUIState.value = ""
        _bloodMeasurementUIState.value = _bloodMeasurementUIState.value.copy(
            average = formatBigDecimal(average)
        )
    }

    private fun formatBigDecimal(value: BigDecimal) =
        if (value.stripTrailingZeros().scale() <= 0)
            value.toPlainString()
        else
            value.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()

    fun isValidInput(inputText: String) =
        inputText.isEmpty() || inputText != "." || inputText.toDoubleOrNull()
            ?.let { number -> number > 0 } == true

    fun onEvent(event: UIEvent) {
        when (event) {
            is UIEvent.SaveBloodMeasurements -> {
                saveBloodMeasurements()
            }
        }
    }

    sealed class UIEvent() {
        data object SaveBloodMeasurements : UIEvent()
    }

}