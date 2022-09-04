package com.example.expensify.fragment

import android.graphics.Color
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.navigation.NavType
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensify.R
import com.example.expensify.data.Transaction
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_transaction.view.*
import kotlinx.android.synthetic.main.monthly_card.view.*

class MonthlyTransactionAdapter : RecyclerView.Adapter<MonthlyTransactionAdapter.MyViewHolder>() {
    private var transactionList = mutableListOf(mutableListOf<Transaction>())

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.monthly_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        transactionList.forEach { item ->
            item.sortBy {
                it.date
            }
        }
        val currentItem = transactionList[position]
        var total = 0.0F
        currentItem.forEach {
            if (it.type == 0) {
                total -= it.amount
            } else {
                total += it.amount
            }
        }
        if (total >= 0) {
            holder.itemView.monthlyStatusImageView.setImageResource(R.drawable.ic_baseline_check_circle_24)
        } else {
            holder.itemView.monthlyStatusImageView.setImageResource(R.drawable.ic_baseline_error_24)
        }
        if (currentItem.size > 0) {
            holder.itemView.monthLabel.text = when (currentItem[0].date.split("/")[1].toInt() - 1) {
                0 -> "JANUARY"
                1 -> "FEBRUARY"
                2 -> "MARCH"
                3 -> "APRIL"
                4 -> "MAY"
                5 -> "JUNE"
                6 -> "JULY"
                7 -> "AUGUST"
                8 -> "SEPTEMBER"
                9 -> "OCTOBER"
                10 -> "NOVEMBER"
                11 -> "DECEMBER"
                else -> ""
            }
        }

        val list = currentItem
        list.sortBy { it.date }

        val adapter = MonthlyCardAdapter(list.reversed())
        holder.itemView.monthlyItemsRecyclerView.adapter = adapter
        holder.itemView.monthlyItemsRecyclerView.layoutManager = LinearLayoutManager(
            holder.itemView.context
        )
        holder.itemView.viewMonthlyDetailsButton.setOnClickListener {
            holder.itemView.findNavController().navigate(
                MonthlyTransactionFragmentDirections.actionMonthlyTransactionFragmentToTransactionListFragment(
                    currentItem[0].date.split("/")[1].toInt()
                )
            )
        }

    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    fun setData(transaction: List<Transaction>) {
        var sortedList: MutableList<MutableList<Transaction>> =
            mutableListOf(
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
            )
        transaction.forEach {
            val month = it.date.split("/")[1].toInt()
            sortedList[month - 1].add(it)
        }
        sortedList = sortedList.filter {
            it.size > 0
        } as MutableList<MutableList<Transaction>>
        this.transactionList = sortedList
        notifyDataSetChanged()
    }
}