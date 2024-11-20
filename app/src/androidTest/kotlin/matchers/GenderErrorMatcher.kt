package matchers

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description

class GenderErrorMatcher(val text: String) : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {
    override fun describeTo(description: Description) {
        description.appendText("error text")
    }

    override fun matchesSafely(item: TextInputLayout): Boolean {
        return item.error.toString() == text
    }

}