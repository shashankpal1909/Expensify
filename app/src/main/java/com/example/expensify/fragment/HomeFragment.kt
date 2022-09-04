package com.example.expensify.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensify.R
import com.example.expensify.data.Transaction
import com.example.expensify.data.TransactionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import org.eazegraph.lib.models.PieModel
import kotlin.math.abs

class HomeFragment : Fragment() {

    private var debit = 0.0F
    private var credit = 0.0F

    private lateinit var mTransactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedPreferences = activity?.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE)

        val adapter = TransactionAdapter()
        transactionRecyclerView.adapter = adapter
        transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        mTransactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        mTransactionViewModel.getTransactions.observe(viewLifecycleOwner, { transaction ->
            adapter.setData(transaction)
            updatePieChart(transaction)
            if (adapter.itemCount == 0) {
                Snackbar.make(
                    recentTransactionsCoordinatorLayout,
                    "You Have No Transactions Added!",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Add") {
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddTransactionFragment())
                    }
                    .show()
            }
        })

        addTransactionButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddTransactionFragment())
        }

        viewAllTransactionsDetailsButton.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToMonthlyTransactionFragment()
            )
        }

        val name = sharedPreferences?.getString("NAME", null)
        if (name.equals(null)) {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToGettingStartedFragment()
            )
        }
    }

    private fun updatePieChart(transaction: List<Transaction>) {
        credit = 0.0F
        debit = 0.0F
        transaction.forEach {
            if (it.type == 0) {
                debit += it.amount
            } else {
                credit += it.amount
            }
        }
        expenseLabel.text = "$" + String.format("%.2f", debit)
        pieChart.clearChart()
        pieChart.addPieSlice(
            PieModel("Expenses", debit, Color.parseColor("#EB5757"))
        )
        if ((credit - debit) >= 0) {
            balanceLabel.text = "$" + String.format("%.2f", (credit - debit))
            pieChart.addPieSlice(
                PieModel("Balance Left", (credit - debit), Color.parseColor("#6FCF97"))
            )
        } else {
            balanceLabel.text = "-$" + String.format("%.2f", abs(credit - debit))
        }
        pieChart.startAnimation()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
        val sharedPref = activity?.getSharedPreferences("THEME", Context.MODE_PRIVATE)
        if (sharedPref != null) {
            if (sharedPref.getString("MODE", null) == "dark") {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                menu.getItem(0).setIcon(R.drawable.ic_baseline_light_mode_24)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                menu.getItem(0).setIcon(R.drawable.ic_baseline_dark_mode_24)
            }
        } else {
            with(sharedPref?.edit()) {
                this?.putString("MODE", "light")?.apply()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteAllTransactionFromDatabase()
        } else if (item.itemId == R.id.menu_theme) {
            item.isChecked = !item.isChecked
            toggleTheme(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggleTheme(item: MenuItem) {
        val sharedPref = activity?.getSharedPreferences("THEME", Context.MODE_PRIVATE)
        if (sharedPref != null) {
            if (sharedPref.getString("MODE", null) == "light") {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                item.setIcon(R.drawable.ic_baseline_light_mode_24)
                sharedPref.edit().putString("MODE", "dark").apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                item.setIcon(R.drawable.ic_baseline_dark_mode_24)
                sharedPref.edit().putString("MODE", "light").apply()
            }
        }
    }

    private fun setUIMode(item: MenuItem, checked: Boolean) {
        if (checked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            item.setIcon(R.drawable.ic_baseline_dark_mode_24)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            item.setIcon(R.drawable.ic_baseline_light_mode_24)
        }
    }

    private fun deleteAllTransactionFromDatabase() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm Delete")
            .setMessage("Delete All Transaction(s)?")
            .setPositiveButton("Yes") { _, _ ->
                mTransactionViewModel.deleteAllTransaction()
                activity?.currentFocus
                    ?.let { it1 ->
                        Snackbar.make(it1, "Transaction Deleted!", Snackbar.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }
}