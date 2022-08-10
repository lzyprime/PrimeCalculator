package io.lzyprime.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

sealed interface Btn {
    val text: String

    enum class Num(override val text: String) : Btn {
        N0("0"),
        N1("1"),
        N2("2"),
        N3("3"),
        N4("4"),
        N5("5"),
        N6("6"),
        N7("7"),
        N8("8"),
        N9("9");

    }

    enum class Infix(val func: (Double, Double) -> Double, override val text: String) :
        Btn {
        Plus(Double::plus, "+"),
        Minus(Double::minus, "-"),
        Times(Double::times, "×"),
        Div(Double::div, "÷"),
        MOD(Double::mod, "%")
    }

    enum class OP(override val text: String) : Btn {
        AC("AC"),
        C("C"),
        Sign("+/-"),
        Del("."),
        Eq("="),
    }
}

class MainViewModel : ViewModel() {
    var text by mutableStateOf("0")
        private set

    private var cacheValue = 0.0

    var cacheInfix by mutableStateOf<Btn.Infix?>(null)
        private set
    var step by mutableStateOf(0)
        private set


    fun onClickBtn(btn: Btn) {
        if (text == "Error") {
            fAC()
        }
        when (btn) {
            Btn.OP.AC -> fAC()
            Btn.OP.Del -> fDel()
            Btn.OP.Sign -> fSign()
            is Btn.Num -> fNum(btn)
            is Btn.Infix -> fInfix(btn)
            Btn.OP.C -> fC()
            Btn.OP.Eq -> fEq()
        }
    }

    // AC
    private fun fAC() {
        cacheValue = 0.0
        cacheInfix = null
        text = "0"
        step = 0
    }

    private fun fC() {
        text = "0"
    }

    // .
    private fun fDel() {
        text = if (text.contains(".")) text else "$text."
    }

    // +/-
    private fun fSign() {
        if (step == 1) {
            cacheValue = text.toDouble()
            text = "-0"
            step = 2
            return
        }
        text = if (text.first() == '-') text.substring(1) else "-$text"
    }

    // =
    private fun fEq() {
        if (step == 1) {
            cacheValue = text.toDouble()
            step = 3
        }
        if (step < 2) return
        val v = text.toDouble()
        val newText = try {
            val newV = if (step == 3)
                cacheInfix!!.func(v, cacheValue)
            else
                cacheInfix!!.func(cacheValue, v)


            newV.toString().let {
                if (it.endsWith(".0")) {
                    it.substring(0, it.lastIndex - 1)
                } else
                    it
            }
        } catch (_: Exception) {
            "Error"
        }
        if(step == 2) {
            cacheValue = v
        }
        step = 3
        text = newText
    }

    // 0 ~ 9
    private fun fNum(num: Btn.Num) {
        if (step == 3 || step == 1) {
            text = "0"
        }

        if (step == 1) {
            step = 2
        } else if (step == 3) {
            step = 0
        }

        text = when (text) {
            "0" -> num.text
            "-0" -> "-${num.text}"
            else -> "$text${num.text}"
        }
    }

    // + - * /
    private fun fInfix(op: Btn.Infix) {
        if (step == 2) {
            fEq()
        }
        cacheValue = text.toDouble()
        cacheInfix = op
        step = 1
    }
}