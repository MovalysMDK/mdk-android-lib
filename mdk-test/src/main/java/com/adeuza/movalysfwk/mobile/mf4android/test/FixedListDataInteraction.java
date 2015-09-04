package com.adeuza.movalysfwk.mobile.mf4android.test;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.Root;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.AdapterViewProtocol;
import android.support.test.espresso.core.deps.guava.base.Optional;
import android.support.test.espresso.core.deps.guava.base.Preconditions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.view.ViewParent;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

/**
 * Data interaction helper for fixedlist
 */
public class FixedListDataInteraction {

    private final Matcher<Object> dataMatcher;
    private Matcher<View> adapterMatcher = ViewMatchers.isAssignableFrom(AbstractFixedListView.class);
    private Optional<Matcher<View>> childViewMatcher = Optional.absent();
    private Optional<Integer> atPosition = Optional.absent();
    private FixedListAdapterViewProtocolImpl adapterViewProtocol = new FixedListAdapterViewProtocolImpl();//AdapterViewProtocols.standardProtocol();
    private Matcher<Root> rootMatcher;

    FixedListDataInteraction(Matcher<Object> dataMatcher) {
        this.rootMatcher = RootMatchers.DEFAULT;
        this.dataMatcher = (Matcher) Preconditions.checkNotNull(dataMatcher);
    }

    public FixedListDataInteraction onChildView(Matcher<View> childMatcher) {
        this.childViewMatcher = Optional.of(Preconditions.checkNotNull(childMatcher));
        return this;
    }

    public FixedListDataInteraction inRoot(Matcher<Root> rootMatcher) {
        this.rootMatcher = (Matcher)Preconditions.checkNotNull(rootMatcher);
        return this;
    }

    public FixedListDataInteraction inAdapterView(Matcher<View> adapterMatcher) {
        this.adapterMatcher = (Matcher)Preconditions.checkNotNull(adapterMatcher);
        return this;
    }

    public FixedListDataInteraction atPosition(Integer atPosition) {
        this.atPosition = Optional.of(Preconditions.checkNotNull(atPosition));
        return this;
    }

    public FixedListDataInteraction usingAdapterViewProtocol(FixedListAdapterViewProtocolImpl adapterViewProtocol) {
        this.adapterViewProtocol = (FixedListAdapterViewProtocolImpl)Preconditions.checkNotNull(adapterViewProtocol);
        return this;
    }

    public ViewInteraction perform(ViewAction... actions) {
        AdapterFixedListDataLoaderAction adapterDataLoaderAction = this.load();
        return Espresso.onView(this.makeTargetMatcher(adapterDataLoaderAction)).inRoot(this.rootMatcher).perform(actions);
    }

    public ViewInteraction check(ViewAssertion assertion) {
        AdapterFixedListDataLoaderAction adapterDataLoaderAction = this.load();
        return Espresso.onView(this.makeTargetMatcher(adapterDataLoaderAction)).inRoot(this.rootMatcher).check(assertion);
    }

    private AdapterFixedListDataLoaderAction load() {
        AdapterFixedListDataLoaderAction adapterDataLoaderAction = new AdapterFixedListDataLoaderAction(this.dataMatcher, this.atPosition, this.adapterViewProtocol);
        Espresso.onView(this.adapterMatcher).inRoot(this.rootMatcher).perform(new ViewAction[]{adapterDataLoaderAction});
        return adapterDataLoaderAction;
    }

    private Matcher<View> makeTargetMatcher(AdapterFixedListDataLoaderAction adapterDataLoaderAction) {
        Matcher targetView = this.displayingData(this.adapterMatcher, this.dataMatcher, this.adapterViewProtocol, adapterDataLoaderAction);
        if(this.childViewMatcher.isPresent()) {
            targetView = Matchers.allOf(new Matcher[]{(Matcher) this.childViewMatcher.get(), ViewMatchers.isDescendantOfA(targetView)});
        }

        return targetView;
    }

    private Matcher<View> displayingData(final Matcher<View> adapterMatcher, final Matcher<Object> dataMatcher, final FixedListAdapterViewProtocolImpl adapterViewProtocol, final AdapterFixedListDataLoaderAction adapterDataLoaderAction) {
        Preconditions.checkNotNull(adapterMatcher);
        Preconditions.checkNotNull(dataMatcher);
        Preconditions.checkNotNull(adapterViewProtocol);
        return new TypeSafeMatcher<View>() {
            public void describeTo(Description description) {
                description.appendText(" displaying data matching: ");
                dataMatcher.describeTo(description);
                description.appendText(" within adapter view matching: ");
                adapterMatcher.describeTo(description);
            }

            public boolean matchesSafely(View view) {
                ViewParent parent;
                for(parent = view.getParent(); parent != null && !(parent instanceof AbstractFixedListView); parent = parent.getParent()) {
                    ;
                }

                if(parent != null && adapterMatcher.matches(parent)) {
                    Optional data = adapterViewProtocol.getDataRenderedByView((AbstractFixedListView)parent, view);
                    if(data.isPresent()) {
                        return adapterDataLoaderAction.getAdaptedData().opaqueToken.equals(((AdapterViewProtocol.AdaptedData)data.get()).opaqueToken);
                    }
                }

                return false;
            }
        };
    }
}
