package com.dev.nicehash.base;

import android.view.View;

/**
 * Created by Alex Gladkov on 23.06.18.
 * Default operation for recycler view adapters
 */
public interface BaseAdapterCallback<T> {
    void onItemClick(T model, View view);
    boolean onLongClick(T model, View view);
}
