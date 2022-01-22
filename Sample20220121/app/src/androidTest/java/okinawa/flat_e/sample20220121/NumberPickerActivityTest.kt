package okinawa.flat_e.sample20220121


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.widget.NumberPicker

@LargeTest
@RunWith(AndroidJUnit4::class)
class NumberPickerActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun numberPickerActivityTest() {
        val materialButton = onView(
            allOf(
                withId(R.id.button2), withText("to number picker"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        // https://stackoverflow.com/questions/24074495/automating-number-picker-in-android-using-espresso
        val funcViewAction = {n:Int ->
            object:ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return isAssignableFrom(NumberPicker::class.java)
                }

                override fun getDescription(): String {
                    return "Set the passed number into the NumberPicker"
                }

                override fun perform(uiController: UiController?, view: View?) {
                    view?.let {
                        (it as NumberPicker).value = n
                    }
                }
            }
        }

        val numberPicker1 = onView(withContentDescription(R.string.number_picker1))
        numberPicker1.perform(funcViewAction(1))

        val numberPicker2 = onView(withContentDescription(R.string.number_picker2))
        numberPicker2.perform(funcViewAction(2))

        val numberPicker3 = onView(withContentDescription(R.string.number_picker3))
        numberPicker3.perform(funcViewAction(3))

        Thread.sleep(3000)
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
