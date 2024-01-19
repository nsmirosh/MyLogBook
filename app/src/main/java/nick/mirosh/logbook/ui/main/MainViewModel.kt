package nick.mirosh.logbook.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nick.mirosh.logbook.domain.model.BloodMeasurementType
import nick.mirosh.logbook.domain.usecase.ConvertMeasurementUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
//    private val saveBloodMeasurementUseCase: SaveBloodMeasurementUseCase,
    private val convertMeasurementUseCase: ConvertMeasurementUseCase

) : ViewModel() {

    private val _bloodMeasurementUIState =
        MutableStateFlow(BloodMeasurementUIState())
    val bloodMeasurementUIState: StateFlow<BloodMeasurementUIState> = _bloodMeasurementUIState

    fun onBloodMeasurementChanged(inputText: String, bloodMeasurementType: BloodMeasurementType) {

//        if (bloodMeasurementType == BloodMeasurementType.Mmol)
//            mmolToMg(value)
//        else
//            mgToMmol(value)

    }

    fun saveBloodMeasurements(value: String, isMg: Boolean) {
        viewModelScope.launch {
        }
    }

}