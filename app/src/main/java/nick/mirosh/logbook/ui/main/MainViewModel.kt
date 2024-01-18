package nick.mirosh.logbook.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nick.mirosh.logbook.domain.usecase.ConvertMeasurementUseCase
import nick.mirosh.logbook.domain.usecase.SaveBloodMeasurementUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveBloodMeasurementUseCase: SaveBloodMeasurementUseCase,
    private val convertMeasurementUseCase: ConvertMeasurementUseCase

) : ViewModel() {

    fun saveBloodMeasurements(value: String, isMg: Boolean) {
        viewModelScope.launch {
//            val result = convertMeasurementUseCase(isMg, value.toBigDecimal())
            saveBloodMeasurementUseCase(value.toBigDecimal())
        }
    }

}