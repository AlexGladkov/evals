package com.dev.nicehash.app.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.nicehash.R
import com.dev.nicehash.app.viewmodels.IncomeViewModel
import com.dev.nicehash.domain.models.Income
import kotlinx.coroutines.flow.StateFlow
import java.text.DecimalFormat

/**
 * Income tab screen in Jetpack Compose. Replicates the former fragment_income.xml layout:
 * hour/day row, divider, week/month row, divider, BTC/USD and BTC/RUB rates bar, divider.
 */
@Composable
fun IncomeScreen(
    state: StateFlow<IncomeViewModel.UiState>,
    accentColor: Int,
    isDark: Boolean,
    onIncomeLoaded: (Income) -> Unit
) {
    val bgPrimary = if (isDark) R.color.dark_background_primary else R.color.light_background_primary
    val textPrimary = if (isDark) R.color.dark_text_color_primary else R.color.light_text_color_primary
    val dividerColor = if (isDark) R.color.dark_divider_color else R.color.light_divider_color
    val textHint = R.color.text_color_hint

    val uiState = state.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = bgPrimary))
    ) {
        when (uiState) {
            is IncomeViewModel.UiState.Loading -> {
                // Same skeleton the XML layout showed before data arrived (empty values)
                IncomeContent(
                    accentColor = accentColor,
                    bgPrimaryRes = bgPrimary,
                    textPrimaryRes = textPrimary,
                    dividerColorRes = dividerColor,
                    textHintRes = textHint
                )
            }
            is IncomeViewModel.UiState.Data -> {
                LaunchedEffect(uiState.income) { onIncomeLoaded(uiState.income) }
                IncomeContent(
                    accentColor = accentColor,
                    bgPrimaryRes = bgPrimary,
                    textPrimaryRes = textPrimary,
                    dividerColorRes = dividerColor,
                    textHintRes = textHint,
                    hourValue = DecimalFormat("##.##").format(uiState.income.hourValue),
                    hourBtc = DecimalFormat("##.########").format(uiState.income.hourBtc),
                    dayValue = DecimalFormat("##.##").format(uiState.income.dayValue),
                    dayBtc = DecimalFormat("##.########").format(uiState.income.dayBtc),
                    weekValue = DecimalFormat("##.##").format(uiState.income.weekValue),
                    weekBtc = DecimalFormat("##.########").format(uiState.income.weekBtc),
                    monthValue = DecimalFormat("##.##").format(uiState.income.monthValue),
                    monthBtc = DecimalFormat("##.########").format(uiState.income.monthBtc),
                    usdRate = " ${DecimalFormat("##.##").format(uiState.usdRate)}",
                    currencyRate = DecimalFormat("##.##").format(uiState.currencyRate)
                )
            }
            is IncomeViewModel.UiState.Error -> {
                IncomeContent(
                    accentColor = accentColor,
                    bgPrimaryRes = bgPrimary,
                    textPrimaryRes = textPrimary,
                    dividerColorRes = dividerColor,
                    textHintRes = textHint
                )
                Text(
                    text = uiState.message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = colorResource(id = textHint)
                )
            }
        }
    }
}

@Composable
private fun IncomeContent(
    accentColor: Int,
    bgPrimaryRes: Int,
    textPrimaryRes: Int,
    dividerColorRes: Int,
    textHintRes: Int,
    hourValue: String = "",
    hourBtc: String = "",
    dayValue: String = "",
    dayBtc: String = "",
    weekValue: String = "",
    weekBtc: String = "",
    monthValue: String = "",
    monthBtc: String = "",
    usdRate: String = "",
    currencyRate: String = ""
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            PeriodCell(
                label = stringResource(id = R.string.income_hour),
                value = hourValue,
                btcValue = hourBtc,
                indented = true,
                accentColor = accentColor,
                textPrimaryRes = textPrimaryRes,
                textHintRes = textHintRes,
                modifier = Modifier.weight(1f)
            )
            PeriodCell(
                label = stringResource(id = R.string.income_day),
                value = dayValue,
                btcValue = dayBtc,
                indented = false,
                accentColor = accentColor,
                textPrimaryRes = textPrimaryRes,
                textHintRes = textHintRes,
                modifier = Modifier.weight(1f)
            )
        }

        DividerLine(colorRes = dividerColorRes, heightDp = 0.5f)

        Row(modifier = Modifier.fillMaxWidth()) {
            PeriodCell(
                label = stringResource(id = R.string.income_week),
                value = weekValue,
                btcValue = weekBtc,
                indented = true,
                accentColor = accentColor,
                textPrimaryRes = textPrimaryRes,
                textHintRes = textHintRes,
                modifier = Modifier.weight(1f)
            )
            PeriodCell(
                label = stringResource(id = R.string.income_month),
                value = monthValue,
                btcValue = monthBtc,
                indented = false,
                accentColor = accentColor,
                textPrimaryRes = textPrimaryRes,
                textHintRes = textHintRes,
                modifier = Modifier.weight(1f)
            )
        }

        DividerLine(colorRes = dividerColorRes, heightDp = 1f)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.income_btc_usd),
                        fontSize = 12.sp,
                        color = colorResource(id = textHintRes)
                    )
                    Text(
                        text = usdRate,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = textPrimaryRes)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(0.5.dp)
                    .height(16.dp)
                    .alpha(0.2f)
                    .background(colorResource(id = dividerColorRes))
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${stringResource(id = R.string.currency_btc)}/" +
                                "${stringResource(id = R.string.currency_rub)} = ",
                        fontSize = 12.sp,
                        color = colorResource(id = textHintRes)
                    )
                    Text(
                        text = currencyRate,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = textPrimaryRes)
                    )
                }
            }
        }

        DividerLine(colorRes = dividerColorRes, heightDp = 1f)
    }
}

@Composable
private fun PeriodCell(
    label: String,
    value: String,
    btcValue: String,
    indented: Boolean,
    accentColor: Int,
    textPrimaryRes: Int,
    textHintRes: Int,
    modifier: Modifier = Modifier
) {
    val startPadding = if (indented) 16.dp else 0.dp

    Column(
        modifier = modifier.height(80.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(top = 12.dp, start = startPadding),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (accentColor == 0) colorResource(id = textHintRes) else Color(accentColor)
        )

        Row(modifier = Modifier.padding(top = 2.dp, start = startPadding)) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(id = textPrimaryRes)
            )
            Text(
                text = stringResource(id = R.string.currency_rub),
                modifier = Modifier.padding(start = 4.dp),
                fontSize = 12.sp,
                color = colorResource(id = textPrimaryRes)
            )
        }

        Row(modifier = Modifier.padding(top = 2.dp, start = startPadding)) {
            Text(
                text = btcValue,
                fontSize = 12.sp,
                color = colorResource(id = textHintRes)
            )
            Text(
                text = stringResource(id = R.string.currency_btc),
                modifier = Modifier.padding(start = 2.dp),
                fontSize = 8.sp,
                color = colorResource(id = textHintRes)
            )
        }
    }
}

@Composable
private fun DividerLine(colorRes: Int, heightDp: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightDp.dp)
            .alpha(0.2f)
            .background(colorResource(id = colorRes))
    )
}
