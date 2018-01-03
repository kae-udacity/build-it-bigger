package com.udacity.gradle.builditbigger;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Android functional tests.
 */

@RunWith(AndroidJUnit4.class)
public class TellJokeTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource idlingResource;
    private IdlingRegistry idlingRegistry;

    @Before
    public void registerIdlingResource() {
        idlingResource =  mainActivityTestRule.getActivity().getIdlingResource();

        idlingRegistry = IdlingRegistry.getInstance();
        idlingRegistry.register(idlingResource);
    }

    @Test
    public void clickTellJokeButton_DisplaysNonEmptyString() {
        onView(withId(R.id.button_tell_joke)).perform(click());
        onView(withId(R.id.text_view_joke)).check(matches(not(withText(""))));
    }

    @After
    public void unregisterIdlingResource() {
        idlingRegistry.unregister(idlingResource);
    }
}
