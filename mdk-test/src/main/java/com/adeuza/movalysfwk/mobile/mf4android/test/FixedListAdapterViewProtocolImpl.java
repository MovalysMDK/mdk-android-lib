package com.adeuza.movalysfwk.mobile.mf4android.test;

import android.database.Cursor;
import android.support.test.espresso.core.deps.guava.base.Optional;
import android.support.test.espresso.core.deps.guava.base.Preconditions;
import android.support.test.espresso.core.deps.guava.collect.Lists;
import android.support.test.espresso.matcher.ViewMatchers;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.NotImplementedException;

import java.util.ArrayList;

public class FixedListAdapterViewProtocolImpl implements FixedListAdapterViewProtocol {
    @Override
    public Iterable<AdaptedData> getDataInAdapterView(AbstractFixedListView var1) {
        ArrayList datas = Lists.newArrayList();

        for(int i = 0; i < var1.getCount(); ++i) {
            Object dataAtPosition = var1.getItem(i);
            datas.add((new AdaptedData.Builder()).withDataFunction(new FixedListAdapterViewProtocolImpl.StandardDataFunction(dataAtPosition, i)).withOpaqueToken(Integer.valueOf(i)).build());
        }

        return datas;
    }

    @Override
    public Optional<AdaptedData> getDataRenderedByView(AbstractFixedListView var1, View var2) {
        if(var1 == var2.getParent()) {
            int position = -1; //var1.getPositionForView(var2);
            for (int rank=0; rank<var1.getCount(); rank++) {
                if (var1.getItemAt(rank).equals(var2)) {
                    position = rank;
                }
            }
            if(position != -1) {
                return Optional.of((new AdaptedData.Builder()).withDataFunction(new FixedListAdapterViewProtocolImpl.StandardDataFunction(var1.getItem(position), position)).withOpaqueToken(Integer.valueOf(position)).build());
            }
        }

        return Optional.absent();
    }

    @Override
    public void makeDataRenderedWithinAdapterView(AbstractFixedListView var1, AdaptedData data) {
        // TODO
    }

    @Override
    public boolean isDataRenderedWithinAdapterView(AbstractFixedListView var1, AdaptedData adaptedData) {
        // TODO
        return true;
    }

    @Override
    public Iterable<AdaptedData> getDataInAdapterView(AdapterView<? extends Adapter> adapterView) {
        // nothing to do
        throw new NotImplementedException();
    }

    @Override
    public Optional<AdaptedData> getDataRenderedByView(AdapterView<? extends Adapter> adapterView, View view) {
        // nothing to do
        throw new NotImplementedException();
    }

    @Override
    public void makeDataRenderedWithinAdapterView(AdapterView<? extends Adapter> adapterView, AdaptedData adaptedData) {
        // nothing to do
        throw new NotImplementedException();
    }

    @Override
    public boolean isDataRenderedWithinAdapterView(AdapterView<? extends Adapter> adapterView, AdaptedData adaptedData) {
        // nothing to do
        throw new NotImplementedException();
    }

    private boolean isElementFullyRendered(AdapterView<? extends Adapter> adapterView, int childAt) {
        View element = adapterView.getChildAt(childAt);
        return ViewMatchers.isDisplayingAtLeast(90).matches(element);
    }

    private static final class StandardDataFunction implements DataFunction {
        private final Object dataAtPosition;
        private final int position;

        private StandardDataFunction(Object dataAtPosition, int position) {
            Preconditions.checkArgument(position >= 0, "position must be >= 0");
            this.dataAtPosition = dataAtPosition;
            this.position = position;
        }

        public Object getData() {
            if(this.dataAtPosition instanceof Cursor && !((Cursor)this.dataAtPosition).moveToPosition(this.position)) {
                Log.e("StdAdapterViewProtocol", "Cannot move cursor to position: " + this.position);
            }

            return this.dataAtPosition;
        }
    }
}
