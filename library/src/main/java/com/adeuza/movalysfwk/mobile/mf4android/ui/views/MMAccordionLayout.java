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
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentSavedState;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;

/**
 * <p>TODO Décrire la classe MMAccordionLayout</p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 */

public class MMAccordionLayout extends MMRelativeLayout implements OnTouchListener, InstanceStatable {

	/**
	 * 
	 * <p>Enumeration of allowed accordion's direction</p>
	 *
	 * <p>Copyright (c) 2012
	 * <p>Company: Adeuza
	 *
	 * @author emalespine
	 *
	 */
	public static enum AccordionOrientation {
		/** from top to down and from down to top. */
		vertical,

		/** from left to right and from right to left. */
		horizontal
	}


	private static final int DEFAULT_CONTENT_COUNT = 0;

	private static final int NO_RESOURCE = -1;

	private static final int DEFAULT_DURATION = 700;

	private static final AccordionOrientation DEFAULT_ORIENTATION = AccordionOrientation.vertical;

	private static final String ATTR_CONTENT_COUNT = "content-count";

	private static final String ATTR_HANDLE_PREFIX = "handle";

	private static final String ATTR_CONTENT_PREFIX = "content";

	private static final String ATTR_ANIMATION_DURATION = "anim-duration";

	private static final String ATTR_ORIENTATION = "orientation";

	/**
	 * Padding of the intervention detail pannel
	 */
	private static final int DISTANCE_TOUCH_SLOP = 32;

	private int duration;

	private AccordionOrientation orientation;

	/** Position of visible child. */
	private int current;

	private OnToggleListener listener;

	private float lastMotionX = -1.0f;
	private float lastMotionY = -1.0f;

	private int[] handles;

	private int[] contents;

	private Animation foldContentAnimation;

	private Animation foldContentReverseAnimation;

	private Animation unfoldContentAnimation;

	private Animation unfoldContentReverseAnimation;

	private Animation[] handleAnimations;

	private Animation[] handleReverseAnimations;

	private boolean handleAlignToContent = true;

	/**
	 * Disabled navigation.
	 */
	private boolean lock = false;

	/**
	 * Constructs a new MMRelativeLayout
	 * @param p_oContext the context to use
	 * @see RelativeLayout#RelativeLayout(Context)
	 */
	public MMAccordionLayout(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.duration = DEFAULT_DURATION;
			this.orientation = DEFAULT_ORIENTATION;
		}
	}

	/**
	 * Constructs a new MMRelativeLayout
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see RelativeLayout#RelativeLayout(Context, AttributeSet)
	 */
	public MMAccordionLayout(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
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
				if (this.orientation == null) {
					this.orientation = DEFAULT_ORIENTATION;
				}
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

	public void lock() {
		this.lock = true;
	}

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

	private boolean isVerticalOrientation() {
		return this.orientation == AccordionOrientation.vertical;
	}

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
	public void destroy() {
		this.handleAnimations = null;
		this.handleReverseAnimations = null;

		for (int i = 0; i < this.getChildCount(); i++) {
			this.getChildAt(i).clearAnimation();
		}

		super.destroy();
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
		return this.aivDelegate.onSaveInstanceState(oState);
	}
	/**
	 * {@inheritDoc}
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		// Etat géré par le délégué
		this.aivDelegate.onRestoreInstanceState(p_oState);

		// Etat du MMAccrodionLayout
		SavedState oLocalState = (SavedState) ((BaseSavedState) p_oState).getSuperState();
		if (oLocalState.current > 0)
			this.initDisplay(oLocalState.current);

		// Etat sauvegardé par la classe mère
		superOnRestoreInstanceState(oLocalState.getSuperState());
	}

	protected class SavedState extends AndroidConfigurableVisualComponentSavedState {
		/** Sauvegarde de la position de l'élément affiché. */
		private int current;

		/**
		 * Internal constructor
		 * @param p_oSuperState
		 */
		SavedState(Parcelable p_oSuperState) {
			super(p_oSuperState);
			this.current = MMAccordionLayout.this.current;
		}
	}

	/**
	 * <p>TODO Décrire la classe OnToggleListener</p>
	 *
	 * <p>Copyright (c) 2012
	 * <p>Company: Adeuza
	 *
	 * @author emalespine
	 *
	 */
	public interface OnToggleListener {
		/**
		 * Call when the accordion hides a view.
		 * @param p_oView
		 */
		public void onHide(final View p_oView);

		/**
		 * Call when a view becomes visible.
		 * @param p_oView
		 */
		public void onUnHide(final View p_oView);
	}
}
