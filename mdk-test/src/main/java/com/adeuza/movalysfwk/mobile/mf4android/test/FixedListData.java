package com.adeuza.movalysfwk.mobile.mf4android.test;

import android.support.test.espresso.DataInteraction;

import org.hamcrest.Matcher;

public final class FixedListData {

    public static FixedListDataInteraction onFixedListData(Matcher<Object> dataMatcher) {
        return new FixedListDataInteraction(dataMatcher);
    }

}
