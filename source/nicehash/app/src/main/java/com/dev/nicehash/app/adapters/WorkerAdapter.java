package com.dev.nicehash.app.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.nicehash.R;
import com.dev.nicehash.base.BaseAdapter;
import com.dev.nicehash.base.BaseViewHolder;
import com.dev.nicehash.domain.models.WorkerHub;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Subclass for worker
 */
public class WorkerAdapter extends BaseAdapter<WorkerHub.Worker> {

    @Override
    public BaseViewHolder<WorkerHub.Worker> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_worker, parent, false));
    }

    public static class ViewHolder extends BaseViewHolder<WorkerHub.Worker> {
        private final TextView txtTitle;
        private final TextView txtSpeed;
        private final TextView txtSpeedValue;
        private final TextView txtDiff;
        private final TextView txtDiffValue;
        private final TextView txtDiffTitle;
        private final TextView txtTime;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtWorkerTitle);
            txtSpeed = itemView.findViewById(R.id.txtWorkerSpeed);
            txtSpeedValue = itemView.findViewById(R.id.txtWorkerSpeedValue);
            txtDiff = itemView.findViewById(R.id.txtWorkerDiff);
            txtDiffValue = itemView.findViewById(R.id.txtWorkerDiffValue);
            txtDiffTitle = itemView.findViewById(R.id.txtWorkerDiffTitle);
            txtTime = itemView.findViewById(R.id.txtWorkerTime);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(WorkerHub.Worker model) {
            txtTitle.setText(model.getTitle());
            txtDiff.setText(" " + model.getDifference());
            txtSpeedValue.setText("+ " + model.getUpDynamic());
            txtSpeed.setText("MH/s");
            txtDiffValue.setText("– " + model.getDownDynamic());
            txtDiffTitle.setText("MH/s");
            txtTime.setText(String.valueOf(model.getMin()));
        }
    }
}
