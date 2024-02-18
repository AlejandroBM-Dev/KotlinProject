package di

import com.kotlinProject.db.AppDatabase
import data.ExpenseManager
import data.ExpenseRepoImplement
import domain.ExpenseRepository
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import presenters.ExpensesViewModel

fun appModule(appDatabase: AppDatabase) =
    module {
        single { ExpenseManager }.withOptions { createdAtStart() }
        single<ExpenseRepository> { ExpenseRepoImplement(appDatabase) }
        factory { ExpensesViewModel(get()) }
    }
