package nick.mirosh.logbook.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import nick.mirosh.logbook.R


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    var isMol by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        Text("Your average is $ mg/dl")
        Text("Add measurement")
        Row {
            RadioButton(selected = !isMol, onClick = {
                isMol = false
            })
            Text(modifier = Modifier.align(Alignment.CenterVertically), text = "mg/dl")
        }
        Row {
            RadioButton(selected = isMol, onClick = {
                isMol = true
            })
            Text(modifier = Modifier.align(Alignment.CenterVertically), text = "mmol/L")
        }
        Row {
            TextField(value = "", onValueChange = {})
            Text("mg/dl")
        }
        Button(
            onClick = {
                mainViewModel.saveBloodMeasurements(
                    value = "value",
                    isMg = isMol
                )
            },
        ) {
            Text(stringResource(R.string.save))
        }
    }
}


@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        modifier = Modifier.background(color = androidx.compose.ui.graphics.Color.White),
        mainViewModel = hiltViewModel()
    )
}