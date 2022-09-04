package com.example.expensify.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.expensify.R
import kotlinx.android.synthetic.main.fragment_getting_started.*


class GettingStartedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_getting_started, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        nameTextField.doOnTextChanged { _, _, _, _ ->
            nameTextFieldLayout.error = null
            nameTextFieldLayout.isErrorEnabled = false
        }
        monthlyBudgetTextField.doOnTextChanged { _, _, _, _ ->
            monthlyBudgetTextFieldLayout.error = null
            monthlyBudgetTextFieldLayout.isErrorEnabled = false
        }
        monthlyIncomeTextField.doOnTextChanged { _, _, _, _ ->
            monthlyIncomeTextFieldLayout.error = null
            monthlyBudgetTextFieldLayout.isErrorEnabled = false
        }
        continueButton.setOnClickListener {
            val name = nameTextField.text.toString()
            val monthlyBudget = monthlyBudgetTextField.text.toString()
            val monthlyIncome = monthlyIncomeTextField.text.toString()
            when {
                name == "" -> {
                    nameTextFieldLayout.error = "Enter Name"
                }
//                monthlyBudget == "" -> {
//                    monthlyBudgetTextFieldLayout.error = "Enter Expense"
//                }
//                monthlyIncome == "" -> {
//                    monthlyIncomeTextFieldLayout.error = "Enter Income"
//                }
                else -> {
                    val sharedPref =
                        activity?.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE)
                    with(sharedPref?.edit()) {
                        this?.putString("NAME", name)?.apply()
                        this?.putString("BUDGET", monthlyBudget)?.apply()
                        this?.putString("INCOME", monthlyIncome)?.apply()
                    }
                    findNavController().navigate(
                        GettingStartedFragmentDirections.actionGettingStartedFragmentToHomeFragment()
                    )
                }
            }

        }
    }
}