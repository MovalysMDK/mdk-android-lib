package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

public class MMExpandableTextView extends MMTextViewEllipsized {

	private static final String ATTR_MAX_LINES = "max-lines";

	protected static final int NO_MAX_LINES = Integer.MAX_VALUE;

	protected static final int DEFAULT_MAX_LINES = 2;

	private int initialLinesCount;

	private OnToggleListener listener;

	public MMExpandableTextView(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.initFromAttributes(p_oAttrs);
		}
	}

	public MMExpandableTextView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.initFromAttributes(p_oAttrs);
		}
	}

	public MMExpandableTextView(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.initialLinesCount = DEFAULT_MAX_LINES;
			this.init();
		}
	}

	private void initFromAttributes(AttributeSet p_oAttrs) {
		this.initialLinesCount = p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, ATTR_MAX_LINES, DEFAULT_MAX_LINES);
		this.init();
	}

	private void init() {
		this.setMaxLines(this.initialLinesCount);
	}

	public void collapse() {
		this.setMaxLines(this.initialLinesCount);
		if (this.listener != null) {
			this.listener.onCollapse();
		}
	}

	public void expand() {
		this.setMaxLines(NO_MAX_LINES);
		if (this.listener != null) {
			this.listener.onExpand();
		}
	}

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

	public final void setOnToggleListener(OnToggleListener p_oOnToggleListener) {
		this.listener = p_oOnToggleListener;
	}

	public interface OnToggleListener {
		public void onExpand();
		public void onCollapse();
	}
}
