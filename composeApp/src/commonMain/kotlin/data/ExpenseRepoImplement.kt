package data

import com.kotlinProject.db.AppDatabase
import domain.ExpenseRepository
import model.Expense
import model.ExpenseCategory

class ExpenseRepoImplement(
    private val appDatabase: AppDatabase,
) : ExpenseRepository {
    private val queries = appDatabase.expensesDbQueries

    override fun getAllExpenses(): List<Expense> {
        return queries.selectAll().executeAsList().map {
            Expense(
                id = it.id,
                amount = it.amount,
                category = ExpenseCategory.valueOf(it.categoryName),
                description = it.description,
            )
        }
        // return expenseManager.fakeExpenseList
    }

    override fun addExpense(expense: Expense) {
        queries.transaction {
            queries.insert(
                amount = expense.amount,
                categoryName = expense.category.name,
                description = expense.description,
            )
        }
        // expenseManager.addNewExpense(expense)
    }

    override fun editExpense(expense: Expense) {
        queries.transaction {
            queries.update(
                id = expense.id,
                amount = expense.amount,
                categoryName = expense.category.name,
                description = expense.description,
            )
        }
        // expenseManager.editExpense(expense)
    }

    override fun getCategories(): List<ExpenseCategory> {
        return queries.categories().executeAsList().map {
            ExpenseCategory.valueOf(it)
        }
        // return expenseManager.getCategory()
    }

    override fun deleteExpense(expense: Expense): List<Expense> {
        queries.transaction {
            queries.delete(
                id = expense.id,
            )
        }

        return queries.selectAll().executeAsList().map {
            Expense(
                id = it.id,
                amount = it.amount,
                category = ExpenseCategory.valueOf(it.categoryName),
                description = it.description,
            )
        }
        // return expenseManager.deleteExpense(expense)
    }
}
