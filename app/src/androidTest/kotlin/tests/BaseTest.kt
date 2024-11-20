package tests

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.kaspersky.components.alluresupport.interceptors.step.AllureMapperStepInterceptor
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.params.FlakySafetyParams
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.After
import org.junit.Before
import ru.tinkoff.favouritepersons.room.PersonDataBase
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

abstract class BaseTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.simple(
        customize = {
            FlakySafetyParams.custom(timeoutMs = 3000L, 500)
        }
    ).apply {
        stepWatcherInterceptors.addAll(listOf(AllureMapperStepInterceptor()))
    }) {

    private lateinit var db: PersonDataBase

    @Before
    fun createDb() {
        db = PersonDataBase.getDBClient(InstrumentationRegistry.getInstrumentation().targetContext)
        db.personsDao().clearTable()
    }

    @After
    fun clearDB() {
        db.personsDao().clearTable()
    }

    fun getFileToString(path: String, context: Context = InstrumentationRegistry.getInstrumentation().context) : String {
        return BufferedReader(InputStreamReader(context.assets.open(path), StandardCharsets.UTF_8)).readText()
    }
}