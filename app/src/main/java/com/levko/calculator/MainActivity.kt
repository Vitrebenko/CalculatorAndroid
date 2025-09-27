package com.levko.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.levko.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorView(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorView(modifier: Modifier = Modifier) {
    var expression by remember { mutableStateOf("9999") }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ExpressionField(
            expression,
            { expression = "" },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(thickness = 2.dp)
        ButtonsGrid({ text -> expression = "$expression$text" }, modifier = Modifier.fillMaxWidth())
    }
}


@Composable
fun ExpressionField(text: String, onClearClicked: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text, style = MaterialTheme.typography.displayMedium, modifier = Modifier.weight(1f))
        IconButton(onClick = onClearClicked) {
            Icon(Icons.Default.Clear, contentDescription = "Clear")
        }
    }
}


@Composable
fun ButtonsGrid(onClicked: (text: String) -> Unit, modifier: Modifier = Modifier) {
    val numbers =
        listOf("7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "0", ".", "=", "+")
    LazyVerticalGrid(columns = GridCells.Fixed(4), modifier = modifier.fillMaxWidth()) {
        items(numbers.size) { index ->
            CalculatorButton(numbers[index], onClicked, modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    onClicked: (text: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Button({
        onClicked(text)
    }, modifier = modifier) {
        Text(text, style = MaterialTheme.typography.displayLarge)
    }

}

@Preview
@Composable
private fun CalculatorViewPreview() {
    CalculatorTheme(darkTheme = true) {
        Surface {
            CalculatorView()
        }
    }
}