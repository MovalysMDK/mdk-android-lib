/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.PerformItemClickEvent;

/**
 * ExpandableListView for the 3dlist
 * Exposes the performClickItem method to trigger {@link PerformItemClickEvent}
 */
@Deprecated
public class InnerExpandableListView extends ExpandableListView implements MMPerformItemClickView {

	/** listener for item click on list (this can intercept event in case of modify data cancel action) */
	private MMPerformItemClickListener performItemClickListener;
	
	/**
	 * Constructor
	 * @param p_oContext context to use
	 */
	public InnerExpandableListView(Context p_oContext) {
		super(p_oContext);
	}

	/**
	 * Constructor
	 * @param p_oContext context to use
	 * @param p_oAttrs attributes to set
	 */
	public InnerExpandableListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}

	/**
	 * Constructor
	 * @param p_oContext context to use
	 * @param p_oAttrs attributes to set
	 * @param p_iDefStyle style to apply
	 */
	public InnerExpandableListView(Context p_oContext, AttributeSet p_oAttrs, int p_iDefStyle) {
		super(p_oContext, p_oAttrs, p_iDefStyle);
	}
	
	/**
	 * Used to set the {@link FlipperExpandableListAdapter} adapter, used to trigger the {@link PerformItemClickEvent}
	 * @param p_oAdapter adapter to set
	 */
	public void setListAdapter(FlipperExpandableListAdapter p_oAdapter) {
		if ( p_oAdapter instanceof MMPerformItemClickListener) {
			this.performItemClickListener = (MMPerformItemClickListener) p_oAdapter;
		}
	}
	
	@Override
	public boolean performItemClick( final View p_oView, final int p_iPosition, final long p_iId) {
		boolean doSuper = true ;
		boolean handled = false ;
		if ( performItemClickListener != null ) {
			doSuper = performItemClickListener.onPerformItemClick(p_oView, p_iPosition, p_iId, this);
		}
		if ( doSuper ) {
			handled = super.performItemClick(p_oView, p_iPosition, p_iId);
		}
		
		return handled ;
	}
	
	@Override
	public boolean superPerformItemClick(View p_oView, int p_iPosition, long p_iId) {
		return super.performItemClick(p_oView, p_iPosition, p_iId);
	}
}
