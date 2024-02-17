import data.ExpenseManager
import data.ExpenseRepoImplement
import model.Expense
import model.ExpenseCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ExpenseRepoTest {
    private val expenseManager = ExpenseManager
    private val repo = ExpenseRepoImplement(expenseManager)

    @Test
    fun expense_list_is_no_empty() {
        // GIVEN
        val expenseList = mutableListOf<Expense>()

        // WHEN
        expenseList.addAll(repo.getAllExpenses())

        // THEN
        assertTrue(expenseList.isNotEmpty())
    }

    @Test
    fun add_new_expense() {
        // GIVEN
        val startExpenseList = repo.getAllExpenses()

        // WHEN
        val newExpenseId = 7L
        repo.addExpense(
            Expense(
                amount = 10.11,
                category = ExpenseCategory.SNACKS,
                description = "Combustible",
            ),
        )

        assertNotNull(startExpenseList.find { it.id == newExpenseId })

        val updateExpense =
            Expense(
                id = newExpenseId,
                amount = 9.00,
                category = ExpenseCategory.GROCERIES,
                description = "Ropaaa",
            )
        repo.editExpense(updateExpense)

        // THEN
        val finalExpenseList = repo.getAllExpenses()

        assertEquals(updateExpense, finalExpenseList.find { it.id == 7L })
    }

    @Test
    fun check_all_categories() {
        // GIVEN
        val allCategories =
            listOf(
                ExpenseCategory.GROCERIES,
                ExpenseCategory.PARTY,
                ExpenseCategory.SNACKS,
                ExpenseCategory.COFFEE,
                ExpenseCategory.CAR,
                ExpenseCategory.HOUSE,
                ExpenseCategory.OTHER,
            )

        // WHEN
        val repoCategories = repo.getCategories()

        // THEN
        assertEquals(allCategories, repoCategories)
    }
}
