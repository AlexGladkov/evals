package com.dev.nicehash.app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dev.nicehash.R
import com.dev.nicehash.base.BaseAdapter
import com.dev.nicehash.base.BaseViewHolder
import com.dev.nicehash.domain.models.HashrateOrder
import com.dev.nicehash.helpers.EnumCollections

/**
 * Adapter for marketplace orders list
 */
class MarketAdapter: BaseAdapter<HashrateOrder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HashrateOrder> {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_market_order, parent, false))
    }

    class ViewHolder(itemView: View): BaseViewHolder<HashrateOrder>(itemView = itemView) {
        private val txtTitle = itemView.findViewById<TextView>(R.id.txtMarketOrderTitle)
        private val txtAlgo = itemView.findViewById<TextView>(R.id.txtMarketOrderAlgo)
        private val txtPrice = itemView.findViewById<TextView>(R.id.txtMarketOrderPrice)
        private val txtSpeed = itemView.findViewById<TextView>(R.id.txtMarketOrderSpeed)
        private val txtAlive = itemView.findViewById<TextView>(R.id.txtMarketOrderAlive)

        @SuppressLint("SetTextI18n", "DefaultLocale")
        override fun bind(model: HashrateOrder) {
            val algoTitle = EnumCollections.Algos.values().firstOrNull { it.id == model.algo }?.title

            if (algoTitle != null) {
                txtTitle.text = algoTitle
                txtAlgo.text = itemView.context.getString(R.string.market_order_algo_number, model.algo)
            } else {
                txtTitle.text = itemView.context.getString(R.string.market_order_algo_number, model.algo)
                txtAlgo.text = ""
            }

            txtPrice.text = String.format("%.5f", model.price)
            txtSpeed.text = String.format("%.2f", model.speed)

            if (model.alive) {
                txtAlive.text = itemView.context.getString(R.string.market_order_alive)
            } else {
                txtAlive.text = itemView.context.getString(R.string.market_order_dead)
            }
        }
    }
}
