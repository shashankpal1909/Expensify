package com.example.expensify.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {
    @Query("SELECT * FROM expensify ORDER BY createdAt DESC")
    fun getTransactions(): LiveData<List<Transaction>>

//    @Query("SELECT SUM(amount) FROM expensify WHERE type = 0")
//    fun getTotalDebit(): Float
//
//    @Query("SELECT SUM(amount) FROM expensify WHERE type = 1")
//    fun getTotalCredit(): Float

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("DELETE FROM expensify")
    suspend fun deleteAllTransactions()
}