package com.adeuza.movalysfwk.mobile.mf4android.test;

import android.support.test.espresso.action.AdapterViewProtocol;
import android.support.test.espresso.core.deps.guava.base.Optional;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView;

public interface FixedListAdapterViewProtocol extends AdapterViewProtocol {
    Iterable<AdapterViewProtocol.AdaptedData> getDataInAdapterView(AbstractFixedListView var1);

    Optional<AdaptedData> getDataRenderedByView(AbstractFixedListView var1, View var2);

    void makeDataRenderedWithinAdapterView(AbstractFixedListView var1, AdapterViewProtocol.AdaptedData var2);

    boolean isDataRenderedWithinAdapterView(AbstractFixedListView var1, AdapterViewProtocol.AdaptedData var2);
}
