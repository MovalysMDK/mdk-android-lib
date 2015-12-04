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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.annotations.BaseComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableTextViewComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentMandatoryLabel;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.base.MMBaseTextView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * <p>TextView Ellipsized widget used in the Movalys Mobile product for Android</p>
 */
@BaseComponent(baseName="MMBaseTextView", baseClass="android.widget.TextView", appCompatClass="android.support.v7.widget.AppCompatTextView")
public class MMTextViewEllipsized extends MMBaseTextView implements ConfigurableVisualComponent, InstanceStatable, ComponentMandatoryLabel {

	/** configurable view framework delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentDelegate<String> aivDelegate = null;

	/** Ellipsis string */
	private static final String ELLIPSIS = "...";

	/** default maximum number of lines displayed */
	private static final int MAX_LINES_NUMBER = 4;

	/**
	 * Listener on ellipsis state modified
	 */
	public interface EllipsizeListener {
		
		/**
		 * triggered when the value of the ellipsis state is modified
		 * @param p_bEllipsized true if the ellipsis is visible
		 */
		void ellipsizeStateChanged(boolean p_bEllipsized);
	}

	/** list of {@link EllipsizeListener} */
	private final List<EllipsizeListener> ellipsizeListeners = new ArrayList<>();
	
	/** true if the ellipsis is visible */
	private boolean isEllipsized;
	
	/** true if the view needs to be drawn again from scratch */
	private boolean isStale;
	
	/** true if the value of the component is being changed by the framework */
	private boolean programmaticChange;
	
	/** the full value hosted by the component */
	private String fullText;
	
	/** current maximum number of lines displayed */
	private int maxLines = -1;
	
	/** the line spacing multiplier used to build the layout */
	private float lineSpacingMultiplier = 1.0F;
	
	/** the additional vertical padding used to build the layout */
	private float lineAdditionalVerticalPadding = 0.0F;


