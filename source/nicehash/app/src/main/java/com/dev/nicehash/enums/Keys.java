package com.dev.nicehash.enums;

/**
 * Created by Alex Gladkov on 27.06.18.
 * Keys enum for bundle key
 */
public enum Keys {
    Name("NameKey"), Miner("MinerKey"), Configuration("ConfigurationKey"), Light("LightKey"),
    Color("ColorKey"), Balance("BalanceKey"), MinerTitle("MinerTitleKey"), MinerID("MinerIDKey");

    private final String value;

    Keys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
