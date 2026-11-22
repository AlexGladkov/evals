package com.dev.nicehash.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.base.BaseAdapter;
import com.dev.nicehash.base.BaseViewHolder;
import com.dev.nicehash.domain.models.Miner;

/**
 * Created by Alex Gladkov on 23.06.18.
 * Adapter for miners list
 * @see ChooseActivity
 */
public class MinerAdapter extends BaseAdapter<Miner> {

    public interface MinerClick {
        void onEditClick(Miner miner);
        void onDeleteClick(Miner miner);
    }

    private final MinerClick minerClick;

    public MinerAdapter(MinerClick minerClick) {
        this.minerClick = minerClick;
    }

    @Override
    public BaseViewHolder<Miner> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_miner, parent, false), minerClick);
    }

    public static class ViewHolder extends BaseViewHolder<Miner> {
        private final String TAG = ViewHolder.class.getSimpleName();
        private final TextView txtTitle;
        private final TextView txtHash;
        private final ImageView btnMore;
        private final FrameLayout flMiner;

        private final MinerClick minerClick;

        public ViewHolder(View itemView, MinerClick minerClick) {
            super(itemView);
            this.minerClick = minerClick;
            txtTitle = itemView.findViewById(R.id.txtMinerTitle);
            txtHash = itemView.findViewById(R.id.txtMinerHash);
            btnMore = itemView.findViewById(R.id.btnMinerMore);
            flMiner = itemView.findViewById(R.id.flMiner);
        }

        @Override
        public void bind(Miner model) {
            txtTitle.setText(model.getName());
            txtHash.setText(model.getHash());

            if (App.isDark) {
                if (model.isSelected()) {
                    flMiner.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.dark_background_secondary));
                } else {
                    flMiner.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.dark_background_primary));
                }
            } else {
                if (model.isSelected()) {
                    flMiner.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.light_background_secondary));
                } else {
                    flMiner.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.light_background_primary));
                }
            }

            btnMore.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), btnMore);
                android.view.MenuInflater menuInflater = popupMenu.getMenuInflater();

                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    int itemId = menuItem.getItemId();
                    if (itemId == R.id.more_edit) {
                        minerClick.onEditClick(model);
                    } else if (itemId == R.id.more_delete) {
                        minerClick.onDeleteClick(model);
                    }

                    return false;
                });

                menuInflater.inflate(R.menu.menu_miner_more, popupMenu.getMenu());
                popupMenu.show();
            });
        }
    }
}
