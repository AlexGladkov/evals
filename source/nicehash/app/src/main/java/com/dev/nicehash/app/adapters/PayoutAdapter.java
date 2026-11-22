package com.dev.nicehash.app.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.nicehash.R;
import com.dev.nicehash.base.BaseAdapter;
import com.dev.nicehash.base.BaseViewHolder;
import com.dev.nicehash.domain.models.Payout;

import java.text.DecimalFormat;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Adapter for payout tab
 */
public class PayoutAdapter extends BaseAdapter<Payout> {

    @Override
    public BaseViewHolder<Payout> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_payout, parent, false));
    }

    public static class ViewHolder extends BaseViewHolder<Payout> {
        private final TextView txtDate;
        private final TextView txtValue;
        private final TextView txtFeeTitle;
        private final TextView txtFeeValue;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtPayoutDate);
            txtValue = itemView.findViewById(R.id.txtPayoutValue);
            txtFeeTitle = itemView.findViewById(R.id.txtPayoutFeeTitle);
            txtFeeValue = itemView.findViewById(R.id.txtPayoutFeeValue);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(Payout model) {
            txtDate.setText(model.getDateTime());
            txtValue.setText(new DecimalFormat("##.########").format(model.getPayBtc()));
            txtFeeTitle.setText(itemView.getContext().getString(R.string.payout_fee_title) + " "
                    + new DecimalFormat("##.##").format(model.getFee()) + "%");
            txtFeeValue.setText(new DecimalFormat("##.########").format(model.getFeeBtc()));
        }
    }
}
