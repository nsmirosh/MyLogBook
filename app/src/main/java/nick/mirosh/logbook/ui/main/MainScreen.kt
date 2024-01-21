package nick.mirosh.logbook.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import nick.mirosh.logbook.R
import nick.mirosh.logbook.domain.model.BloodGlucoseEntry
import nick.mirosh.logbook.domain.model.BmType


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val bloodMeasurementUiState by viewModel.bloodMeasurementUIState.collectAsState()
    val entriesUiState by viewModel.entriesUIState.collectAsState()
    val inputTextUiState by viewModel.inputTextUIState.collectAsState()
    MainScreenContent(
        modifier = modifier,
        bloodMeasurementUiState = bloodMeasurementUiState,
        inputTextState = inputTextUiState,
        onConvertType = { viewModel.convertTo(it) },
        isValidInput = { viewModel.isValidInput(it) },
        onTextChanged = { viewModel.onTextChanged(it) },
        onSave = { viewModel.saveBloodMeasurements() },
        entriesUIState = entriesUiState
    )

}


@Composable
fun MainScreenContent(
    modifier: Modifier,
    bloodMeasurementUiState: BloodMeasurementUIState,
    entriesUIState: BloodEntriesUIState,
    inputTextState: String,
    onConvertType: (BmType) -> Unit,
    isValidInput: (String) -> Boolean,
    onTextChanged: (String) -> Unit,
    onSave: () -> Unit

) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text("Your average is ${bloodMeasurementUiState.average} ${bloodMeasurementUiState.type}")
        Box(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black)
        )
        Text(stringResource(R.string.add_measurement))
        Row {
            RadioButton(selected = bloodMeasurementUiState.type == BmType.Mg, onClick = {
                onConvertType(BmType.Mg)
            })
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = BmType.Mg.unit
            )
        }
        Row {
            RadioButton(selected = bloodMeasurementUiState.type == BmType.Mmol, onClick = {
                onConvertType(BmType.Mmol)
            })
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = BmType.Mmol.unit
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            InputText(
                inputTextState,
                onTextChanged = onTextChanged,
                isValidInput = isValidInput
            )
            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically), text = bloodMeasurementUiState.type.unit
            )
        }
        Button(
            onClick = {
                onSave()
            },
        ) {
            Text(stringResource(R.string.save))
        }
        EntriesList(entriesUIState)
    }
}

@Composable
fun InputText(
    inputText: String,
    onTextChanged: (String) -> Unit,
    isValidInput: (String) -> Boolean
) {

    TextField(value = inputText,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,

        onValueChange = {
            if (isValidInput(it)) {
                onTextChanged(it)
            }
        })
}

@Composable
fun EntriesList(entriesUIState: BloodEntriesUIState) {
    when (entriesUIState) {
        is BloodEntriesUIState.Empty -> {
            Text(stringResource(R.string.no_entries_yet))
        }

        is BloodEntriesUIState.Success -> {
            val entries = entriesUIState.entries
            LazyColumn {
                items(entries.size) { index ->
                    Text(entries[index].toString())
                }
            }
        }

        else -> {}
    }
}


@Preview
@Composable
fun MainScreenPreview() {
    MainScreenContent(
        modifier = Modifier.background(color = Color.White),
        bloodMeasurementUiState = BloodMeasurementUIState(
            type = BmType.Mg,
            average = "0"
        ),
        inputTextState = "34.9998",
        entriesUIState = BloodEntriesUIState.Success(
            entries = listOf(
                BloodGlucoseEntry(
                    type = BmType.Mg,
                    value = 85.3894.toBigDecimal()
                ),
                BloodGlucoseEntry(
                    type = BmType.Mmol,
                    value = 5.3423.toBigDecimal()
                )
            )
        ),
        onConvertType = {},
        isValidInput = { true },
        onTextChanged = {},
        onSave = {}
    )
}