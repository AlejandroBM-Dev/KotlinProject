package navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import data.ExpenseManager
import data.ExpenseRepoImplement
import getColorsTheme
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.viewmodel.viewModel
import presenters.ExpensesViewModel
import ui.ExpenseScreen
import ui.ExpensesDetailScreen

@Suppress("ktlint:standard:function-naming")
@Composable
fun Navigation(navigator: Navigator) {
    val colors = getColorsTheme()
    val viewModel =
        viewModel(modelClass = ExpensesViewModel::class) {
            ExpensesViewModel(ExpenseRepoImplement(ExpenseManager))
        }
    // Elimina el estado al desaparecer la vista., se conecta con el lifeCycle

    NavHost(
        modifier = Modifier.background(colors.backgroundColor),
        navigator = navigator,
        initialRoute = "/home",
    ) {
        scene(route = "/home") {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ExpenseScreen(uiState) { expense ->
                navigator.navigate("/addExpenses/${expense.id}")
            }
        }

        scene(route = "/addExpenses/{id}?") { backStackEntry ->
            val idFromPath = backStackEntry.path<Long>("id")
            val expenseEditOrAdd = idFromPath?.let { id -> viewModel.getExpenseWithId(id) }
            ExpensesDetailScreen(expenseToEdit = expenseEditOrAdd, categoryList = viewModel.getCategories()) { expense ->

                if (expenseEditOrAdd == null) {
                    viewModel.addExpenses(expense)
                } else {
                    viewModel.editExpense(expense)
                }
                navigator.popBackStack()
            }
        }
    }
}
