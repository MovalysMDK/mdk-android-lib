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
import android.view.MotionEvent;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
/**
 * Component to display long text and expand text to see full
 * text.
 *
 */
public class MMExpandableTextView extends MMTextViewEllipsized {

	/** Maximum number of displayed lines XML attr name */
	private static final String ATTR_MAX_LINES = "max-lines";
	/** If no max define */
	protected static final int NO_MAX_LINES = Integer.MAX_VALUE;
	/** default maximum lines displayed */
	protected static final int DEFAULT_MAX_LINES = 2;
	/** initial line count (on text set) */
	private int initialLinesCount;
	/** toggle listener */
	private OnToggleListener listener;

	/**
	 * Create new MMExpandableTextView instance
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view attributes (define in layout XML)
	 * @param p_oDefStyle the android style (define in XML)
	 */
	public MMExpandableTextView(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.initFromAttributes(p_oAttrs);
		}
	}
	/**
	 * Create new MMExpandableTextView instance
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view attributes (define in layout XML)
	 */
	public MMExpandableTextView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.initFromAttributes(p_oAttrs);
		}
	}
	/**
	 * Create new MMExpandableTextView instance
	 * @param p_oContext the android context
	 */
	public MMExpandableTextView(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.initialLinesCount = DEFAULT_MAX_LINES;
			this.init();
		}
	}

	/**
	 * Read android attributes
	 * @param p_oAttrs the view attributes
	 */
	private final void initFromAttributes(AttributeSet p_oAttrs) {
		this.initialLinesCount = p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, ATTR_MAX_LINES, DEFAULT_MAX_LINES);
		this.init();
	}
	/**
	 * Initialize component
	 */
	private final void init() {
		this.setMaxLines(this.initialLinesCount);
	}
	/**
	 * Collapse component
	 */
	public void collapse() {
		this.setMaxLines(this.initialLinesCount);
		if (this.listener != null) {
			this.listener.onCollapse();
		}
	}
	/**
	 * Expand component
	 */
	public void expand() {
		this.setMaxLines(NO_MAX_LINES);
		if (this.listener != null) {
			this.listener.onExpand();
		}
	}
	/**
	 * Change component expand/collapse
	 */
	public void toggle() {
		if (this.getMaxLines() == NO_MAX_LINES) {
			this.collapse();
		}
		else {
			this.expand();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent p_oEvent) {
		switch (p_oEvent.getAction()) {
		case MotionEvent.ACTION_UP :
			this.toggle();
			return true;
		default:
			return super.onTouchEvent(p_oEvent);
		}
	}

	/**
	 * Set the Toggle listener
	 * @param p_oOnToggleListener the new toggle listener
	 */
	public final void setOnToggleListener(OnToggleListener p_oOnToggleListener) {
		this.listener = p_oOnToggleListener;
	}

	/**
	 * Interface called on toggle change
	 *
	 */
	public interface OnToggleListener {
		/**
		 * Call on expand
		 */
		public void onExpand();
		/**
		 * Call on collase
		 */
		public void onCollapse();
	}
}
