package com.example.csci310eventme;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.activity.result.ActivityResult;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MapGuestActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

//    @Rule
//    public IntentsTestRule<MyActivity> intentsTestRule =
//            new IntentsTestRule<>(MyActivity.class);

    //test to assert navigation bar redirects to map activity
    @Test
    public void click_navbar_redirect_maps() {

//        // Build the result to return when the activity is launched.
//        Intent resultData = new Intent();
//        String phoneNumber = "123-345-6789";
//        resultData.putExtra("phone", phoneNumber);
//        ActivityResult result = new ActivityResult(Activity.RESULT_OK, resultData);
//
//        // Set up result stubbing when an intent sent to "contacts" is seen.
//        intending(toPackage("com.android.contacts")).respondWith(result);
//
//        // User action that results in "contacts" activity being launched.
//        // Launching activity expects phoneNumber to be returned and displayed.
//        onView(withId(R.id.pickButton)).perform(click());
//
//        // Assert that the data we set up above is shown.
//        onView(withId(R.id.phoneNumber)).check(matches(withText(phoneNumber)));
//
//
//
//
//        ViewInteraction materialButton = onView(
//                allOf(withId(R.id.Guest), withText("Guest"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                6),
//                        isDisplayed()));
//        materialButton.perform(click());
//
//        ViewInteraction bottomNavigationItemView = onView(
//                allOf(withId(R.id.map),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.bottom_navigation),
//                                        0),
//                                0),
//                        isDisplayed()));
//        bottomNavigationItemView.perform(click());




    }

    @Test
    public void select_map_eventWidget_clickable() {
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
                allOf(withId(R.id.map),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction view = onView(
                allOf(withContentDescription("USC Rare Clothing Pop Up. "),
                        withParent(allOf(withContentDescription("Google Map"),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isClickable()));
        view.check(matches(isClickable()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.eventWidget), withText("Football Game\nUSC\nSports\nNovember 19, 2022   4:30 PM\n$60"),
                        withParent(withParent(withId(R.id.list_view))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));
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
