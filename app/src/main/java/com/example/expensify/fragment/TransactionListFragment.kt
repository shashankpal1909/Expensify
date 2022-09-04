package com.example.expensify.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensify.R
import com.example.expensify.data.Transaction
import com.example.expensify.data.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_monthly_transacation.*
import kotlinx.android.synthetic.main.fragment_transaction_list.*
import kotlinx.android.synthetic.main.fragment_transaction_list.view.*
import kotlinx.android.synthetic.main.monthly_card.view.*
import java.lang.Math.abs

class TransactionListFragment : Fragment() {

    private lateinit var mTransactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mTransactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        return inflater.inflate(R.layout.fragment_transaction_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = TransactionListFragmentArgs.fromBundle(requireArguments()).item
        val adapter = TransactionListAdapter(item)
        var credit = 0.0F
        var debit = 0.0F
        mTransactionViewModel.getTransactions.observe(viewLifecycleOwner, { list ->
            adapter.setData(list)
            val tempList = mutableListOf<Transaction>()
            list.forEach {
                if (it.date.split("/")[1].toInt() == item) {
                    tempList.add(it)
                }
            }
            tempList.forEach {
                if (it.type == 0) {
                    debit += it.amount
                } else {
                    credit += it.amount
                }
            }
            debitAmountLabel.text = "$" + String.format("%.2f", debit)
            creditAmountLabel.text = "$" + String.format("%.2f", credit)
            if (credit - debit > 0) {
                viewAllTransactionsStatusIcon.setImageResource(R.drawable.ic_baseline_check_circle_24)
                viewAllTransactionsStatusTextView.text = "IN BUDGET"
                viewAllTransactionsStatusTextView.setTextColor(Color.parseColor("#6FCF97"))
                currentBalanceAmountLabel.text = "$" + String.format("%.2f", credit - debit)
            } else {
                viewAllTransactionsStatusIcon.setImageResource(R.drawable.ic_baseline_error_24)
                viewAllTransactionsStatusTextView.text = "OUT OF BUDGET"
                viewAllTransactionsStatusTextView.setTextColor(Color.parseColor("#EB5757"))
                currentBalanceAmountLabel.text = "-$" + String.format("%.2f", abs(credit - debit))
            }
        })
        viewAllTransactionsRecyclerView.adapter = adapter
        viewAllTransactionsRecyclerView.layoutManager = LinearLayoutManager(
            requireContext()
        )
        (when (item - 1) {
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
        }).also { viewAllTransactionsTitleTextView.text = it }
    }
}