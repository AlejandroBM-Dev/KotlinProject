package presenters

import domain.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.Expense
import model.ExpenseCategory
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
    val total: Double = 0.0,
)

// POR QUE EXTENDER DEL VIEWMODEL___
// -> Al extender de viewmodel, entendemos que debe sobrevivir a los cambios de configuracion
// -> Los datos permanecen dentro del sistema
// -> La configuración se extiende desde el viewModel y con state podemos extender a la vista
class ExpensesViewModel(private val repo: ExpenseRepository) : ViewModel() {
    @Suppress("ktlint:standard:property-naming")
    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState = _uiState.asStateFlow()

    private val allExpenses = repo.getAllExpenses()

    init {
        getAllExpenses()
    }

    private fun updateState() {
        _uiState.update { state ->
            state.copy(expenses = allExpenses, total = allExpenses.sumOf { it.amount })
        }
    }

    // corutines -> Funciones que se suspenden para esperar un valor.
    private fun getAllExpenses() {
        viewModelScope.launch {
            updateState()
        }
    }

    fun addExpenses(expense: Expense) {
        viewModelScope.launch {
            repo.addExpense(expense).also {
                updateState()
            }
        }
    }

    fun editExpense(expense: Expense) {
        viewModelScope.launch {
            repo.editExpense(expense).also {
                updateState()
            }
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repo.deleteExpense(expense).also {
                updateState()
            }
        }
    }

    fun getExpenseWithId(id: Long): Expense {
        return allExpenses.first { it.id == id }
    }

    fun getCategories(): List<ExpenseCategory> {
        return repo.getCategories()
    }
}
