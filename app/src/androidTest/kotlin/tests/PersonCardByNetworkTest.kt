package tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.http.Fault
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED
import io.qameta.allure.kotlin.Allure.step
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import screens.MainScreen
import utils.PreferenceRule

class PersonCardByNetworkTest : BaseTest() {

    @get:Rule(order = 0)
    val prefs = PreferenceRule(true)

    @get:Rule(order = 1)
    val mockRule = WireMockRule(5000)

    @get:Rule(order = 2)
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun addPersonByNetworkTest() = run {

        stubFor(
            get("/api/")
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(getFileToString("responses/person1_response.json"))
                )
        )

        with(MainScreen()) {
            step("Добавление 1 человека через сеть") {
                addPersonByNetwork()
            }
            step("Проверка отсутствия текста") {
                checkTextNotFoundNotDisplayed()
            }
        }
    }

    @Test
    fun deletePersonTest() = run {
        val countCards = 3
        val scenario = "scenario"

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
            step("Добавление $countCards людей через сеть") {
                addPersonByNetwork(countCards)
            }
            step("Свайп карточки влево") {
                deletePerson(2)
            }
            step("Проверка удаления карточки") {
                checkSizeListPerson(countCards)
            }
        }
    }

    @Test
    fun checkSnackbarInternetConnectionTest() {

        stubFor(
            get("/api/")
                .willReturn(
                    aResponse()
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)
                )
        )

        with(MainScreen()) {
            step("Добавление карточки пользователя через сеть") {
                addPersonByNetwork()
            }
            step("Проверка отображение снекбара") {
                checkSnackbarIsDisplayed()
            }
        }
    }
}