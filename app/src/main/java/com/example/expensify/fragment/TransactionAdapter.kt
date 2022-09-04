package com.example.expensify.fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.expensify.R
import com.example.expensify.data.Transaction
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.transaction_item.view.*
import org.eazegraph.lib.models.PieModel

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {

    private var transactionList = emptyList<Transaction>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = transactionList[position]

        holder.itemView.displayTitleTextView.text = currentItem.title
        holder.itemView.displayDetailTextView.text = currentItem.detail + " • " + currentItem.date
        holder.itemView.displayAmountTextView.text = "$" + String.format("%.2f", currentItem.amount)

        holder.itemView.transactionItemCardView.setOnClickListener {
            holder.itemView.findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToViewTransactionFragment(currentItem)
            )
        }

        when (currentItem.tag) {
            "Housing" -> {
                holder.itemView.displayTypeTextView.setImageResource(R.drawable.ic_baseline_house_24)
            }
            "Transportation" -> {
                holder.itemView.displayTypeTextView.setImageResource(R.drawable.ic_baseline_directions_transit_24)
            }
            "Food" -> {
                holder.itemView.displayTypeTextView.setImageResource(R.drawable.ic_baseline_food_bank_24)
            }
            "Utilities" -> {
                holder.itemView.displayTypeTextView.setImageResource(R.drawable.ic_baseline_utilities_24)
            }
            "Insurance" -> {
                holder.itemView.displayTypeTextView.setImageResource(R.drawable.ic_baseline_insurance_24)
            }
            "Healthcare" -> {
                holder.itemView.displayTypeTextView.setImageResource(R.drawable.ic_baseline_medical_services_24)
            }
            "Saving & Debts" -> {
                holder.itemView.displayTypeTextView.setImageResource(R.drawable.ic_baseline_attach_money_24)
            }
            "Personal Spending" -> {
                holder.itemView.displayTypeTextView.setImageResource(R.drawable.ic_baseline_person_24)
            }
            "Entertainment" -> {
                holder.itemView.displayTypeTextView.setImageResource(R.drawable.ic_baseline_live_tv_24)
            }
            "Miscellaneous" -> {
                holder.itemView.displayTypeTextView.setImageResource(R.drawable.ic_baseline_info_24)
            }
            else -> {
                holder.itemView.displayTypeTextView.setImageResource(R.drawable.ic_baseline_info_24)
            }
        }

        if (currentItem.type == 0) {
            holder.itemView.displayAmountTextView.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.expense
                )
            )
        } else {
            holder.itemView.displayAmountTextView.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.income
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return if (transactionList.size > 10) 10 else transactionList.size
    }

    fun setData(transaction: List<Transaction>) {
        this.transactionList = transaction
        notifyDataSetChanged()
    }
}