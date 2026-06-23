package com.program.diefit

import android.content.Context
import com.program.diefit.data.AppDatabase

object AppProvider {
    lateinit var db: AppDatabase
        private set

    fun init(context: Context) {
        db = AppDatabase.getDatabase(context)
    }
}