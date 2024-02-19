package di

import com.kotlinProject.db.AppDatabase
import data.ExpenseRepoImplement
import domain.ExpenseRepository
import org.koin.dsl.module
import presenters.ExpensesViewModel

fun appModule(appDatabase: AppDatabase) =
    module {
        // single { ExpenseManager }.withOptions { createdAtStart() } // Singleton que se usa en la app.
        single<ExpenseRepository> { ExpenseRepoImplement(appDatabase) }
        factory { ExpensesViewModel(get()) }
    }
