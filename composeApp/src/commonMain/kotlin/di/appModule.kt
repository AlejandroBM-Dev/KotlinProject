package di

import data.ExpenseManager
import data.ExpenseRepoImplement
import domain.ExpenseRepository
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import presenters.ExpensesViewModel

fun appModule() =
    module {
        single { ExpenseManager }.withOptions { createdAtStart() } // Singleton que se usa en la app.
        single<ExpenseRepository> { ExpenseRepoImplement(get()) }
        factory { ExpensesViewModel(get()) }
    }
