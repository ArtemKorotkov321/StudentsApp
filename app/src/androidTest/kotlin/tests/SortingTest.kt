package tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import screens.MainScreen
import utils.PreferenceRule

class SortingTest : BaseTest() {

    @get:Rule(order = 0)
    val prefs = PreferenceRule(true)

    @get:Rule(order = 1)
    val mockRule = WireMockRule(5000)

    @get:Rule(order = 2)
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun defaultSortSelectedTest() = run {
        with(MainScreen()) {
            step("Открытие окна сортировки") {
                clickButtonSorting()
            }
            step("Проверка выбранной сортировки по умолчанию") {
                checkSelectedDefaultButton()
            }
        }
    }

    @Test
    fun sortingWithAgeTest() = run {
        val sortedAges = arrayOf("100", "70", "26")
        val scenario = "getPersonInfo"

        stubFor(
            get("/api/").inScenario(scenario).whenScenarioStateIs(STARTED).willSetStateTo("person2")
                .willReturn(
                    aResponse().withStatus(200)
                        .withBody(getFileToString("responses/person1_response.json"))
                )
        )
        stubFor(
            get("/api/").inScenario(scenario).whenScenarioStateIs("person2")
                .willSetStateTo("person3").willReturn(
                    aResponse().withStatus(200)
                        .withBody(getFileToString("responses/person2_response.json"))
                )
        )
        stubFor(
            get("/api/").inScenario(scenario).whenScenarioStateIs("person3").willReturn(
                    aResponse().withStatus(200)
                        .withBody(getFileToString("responses/person3_response.json"))
                )
        )

        with(MainScreen()) {
            step("Добавление 3 карточек через сеть") {
                addPersonByNetwork(3)
            }
            step("Выбор сортировки по возрасту") {
                selectAgeSorting()
            }
            step("Проверка сортировки по возрасту от большего к меньшему") {
                checkSortingWithAge(sortedAges)
            }
        }

    }

}