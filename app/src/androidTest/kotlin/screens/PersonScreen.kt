package screens

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.kaspersky.kaspresso.screens.KScreen
import data.PersonData
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import matchers.GenderErrorMatcher
import ru.tinkoff.favouritepersons.R

class PersonScreen : KScreen<PersonScreen>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val nameEditText = KEditText { withId(R.id.et_name) }
    val surnameEditText = KEditText { withId(R.id.et_surname) }
    val genderEditText = KEditText { withId(R.id.et_gender) }
    val birhdayEditText = KEditText { withId(R.id.et_birthdate) }
    val emailEditText = KEditText { withId(R.id.et_email) }
    val phoneEditText = KEditText { withId(R.id.et_phone) }
    val addressEditText = KEditText { withId(R.id.et_address) }
    val photoUrlEditText = KEditText { withId(R.id.et_image) }
    val scoreEditText = KEditText { withId(R.id.et_score) }
    val submitButton = KButton { withId(R.id.submit_button) }
    val genderTextInputLayout = onView(withId(R.id.til_gender))
    val genderErrorText = KTextView {withText("Поле должно быть заполнено буквами М или Ж")}


    fun checkFieldValue(field: String, expectedValue: String) {
        when (field) {
            "Имя" -> nameEditText.hasText(expectedValue)
            "Фамилия" -> surnameEditText.hasText(expectedValue)
            "Пол" -> genderEditText.hasText(expectedValue)
            "Дата рождения" -> birhdayEditText.hasText(expectedValue)
            else -> throw Exception("Некорректное поле")
        }
    }

    fun editName(text: String) {
        nameEditText.replaceText(text)
    }

    fun clickSubmit() {
        submitButton.click()
    }

    fun createPerson() {
        val person = PersonData()
        nameEditText.replaceText(person.name)
        surnameEditText.replaceText(person.surname)
        genderEditText.replaceText(person.gender)
        birhdayEditText.replaceText(person.birthday)
        emailEditText.replaceText(person.email)
        phoneEditText.replaceText(person.phone)
        addressEditText.replaceText(person.address)
        photoUrlEditText.replaceText(person.photo)
        scoreEditText.replaceText(person.score)
        clickSubmit()
    }

    fun editGender(text: String) {
        genderEditText.replaceText(text)
    }

    fun clearGender() {
        genderEditText.clearText()
        Thread.sleep(2000L)
    }

    fun checkGenderErrorTextIsDisplayed() {
        genderErrorText.isDisplayed()
    }

    fun checkGenderErrorTextIsNotDisplayed() {
        genderErrorText.doesNotExist()
    }

    fun checkErrorGender() {
        genderTextInputLayout.check(
            matches(
                GenderErrorMatcher("Поле должно быть заполнено буквами М или Ж")
            )
        )
    }
}
