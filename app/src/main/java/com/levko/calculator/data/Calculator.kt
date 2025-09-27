package com.levko.calculator.data

class Calculator() {
    private var expression = ""
    private var position = 0
    private var current: Char = ' '

    val operationsMap = mapOf(
        '+' to { a: Int, b:
        Int ->
            println("$a + $b")
            a + b
        },
        '-' to { a: Int, b: Int ->
            println("$a - $b")
            a - b
        },
        '*' to { a: Int, b: Int ->
            println("$a * $b")
            a * b
        },
        '/' to { a: Int, b: Int ->
            println("$a / $b")
            a / b
        }

    )

    fun isEnd(): Boolean {
        return position >= expression.length
    }

    fun whiteSpaces() {
        while (!isEnd() && expression[position] == ' ') {
            position++
        }
    }


    fun next(): Char {
        val c = current
        position++
        whiteSpaces()
        current = (if (position >= expression.length) ' ' else expression[position])
        return c
    }

    fun number(): Int {
        var result = 0
        while (current in '0'..'9') {
            result = result * 10 + next().toString().toInt()
        }
        return result
    }

    fun multiply(): Int {
        var result = number()
        while (current in "*/") {
            result = operationsMap[next()]?.invoke(result, number()) ?: 0
        }
        return result
    }

    fun sum(): Int {
        var result = multiply()
        while (current in "+-") {
            result = operationsMap[next()]?.invoke(result, multiply()) ?: 0
        }
        return result
    }


    fun calculate(s: String): Int {
        expression = s
        println(expression)
        position = 0
        whiteSpaces()
        current = expression[position]
        return sum()
    }
}