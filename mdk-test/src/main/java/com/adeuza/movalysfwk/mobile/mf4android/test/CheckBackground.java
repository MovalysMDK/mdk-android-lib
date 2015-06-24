package com.adeuza.movalysfwk.mobile.mf4android.test;

import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMRelativeBackgroundedLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

public class CheckBackground {

    public static Matcher<View> withResource(int backgroundImageId) {
        return withResource(Matchers.is(backgroundImageId));
    }

    public static Matcher<View> withResource(final Matcher<Integer> backgroundImageId) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof MMRelativeBackgroundedLayout)) {
                    return false;
                }
                return backgroundImageId.matches(((MMRelativeBackgroundedLayout) view).getBackgroundResource());
            }

            @Override
            public void describeTo(Description description) {
                backgroundImageId.describeTo(description);
            }
        };
    }

//    public static Matcher<View> withBackground(Drawable backgroundImage){
//        return withBackground(Matchers.is(backgroundImage.getConstantState()));
//    }
//
//    public static Matcher<View> withBackground(final Matcher<Drawable.ConstantState> backgroundImage){
//        //checkNotNull(backgroundImageId);
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public boolean matchesSafely(View view) {
//                if (!(view instanceof MMRelativeBackgroundedLayout)) {
//                    return false;
//                }
//
//                return backgroundImage.matches(((MMRelativeBackgroundedLayout)view).getBackground().getConstantState());
//                //return ((MMRelativeBackgroundedLayout)view).getBackground().getConstantState().equals(backgroundImage.getConstantState());
//            }
//
//            @Override
//            public void describeTo(Description description) {
//                backgroundImage.describeTo(description);
//            }
//        };
//    }
}