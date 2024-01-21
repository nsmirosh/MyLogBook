package nick.mirosh.logbook.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    private val _inputTextUIState =
        MutableStateFlow("")
    val inputTextUIState: StateFlow<String> = _inputTextUIState

    private var entries = mutableListOf<BloodGlucoseEntry>()

    init {
        getEntries()
    }

    fun convertMeasurement(text: String, typeToConvertTo: BmType) {
        if (text.isNotEmpty() && text != ".") {
            val converted = convertMeasurementUseCase(typeToConvertTo, text.toBigDecimal())
            _inputTextUIState.value = formatBigDecimal(converted)
        }

        val average = getAverageEntryValueUseCase(entries, typeToConvertTo)
        _bloodMeasurementUIState.value = _bloodMeasurementUIState.value.copy(
            average = formatBigDecimal(average),
            type = typeToConvertTo
        )
    }

    fun saveBloodMeasurements(text: String) {
        //TODO make sure input is validated here beforehand
        viewModelScope.launch {
            val entry = BloodGlucoseEntry(
                type = _bloodMeasurementUIState.value.type,
                value = text.toBigDecimal()
            )
            saveBloodMeasurementUseCase(entry).collect {
                when (it) {
                    is DomainState.Success -> {
                        _inputTextUIState.value = ""
                        getEntries()
                    }

                    is DomainState.Error -> {
                        _entriesUIState.value = BloodEntriesUIState.Error
                    }

                    is DomainState.Loading -> {
                        _entriesUIState.value = BloodEntriesUIState.Loading
                    }

                    is DomainState.Empty -> {

                    }
                }
            }
        }
    }

    private fun getEntries() {
        viewModelScope.launch {
            getEntriesUseCase()
                .collect { domainState ->
                    when (domainState) {
                        is DomainState.Success -> {
                            _entriesUIState.value = BloodEntriesUIState.Success(domainState.data)
                            entries.clear()
                            entries.addAll(domainState.data)
                            val type = bloodMeasurementUIState.value.type
                            val average = getAverageEntryValueUseCase(entries, type)
                            _bloodMeasurementUIState.value =
                                bloodMeasurementUIState.value.copy(
                                    average = formatBigDecimal(average),
                                )
                        }
                        is DomainState.Empty -> BloodEntriesUIState.Empty
                        is DomainState.Error -> BloodEntriesUIState.Error
                        is DomainState.Loading -> BloodEntriesUIState.Loading
                    }
                }
        }
    }

    private fun formatBigDecimal(value: BigDecimal) =
        if (value.stripTrailingZeros().scale() <= 0)
            value.toPlainString()
        else
            value.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()

    fun isValidInput(inputText: String) =
        inputText.isEmpty() || inputText != "." || inputText.toDoubleOrNull()
            ?.let { number -> number > 0 } == true
}