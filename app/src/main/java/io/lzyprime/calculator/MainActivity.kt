package io.lzyprime.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lzyprime.calculator.ui.theme.ItemPadding
import io.lzyprime.calculator.ui.theme.PrimeCalculatorTheme

class MainActivity : ComponentActivity() {
    private val model by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrimeCalculatorTheme {
                MainContent()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    private fun MainContent() {
        val sizeClass = calculateWindowSizeClass(activity = this)
        val isExpanded = sizeClass.widthSizeClass == WindowWidthSizeClass.Medium
        Scaffold { pd ->
            if (isExpanded)
                Row(
                    modifier = Modifier
                        .padding(pd)
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(), contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            model.text, fontSize = when (model.text.length) {
                                in 0..5 -> 60
                                in 5..10 -> 48
                                else -> 24
                            }.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        ButtonGroupGrid(
                            isExpanded,
                            model.text == "0" || model.text == "-0",
                            if (model.step == 1) model.cacheInfix else null,
                            model::onClickBtn
                        )
                    }
                }
            else
                Column(
                    modifier = Modifier
                        .padding(pd)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = model.text,
                        fontSize = when (model.text.length) {
                            in 0..7 -> 60
                            in 7..10 -> 48
                            else -> 24
                        }.sp
                    )
                    ButtonGroupGrid(
                        isExpanded,
                        model.text == "0" || model.text == "-0",
                        if (model.step == 1) model.cacheInfix else null,
                        model::onClickBtn
                    )
                }
        }
    }
}

@Composable
fun ButtonGroupGrid(
    isExpanded: Boolean,
    showAC: Boolean,
    curInfix: Btn.Infix?,
    onClick: (Btn) -> Unit,
) {
    val btn = listOf<List<Btn>>(
        listOf(if (showAC) Btn.OP.AC else Btn.OP.C, Btn.OP.Sign, Btn.Infix.MOD, Btn.Infix.Div),
        listOf(Btn.Num.N7, Btn.Num.N8, Btn.Num.N9, Btn.Infix.Times),
        listOf(Btn.Num.N4, Btn.Num.N5, Btn.Num.N6, Btn.Infix.Minus),
        listOf(Btn.Num.N1, Btn.Num.N2, Btn.Num.N3, Btn.Infix.Plus),
        listOf(Btn.Num.N0, Btn.OP.Del, Btn.OP.Eq),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = ItemPadding.dp, top = ItemPadding.dp),
    ) {
        btn.forEach { list ->
            Row {
                list.forEach { btn ->
                    BoxWithConstraints(
                        modifier = Modifier
                            .weight(if (btn == Btn.Num.N0) 2f else 1f)
                            .padding(end = ItemPadding.dp, bottom = ItemPadding.dp),
                    ) {

                        TextButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape)
                                .background(
                                    when (btn) {
                                        Btn.OP.AC, Btn.OP.C, Btn.OP.Sign, Btn.Infix.MOD -> MaterialTheme.colorScheme.secondary
                                        is Btn.Num, Btn.OP.Del -> MaterialTheme.colorScheme.tertiary
                                        curInfix -> MaterialTheme.colorScheme.inversePrimary
                                        is Btn.Infix, Btn.OP.Eq -> MaterialTheme.colorScheme.primary //Color(0xFFFFB300)
                                    }
                                ).let {
                                    if(!isExpanded) it.height(if (btn == Btn.Num.N0) maxWidth / 2 else maxWidth) else it
                                },
                            onClick = { onClick(btn) },
                        ) {
                            Text(
                                btn.text,
                                color = when (btn) {
                                    Btn.OP.AC, Btn.OP.C, Btn.OP.Sign, Btn.Infix.MOD -> MaterialTheme.colorScheme.onSecondary
                                    is Btn.Num, Btn.OP.Del -> MaterialTheme.colorScheme.onTertiary
                                    curInfix -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.onPrimary
                                },
                                fontSize = 32.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}
