package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;

/**
 * <p>Simple fixed list component : there is no detail popup for its items.</p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author ktilhou
 * 
 * @param <ITEM>
 * @param <ITEMVM>
 */
public class MMSimpleFixedListView<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> extends MMFixedListView<ITEM, ITEMVM> {
	
	/**
	 * <p>
	 *  Construct an object <em>MMSimpleFixedListView</em>.
	 * </p>
	 * @param p_oContext The application context
	 */
	public MMSimpleFixedListView(Context p_oContext) {
		this(p_oContext, null);
	}

	/**
	 * <p>
	 *  Construct an object <em>MMSimpleFixedListView</em>.
	 * </p>
	 * @param p_oContext
	 * 		The application context
	 * @param p_oAttrs
	 * 		parameter to configure the <em>MMSimpleFixedListView</em> object.
	 */
	public MMSimpleFixedListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		super.configurationDisabledComponent(false);
	}
	
	@Override
	protected void wrapCurrentView(View p_oView, ITEMVM p_oCurrentViewModel) {
		super.wrapCurrentView(p_oView, p_oCurrentViewModel);
		p_oView.setClickable(false);
	}
}
