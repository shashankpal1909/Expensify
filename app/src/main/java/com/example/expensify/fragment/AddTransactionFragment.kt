package com.example.expensify.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensify.R
import com.example.expensify.data.Transaction
import com.example.expensify.data.TransactionViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import kotlinx.android.synthetic.main.fragment_getting_started.*
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionFragment : Fragment() {

    private lateinit var mTransactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mTransactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_transaction, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        titleTextField.doAfterTextChanged {
            titleTextFieldLayout.error = null
            titleTextFieldLayout.isErrorEnabled = false
        }

        detailTextField.doAfterTextChanged {
            detailTextFieldLayout.error = null
            detailTextFieldLayout.isErrorEnabled = false
        }

        amountTextField.doAfterTextChanged {
            amountTextFieldLayout.error = null
            amountTextFieldLayout.isErrorEnabled = false
        }

        addTransactionToDatabaseButton.setOnClickListener {
            insertTransactionToDatabase()
        }

        dateTextField.transformIntoDatePicker(
            requireContext(),
            "dd/MM/yyyy",
            Date()
        )

        val myCalendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
        dateTextField.setText(sdf.format(myCalendar.time))

        val items = listOf("Debit", "Credit")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        transactionTypeMenu.setAdapter(adapter)
        transactionTypeMenu.setText("Debit", false)

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
        transactionTagMenu.setAdapter(tagsAdapter)
        transactionTagMenu.setText("Housing", false)

    }

    private fun insertTransactionToDatabase() {
        val title = titleTextField.text.toString()
        val detail = detailTextField.text.toString()
        val amount = amountTextField.text.toString()
        val transactionType = transactionTypeInput.editText?.text.toString()
        val date = dateTextField.text.toString()
        val tag = transactionTagInput.editText?.text.toString()
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
                val transaction = Transaction(0, title, detail, amount.toFloat(), type, tag, date)
                mTransactionViewModel.addTransaction(transaction)
                findNavController().navigate(AddTransactionFragmentDirections.actionAddTransactionFragmentToHomeFragment())
                activity?.currentFocus
                    ?.let { it1 ->
                        Snackbar.make(it1, "Transaction Added!", Snackbar.LENGTH_SHORT).show()
                    }
            }
        }
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