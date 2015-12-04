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
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentSavedState;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentBindDestroy;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * A component that handle a accordion animation on a view
 */

public class MMAccordionLayout extends RelativeLayout implements OnTouchListener, InstanceStatable, ComponentBindDestroy, ConfigurableLayoutComponent {

	/** Enumeration of allowed accordion's direction */
	public static enum AccordionOrientation {
		/** from top to down and from down to top. */
		vertical,

		/** from left to right and from right to left. */
		horizontal
	}

	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentDelegate<NoneType> aivDelegate = null;
	
	/** configurable view framework delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** default content count */
	private static final int DEFAULT_CONTENT_COUNT = 0;
	
	/** no resource content */
	private static final int NO_RESOURCE = -1;
	
	/** default animation duration */
	private static final int DEFAULT_DURATION = 700;
	
	/** default animation orientation */
	private static final AccordionOrientation DEFAULT_ORIENTATION = AccordionOrientation.vertical;
	
	/** attribute content-count (usage in XML) : specify the content count */
	private static final String ATTR_CONTENT_COUNT = "content-count";
	
	/** attribute handle (usage in XML) */
	private static final String ATTR_HANDLE_PREFIX = "handle";
	
	/** attribute content (usage in XML) : specify the animation content */
	private static final String ATTR_CONTENT_PREFIX = "content";
	
	/** attribute anim-duration (usage in XML) : specify the animation duration */
	private static final String ATTR_ANIMATION_DURATION = "anim-duration";
	
	/** attribute orientation (usage in XML) : specify the animation orientation */
	private static final String ATTR_ORIENTATION = "orientation";

	/** Padding of the intervention detail panel */
	private static final int DISTANCE_TOUCH_SLOP = 32;

	/** the animation duration */
	private int duration;
	
	/** the animation orientation */
	private AccordionOrientation orientation;

	/** Position of visible child. */
	private int current;
	
	/** the toggle listener */
	private OnToggleListener listener;
	
	/** the last motion on X axe */
	private float lastMotionX = -1.0f;
	
	/** the last motion on Y axe */
	private float lastMotionY = -1.0f;
	
	/** the handles */
	private int[] handles;
	
	/** the contents */
	private int[] contents;
	
	/** the fold content animation */
	private Animation foldContentAnimation;
	
	/** the fold content animation in reverse */
	private Animation foldContentReverseAnimation;
	
	/** the unfold content animation */
	private Animation unfoldContentAnimation;
	
	/** the unfold content animation in reverse */
	private Animation unfoldContentReverseAnimation;
	
	/** the handles animation */
	private Animation[] handleAnimations;
	
	/** the handles reverse animation */
	private Animation[] handleReverseAnimations;
	
	/** is handle align to content */
	private boolean handleAlignToContent = true;

	/** a true value means a disabled navigation. */
	private boolean lock = false;

	/**
	 * Constructs a new MMAccordionLayout
	 * @param p_oContext the context to use
	 * @see RelativeLayout#RelativeLayout(Context)
	 */
	public MMAccordionLayout(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<NoneType>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{NoneType.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
			
			this.duration = DEFAULT_DURATION;
			this.orientation = DEFAULT_ORIENTATION;
		}
	}

