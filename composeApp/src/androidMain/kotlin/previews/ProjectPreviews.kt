package previews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import data.ExpenseManager
import presenters.ExpensesUiState
import ui.AllExpensesHeader
import ui.ExpenseScreen
import ui.ExpensesItem
import ui.ExpensesTotalHeader

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
fun ExpenseTotalHeaderPreview() {
    BoxPadding {
        ExpensesTotalHeader(total = 20000.00)
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun ExpenseItemPreview() {
    BoxPadding {
        ExpensesItem(
            ExpenseManager.fakeExpenseList[0],
        ) {
            // NOTHING...
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun AllExpensesHeaderPreview() {
    BoxPadding {
        AllExpensesHeader()
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun BoxPadding(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.padding(20.dp),
    ) {
        content()
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview( showBackground = true)
@Composable
fun ExpenseScreenView() {
    ExpenseScreen(uiState = ExpensesUiState(), onExpenseClick = {})
}
