package com.adeuza.movalysfwk.mobile.mf4android.test;

import android.support.test.espresso.action.AdapterViewProtocol;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView;
import com.google.android.apps.common.testing.deps.guava.base.Optional;


public interface FixedListAdapterViewProtocol extends AdapterViewProtocol {
    Iterable<AdapterViewProtocol.AdaptedData> getDataInAdapterView(AbstractFixedListView var1);

    Optional<AdapterViewProtocol.AdaptedData> getDataRenderedByView(AbstractFixedListView var1, View var2);

    void makeDataRenderedWithinAdapterView(AbstractFixedListView var1, AdapterViewProtocol.AdaptedData var2);

    boolean isDataRenderedWithinAdapterView(AbstractFixedListView var1, AdapterViewProtocol.AdaptedData var2);
}
