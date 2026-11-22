package com.dev.nicehash.app.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.dev.nicehash.R;
import com.dev.nicehash.base.BaseAdapter;
import com.dev.nicehash.base.BaseViewHolder;
import com.dev.nicehash.domain.models.Currency;
import com.dev.nicehash.helpers.EnumCollections;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Adapter for currency activity
 */
public class CurrencyAdapter extends BaseAdapter<Currency> {
    private final String TAG = CurrencyAdapter.class.getSimpleName();

    @Override
    public BaseViewHolder<Currency> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_currency, parent, false));
    }

    public boolean checkSameFirstPosition(Currency model) {
        return getItems().get(0).getId() == model.getId();
    }

    public boolean checkIsPopular(Currency model) {
        return model.getId() == EnumCollections.Currency.Euro.getId()
                || model.getId() == EnumCollections.Currency.USD.getId()
                || model.getId() == EnumCollections.Currency.BritainPound.getId();
    }

    public void performPopularClicked(Currency model) {
        if (getItems().get(4).getId() == EnumCollections.Currency.Australian.getId()) {
//            getItems().firstOrNull { currency -> model.id == currency.id }?.let {
//                removeItemAt(getItems().indexOf(it))
//            }
//            removeItemAt(position = 1)
//            removeItemAt(position = 1)
//            removeItemAt(position = 1)
//
//            when (model.id) {
//                EnumCollections.Currency.Euro.id -> {
//
//                }
//
//                EnumCollections.Currency.USD.id -> {
//
//                }
//
//                EnumCollections.Currency.BritainPound.id -> {
//
//                }
//            }
        } else {
            removeItemAt(1);
            removeItemAt(1);
        }
    }

    public static class ViewHolder extends BaseViewHolder<Currency> {
        private final TextView txtTitle;
        private final TextView txtHeaderTitle;
        private final TextView txtBtc;
        private final FrameLayout flHeader;
        private final ImageView imgFlag;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtCurrencyTitle);
            txtHeaderTitle = itemView.findViewById(R.id.txtCurrencyHeaderTitle);
            txtBtc = itemView.findViewById(R.id.txtCurrencyBtc);
            flHeader = itemView.findViewById(R.id.flCurrencyHeader);
            imgFlag = itemView.findViewById(R.id.imgCurrencyFlag);
        }

        @Override
        public void bind(Currency model) {
            txtTitle.setText(itemView.getContext().getString(model.getTitle()));
            txtBtc.setText(model.getBtc());

            if (model.getImgRes() > 0) {
                imgFlag.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), model.getImgRes()));
            }

            if (model.getHeader() == -1) {
                flHeader.setVisibility(View.GONE);
            } else {
                flHeader.setVisibility(View.VISIBLE);
                txtHeaderTitle.setText(itemView.getContext().getString(model.getHeader()));
            }
        }
    }
}
