package com.example.expensify.data

import androidx.lifecycle.LiveData

class TransactionRepository(private val transactionDao: TransactionDao) {
    val getTransactions: LiveData<List<Transaction>> = transactionDao.getTransactions()

//    val getTotalDebit = transactionDao.getTotalDebit()
//
//    val getTotalCredit = transactionDao.getTotalCredit()

    suspend fun addTransaction(transaction: Transaction) {
        transactionDao.addTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun deleteAllTransactions() {
        transactionDao.deleteAllTransactions()
    }

}