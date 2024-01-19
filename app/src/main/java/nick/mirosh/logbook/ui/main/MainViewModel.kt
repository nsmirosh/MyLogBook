package nick.mirosh.logbook.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nick.mirosh.logbook.domain.model.BmEntry
import nick.mirosh.logbook.domain.model.BmType
import nick.mirosh.logbook.domain.usecase.GetEntriesUseCase
import nick.mirosh.logbook.domain.usecase.SaveBloodMeasurementUseCase
import nick.mirosh.logbook.domain.usecase.mgToMmol
import nick.mirosh.logbook.domain.usecase.mmolToMg
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveBloodMeasurementUseCase: SaveBloodMeasurementUseCase,
    private val getEntriesUseCase: GetEntriesUseCase
//    private val convertMeasurementUseCase: ConvertMeasurementUseCase

) : ViewModel() {

    private val _bloodMeasurementUIState =
        MutableStateFlow(BloodMeasurementUIState())
    val bloodMeasurementUIState: StateFlow<BloodMeasurementUIState> = _bloodMeasurementUIState
    private val _entries =
        MutableStateFlow(listOf<String>())
    val entries: StateFlow<List<String>> = _entries

    private var inputTextValue = BigDecimal(0)

    fun convertTo(bloodMeasurementType: BmType) {
        val result = if (inputTextValue != BigDecimal(0)) {
            if (bloodMeasurementType == BmType.Mmol)
                mgToMmol(inputTextValue)
            else
                mmolToMg(inputTextValue)
        } else {
            BigDecimal(0)
        }
        inputTextValue = result

        _bloodMeasurementUIState.value = BloodMeasurementUIState(
            input = if (result == BigDecimal(0)) "" else formatBigDecimal(result),
            average = "0",
            type = bloodMeasurementType
        )
    }

    fun onTextChanged(inputText: String) {
        //TODO make sure the whole layout is not recomposing when I'm entering the text
        inputTextValue = inputText.toBigDecimal()
        _bloodMeasurementUIState.value = _bloodMeasurementUIState.value.copy(input = inputText)
    }

    fun saveBloodMeasurements() {
        viewModelScope.launch {
            val entry = BmEntry(
                type = _bloodMeasurementUIState.value.type,
                value = inputTextValue
            )
            saveBloodMeasurementUseCase(entry)
        }
    }


    fun formatBigDecimal(value: BigDecimal): String {
        return if (value.stripTrailingZeros().scale() <= 0) {
            value.toPlainString()
        } else {
            value.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        }
    }

    fun getEntries() {
        viewModelScope.launch {
            val entries = getEntriesUseCase()
            _entries.value = entries
        }
    }

}