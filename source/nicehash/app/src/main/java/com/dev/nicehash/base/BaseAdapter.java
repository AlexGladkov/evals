package com.dev.nicehash.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex Gladkov on 23.06.18.
 * Base adapter for RecyclerView
 */
public abstract class BaseAdapter<P> extends RecyclerView.Adapter<BaseViewHolder<P>> {
    private final List<P> mDataList = new ArrayList<>();
    private BaseAdapterCallback<P> mCallback = null;
    private boolean hasItems = false;

    public void attachCallback(BaseAdapterCallback<P> callback) {
        this.mCallback = callback;
    }

    public void detachCallback() {
        this.mCallback = null;
    }

    public boolean hasItems() {
        return hasItems;
    }

    public void setHasItems(boolean hasItems) {
        this.hasItems = hasItems;
    }

    public void setList(List<P> dataList) {
        ((ArrayList<P>) mDataList).addAll(dataList);
        hasItems = true;
        notifyDataSetChanged();
    }

    public void deleteItem(P item) {
        int position = mDataList.indexOf(item);
        ((ArrayList<P>) mDataList).remove(item);
        notifyItemRemoved(position);
    }

    public void addItems(List<P> newItems) {
        for (P item : newItems) {
            addItem(item);
        }
    }

    public void addItem(P newItem) {
        ((ArrayList<P>) mDataList).add(newItem);
        notifyItemInserted(mDataList.size() - 1);
    }

    public void addItemToTop(P newItem) {
        ((ArrayList<P>) mDataList).add(0, newItem);
        notifyItemInserted(0);
    }

    public void addItemsToTop(List<P> newItems) {
        List<P> reversed = new ArrayList<>(newItems);
        java.util.Collections.reverse(reversed);
        for (P item : reversed) {
            addItemToTop(item);
        }
    }

    public void updateItem(int position, P newItem) {
        if (position < mDataList.size()) {
            ((ArrayList<P>) mDataList).set(position, newItem);
            notifyItemChanged(position);
        }
    }

    public void updateItems(List<P> itemsList) {
        ((ArrayList<P>) mDataList).clear();
        setList(itemsList);
    }

    public void removeItem(P item) {
        int position = mDataList.indexOf(item);
        ((ArrayList<P>) mDataList).remove(item);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataList.size());
    }

    public void removeItemAt(int position) {
        ((ArrayList<P>) mDataList).remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataList.size());
    }

    public void insertItemAt(int position, P newItem) {
        ((ArrayList<P>) mDataList).add(position, newItem);
        notifyItemInserted(position);
    }

    public List<P> getItems() {
        return mDataList;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<P> holder, int position) {
        holder.bind(mDataList.get(position));

        holder.itemView.setOnClickListener(v -> {
            if (mCallback != null) {
                mCallback.onItemClick(mDataList.get(position), holder.itemView);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (mCallback == null) {
                return false;
            } else {
                return mCallback.onLongClick(mDataList.get(position), holder.itemView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
