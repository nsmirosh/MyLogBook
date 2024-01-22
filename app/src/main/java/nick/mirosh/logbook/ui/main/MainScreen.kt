package nick.mirosh.logbook.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import nick.mirosh.logbook.R
import nick.mirosh.logbook.domain.model.BloodGlucoseEntry
import nick.mirosh.logbook.domain.model.BmType
import java.math.BigDecimal
import java.math.RoundingMode


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val bloodMeasurementUiState by viewModel.bloodMeasurementUIState.collectAsState()
    val entriesUiState by viewModel.entriesUIState.collectAsState()
    val inputTextUiState by viewModel.inputTextUIState.collectAsState()

    var inputText by remember { mutableStateOf(inputTextUiState) }

    LaunchedEffect(inputTextUiState) {
        inputText = inputTextUiState
    }

    MainScreenContent(
        modifier = modifier,
        bloodMeasurementUiState = bloodMeasurementUiState,
        inputTextUiState = inputTextUiState,
        onConvertType = { viewModel.convertMeasurement(inputText, it) },
        isValidInput = { viewModel.isValidInput(it) },
        onTextChanged = {
            inputText = it
        },
        onSave = {
            viewModel.saveBloodMeasurements(inputText)
            inputText = ""
        },

        entriesUiState = entriesUiState
    )

}


@Composable
fun MainScreenContent(
    modifier: Modifier,
    bloodMeasurementUiState: BloodMeasurementUIState,
    entriesUiState: BloodEntriesUIState,
    inputTextUiState: String,
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
        Text(
            text = stringResource(
                R.string.average_blood_measurement,
                bloodMeasurementUiState.average,
                bloodMeasurementUiState.type
            )
        )
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
                if (bloodMeasurementUiState.type != BmType.Mg) {
                    onConvertType(BmType.Mg)
                }
            })
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = BmType.Mg.unit
            )
        }
        Row {
            RadioButton(selected = bloodMeasurementUiState.type == BmType.Mmol, onClick = {
                if (bloodMeasurementUiState.type != BmType.Mmol) {
                    onConvertType(BmType.Mmol)
                }
            })
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = BmType.Mmol.unit
            )
        }
        Input(
            inputTextUiState = inputTextUiState,
            bloodMeasurementUiState = bloodMeasurementUiState,
            onTextChanged = onTextChanged,
            isValidInput = isValidInput,
            onSave = onSave
        )
        EntriesList(entriesUiState)
    }
}

@Composable
fun Input(
    inputTextUiState: String,
    bloodMeasurementUiState: BloodMeasurementUIState,
    isValidInput: (String) -> Boolean,
    onTextChanged: (String) -> Unit,
    onSave: () -> Unit,
) {
    Column {
        var text by remember { mutableStateOf(inputTextUiState) }
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {

            LaunchedEffect(inputTextUiState) {
                text = inputTextUiState
            }
            TextField(
                value = text,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,

                onValueChange = {
                    if (isValidInput(it)) {
                        onTextChanged(it)
                        text = it
                    }
                })

            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically), text = bloodMeasurementUiState.type.unit
            )
        }
        Button(
            modifier = Modifier
                .align(Alignment.End)
                .width(150.dp)
                .padding(top = 8.dp),
            enabled = text.isNotEmpty(),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 12.dp,
                pressedElevation = 14.dp,
            ),
            onClick = {
                text = ""
                onSave()
            },
        ) {
            Text(stringResource(R.string.save))
        }
    }

}

@Composable
fun EntriesList(entriesUIState: BloodEntriesUIState) {
    when (entriesUIState) {
        is BloodEntriesUIState.Empty -> {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.no_entries_yet))
            }
        }

        is BloodEntriesUIState.Success -> {
            val entries = entriesUIState.entries
            LazyColumn {
                items(entries.size) { index ->
                    BloodGlucoseEntryItem(entries[index])
                }
            }
        }

        is BloodEntriesUIState.Loading ->
            Text(stringResource(R.string.loading))

        is BloodEntriesUIState.Error ->
            Text(stringResource(R.string.entries_error))

    }
}

@Composable
fun BloodGlucoseEntryItem(entry: BloodGlucoseEntry) {
    Column {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.bm_entry_type, entry.type.name),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = stringResource(R.string.bm_entry_value, entry.value.setScale(2, RoundingMode.HALF_UP)),
                modifier = Modifier.weight(1f)
            )
        }
        Divider(color = Color.Gray, thickness = 1.dp)
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
        inputTextUiState = "34.9998",
        entriesUiState = BloodEntriesUIState.Success(
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