package com.dev.nicehash.app.navigation;

import com.github.terrakok.cicerone.Cicerone;
import com.github.terrakok.cicerone.Router;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by agladkov on 11.01.18.
 * Performs subnNvigation for holding fragments state when clicks activity holder tabs
 */
public class LocalCiceroneHolder {
    private final Map<String, Cicerone<Router>> containers = new HashMap<>();

    public Cicerone<Router> getCicerone(String containerTag) {
        if (!containers.containsKey(containerTag)) {
            containers.put(containerTag, Cicerone.create());
        }

        return containers.get(containerTag);
    }
}
