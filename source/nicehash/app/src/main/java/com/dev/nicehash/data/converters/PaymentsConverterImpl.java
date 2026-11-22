package com.dev.nicehash.data.converters;

import com.dev.nicehash.data.providers.models.ApiPayment;
import com.dev.nicehash.domain.models.Payout;

import org.joda.time.format.DateTimeFormat;

/**
 * Created by Alex Gladkov on 05.08.18.
 * Implementation for payments converter
 */
public class PaymentsConverterImpl implements PaymentsConverter {

    @Override
    public Payout apiToModel(ApiPayment apiPayment) {
        // Kotlin named args: dateTime = ..., fee = ..., payBtc = amount, feeBtc = fee
        return new Payout(
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(apiPayment.getTime())
                        .toString("d/MM · HH:mm"),
                (float) ((apiPayment.getFee() / apiPayment.getAmount()) * 100),
                apiPayment.getAmount(),
                apiPayment.getFee());
    }
}
