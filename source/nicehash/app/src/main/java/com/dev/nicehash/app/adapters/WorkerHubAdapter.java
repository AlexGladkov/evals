package com.dev.nicehash.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dev.nicehash.R;
import com.dev.nicehash.base.BaseAdapter;
import com.dev.nicehash.base.BaseViewHolder;
import com.dev.nicehash.domain.models.WorkerHub;
import com.dev.nicehash.helpers.ListConfig;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Adapter for worker tab
 */
public class WorkerHubAdapter extends BaseAdapter<WorkerHub> {

    @Override
    public BaseViewHolder<WorkerHub> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_worker_hub, parent, false));
    }

    public static class ViewHolder extends BaseViewHolder<WorkerHub> {
        private final TextView txtTitle;
        private final RecyclerView recyclerWorkerHub;
        private final WorkerAdapter mAdapter = new WorkerAdapter();

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtWorkerHubTitle);
            recyclerWorkerHub = itemView.findViewById(R.id.recyclerWorkerHub);
        }

        @Override
        public void bind(WorkerHub model) {
            txtTitle.setText(model.getTitle());

            Context context = itemView.getContext();
            if (context != null) {
                ListConfig listConfig = new ListConfig.Builder(mAdapter)
                        .setHasFixedSize(true)
                        .setHasNestedScroll(false)
                        .build(context);
                listConfig.applyConfig(context, recyclerWorkerHub);
            }

            mAdapter.setList(model.getWorkerTitles());
        }
    }
}
