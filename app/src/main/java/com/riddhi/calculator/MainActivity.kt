package com.riddhi.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Calculator() }
    }
}

@Composable
fun Calculator() {

    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(8.dp)
    ) {

        Display(input, result)

        Spacer(modifier = Modifier.height(8.dp))

        val buttons = listOf(
            listOf("C","⌫","÷","×"),
            listOf("7","8","9","-"),
            listOf("4","5","6","+"),
            listOf("1","2","3","="),
            listOf("0",".")
        )

        ButtonGrid(buttons) { btn ->
            val (newInput, newResult) = handleButtonClick(btn, input, result)
            input = newInput
            result = newResult
        }
    }
}

@Composable
fun ColumnScope.Display(input: String, result: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Text(input, fontSize = 26.sp, color = Color.Gray)
        Text(result, fontSize = 42.sp, color = Color.White)
    }
}

@Composable
fun ColumnScope.ButtonGrid(
    buttons: List<List<String>>,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.weight(2f),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        buttons.forEachIndexed { index, row ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (index == buttons.lastIndex) {
                    Spacer(modifier = Modifier.weight(1f))
                }

                row.forEach { btn ->
                    CalcButton(btn) { onClick(btn) }
                }

                if (index == buttons.lastIndex) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
@Composable
fun RowScope.CalcButton(
    text: String,
    onClick: () -> Unit
) {

    val color = when (text) {
        "C","⌫" -> Color(0xFF454B54)
        "+","-","×","÷" -> Color(0xFFFF9F0A)
        "=" -> Color(0xFF007AFF)
        else -> Color(0xFF1C1C1E)
    }

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
    ) {
        Text(text, fontSize = 18.sp, color = Color.White)
    }
}


fun handleButtonClick(
    btn: String,
    input: String,
    result: String
): Pair<String, String> {

    return when (btn) {

        "C" -> "" to "0"

        "⌫" -> {
            if (input.isNotEmpty())
                input.dropLast(1) to result
            else input to result
        }

        "=" -> input to calculate(input)

        else -> (input + btn) to result
    }
}

fun calculate(exp: String): String {
    return try {
        val e = exp.replace("×","*").replace("÷","/")
        val result = simple(e)
        if (result % 1 == 0.0) result.toInt().toString()
        else result.toString()
    } catch (_: Exception) {
        "Error"
    }
}

fun simple(exp: String): Double {

    var result = 0.0
    var number = ""
    var op = '+'

    for (c in "$exp+") {

        if (c.isDigit() || c == '.') {
            number += c
        } else {
            val n = number.toDoubleOrNull() ?: 0.0

            when (op) {
                '+' -> result += n
                '-' -> result -= n
                '*' -> result *= n
                '/' -> result /= n
            }

            op = c
            number = ""
        }
    }

    return result
}
@Preview(showBackground = true)
@Composable
fun PreviewCalculator() {
    Calculator()
}
