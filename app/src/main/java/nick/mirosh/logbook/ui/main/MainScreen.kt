package nick.mirosh.logbook.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import nick.mirosh.logbook.R


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
//    mainViewModel: MainViewModel = hiltViewModel()
) {
    var isMol by remember { mutableStateOf(false) }
    var text by remember {
        mutableStateOf("")
    }
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text("Your average is $ 12.2 mg /dl")
        Box(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black)
        )
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
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            TextField(value = text,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                onValueChange = {
                    if (it.isEmpty() || it == "." || it.toDoubleOrNull()?.let { number -> number > 0 } == true) {
                        text = it
                    }
                })
            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically), text = "mg/dl"
            )
        }
        Button(
            onClick = {
//                mainViewModel.saveBloodMeasurements(
//                    value = "value",
//                    isMg = isMol
//                )
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
//        mainViewModel = hiltViewModel()
    )
}