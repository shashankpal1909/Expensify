package com.example.expensify.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensify.R
import com.example.expensify.data.TransactionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_view_transaction.*

class ViewTransactionFragment : Fragment() {

    private lateinit var mTransactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        mTransactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        return inflater.inflate(R.layout.fragment_view_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transaction = ViewTransactionFragmentArgs.fromBundle(requireArguments()).transaction
        viewTitleTextView.text = transaction.title
        viewDetailTextView.text = transaction.detail
        viewAmountTextView.text = "$" + String.format("%.2f", transaction.amount)

        if (transaction.type == 0) {
            viewTypeTextView.text = "Debit"
        } else {
            viewTypeTextView.text = "Credit"
        }
        viewTagTextView.text = transaction.tag
        viewDateTextView.text = transaction.date
        viewCreatedAtTextView8.text = transaction.createdAtDateFormat

        editTransactionToDatabaseButton.setOnClickListener {
            findNavController().navigate(
                ViewTransactionFragmentDirections.actionViewTransactionFragmentToUpdateTransactionFragment(
                    transaction
                )
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
        menu.removeItem(R.id.menu_theme)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteTransactionFromDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteTransactionFromDatabase() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm Delete")
            .setMessage("Delete this transaction?")
            .setPositiveButton("Yes") { _, _ ->
                mTransactionViewModel.deleteTransaction(
                    ViewTransactionFragmentArgs.fromBundle(
                        requireArguments()
                    ).transaction
                )
                findNavController().navigate(ViewTransactionFragmentDirections.actionViewTransactionFragmentToHomeFragment())
                activity?.currentFocus
                    ?.let { it1 ->
                        Snackbar.make(it1, "Transaction Deleted!", Snackbar.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }
}