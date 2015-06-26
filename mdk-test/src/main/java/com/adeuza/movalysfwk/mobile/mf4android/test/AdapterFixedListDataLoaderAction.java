package com.adeuza.movalysfwk.mobile.mf4android.test;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.AdapterViewProtocol;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView;
import com.google.android.apps.common.testing.deps.guava.base.Optional;
import com.google.android.apps.common.testing.deps.guava.base.Preconditions;
import com.google.android.apps.common.testing.deps.guava.collect.Lists;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;

import java.util.ArrayList;
import java.util.Iterator;

public final class AdapterFixedListDataLoaderAction implements ViewAction {
    private final Matcher<Object> dataToLoadMatcher;
    private final FixedListAdapterViewProtocol adapterViewProtocol;
    private final Optional<Integer> atPosition;
    private AdapterViewProtocol.AdaptedData adaptedData;
    private boolean performed = false;
    private final Object dataLock = new Object();

    public AdapterFixedListDataLoaderAction(Matcher<Object> dataToLoadMatcher, Optional<Integer> atPosition, AdapterViewProtocol adapterViewProtocol) {
        this.dataToLoadMatcher = (Matcher) Preconditions.checkNotNull(dataToLoadMatcher);
        this.atPosition = (Optional)Preconditions.checkNotNull(atPosition);
        this.adapterViewProtocol = (FixedListAdapterViewProtocol)Preconditions.checkNotNull(adapterViewProtocol);
    }

    public AdapterViewProtocol.AdaptedData getAdaptedData() {
        Object var1 = this.dataLock;
        synchronized(this.dataLock) {
            Preconditions.checkState(this.performed, "perform hasn\'t been called yet!");
            return this.adaptedData;
        }
    }

    public Matcher<View> getConstraints() {
        return Matchers.allOf(new Matcher[]{ViewMatchers.isAssignableFrom(AbstractFixedListView.class), ViewMatchers.isDisplayed()});
    }

    public void perform(UiController uiController, View view) {
        AbstractFixedListView adapterView = (AbstractFixedListView)view;
        ArrayList matchedDataItems = Lists.newArrayList();
        Iterator requestCount = this.adapterViewProtocol.getDataInAdapterView(adapterView).iterator();

        while(requestCount.hasNext()) {
            AdapterViewProtocol.AdaptedData dataMatcherDescription = (AdapterViewProtocol.AdaptedData)requestCount.next();
            if(this.dataToLoadMatcher.matches(dataMatcherDescription.getData())) {
                matchedDataItems.add(dataMatcherDescription);
            }
        }

        if(matchedDataItems.size() == 0) {
            StringDescription var9 = new StringDescription();
            this.dataToLoadMatcher.describeTo(var9);
            if(matchedDataItems.isEmpty()) {
                var9.appendText(" contained values: ");
                var9.appendValue(this.adapterViewProtocol.getDataInAdapterView(adapterView));
                throw (new PerformException.Builder()).withActionDescription(this.getDescription()).withViewDescription(HumanReadables.describe(view)).withCause(new RuntimeException("No data found matching: " + var9)).build();
            }
        }

        Object var11 = this.dataLock;
        synchronized(this.dataLock) {
            Preconditions.checkState(!this.performed, "perform called 2x!");
            this.performed = true;
            if(this.atPosition.isPresent()) {
                int var13 = matchedDataItems.size() - 1;
                if(((Integer)this.atPosition.get()).intValue() > var13) {
                    throw (new PerformException.Builder()).withActionDescription(this.getDescription()).withViewDescription(HumanReadables.describe(view)).withCause(new RuntimeException(String.format("There are only %d elements that matched but requested %d element.", new Object[]{Integer.valueOf(var13), this.atPosition.get()}))).build();
                }

                this.adaptedData = (AdapterViewProtocol.AdaptedData)matchedDataItems.get(((Integer)this.atPosition.get()).intValue());
            } else {
                if(matchedDataItems.size() != 1) {
                    StringDescription var12 = new StringDescription();
                    this.dataToLoadMatcher.describeTo(var12);
                    throw (new PerformException.Builder()).withActionDescription(this.getDescription()).withViewDescription(HumanReadables.describe(view)).withCause(new RuntimeException("Multiple data elements matched: " + var12 + ". Elements: " + matchedDataItems)).build();
                }

                this.adaptedData = (AdapterViewProtocol.AdaptedData)matchedDataItems.get(0);
            }
        }

        for(int var10 = 0; !this.adapterViewProtocol.isDataRenderedWithinAdapterView(adapterView, this.adaptedData); ++var10) {
            if(var10 > 1) {
                if(var10 % 50 == 0) {
                    adapterView.invalidate();
                    this.adapterViewProtocol.makeDataRenderedWithinAdapterView(adapterView, this.adaptedData);
                }
            } else {
                this.adapterViewProtocol.makeDataRenderedWithinAdapterView(adapterView, this.adaptedData);
            }

            uiController.loopMainThreadForAtLeast(100L);
        }

    }

    public String getDescription() {
        return "load adapter data";
    }
}

