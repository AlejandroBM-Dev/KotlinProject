package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.TitleTopBarTypes
import getColorsTheme
import kotlinx.coroutines.launch
import model.Expense
import model.ExpenseCategory

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ExpensesDetailScreen(
    expenseToEdit: Expense? = null,
    categoryList: List<ExpenseCategory>,
    addExpenseAndNavigateBack: (Expense) -> Unit,
) {
    val colors = getColorsTheme()
    var price by remember { mutableStateOf(expenseToEdit?.amount ?: 0.00) }
    var description by remember { mutableStateOf(expenseToEdit?.description ?: "") }
    var expenseCategory by remember { mutableStateOf(expenseToEdit?.category?.name ?: "") }
    var categorySelected by remember { mutableStateOf(expenseToEdit?.category?.name ?: "Select a category") }
    val sheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
        )
    val keyBoardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(sheetState.targetValue) {
        if (sheetState.targetValue == ModalBottomSheetValue.Expanded) {
            keyBoardController?.hide()
        }
    }
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            CategoryBottomSheerContent(categoryList) {
                expenseCategory = it.name
                categorySelected = it.name
                scope.launch {
                    sheetState.hide()
                }
            }
        },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 16.dp),
        ) {
            ExpenseAmount(
                priceContent = price,
                onPrinceChange = {
                    price = it
                },
                keyBoardController = keyBoardController,
            )

            Spacer(modifier = Modifier.height(30.dp))

            ExpenseTypeSelector(
                categorySelected,
            ) {
                scope.launch {
                    sheetState.show()
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            ExpenseDescription(
                descriptionContent = description,
                onDescriptionChange = {
                    description = it
                },
                keyBoardController = keyBoardController,
            )
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(45)),
                onClick = {
                    val expense =
                        Expense(
                            amount = price,
                            category = ExpenseCategory.valueOf(expenseCategory),
                            description = description,
                        )
                    val expenseFromEdit =
                        expenseToEdit?.id?.let {
                            expense.copy(id = it)
                        }

                    addExpenseAndNavigateBack(expenseFromEdit ?: expense)
                },
                colors =
                    ButtonDefaults.buttonColors(
                        backgroundColor = colors.purple,
                        contentColor = Color.White,
                    ),
                enabled = price != 0.0 && description.isNotBlank() && expenseCategory.isNotBlank(),
            ) {
                expenseToEdit?.let {
                    Text(text = TitleTopBarTypes.EDIT.value)
                    return@Button
                }
                Text(text = TitleTopBarTypes.ADD.value)
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun ExpenseTypeSelector(
    categorySelected: String,
    openButtonSheet: () -> Unit,
) {
    val colors = getColorsTheme()
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "Expenses made for",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
            )
            Text(
                text = categorySelected,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.textColor,
            )
        }
        IconButton(
            modifier = Modifier.clip(RoundedCornerShape(35)).background(colors.colorArrowRound),
            onClick = {
                openButtonSheet.invoke()
            },
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Button expense type",
                tint = colors.textColor,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ExpenseAmount(
    priceContent: Double,
    onPrinceChange: (Double) -> Unit,
    keyBoardController: SoftwareKeyboardController?,
) {
    val colors = getColorsTheme()
    var text by remember { mutableStateOf("$priceContent") }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Amount",
            fontSize = 20.sp,
            color = Color.Gray,
            // fontWeight = FontWeight.SEMI_BOLD,
            fontWeight = FontWeight.SemiBold,
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.textColor,
            )
            TextField(
                modifier = Modifier.weight(1f),
                value = text,
                onValueChange = { newText ->
                    val numericText = newText.filter { it.isDigit() || it == '.' }
                    text =
                        if (numericText.isNotEmpty() && numericText.count { it == '.' } <= 1) {
                            try {
                                val newValue = numericText.toDouble()
                                onPrinceChange(newValue)
                                numericText
                            } catch (e: NumberFormatException) {
                                e.printStackTrace()
                                ""
                            }
                        } else {
                            onPrinceChange(0.00)
                            ""
                        }
                },
                keyboardOptions =
                    KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = {
                            keyBoardController?.hide()
                        },
                    ),
                singleLine = true,
                colors =
                    TextFieldDefaults.textFieldColors(
                        textColor = colors.textColor,
                        backgroundColor = colors.backgroundColor,
                        focusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color.Transparent,
                        unfocusedLabelColor = Color.Transparent,
                    ),
                textStyle = TextStyle(fontSize = 35.sp, fontWeight = FontWeight.ExtraBold),
            )
            Text(
                "USD",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Gray,
            )
        }
        Divider(color = Color.Black, thickness = 2.dp)
    }
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExpenseDescription(
    descriptionContent: String,
    onDescriptionChange: (String) -> Unit,
    keyBoardController: SoftwareKeyboardController?,
) {
    var text by remember { mutableStateOf(descriptionContent) }
    val colors = getColorsTheme()
    Column {
        Text(
            text = "Description",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray,
        )
        TextField(
            value = text,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { newText ->
                if (newText.length <= 200) {
                    text = newText
                    onDescriptionChange(text)
                }
            },
            singleLine = true,
            colors =
                TextFieldDefaults.textFieldColors(
                    textColor = colors.textColor,
                    backgroundColor = colors.backgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent,
                ),
            textStyle =
                TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
            keyboardActions =
                KeyboardActions(
                    onDone = {
                        keyBoardController?.hide()
                    },
                ),
        )
        Divider(color = Color.Black, thickness = 2.dp)
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun CategoryBottomSheerContent(
    categories: List<ExpenseCategory>,
    onCategorySelected: (ExpenseCategory) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(16.dp),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center,
    ) {
        items(categories) {
            CategoryItem(it, onCategorySelected)
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun CategoryItem(
    category: ExpenseCategory,
    onCategorySelected: (ExpenseCategory) -> Unit,
) {
    Column(
        modifier =
            Modifier.fillMaxWidth().padding(8.dp).clickable {
                onCategorySelected(category)
            },
    ) {
        Image(
            modifier = Modifier.size(40.dp).clip(CircleShape),
            imageVector = category.icon,
            contentDescription = "Category icon.",
            contentScale = ContentScale.Crop,
        )
        Text(text = category.name)
    }
}
