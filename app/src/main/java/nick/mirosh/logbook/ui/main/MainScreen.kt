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
import nick.mirosh.logbook.data.model.MeasurementUnit
import nick.mirosh.logbook.domain.model.BmType


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {

    val uiState by viewModel.bloodMeasurementUIState.collectAsState()
    val entries by viewModel.entries.collectAsState()

    with(uiState) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Your average is $average $type")
            Box(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Black)
            )
            Text("Add measurement")
            Row {
                RadioButton(selected = type == BmType.Mg, onClick = {
                    viewModel.convertTo(BmType.Mg)
                })
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = BmType.Mg.unit
                )
            }
            Row {
                RadioButton(selected = type == BmType.Mmol, onClick = {
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
                TextField(value = input,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,

                    onValueChange = {
                        if (it.isEmpty() || it == "." || it.toDoubleOrNull()
                                ?.let { number -> number > 0 } == true
                        ) {
                            viewModel.onTextChanged(it)
                        }
                    })
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.CenterVertically), text = type.unit
                )
            }
            Button(
                onClick = {
                    viewModel.saveBloodMeasurements()
                },
            ) {
                Text(stringResource(R.string.save))
            }
            Button(
                onClick = {
                    viewModel.getEntries()
                },
            ) {
                Text("get entries")
            }

            LazyColumn {
                items(entries.size) { index ->
                    Text(entries[index])
                }
            }
        }
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