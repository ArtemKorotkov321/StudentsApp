package tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import screens.MainScreen
import screens.PersonScreen
import utils.PreferenceRule

@RunWith(Parameterized::class)
class PersonScreenTest(
    private val fieldName: String,
    private val expectedFieldValue: String
) : BaseTest() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf("Имя", "Ariana"),
                arrayOf("Фамилия", "Wilson"),
                arrayOf("Пол", "Ж"),
                arrayOf("Дата рождения", "1954-08-07")
            )
        }
    }

    @get:Rule(order = 0)
    val prefs = PreferenceRule(true)

    @get:Rule(order = 1)
    val mockRule = WireMockRule(5000)

    @get:Rule(order = 2)
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun personFieldsCheckTest() = run {

        stubFor(
            get("/api/")
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(getFileToString("responses/person1_response.json"))
                )
        )

        with(MainScreen()) {
            step("Добавление человека через сеть") {
                addPersonByNetwork()
            }
            step("Открытие карточки человека") {
                openPersonScreen(0)
            }
        }
        step("Проверка значения поля $fieldName") {
            PersonScreen().checkFieldValue(fieldName, expectedFieldValue)
        }
    }
}