	/**
	 * Constructs a new MMAccordionLayout
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see RelativeLayout#RelativeLayout(Context, AttributeSet)
	 */
	public MMAccordionLayout(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<NoneType>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{NoneType.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			
			final AndroidApplication oAppli = ((AndroidApplication) Application.getInstance());

			int iCount = p_oAttrs.getAttributeIntValue(AndroidApplication.MOVALYSXMLNS, ATTR_CONTENT_COUNT, DEFAULT_CONTENT_COUNT);

			this.handles = new int[iCount];
			this.contents = new int[iCount];

			for (int i=0; i < iCount; i++) {
				this.handles[i] = p_oAttrs.getAttributeResourceValue(AndroidApplication.MOVALYSXMLNS,
						ATTR_HANDLE_PREFIX + (i+1), NO_RESOURCE);

				this.contents[i] = p_oAttrs.getAttributeResourceValue(AndroidApplication.MOVALYSXMLNS,
						ATTR_CONTENT_PREFIX + (i+1), NO_RESOURCE);
			}

			this.duration = p_oAttrs.getAttributeIntValue(AndroidApplication.MOVALYSXMLNS, ATTR_ANIMATION_DURATION, DEFAULT_DURATION);

			this.foldContentAnimation = AnimationUtils.loadAnimation(this.getContext(), oAppli.getAndroidIdByStringKey(ApplicationRGroup.ANIM, "mmaccordionlayout_topbottom_out"));
			this.foldContentAnimation.setDuration(this.duration);

			this.foldContentReverseAnimation = AnimationUtils.loadAnimation(this.getContext(), oAppli.getAndroidIdByStringKey(ApplicationRGroup.ANIM, "mmaccordionlayout_bottomtop_out"));
			this.foldContentReverseAnimation.setDuration(this.duration);

			this.unfoldContentAnimation = AnimationUtils.loadAnimation(this.getContext(), oAppli.getAndroidIdByStringKey(ApplicationRGroup.ANIM, "mmaccordionlayout_topbottom_in"));
			this.unfoldContentAnimation.setDuration(this.duration);

			this.unfoldContentReverseAnimation = AnimationUtils.loadAnimation(this.getContext(), oAppli.getAndroidIdByStringKey(ApplicationRGroup.ANIM, "mmaccordionlayout_bottomtop_in"));
			this.unfoldContentReverseAnimation.setDuration(this.duration);

			String sDirection = p_oAttrs.getAttributeValue(AndroidApplication.MOVALYSXMLNS, ATTR_ORIENTATION);
			if (sDirection == null) {
				this.orientation = DEFAULT_ORIENTATION;
			}
			else {
				this.orientation = AccordionOrientation.valueOf(sDirection);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if(!isInEditMode()) {
			this.initDisplay(0);
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
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<NoneType> getComponentDelegate() {
		return this.aivDelegate;
	}

	/**
	 * Initialize display (reset all animation and views)
	 * @param p_iInitialPosition the initiale value to begin init
	 */
	private void initDisplay(int p_iInitialPosition) {
		this.handleAnimations = new Animation[this.handles.length];
		this.handleReverseAnimations = new Animation[this.handles.length];

		View oHandle = null;
		View oContent = null;
		for (int i=0; i <this.handles.length; i++) {
			oHandle = this.findViewById(this.handles[i]);
			oContent = this.findViewById(this.contents[i]);
			if (oHandle == null) {
				throw new RuntimeException();
			}

			oHandle.setOnTouchListener(this);

			if(i == p_iInitialPosition){
				oContent.setVisibility(View.VISIBLE);
			}
			else {
				oContent.setVisibility(View.GONE);
			}
			
			if (i == p_iInitialPosition + 1) {
				((LayoutParams) oHandle.getLayoutParams()).addRule(RelativeLayout.BELOW, this.contents[p_iInitialPosition]);
			}
			else if (i > 0) {
				((LayoutParams) oHandle.getLayoutParams()).addRule(RelativeLayout.BELOW, this.handles[i-1]);
			}
			this.current = p_iInitialPosition;
		}
	}

	@Override
	public boolean onTouch(View p_oView, MotionEvent p_oEvent) {
		boolean r_bResult = !this.lock;

		if (!this.lock) {
			int iPosition = 0;
			for (; iPosition < this.handles.length; iPosition++) {
				if (this.handles[iPosition] == p_oView.getId()) {
					break;
				}
			}

			switch (p_oEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				this.lastMotionX = p_oEvent.getX();
				this.lastMotionY = p_oEvent.getY();
				p_oView.setPressed(true);
				break;
			case MotionEvent.ACTION_UP:
				int positionToToggle = iPosition;
				if (this.isVerticalOrientation()) {
					float fDeltaY = p_oEvent.getY() - this.lastMotionY;
					if (Math.abs(fDeltaY) > DISTANCE_TOUCH_SLOP && fDeltaY > 0) {
						positionToToggle--;
					}
				}
				else {
					float fDeltaX = p_oEvent.getX() - this.lastMotionX;
					if (Math.abs(fDeltaX) > DISTANCE_TOUCH_SLOP && fDeltaX > 0) {
						positionToToggle--;
					}
				}
				this.toggle(positionToToggle);
				p_oView.setPressed(false);
				break;
			default:
				// NOTHING TO DO.
			}
		}
		return r_bResult;
	}

	/**
	 * Display or hide the content of a specific child of this accordion.
	 * @param p_iPosition The child's position.
	 */
	public void toggle(final int p_iPosition) {
		if (p_iPosition != this.current && p_iPosition >=0 && p_iPosition < this.handles.length) {
			boolean bUnhide = false;
			View oChild = null;

			final View oCurrentContent = this.findViewById(this.contents[this.current]);
			oCurrentContent.clearAnimation();

			Animation oAnimationOut = null;
			Animation oAnimationIn = null;
			if (p_iPosition > this.current) {
				oAnimationIn = this.unfoldContentReverseAnimation;
				oAnimationOut = this.foldContentReverseAnimation;

				for (int i = p_iPosition; i > this.current; i--) {
					bUnhide = true;
					oChild = this.findViewById(this.handles[i]);
					if (this.handleAnimations[i] != null) {
						oChild.clearAnimation();
						oChild.startAnimation(this.handleAnimations[i]);
					}
				}
			}
			else if (p_iPosition < this.current) {
				oAnimationIn = this.unfoldContentAnimation;
				oAnimationOut = this.foldContentAnimation;

				for (int i = p_iPosition + 1; i <= this.current; i++) {
					bUnhide = true;
					oChild = this.findViewById(this.handles[i]);
					if (this.handleAnimations[i] != null) {
						oChild.clearAnimation();
						oChild.startAnimation(this.handleReverseAnimations[i]);
					}
				}
			}

			if (this.current < this.handles.length - 1) {
				((LayoutParams) this.findViewById(this.handles[this.current + 1]).getLayoutParams()).addRule(RelativeLayout.BELOW, this.handles[this.current]);
			}

			if (p_iPosition < this.handles.length - 1) {
				((LayoutParams) this.findViewById(this.handles[p_iPosition + 1]).getLayoutParams()).addRule(RelativeLayout.BELOW, this.contents[p_iPosition]);
			}

			if (p_iPosition > 0) {
				((LayoutParams) this.findViewById(this.contents[p_iPosition]).getLayoutParams()).addRule(RelativeLayout.BELOW, this.handles[p_iPosition - 1]);
			}

			oChild = this.findViewById(this.contents[p_iPosition]);
			oChild.clearAnimation();
			oChild.setVisibility(View.VISIBLE);
			if (oAnimationIn != null) {
				oChild.startAnimation(oAnimationIn);
			}

			if (oAnimationOut != null) {
				oCurrentContent.startAnimation(oAnimationOut);
			}

			if (bUnhide && this.listener != null) {
				this.listener.onHide(oCurrentContent);
				this.listener.onUnHide(oChild);
			}

			oCurrentContent.setVisibility(View.GONE);
			this.current = p_iPosition;
		}
	}

	/**
	 * Lock the component
	 */
	public void lock() {
		this.lock = true;
	}

	/**
	 * Unlock the component
	 */
	public void unlock() {
		this.lock = false;
	}

	@Override
	protected void onMeasure(int p_iWidthMeasureSpec, int p_iHeightMeasureSpec) {
		super.onMeasure(p_iWidthMeasureSpec, p_iHeightMeasureSpec);

		if (this.getChildCount() > 0) {
			// All handles must have the same sizes.
			final int iHandleWidth = this.findViewById(this.handles[0]).getMeasuredWidth();
			final int iHandleHeight = this.findViewById(this.handles[0]).getMeasuredHeight();

			// Content's size. To compute.
			int iContentWidth = 0;
			int iContentHeight = 0;

			// Used by handles' animations
			float fFromX	= 0.0f;
			float fFromY	= 0.0f;

			if (this.isVerticalOrientation()) {
				iContentWidth = this.getMeasuredWidth();
				iContentHeight = this.getMeasuredHeight() - iHandleHeight * this.handles.length;

				// Used by handles' animations
				fFromY = ((float) iContentHeight) / ((float) this.getMeasuredHeight());

				if (this.handleAlignToContent) {

					iContentHeight += iHandleHeight;
				}
			}
			else {
				iContentWidth = this.getMeasuredWidth() - iHandleWidth * this.handles.length;
				iContentHeight = this.getMeasuredHeight();

				// Used by handles' animations
				fFromX = ((float) iContentWidth) / ((float) this.getMeasuredWidth());

				if (this.handleAlignToContent) {
					// All handles must have the same sizes.
					iContentWidth += iHandleWidth;
				}
			}

			for (int i=1; i < this.handles.length; i++) {
				this.handleAnimations[i] = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, fFromX,
						Animation.RELATIVE_TO_PARENT, 0,
						Animation.RELATIVE_TO_PARENT, fFromY,
						Animation.RELATIVE_TO_PARENT, 0);

				this.handleReverseAnimations[i] = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1 * fFromX,
						Animation.RELATIVE_TO_PARENT, 0,
						Animation.RELATIVE_TO_PARENT, -1 * fFromY,
						Animation.RELATIVE_TO_PARENT, 0);

				this.handleAnimations[i].setDuration(this.duration);
				this.handleReverseAnimations[i].setDuration(this.duration);
			}

			View oChild = null;
			for (int i=0; i < this.contents.length; i++) {
				oChild = this.findViewById(this.contents[i]);
				((LayoutParams) oChild.getLayoutParams()).width = iContentWidth;
				((LayoutParams) oChild.getLayoutParams()).height = iContentHeight;
			}

			super.onMeasure(p_iWidthMeasureSpec, p_iHeightMeasureSpec);
		}
	}

	/**
	 * Check if the orientation is vertical
	 * @return true if the orientation is vertical, false otherwise
	 */
	private boolean isVerticalOrientation() {
		return this.orientation == AccordionOrientation.vertical;
	}

	/**
	 * Set the toggle listener
	 * @param p_oListener the new toggle listener
	 */
	public void setOnToggleListener(OnToggleListener p_oListener) {
		this.listener = p_oListener;
		if (this.listener != null) {
			for (int i=0; i < this.contents.length; i++) {
				if (this.current == i) {
					this.listener.onUnHide(this.findViewById(this.contents[i]));
				}
				else {
					this.listener.onHide(this.findViewById(this.contents[i]));
				}
			}
		}
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		return super.onSaveInstanceState();
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		SavedState oState = new SavedState(super.onSaveInstanceState());
		return this.aivFwkDelegate.onSaveInstanceState(oState);
	}
	/**
	 * {@inheritDoc}
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		// Etat géré par le délégué
		this.aivFwkDelegate.onRestoreInstanceState(p_oState);

		// Etat du MMAccrodionLayout
		SavedState oLocalState = (SavedState) ((BaseSavedState) p_oState).getSuperState();
		if (oLocalState.current > 0) {
			this.initDisplay(oLocalState.current);
		}

		// Etat sauvegardé par la classe mère
		superOnRestoreInstanceState(oLocalState.getSuperState());
	}

	/**
	 * Save object on view rotation (or going in background)
	 * @see AndroidConfigurableVisualComponentSavedState
	 */
	protected class SavedState extends AndroidConfigurableVisualComponentSavedState {
		/** Sauvegarde de la position de l'élément affiché. */
		private int current;

		/**
		 * Internal constructor
		 * @param p_oSuperState Parcelable to build on
		 */
		SavedState(Parcelable p_oSuperState) {
			super(p_oSuperState);
			this.current = MMAccordionLayout.this.current;
		}
	}

	/**
	 * <p>TODO Décrire la classe OnToggleListener</p>
	 *
	 *
	 *
	 */
	public interface OnToggleListener {
		/**
		 * Call when the accordion hides a view.
		 * @param p_oView view becoming invisible
		 */
		public void onHide(final View p_oView);

		/**
		 * Call when a view becomes visible.
		 * @param p_oView view becoming visible
		 */
		public void onUnHide(final View p_oView);
	}
	
	/************************************************************************************************************
	 ************************************ Framework delegate callback *******************************************
	 ************************************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentBindDestroy#doOnPostAutoBind()
	 */
	@Override
	public void doOnPostAutoBind() {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentBindDestroy#destroy()
	 */
	@Override
	public void destroy() {
		this.clearAnimation();
		for (int i = 0; i < this.getChildCount(); i++) {
			this.getChildAt(i).clearAnimation();
		}
	}
}
