import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.TitleTopBarTypes
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import navigation.Navigation
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.KoinContext

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    PreComposeApp {
        val colors = getColorsTheme()
        KoinContext {
            AppTheme {
                val navigator = rememberNavigator()
                val titleTopBar = getTitleTopAppBar(navigator)
                val isEditOrAddExpenses = titleTopBar != TitleTopBarTypes.DASHBOARD.value

                Scaffold(
                    modifier = Modifier.fillMaxWidth(),
                    topBar = {
                        TopAppBar(
                            backgroundColor = colors.backgroundColor,
                            navigationIcon = {
                                IconButton(
                                    onClick =
                                        {
                                            if (isEditOrAddExpenses) {
                                                navigator.popBackStack()
                                            }
                                        },
                                ) {
                                    if (isEditOrAddExpenses) {
                                        Icon(
                                            modifier = Modifier.padding(start = 16.dp),
                                            imageVector = Icons.Default.ArrowBack,
                                            tint = colors.textColor,
                                            contentDescription = "menu back",
                                        )
                                    } else {
                                        Icon(
                                            modifier = Modifier.padding(start = 16.dp),
                                            imageVector = Icons.Default.Apps,
                                            tint = colors.textColor,
                                            contentDescription = "Dashbord icon",
                                        )
                                    }
                                }
                            },
                            elevation = 0.dp,
                            title = {
                                Text(
                                    text = titleTopBar,
                                    fontSize = 25.sp,
                                    color = colors.textColor,
                                )
                            },
                        )
                    },
                    floatingActionButton = {
                        if (!isEditOrAddExpenses) {
                            FloatingActionButton(
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    navigator.navigate("/addExpenses")
                                },
                                shape = RoundedCornerShape(50),
                                backgroundColor = colors.addIconColor,
                                contentColor = Color.White,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    tint = Color.White,
                                    contentDescription = "Floating Add Icon",
                                )
                            }
                        }
                    },
                ) {
                    Navigation(navigator)
                }
            }
        }
    }
}

@Composable
fun getTitleTopAppBar(navigator: Navigator): String {
    val isOnAddExpenses = navigator.currentEntry.collectAsState(null).value?.route?.route.equals("/addExpenses/{id}?")
    val isOnEditExpense = navigator.currentEntry.collectAsState(null).value?.path<Long>("id")

    var titleTopBar = TitleTopBarTypes.DASHBOARD

    if (isOnAddExpenses) {
        titleTopBar = TitleTopBarTypes.ADD
    }

    isOnEditExpense?.let {
        titleTopBar = TitleTopBarTypes.EDIT
    }

    return titleTopBar.value
}
