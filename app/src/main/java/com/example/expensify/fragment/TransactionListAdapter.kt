package com.example.expensify.fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.expensify.R
import com.example.expensify.data.Transaction
import kotlinx.android.synthetic.main.fragment_transaction_list.view.*
import kotlinx.android.synthetic.main.monthly_card_item.view.*

class TransactionListAdapter(private val item: Int) :
    RecyclerView.Adapter<TransactionListAdapter.MyViewHolder>() {

    private var transactionList = emptyList<Transaction>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.monthly_card_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = transactionList[position]
        holder.itemView.monthlyItemTitleLabel.text = currentItem.title
        holder.itemView.monthlyItemDetailLabel.text = currentItem.detail + " • " + currentItem.date
        holder.itemView.monthlyItemPriceLabel.text = "$" + currentItem.amount
        holder.itemView.monthlyItemPriceLabel.setTextColor(
            if (currentItem.type == 0) {
                Color.parseColor("#EB5757")
            } else {
                Color.parseColor("#6FCF97")
            }
        )
        holder.itemView.monthlyItemConstraintLayout.setOnClickListener {
            holder.itemView.findNavController().navigate(
                TransactionListFragmentDirections.actionTransactionListFragmentToViewTransactionFragment(
                    currentItem
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    fun setData(transaction: List<Transaction>) {
        val list = mutableListOf<Transaction>()
        transaction.forEach {
            if (it.date.split("/")[1].toInt() == item) {
                list.add(it)
            }
        }
        list.sortBy {
            it.date
        }
        this.transactionList = list.reversed()
        notifyDataSetChanged()
    }
}