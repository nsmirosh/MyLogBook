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
import nick.mirosh.logbook.domain.usecase.mgToMmol
import nick.mirosh.logbook.domain.usecase.mmolToMg
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

    private var input = BigDecimal(0)
    private var entries = mutableListOf<BmEntry>()

    init {
        getEntries()
    }

    fun convertTo(bloodMeasurementType: BmType) {
        input = convertMeasurementUseCase(bloodMeasurementType, input)
//        val average = entries.averageValue(bloodMeasurementType)
        val average = getAverageEntryValueUseCase(entries, bloodMeasurementType)
        _bloodMeasurementUIState.value = _bloodMeasurementUIState.value.copy(
            input = if (input == BigDecimal(0)) "" else formatBigDecimal(input),
            average = formatBigDecimal(average),
            type = bloodMeasurementType
        )
    }

    fun onTextChanged(inputText: String) {
        //TODO make sure the whole layout is not recomposing when I'm entering the text
        if (inputText.isEmpty()) {
            _bloodMeasurementUIState.value = _bloodMeasurementUIState.value.copy(input = "")
            return
        }
        input = inputText.toBigDecimal()
        _bloodMeasurementUIState.value =
            _bloodMeasurementUIState.value.copy(input = inputText)
    }

    fun saveBloodMeasurements() {
        viewModelScope.launch {
            val entry = BmEntry(
                type = _bloodMeasurementUIState.value.type,
                value = input
            )
            saveBloodMeasurementUseCase(entry)
            getEntries()
        }
    }

    private fun getEntries() {
        viewModelScope.launch {
            getEntriesUseCase()
                .collect { domainState ->
                    when (domainState) {
                        is DomainState.Success -> {
                            _entriesUIState.value = BloodEntriesUIState.Success(domainState.data)

                            //TODO don't know if introducing another global var is a good idea
                            entries.clear()
                            entries.addAll(domainState.data)
                            val type = _bloodMeasurementUIState.value.type
                            val average = getAverageEntryValueUseCase(entries, type)
//                            val average = entries.averageValue(type)

                            input = BigDecimal(0)
                            _bloodMeasurementUIState.value =
                                _bloodMeasurementUIState.value.copy(
                                    average = formatBigDecimal(average),
                                    input = "",
                                )
                        }

                        is DomainState.Empty -> BloodEntriesUIState.Empty

                        else -> {}
                    }
                }
        }
    }

    private fun formatBigDecimal(value: BigDecimal) =
        if (value.stripTrailingZeros().scale() <= 0)
            value.toPlainString()
        else
            value.setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()


    private fun List<BmEntry>.averageValue(averageOnType: BmType): BigDecimal {
        if (this.isEmpty()) return BigDecimal.ZERO

        val mMolEntries = this.filter { it.type == BmType.Mmol }
        val mMgEntries = this.filter { it.type == BmType.Mg }


        var sum = BigDecimal.ZERO

        if (averageOnType == BmType.Mmol) {
            sum = mMolEntries.fold(BigDecimal.ZERO) { sum, entry -> sum.add(entry.value) }
            val convertedMgToMol = mMgEntries.map { mgToMmol(it.value) }
            convertedMgToMol.forEach {
                sum = sum.add(it)
            }
        } else {
            sum = mMgEntries.fold(BigDecimal.ZERO) { sum, entry -> sum.add(entry.value) }
            val convertedMolToMg = mMolEntries .map { mmolToMg(it.value) }
            convertedMolToMg.forEach {
                sum = sum.add(it)
            }
        }

        return sum.divide(
            BigDecimal(this.size),
            5,
            RoundingMode.HALF_UP
        )
    }

}