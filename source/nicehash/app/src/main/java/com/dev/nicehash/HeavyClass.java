package com.dev.nicehash;

import android.util.Log;

public class HeavyClass {

    public static void heavyFunction() {
        for (int i = 0; i < 1000; i++) {
            Log.e("TAG", "Hello " + i);
        }
    }
}
