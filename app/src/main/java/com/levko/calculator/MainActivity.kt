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
import com.levko.calculator.data.Calculator
import com.levko.calculator.ui.theme.CalculatorTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var calculator: Calculator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorView(
                        calculator,
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
fun CalculatorView(calculator: Calculator, modifier: Modifier = Modifier) {
    var expression by remember { mutableStateOf(calculator.defaultExpression) }
    var isResult by remember { mutableStateOf(true) }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ExpressionField(
            expression,
            { expression = calculator.defaultExpression },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(thickness = 2.dp)
        ButtonsGrid({ text ->
            when (text) {
                "=" -> {
                    expression = calculator.calculate(expression).toString()
                    isResult = true
                }

                in "0".."9" -> {
                    expression = if (isResult) text else "$expression$text"
                    isResult = false
                }

                else -> {
                    expression = "$expression$text"
                    isResult = false
                }
            }
        }, modifier = Modifier.fillMaxWidth())
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
    val buttons =
        listOf("7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "0", ".", "=", "+")
    LazyVerticalGrid(columns = GridCells.Fixed(4), modifier = modifier.fillMaxWidth()) {
        items(buttons.size) { index ->
            CalculatorButton(buttons[index], onClicked, modifier = Modifier.padding(8.dp))
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
    val calculator = Calculator()
    CalculatorTheme(darkTheme = true) {
        Surface {
            CalculatorView(calculator)
        }
    }
}