package com.example.expensify.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensify.R
import com.example.expensify.data.TransactionViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_monthly_transacation.*

class MonthlyTransactionFragment : Fragment() {

    private lateinit var mTransactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mTransactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        return inflater.inflate(R.layout.fragment_monthly_transacation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val transactions = mTransactionViewModel.getTransactions
        val adapter = MonthlyTransactionAdapter()
        monthlyTransactionRecyclerView.adapter = adapter
        monthlyTransactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        mTransactionViewModel.getTransactions.observe(viewLifecycleOwner, {
            adapter.setData(it)
            if (adapter.itemCount == 0) {
                Snackbar.make(
                    monthlyTransactionRecyclerView,
                    "You Have No Transactions Added!",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Add") {
                        findNavController().navigate(MonthlyTransactionFragmentDirections.actionMonthlyTransactionFragmentToAddTransactionFragment())
                    }
                    .show()
            }
        })

    }
}