package com.example.expensify.fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.expensify.R
import com.example.expensify.data.Transaction
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.monthly_card.view.*
import kotlinx.android.synthetic.main.monthly_card_item.view.*

class MonthlyCardAdapter(private var transactions: List<Transaction>) :
    RecyclerView.Adapter<MonthlyCardAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.monthly_card_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = transactions[position]
        holder.itemView.monthlyItemTitleLabel.text = currentItem.title
        holder.itemView.monthlyItemDetailLabel.text = currentItem.detail + " • " + currentItem.date
        holder.itemView.monthlyItemPriceLabel.text = "$" + String.format("%.2f", currentItem.amount)
        holder.itemView.monthlyItemPriceLabel.setTextColor(
            if (currentItem.type == 0) {
                Color.parseColor("#EB5757")
            } else {
                Color.parseColor("#6FCF97")
            }
        )
        holder.itemView.monthlyItemConstraintLayout.setOnClickListener {
            holder.itemView.findNavController().navigate(
                MonthlyTransactionFragmentDirections.actionMonthlyTransactionFragmentToViewTransactionFragment(
                    currentItem
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return if (transactions.size > 3) 3 else transactions.size
    }
}