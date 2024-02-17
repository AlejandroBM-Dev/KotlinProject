package data

import model.Expense
import model.ExpenseCategory

object ExpenseManager {
    private var currentId = 1L

    val fakeExpenseList =
        mutableListOf(
            Expense(
                id = currentId++,
                amount = 70.00,
                category = ExpenseCategory.GROCERIES,
                description = "compra semanal",
            ),
            Expense(
                id = currentId++,
                amount = 7.00,
                category = ExpenseCategory.SNACKS,
                description = "HommiesBurguer",
            ),
            Expense(
                id = currentId++,
                amount = 21000.00,
                category = ExpenseCategory.CAR,
                description = "Audi A1",
            ),
            Expense(
                id = currentId++,
                amount = 120.00,
                category = ExpenseCategory.PARTY,
                description = "Peda con los amigos",
            ),
            Expense(
                id = currentId++,
                amount = 180.00,
                category = ExpenseCategory.HOUSE,
                description = "Limpieza",
            ),
            Expense(
                id = currentId++,
                amount = 30.00,
                category = ExpenseCategory.OTHER,
                description = "Servicios",
            ),
        )

    fun addNewExpense(expense: Expense?) {
        expense?.let {
            fakeExpenseList
                .add(
                    it.copy(id = currentId++),
                )
        }
    }

    fun editExpense(expense: Expense?) {
        expense?.let {
                expense ->

            val index =
                fakeExpenseList.indexOfFirst {
                    it.id == expense.id
                }
            if (index != -1) {
                fakeExpenseList[index] =
                    fakeExpenseList[index].copy(
                        amount = expense.amount,
                        category = expense.category,
                        description = expense.description,
                    )
            }
        }
    }

    fun deleteExpense(expense: Expense): List<Expense> {
        val index =
            fakeExpenseList.indexOfFirst {
                it.id == expense.id
            }
        fakeExpenseList.removeAt(index)

        return fakeExpenseList
    }

    fun getCategory(): List<ExpenseCategory> {
        return listOf(
            ExpenseCategory.GROCERIES,
            ExpenseCategory.PARTY,
            ExpenseCategory.SNACKS,
            ExpenseCategory.COFFEE,
            ExpenseCategory.CAR,
            ExpenseCategory.HOUSE,
            ExpenseCategory.OTHER,
        )
    }
}
