package com.dev.nicehash.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dev.nicehash.R;
import com.dev.nicehash.base.BaseAdapter;
import com.dev.nicehash.base.BaseViewHolder;
import com.dev.nicehash.domain.models.Status;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Adapter for status activity
 */
public class StatusAdapter extends BaseAdapter<Status> {

    @Override
    public BaseViewHolder<Status> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_status, parent, false));
    }

    public static class ViewHolder extends BaseViewHolder<Status> {
        private final TextView txtTitle;
        private final TextView txtHeaderTitle;
        private final FrameLayout flHeader;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtStatusTitle);
            txtHeaderTitle = itemView.findViewById(R.id.txtStatusHeaderTitle);
            flHeader = itemView.findViewById(R.id.flStatusHeader);
        }

        @Override
        public void bind(Status model) {
            txtTitle.setText(model.getTitle());

            if (model.getHeader().isEmpty()) {
                flHeader.setVisibility(View.GONE);
            } else {
                flHeader.setVisibility(View.VISIBLE);
                txtHeaderTitle.setText(model.getHeader());
            }
        }
    }
}
