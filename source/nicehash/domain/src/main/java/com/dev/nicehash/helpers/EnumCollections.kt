package com.dev.nicehash.helpers

/**
 * Created by Alex Gladkov on 16.07.18.
 * Holder for small enum collections
 *
 * Pure Kotlin: lives in the :domain module, so it must not reference Android resources (R).
 * The res-id mapping for Language/Currency display values lives in the :app module
 * (see com.dev.nicehash.helpers.CurrencyResources).
 */
class EnumCollections {

    enum class Language(val intCode: Int, val countryCode: String) {
        Russian(intCode = 0, countryCode = "ru"),
        English(intCode = 1, countryCode = "en"),
        Chinese(intCode = 2, countryCode = "cn")
    }

    enum class Algos(val id: Int, val title: String) {
        Scrypt(id = 0, title = "Scrypt"), Sha256(id = 1, title = "SHA256"),
        ScryptNf(id = 2, title = "Scrypt Nf"), X11(id = 3, title = "X11"),
        X13(id = 4, title = "X13"), Keccak(id = 5, title = "Keccak"),
        X15(id = 6, title = "X15"), Nist5(id = 7, title = "Nist 5"),
        NeoScrypt(id = 8, title = "Neo Scrypt"), Lyra2RE(id = 9, title = "Lyra2RE"),
        WhirlpoolX(id = 10, title = "WhirlPoolX"), Qubit(id = 11, title = "Qubit"),
        Quark(id = 12, title = "Quark"), Axiom(id = 13, title = "Axiom"),
        Lyra2REv2(id = 14, title = "Lyra2REv2"), ScryptJaneNf16(id = 15, title = "Scrypt Jane Nf16"),
        Blake256r8(id = 16, title = "Blake 256 r8"), Black256r14(id = 17, title = "Blake 256 r14"),
        Blake256r8vnl(id = 18, title = "Blake 256 r8 vnl"), Hodl(id = 19, title = "Hodl"),
        DaggerHashimoto(id = 20, title = "DaggerHashimoto"), Decred(id = 21, title = "Decred"),
        CryptoNight(id = 22, title = "CryptoNight"), Lbry(id = 23, title = "Lbry"),
        Equihash(id = 24, title = "Equihash"), Pascal(id = 25, title = "Pascal"),
        X11Gost(id = 26, title = "X11Gost"), Sia(id = 27, title = "Sia"),
        Blake2s(id = 28, title = "Blake2s"), Skunk(id = 29, title = "Skunk"),
        CryptoNightV7(id = 30, title = "CryptoNightV7"), CryptoNightHeavy(id = 31, title = "CryptoNightHeavy"),
        Lyra2Z(id = 32, title = "Lyra2Z"), X16R(id = 33, title = "X16R")
    }

    enum class Status {
        Green, Yellow, Red
    }

    enum class Currency(val id: Int) {
        Russian(id = 0),
        Euro(id = 1),
        BritainPound(id = 2),
        USD(id = 3),
        Australian(id = 4),
        Belgium(id = 5),
        Canada(id = 6),
        CHF(id = 7),
        China(id = 8),
        Czech(id = 9),
        Dania(id = 10)
    }
}