	/**
	 * Constructs a new MMTextViewEllipsized
	 * @param p_oContext the context to use
	 * @see TextView#TextView(Context)
	 */
	public MMTextViewEllipsized(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableTextViewComponentDelegate) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
		}
	}

	/**
	 * Constructs a new MMTextViewEllipsized
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see TextView#TextView(Context, AttributeSet)
	 */
	public MMTextViewEllipsized(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableTextViewComponentDelegate) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			
			this.setMaxLines(MAX_LINES_NUMBER);
			this.setVerticalScrollBarEnabled(true);
			this.setMovementMethod(new ScrollingMovementMethod());
			//LMI: la ligne ci-dessous fait planter le sliding dans le workspace:
			//		this.setScrollbarFadingEnabled(false);
		}
	}


	/**
	 * Constructs a new MMTextViewEllipsized
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see TextView#TextView(Context, AttributeSet, int)
	 */
	public MMTextViewEllipsized(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableTextViewComponentDelegate) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			
			this.setMaxLines(MAX_LINES_NUMBER);
			this.setMovementMethod(new ScrollingMovementMethod()); 
			//LMI: la ligne ci-dessous fait planter le sliding dans le workspace:
			//		this.setScrollbarFadingEnabled(false);
			this.setVerticalScrollBarEnabled(true);
			this.setScrollbarFadingEnabled(false);
			//completed by the onFinishInflate method
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.onSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString("text", fullText);
		return r_oBundle;
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.onRestoreInstanceState(oParentParcelable);
		fullText = r_oBundle.getString("text");
	}

	/**
	 * Adds an {@link EllipsizeListener} to the list
	 * @param p_oListener the listener to add
	 */
	public void addEllipsizeListener(EllipsizeListener p_oListener) {
		if (p_oListener == null) {
			throw new NullPointerException();
		}
		ellipsizeListeners.add(p_oListener);
	}

	/**
	 * Removes an {@link EllipsizeListener} from the list
	 * @param p_oListener the listener to remove
	 */
	public void removeEllipsizeListener(EllipsizeListener p_oListener) {
		ellipsizeListeners.remove(p_oListener);
	}

	/**
	 * Return true if the component is displaying an ellipsis
	 * @return true if the component is displaying an ellipsis
	 */
	public boolean isEllipsized() {
		return isEllipsized;
	}

	@Override
	public void setMaxLines(int p_iMaxLines) {
		super.setMaxLines(p_iMaxLines);
		this.maxLines = p_iMaxLines;
		isStale = true;
	}

	@Override
	public int getMaxLines() {
		return maxLines;
	}

	@Override
	public void setLineSpacing(float p_fAdd, float p_fMult) {
		this.lineAdditionalVerticalPadding = p_fAdd;
		this.lineSpacingMultiplier = p_fMult;
		super.setLineSpacing(p_fAdd, p_fMult);
	}

	@Override
	protected void onTextChanged(CharSequence p_sText, int p_iStart, int p_iBefore, int p_iAfter) {
		super.onTextChanged(p_sText, p_iStart, p_iBefore, p_iAfter);
		if (!programmaticChange) {
			fullText = p_sText.toString();
			isStale = true;
		}
	}

	@Override
	protected void onDraw(Canvas p_oCanvas) {
		if (isStale) {
			super.setEllipsize(null);
			resetText();
		}
		super.onDraw(p_oCanvas);
	}

	/**
	 * Resets the text displayed by the component
	 */
	private void resetText() {
		int iMaxLines = getMaxLines();
		String sWorkingText = fullText;
		boolean bEllipsized = false;
		if (iMaxLines != -1) {
			Layout oLayout = this.createWorkingLayout(sWorkingText);
			if (oLayout.getLineCount() > iMaxLines) {
				sWorkingText = fullText.substring(0, oLayout.getLineEnd(iMaxLines - 1)).trim();
				while (this.createWorkingLayout(sWorkingText + ELLIPSIS).getLineCount() > iMaxLines) {
					int iLastSpace = sWorkingText.lastIndexOf(' ');
					if (iLastSpace == -1) {
						break;
					}
					sWorkingText = sWorkingText.substring(0, iLastSpace);
				}
				sWorkingText = new StringBuilder(sWorkingText).append(ELLIPSIS).toString();
				bEllipsized = true;
			}
		}
		if (!sWorkingText.equals( getText().toString()) ) {
			programmaticChange = true;
			try {
				setText(sWorkingText);
			} finally {
				programmaticChange = false;
			}
		}
		isStale = false;
		if (bEllipsized != isEllipsized) {
			isEllipsized = bEllipsized;
			for (EllipsizeListener oListener : ellipsizeListeners) {
				oListener.ellipsizeStateChanged(bEllipsized);
			}
		}
	}

	/**
	 * Creates a new layout with the given text
	 * @param p_sWorkingText the text to display
	 * @return the view built with the text
	 */
	private Layout createWorkingLayout(String p_sWorkingText) {
		return new StaticLayout(p_sWorkingText, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(),
				Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineAdditionalVerticalPadding, false);
	}

	/**
	 * Return the full value hosted by the component
	 * @return the full value hosted by the component
	 */
	public String getFullText() {
		return fullText;
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<String> getComponentDelegate() {
		return this.aivDelegate;
	}
	
	/**
	 * {@inheritDoc}
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivFwkDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivFwkDelegate.onRestoreInstanceState(p_oState);
	}
	
	/**********************************************************************************************
	 ******************************* Framework delegate callback **********************************
	 **********************************************************************************************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMandatoryLabel(boolean p_bMandatory) {
		String sOriginalLabel = this.getText().toString();
		String sComputedMandatoryLabel = this.aivDelegate.computeMandatoryLabel(sOriginalLabel, p_bMandatory);
		this.setText(sComputedMandatoryLabel);
	}
}
