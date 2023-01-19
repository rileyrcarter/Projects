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
public class ProfileTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void profileTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.Guest), withText("Guest"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.profile),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.loginButton), withText("LOGIN/SIGN UP"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.loginButton), withText("Login/Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.sign_up), withText("Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText(""), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("testing"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("testing1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.first_name),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.first_name), withText("test"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.birthday),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("05092002"), closeSoftKeyboard());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.birthday), withText("05092002"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText8.perform(pressImeActionButton());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.sign_up), withText("Sign Up"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                6),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.container),
                        withParent(allOf(withId(android.R.id.content),
                                withParent(withId(androidx.appcompat.R.id.decor_content_parent)))),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction viewGroup2 = onView(
                allOf(withId(R.id.container),
                        withParent(allOf(withId(android.R.id.content),
                                withParent(withId(androidx.appcompat.R.id.decor_content_parent)))),
                        isDisplayed()));
        viewGroup2.check(matches(isDisplayed()));

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.last_name),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("testLast"), closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.last_name), withText("testLast"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText10.perform(pressImeActionButton());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.sign_up), withText("Sign Up"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                6),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.profile),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.first_name), withText("test"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("test")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.last_name), withText("testLast"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView2.check(matches(withText("testLast")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.last_name), withText("testLast"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView3.check(matches(withText("testLast")));


        ViewInteraction button2 = onView(
                allOf(withId(R.id.logOutButton), withText("LOG OUT"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.logOutButton), withText("LOG OUT"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.logOutButton), withText("Log Out"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.textView5), withText("Log In"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView5.check(matches(withText("Log In")));
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
