package com.dev.nicehash.domain.models

/**
 * Created by Alex Gladkov on 15.07.18.
 * Model for balance cell (domain)
 */
data class Balance(val id: Int, val title: String, var value: Float, val btc: Double,
                   var incomeValue: Float, val incomeBtc: Double, val suffix: String)