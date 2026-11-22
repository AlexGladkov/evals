package com.dev.nicehash.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.dev.nicehash.R;
import com.dev.nicehash.app.models.Switch;
import com.dev.nicehash.base.BaseAdapter;
import com.dev.nicehash.base.BaseViewHolder;

/**
 * Created by Alex Gladkov on 25.06.18.
 * Adapter for switchable cells
 */
public class SwitchAdapter extends BaseAdapter<Switch> {

    @Override
    public BaseViewHolder<Switch> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_switch, parent, false));
    }

    public static class ViewHolder extends BaseViewHolder<Switch> {
        private final TextView txtTitle;
        private final SwitchCompat switcher;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtSwitchTitle);
            switcher = (SwitchCompat) itemView.findViewById(R.id.swSwitch);
        }

        @Override
        public void bind(Switch model) {
            txtTitle.setText(model.getTitle());
            switcher.setChecked(model.isChecked());
        }
    }
}
