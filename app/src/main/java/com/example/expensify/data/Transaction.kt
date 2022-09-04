package com.example.expensify.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.DateFormat

@Entity(tableName = "expensify")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val detail: String,
    val amount: Float,
    val type: Int,
    val tag: String,
    val date: String,
    var createdAt: Long = System.currentTimeMillis()
) : Serializable {
    val createdAtDateFormat: String
        get() = DateFormat.getDateTimeInstance()
            .format(createdAt) // Date Format: Jan 11, 2021, 11:30 AM
}