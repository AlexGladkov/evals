package com.dev.nicehash.app.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.nicehash.R;
import com.dev.nicehash.base.BaseAdapter;
import com.dev.nicehash.base.BaseViewHolder;
import com.dev.nicehash.domain.models.Balance;

import java.text.DecimalFormat;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Adapter for balance tab
 */
public class BalanceAdapter extends BaseAdapter<Balance> {

    @Override
    public BaseViewHolder<Balance> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_balance, parent, false));
    }

    public static class ViewHolder extends BaseViewHolder<Balance> {
        private final TextView txtTitle;
        private final TextView txtValue;
        private final TextView txtBtcValue;
        private final TextView txtIncomeValue;
        private final TextView txtIncomeBtcValue;
        private final TextView txtAlgoSpeed;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtBalanceTitle);
            txtValue = itemView.findViewById(R.id.txtBalanceValue);
            txtBtcValue = itemView.findViewById(R.id.txtBalanceBTCValue);
            txtIncomeValue = itemView.findViewById(R.id.txtBalanceIncomeValue);
            txtIncomeBtcValue = itemView.findViewById(R.id.txtBalanceIncomeBTCValue);
            txtAlgoSpeed = itemView.findViewById(R.id.txtBalanceAlgoSpeed);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(Balance model) {
            txtTitle.setText(model.getTitle());
            txtValue.setText(new DecimalFormat("##.##").format(model.getValue()));
            txtBtcValue.setText(new DecimalFormat("##.########").format(model.getBtc()));
            txtIncomeValue.setText(new DecimalFormat("##.##").format(model.getIncomeValue()));
            txtIncomeBtcValue.setText(new DecimalFormat("##.########").format(model.getIncomeBtc()));
            txtAlgoSpeed.setText(model.getSuffix() + itemView.getContext().getString(R.string.balance_algo_speed));
        }
    }
}
