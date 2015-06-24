package com.adeuza.movalysfwk.mobile.mf4android.test;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.TextView;

import com.google.android.apps.common.testing.deps.guava.base.Preconditions;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

/**
 * Helper for testing text in upper case
 * Use this to avoid some of the automatic correction of the android keyboards
 */
public class UpperTextHelper {

    public static Matcher<View> withTextUpperCase(String text) {
        return withTextUpperCase(Matchers.is(text.toUpperCase()));
    }

    public static Matcher<View> withTextUpperCase(final Matcher<String> stringMatcher) {
        Preconditions.checkNotNull(stringMatcher);
        return new BoundedMatcher<View, TextView>(TextView.class) {

            public void describeTo(Description description) {
                description.appendText("with text: ");
                stringMatcher.describeTo(description);
            }

            public boolean matchesSafely(TextView textView) {
                return stringMatcher.matches(textView.getText().toString().toUpperCase());
            }
        };
    }
}
