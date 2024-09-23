package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout() {
    var amountInput by remember { mutableStateOf("") }
    var tipPercentage by remember { mutableStateOf(0.20) } // Default 20% for "Amazing"
    var roundUp by remember { mutableStateOf(false) }
    var selectedTip by remember { mutableStateOf(20.0) } // Default to Amazing 20%

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, selectedTip, roundUp)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = amountInput,
            onValueChange = { amountInput = it },
            label = { Text(stringResource(R.string.bill_amount)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(R.string.how_was_the_service))

        Spacer(modifier = Modifier.height(8.dp))

        ServiceQualityRadioGroup(
            selectedTip = selectedTip,
            onTipSelected = { selectedTip = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.round_up_tip))
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = roundUp,
                onCheckedChange = { roundUp = it }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.calculate_tip))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )
    }
}

@Composable
fun ServiceQualityRadioGroup(
    selectedTip: Double,
    onTipSelected: (Double) -> Unit
) {
    Column {
        Row {
            RadioButton(
                selected = selectedTip == 20.0,
                onClick = { onTipSelected(20.0) }
            )
            Text(text = "Amazing (20%)")
        }

        Row {
            RadioButton(
                selected = selectedTip == 18.0,
                onClick = { onTipSelected(18.0) }
            )
            Text(text = "Good (18%)")
        }

        Row {
            RadioButton(
                selected = selectedTip == 15.0,
                onClick = { onTipSelected(15.0) }
            )
            Text(text = "OK (15%)")
        }
    }
}

private fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
    var tip = tipPercent / 100 * amount
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipTimeTheme {
        TipTimeLayout()
    }
}
