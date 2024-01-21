package nick.mirosh.logbook.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nick.mirosh.logbook.domain.DomainState
import nick.mirosh.logbook.domain.model.BmEntry
import nick.mirosh.logbook.domain.model.BmType
import nick.mirosh.logbook.domain.usecase.GetAverageEntryValueUseCase
import nick.mirosh.logbook.domain.usecase.ConvertMeasurementUseCase
import nick.mirosh.logbook.domain.usecase.GetEntriesUseCase
import nick.mirosh.logbook.domain.usecase.SaveBloodMeasurementUseCase
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

/**
 * Create a MainScreenUseCases data class with these 4 use cases, make it prettier.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveBloodMeasurementUseCase: SaveBloodMeasurementUseCase,
    private val getEntriesUseCase: GetEntriesUseCase,
    private val convertMeasurementUseCase: ConvertMeasurementUseCase,
    private val getAverageEntryValueUseCase: GetAverageEntryValueUseCase

) : ViewModel() {

    /**
     * You should only have one UIState, in this UIState you can put the domain models
     * for this screen.
     *
     *  data class UIState {
     *     val average:String = ""
     *     val entries:List<BmEntry> = emptyList()
     *     val input: BigDecimal = BigDecimal()
     *     val type: BmType = Mmol
     *  }
     *
     *  then when you load the different parts you can chose to wait for all or just copy
     *  the data class: uiState = uiState.copy(average = newValue)
     */
    private val _bloodMeasurementUIState =
        MutableStateFlow(BloodMeasurementUIState())
    val bloodMeasurementUIState: StateFlow<BloodMeasurementUIState> = _bloodMeasurementUIState
    private val _entriesUIState =
        MutableStateFlow<BloodEntriesUIState>(BloodEntriesUIState.Empty)
    val entriesUIState: StateFlow<BloodEntriesUIState> = _entriesUIState
    private val _inputTextUIState =
        MutableStateFlow("")
    val inputTextUIState: StateFlow<String> = _inputTextUIState

    /**
     * Should rarely need anything outside the UIState.
     */
    private var inputBigDecimal = BigDecimal(0)
    private var entries = mutableListOf<BmEntry>()

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

    /**
     * UIEvents are a way to define what the user can do, I do them as sealed class and sent
     * them to an onEvent function in the view model.
     */
    fun onTextChanged(inputText: String) {
        if (inputText.isEmpty()) {
            _inputTextUIState.value = ""
            inputBigDecimal = BigDecimal(0)
            return
        }
        inputBigDecimal = inputText.toBigDecimal()
        _inputTextUIState.value = inputText
    }

    fun saveBloodMeasurements() {
        viewModelScope.launch {
            val entry = BmEntry(
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
                .collect { domainState ->
                    when (domainState) {
                        is DomainState.Success -> {
                            _entriesUIState.value = BloodEntriesUIState.Success(domainState.data)
                            entries.clear()
                            entries.addAll(domainState.data)
                            val type = bloodMeasurementUIState.value.type
                            val average = getAverageEntryValueUseCase(entries, type)
                            inputBigDecimal = BigDecimal(0)
                            _inputTextUIState.value = ""
                            _bloodMeasurementUIState.value =
                                bloodMeasurementUIState.value.copy(
                                    average = formatBigDecimal(average),
                                )
                        }

                        is DomainState.Empty -> BloodEntriesUIState.Empty

                        /**
                         * else is never good like this, if a new state is added it's easy to miss,
                         * that's why having a domain state like this have some issues.
                         **/
                        else -> {}
                    }
                }
        }
    }

    /**
     * Move to use case, use case can deliver ready to show values as strings.
     * ALso very easy to write unit test then.
     */
    private fun formatBigDecimal(value: BigDecimal) =
        if (value.stripTrailingZeros().scale() <= 0)
            value.toPlainString()
        else
            value.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()

    /**
     * Move to use case, very easy to write unit test then.
     */
    fun isValidInput(inputText: String) =
        inputText.isEmpty() || inputText != "." || inputText.toDoubleOrNull()
            ?.let { number -> number > 0 } == true
}