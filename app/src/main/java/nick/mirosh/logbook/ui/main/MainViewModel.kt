package nick.mirosh.logbook.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nick.mirosh.logbook.domain.model.BloodMeasurementType
import nick.mirosh.logbook.domain.usecase.mgToMmol
import nick.mirosh.logbook.domain.usecase.mmolToMg
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
//    private val saveBloodMeasurementUseCase: SaveBloodMeasurementUseCase,
//    private val convertMeasurementUseCase: ConvertMeasurementUseCase

) : ViewModel() {

    private val _bloodMeasurementUIState =
        MutableStateFlow(BloodMeasurementUIState())
    val bloodMeasurementUIState: StateFlow<BloodMeasurementUIState> = _bloodMeasurementUIState

    private var inputTextValue = BigDecimal(0)
    private var measurementType = BloodMeasurementType.Mg

    fun convertTo(bloodMeasurementType: BloodMeasurementType) {
        val result = if (inputTextValue != BigDecimal(0)) {
            if (bloodMeasurementType == BloodMeasurementType.Mmol)
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
        inputTextValue = inputText.toBigDecimal()
        _bloodMeasurementUIState.value = _bloodMeasurementUIState.value.copy(input = inputText)
    }

    fun saveBloodMeasurements(value: String, isMg: Boolean) {
        viewModelScope.launch {
        }
    }


    fun formatBigDecimal(value: BigDecimal): String {
        return if (value.stripTrailingZeros().scale() <= 0) {
            value.toPlainString()
        } else {
            value.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        }
    }

}