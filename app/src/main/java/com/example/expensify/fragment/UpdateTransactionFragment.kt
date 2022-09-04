package com.example.expensify.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensify.R
import com.example.expensify.data.Transaction
import com.example.expensify.data.TransactionViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import kotlinx.android.synthetic.main.fragment_update_transaction.*
import java.text.SimpleDateFormat
import java.util.*

class UpdateTransactionFragment : Fragment() {

    private lateinit var mTransactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mTransactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        return inflater.inflate(R.layout.fragment_update_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transaction = UpdateTransactionFragmentArgs.fromBundle(requireArguments()).transaction
        updateTitleTextField.setText(transaction.title)
        updateDetailTextField.setText(transaction.detail)
        updateAmountTextField.setText(transaction.amount.toString())

        updateDateTextField.transformIntoDatePicker(
            requireContext(),
            "dd/MM/yyyy",
            Date()
        )
        updateDateTextField.setText(transaction.date)

        updateTitleTextField.doAfterTextChanged {
            updateTitleTextFieldLayout.error = null
            updateTitleTextFieldLayout.isErrorEnabled = false
        }

        updateDetailTextField.doAfterTextChanged {
            updateDetailTextFieldLayout.error = null
            updateDetailTextFieldLayout.isErrorEnabled = false
        }

        updateAmountTextField.doAfterTextChanged {
            updateAmountTextFieldLayout.error = null
            updateAmountTextFieldLayout.isErrorEnabled = false
        }

        val items = listOf("Debit", "Credit")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        updateTransactionTypeMenu.setAdapter(adapter)
        if (transaction.type == 0) {
            updateTransactionTypeMenu.setText("Debit", false)
        } else {
            updateTransactionTypeMenu.setText("Credit", false)
        }

        val tags = listOf(
            "Housing",
            "Transportation",
            "Food",
            "Utilities",
            "Insurance",
            "Healthcare",
            "Saving & Debts",
            "Personal Spending",
            "Entertainment",
            "Miscellaneous",
            "Others"
        )

        val tagsAdapter = ArrayAdapter(requireContext(), R.layout.list_item, tags)
        updateTransactionTagMenu.setAdapter(tagsAdapter)
        updateTransactionTagMenu.setText(transaction.tag, false)

        updateTransactionToDatabaseButton.setOnClickListener {
            updateTransactionToDatabase(transaction)
        }

    }

    private fun updateTransactionToDatabase(transaction: Transaction) {
        val title = updateTitleTextField.text.toString()
        val detail = updateDetailTextField.text.toString()
        val amount = updateAmountTextField.text.toString()
        val transactionType = updateTransactionTypeInput.editText?.text.toString()
        val date = updateDateTextField.text.toString()
        val tag = updateTransactionTagInput.editText?.text.toString()
        val type: Int = if (transactionType == "Debit") {
            0
        } else {
            1
        }
        when {
            title == "" -> {
                titleTextFieldLayout.error = "Enter Title"
            }
            detail == "" -> {
                detailTextFieldLayout.error = "Enter Details"
            }
            amount == "" -> {
                amountTextFieldLayout.error = "Enter Amount"
            }
            else -> {
                val updatedTransaction =
                    Transaction(transaction.id, title, detail, amount.toFloat(), type, tag, date)
                mTransactionViewModel.updateTransaction(updatedTransaction)
                findNavController().navigate(UpdateTransactionFragmentDirections.actionUpdateTransactionFragmentToHomeFragment())
                activity?.currentFocus
                    ?.let { it1 ->
                        Snackbar.make(it1, "Transaction Updated!", Snackbar.LENGTH_SHORT).show()
                    }
            }}
    }

    private fun TextInputEditText.transformIntoDatePicker(
        context: Context,
        format: String,
        maxDate: Date? = null
    ) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context,
                datePickerOnDataSetListener,
                myCalendar
                    .get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }
}