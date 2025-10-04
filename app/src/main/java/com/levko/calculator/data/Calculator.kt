package com.levko.calculator.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Calculator @Inject constructor() {
    private var expression = ""
    private var position = 0
    private var current: Char = ' '
    val defaultExpression: String = "0.0"

    private val operationsMap = mapOf(
        '+' to { a: Double, b: Double ->
            a + b
        },
        '-' to { a: Double, b: Double ->
            a - b
        },
        '*' to { a: Double, b: Double ->
            a * b
        },
        '/' to { a: Double, b: Double ->
            a / b
        }
    )

    companion object {
        private val DIGITS = '0'..'9'
        private const val MULTIPLY_DIVIDE_OPS = "*/"
        private const val ADD_SUBTRACT_OPS = "+-"
        private const val END_CHAR = ' '
    }

    private fun isEnd(): Boolean = position >= expression.length

    private fun skipWhitespaces() {
        while (!isEnd() && expression[position] == ' ') {
            position++
        }
    }

    private fun next(): Char {
        val previousChar = current
        position++
        skipWhitespaces()
        current = if (position >= expression.length) END_CHAR else expression[position]
        return previousChar
    }

    private fun parseNumber(): Double {
        var result = 0.0
        while (current in DIGITS) {
            result = result * 10 + next().digitToInt()
        }
        return result
    }

    private fun parseMultiplyDivide(): Double {
        var result = parseNumber()
        while (current in MULTIPLY_DIVIDE_OPS) {
            result = operationsMap[next()]?.invoke(result, parseNumber()) ?: 0.0
        }
        return result
    }

    private fun parseAddSubtract(): Double {
        var result = parseMultiplyDivide()
        while (current in ADD_SUBTRACT_OPS) {
            result = operationsMap[next()]?.invoke(result, parseMultiplyDivide()) ?: 0.0
        }
        return result
    }

    private fun initializeParser(expression: String) {
        this.expression = expression
        position = 0
        skipWhitespaces()
        current = if (expression.isNotEmpty()) expression[position] else END_CHAR
    }

    fun calculate(expression: String): Double {
        println(expression)
        initializeParser(expression)
        return parseAddSubtract()
    }
}