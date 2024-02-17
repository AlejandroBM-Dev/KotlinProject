import model.Expense
import model.ExpenseCategory
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ExampleTest {
    // Given - When - Then
    @Test
    fun myFirstTest() {
        // GIVEN - dado cierta X variable
        val x = 5
        val i = 10
        // WHEN - cuando realizamos cierta tarea
        val result = x + i
        // THEN - Quiero tener X resultado
        assertEquals(15, result)
    }

    @Test
    fun mySecondTest() {
        for (i in 0 until 300) {
            // Generar valores aleatorios para x e i
            val x = (1..100).random()
            val i = (1..100).random()

            // Calcular el resultado
            val result = x + i

            // Verificar que el resultado sea correcto
            assertEquals(x + i, result)
        }
    }

    @Test
    fun expense_model_list_test() {
        // Given
        val expenseList = mutableListOf<Expense>()
        val expense = Expense(id = 1, amount = 10.00, category = ExpenseCategory.OTHER, description = "Combustible")

        // When
        expenseList.add(expense)

        // THEN
        assertContains(expenseList, expense)
    }

    @Test
    fun expense_model_param_test() {
        // Given
        val expenseList = mutableListOf<Expense>()
        val expense = Expense(id = 1, amount = 10.00, category = ExpenseCategory.OTHER, description = "Combustible")
        val expense2 = Expense(id = 2, amount = 12.00, category = ExpenseCategory.OTHER, description = "Limpieza")
        // When
        expenseList.add(expense)
        expenseList.add(expense2)

        // THEN
        assertEquals(expenseList[0].category, expenseList[1].category)
    }

    @Test
    fun expense_model_different_categories_test() {
        // Given
        val expenseList = mutableListOf<Expense>()
        val expense1 = Expense(id = 1, amount = 10.00, category = ExpenseCategory.OTHER, description = "Combustible")
        val expense2 = Expense(id = 2, amount = 12.00, category = ExpenseCategory.SNACKS, description = "Almuerzo")
        expenseList.add(expense1)
        expenseList.add(expense2)

        // THEN
        assertEquals(ExpenseCategory.OTHER, expenseList[0].category)
        assertEquals(ExpenseCategory.SNACKS, expenseList[1].category)
    }

    @Test
    fun expense_model_get_by_id_test() {
        // Given
        val expenseList = mutableListOf<Expense>()
        val expense1 = Expense(id = 1, amount = 10.00, category = ExpenseCategory.OTHER, description = "Combustible")
        val expense2 = Expense(id = 2, amount = 12.00, category = ExpenseCategory.OTHER, description = "Limpieza")
        expenseList.add(expense1)
        expenseList.add(expense2)

        // When
        val foundExpense = expenseList.find { it.id == expense2.id }
        // val foundExpense = expenseList.filter { it.id == expense2.id }

        // THEN
        assertNotNull(foundExpense)
        assertEquals(expense2, foundExpense)
    }
}
