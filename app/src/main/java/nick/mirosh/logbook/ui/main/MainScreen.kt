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
import nick.mirosh.logbook.domain.model.BmType


/**
 * If you create a wrapper function that takes the viewmodel extract the state and send it to the
 * screen function it will be much easier to use the preview, that means you can also show that you
 * know how to send lambdas to into a function as well.
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.bloodMeasurementUIState.collectAsState()
    val entriesUIStates by viewModel.entriesUIState.collectAsState()

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text("Your average is ${uiState.average} ${uiState.type}")
        Box(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black)
        )
        Text("Add measurement")
        /**
         * You have 2 similar components,
         * I would create a custom component here to illustrate re-usability.
         */
        Row {
            RadioButton(selected = uiState.type == BmType.Mg, onClick = {
                viewModel.convertTo(BmType.Mg)
            })
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = BmType.Mg.unit
            )
        }
        Row {
            RadioButton(selected = uiState.type == BmType.Mmol, onClick = {
                viewModel.convertTo(BmType.Mmol)
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
            InputText(viewModel)
            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically), text = uiState.type.unit
            )
        }
        Button(
            onClick = {
                viewModel.saveBloodMeasurements()
            },
        ) {
            Text(stringResource(R.string.save))
        }
        EntriesList(entriesUIStates)
    }
}


@Composable
fun InputText(viewModel: MainViewModel) {

    val inputText by viewModel.inputTextUIState.collectAsState()
    TextField(value = inputText,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,

        onValueChange = {
            if (viewModel.isValidInput(it)) {
                viewModel.onTextChanged(it)
            }
        })

}


@Composable
fun EntriesList(entriesUIStates: BloodEntriesUIState) {
    when (entriesUIStates) {
        is BloodEntriesUIState.Empty -> {
            Text("No entries yet")
        }

        is BloodEntriesUIState.Success -> {
            val entries = (entriesUIStates as BloodEntriesUIState.Success).entries
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
    MainScreen(
        modifier = Modifier.background(color = androidx.compose.ui.graphics.Color.White),
//        mainViewModel = hiltViewModel()
    )
}