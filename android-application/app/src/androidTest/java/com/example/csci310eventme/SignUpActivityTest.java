package com.example.csci310eventme;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    //tests that all fields are filled in response to user keyboard input
    @Test
    public void signUpActivityTest_onType_view_updates() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.sign_up), withText("Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("rileyTest"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("1234567891"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.first_name),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("Riley"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.last_name),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("Carter"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.birthday),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("05242000"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.birthday), withText("05242000"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());

        ViewInteraction editText = onView(
                allOf(withId(R.id.username), withText("rileyTest"),
                        withParent(allOf(withId(R.id.container),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        editText.check(matches(withText("rileyTest")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.password), withText("••••••••••"),
                        withParent(allOf(withId(R.id.container),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        editText2.check(matches(withText("••••••••••")));

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.first_name), withText("Riley"),
                        withParent(allOf(withId(R.id.container),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        editText3.check(matches(withText("Riley")));

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.last_name), withText("Carter"),
                        withParent(allOf(withId(R.id.container),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        editText4.check(matches(withText("Carter")));

        ViewInteraction editText5 = onView(
                allOf(withId(R.id.birthday), withText("05242000"),
                        withParent(allOf(withId(R.id.container),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        editText5.check(matches(withText("05242000")));

        ViewInteraction editText6 = onView(
                allOf(withId(R.id.birthday), withText("05242000"),
                        withParent(allOf(withId(R.id.container),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        editText6.check(matches(withText("05242000")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
