package com.dev.nicehash.helpers;

import com.dev.nicehash.R;

/**
 * Created by Alex Gladkov on 16.07.18.
 * Holder for small enum collections
 */
public class EnumCollections {

    public enum Language {
        Russian(R.string.settings_language_russian, 0, "ru"),
        English(R.string.settings_language_english, 1, "en"),
        Chinese(R.string.settings_language_chinese, 2, "cn");

        private final int title;
        private final int intCode;
        private final String countryCode;

        Language(int title, int intCode, String countryCode) {
            this.title = title;
            this.intCode = intCode;
            this.countryCode = countryCode;
        }

        public int getTitle() {
            return title;
        }

        public int getIntCode() {
            return intCode;
        }

        public String getCountryCode() {
            return countryCode;
        }
    }

    public enum Algos {
        Scrypt(0, "Scrypt"), Sha256(1, "SHA256"),
        ScryptNf(2, "Scrypt Nf"), X11(3, "X11"),
        X13(4, "X13"), Keccak(5, "Keccak"),
        X15(6, "X15"), Nist5(7, "Nist 5"),
        NeoScrypt(8, "Neo Scrypt"), Lyra2RE(9, "Lyra2RE"),
        WhirlpoolX(10, "WhirlPoolX"), Qubit(11, "Qubit"),
        Quark(12, "Quark"), Axiom(13, "Axiom"),
        Lyra2REv2(14, "Lyra2REv2"), ScryptJaneNf16(15, "Scrypt Jane Nf16"),
        Blake256r8(16, "Blake 256 r8"), Black256r14(17, "Blake 256 r14"),
        Blake256r8vnl(18, "Blake 256 r8 vnl"), Hodl(19, "Hodl"),
        DaggerHashimoto(20, "DaggerHashimoto"), Decred(21, "Decred"),
        CryptoNight(22, "CryptoNight"), Lbry(23, "Lbry"),
        Equihash(24, "Equihash"), Pascal(25, "Pascal"),
        X11Gost(26, "X11Gost"), Sia(27, "Sia"),
        Blake2s(28, "Blake2s"), Skunk(29, "Skunk"),
        CryptoNightV7(30, "CryptoNightV7"), CryptoNightHeavy(31, "CryptoNightHeavy"),
        Lyra2Z(32, "Lyra2Z"), X16R(33, "X16R");

        private final int id;
        private final String title;

        Algos(int id, String title) {
            this.id = id;
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }

    public enum Status {
        Green, Yellow, Red
    }

    public enum Currency {
        Russian(0, R.string.currency_rub, R.drawable.ic_rub),
        Euro(1, R.string.currency_eur, R.drawable.ic_eur),
        BritainPound(2, R.string.currency_eng, R.drawable.ic_gbp),
        USD(3, R.string.currency_usd, R.drawable.ic_usd),
        Australian(4, R.string.currency_aud, 0),
        Belgium(5, R.string.currency_bgn, R.drawable.ic_bgn),
        Canada(6, R.string.currency_cad, R.drawable.ic_cad),
        CHF(7, R.string.currency_chf, R.drawable.ic_chf),
        China(8, R.string.currency_cny, R.drawable.ic_cny),
        Czech(9, R.string.currency_czk, R.drawable.ic_czk),
        Dania(10, R.string.currency_dkk, R.drawable.ic_dkk);

        private final int id;
        private final int titleRes;
        private final int flagRes;

        Currency(int id, int titleRes, int flagRes) {
            this.id = id;
            this.titleRes = titleRes;
            this.flagRes = flagRes;
        }

        public int getId() {
            return id;
        }

        public int getTitleRes() {
            return titleRes;
        }

        public int getFlagRes() {
            return flagRes;
        }
    }
}
