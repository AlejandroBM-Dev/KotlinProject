import androidx.compose.ui.window.ComposeUIViewController
import com.kotlinProject.db.AppDatabase
import data.local.DatabaseDriverFactory
import di.appModule
import org.koin.core.context.startKoin

@Suppress("ktlint:standard:function-naming")
fun MainViewController() = ComposeUIViewController { App() }

fun initKoin() {
    startKoin {
        modules(appModule(AppDatabase.invoke(DatabaseDriverFactory().createDriver())))
    }.koin
}
