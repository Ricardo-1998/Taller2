package com.example.monedas.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.monedas.Pojos.Coin
import com.example.monedas.R

class CoinAdapter (var coins: List<Coin>, val clickListener: (Coin) -> Unit): RecyclerView.Adapter<CoinAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): coins.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(coins[position],clickListener)

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: Coin, clickListener: (Coin) -> Unit)= with(itemView){
            Glide.with(itemView.context)
                .load()
                .placeholder(R.drawable.ic_launcher_background)
                .into()

        }
    }
}