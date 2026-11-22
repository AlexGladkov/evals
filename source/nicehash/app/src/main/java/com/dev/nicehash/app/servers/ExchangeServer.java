package com.dev.nicehash.app.servers;

/**
 * Created by Alex Gladkov on 13.08.18.
 * Server for get current exchange rates (updates in background)
 */
public interface ExchangeServer {
    float convertBtcFor(String code, double value);
    float fetchRate(String code);
}
