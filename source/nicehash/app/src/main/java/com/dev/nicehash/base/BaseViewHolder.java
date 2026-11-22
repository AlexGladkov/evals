package com.dev.nicehash.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by agladkov on 25.12.17.
 * Base view holder
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T model);
}
