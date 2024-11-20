package tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.qameta.allure.kotlin.Allure.step
import org.junit.Rule
import org.junit.Test
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import screens.MainScreen
import screens.PersonScreen
import utils.PreferenceRule


class PersonCardByManuallyTest : BaseTest() {

    @get:Rule(order = 0)
    val prefs = PreferenceRule(true)

    @get:Rule(order = 1)
    val mockRule = WireMockRule(5000)

    @get:Rule(order = 2)
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun changeNamePersonTest() = run {

        stubFor(
            get("/api/")
                .willReturn(
                    aResponse().withStatus(200)
                        .withBody(getFileToString("responses/person1_response.json"))
                )
        )

        val name = "Семен"
        with(MainScreen()) {
            step("Добавление человека через сеть") {
                addPersonByNetwork()
            }
            step("Открытие карточки человека") {
                openPersonScreen(0)
            }
        }
        with(PersonScreen()) {
            step("Редактирование имени") {
                editName(name)
                clickSubmit()
            }
        }
        with(MainScreen()) {
            checkNameInCard(name)
        }
    }

    @Test
    fun addPersonManuallyTest() {
        step("Создание карточки вручную") {
            with(MainScreen()) {
                addPersonByManually()
            }
            with(PersonScreen()) {
                createPerson()
            }
        }
        step("Проверка заполненых данных в карточке") {
            with(MainScreen()) {
                checkFieldsPersonInCard()
            }
        }
    }

    @Test
    fun checkErrorTextGenderTest() {
        step("Создание карточки вручную") {
            with(MainScreen()) {
                addPersonByManually()
            }
        }
        step("Проверка отображения текста ошибки поля 'Пол'") {
            with(PersonScreen()) {
                clickSubmit()
                checkErrorGender()
            }
        }
    }

    @Test
    fun checkErrorGenderHideTest() {
        with(MainScreen()) {
            step("Создание карточки вручную") {
                addPersonByManually()
            }
        }
        with(PersonScreen()) {
            step("Ввод невалидного текста") {
                editGender("тест")
                clickSubmit()
            }
            step("Проверка отображения ошибки") {
                checkGenderErrorTextIsDisplayed()
            }
            step("Проверка скрытия ошибки после удаления текста") {
                clearGender()
                checkGenderErrorTextIsNotDisplayed()
            }
        }
    }
}