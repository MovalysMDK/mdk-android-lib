package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentSavedState;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * The workspace is a wide area with a infinite number of screens. Each screen contains a view. 
 * <br/>
 * This code has been done by using com.android.launcher.Workspace.java
 */
public class MMPagedView extends ViewGroup implements ConfigurableVisualComponent<NoneType>, MMIdentifiableView, OnTouchListener, InstanceStatable {
	/**
	 * Attribute used to define page's columns number.
	 */
	private static final String ATTR_VISIBLE_COLUMNS_COUNT = "visible-columns";

	/**
	 * Count of visible columns by default.
	 */
	private static final int DEFAULT_VISIBLE_COLUMN_COUNT = 1;

	/** First page's position. */
	private static final int FIRST_PAGE = 0;

	/**
	 * Padding of the intervention detail pannel
	 */
	private static final int DISTANCE_TOUCH_SLOP = 32;

	/**
	 * Left padding of the intervention detail pannel used to delimit the planning
	 */
	private static final int DEFAULT_MARGIN = 0;

	private static final int DEFAULT_PREVIEW_SIZE = 10;

	/** Flag to allow velocity computing per second (see {@link VelocityTracker}) */
	private static final int VELOCITY_COMPUTED_PER_SECOND = 1000;

	/** the invalid pointer flag */
	private static final int INVALID_POINTER = -1;

	/** the current pointer */
	private int mActivePointerId = INVALID_POINTER;

	/** invalid screen flag */
	private static final int INVALID_SCREEN = -1;

	/** The velocity at which a fling gesture will cause us to snap to the next screen */
	private static final int SNAP_VELOCITY = 500;

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<NoneType> aivDelegate = null;

	// XML Configurable attributes

	private int marginBetweenVisibleColumns;

	private int previousPreviewSize;

	private int nextPreviewSize;

	private int visibleColumns;

	/** The current screen index */
	private int currentScreen;

	/** The next screen index */
	private int nextScreen = INVALID_SCREEN;

	/** The scroller which scroll each view */
	private Scroller scroller;

	/** A tracker which to calculate the velocity of a mouvement */
	private VelocityTracker mVelocityTracker;

	/** The last known values of X position of the gesture */
	private float lastMotionX;

	/** The minimal distance of a touch slop */
	private int touchSlop;

	/** An internal flag to reset long press when user is scrolling */
	private boolean allowLongPress;

	/** A flag to know if touch event have to be ignored. Used also in internal */
	private boolean locked;

	/** Maximum velocity to initiate a fling, as measured in pixels per second. */
	private int mMaximumVelocity;

	private boolean mIsBeingDragged;

	private OnPageChangeListener listener;

	/**
	 * Used to contruct an empty Workspace.
	 * 
	 * @param p_oContext
	 *            The application's context.
	 */
	public MMPagedView(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
			this.visibleColumns = DEFAULT_VISIBLE_COLUMN_COUNT;
		}
	}

	/**
	 * Used to inflate the Workspace from XML.
	 * 
	 * @param p_oContext
	 *            The application's context.
	 * @param p_oAttrs
	 *            The attribtues set containing the Workspace's customization values.
	 */
	public MMPagedView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.init(p_oAttrs);
		}
	}

	/**
	 * Used to inflate the Workspace from XML.
	 * 
	 * @param p_oContext
	 *            The application's context.
	 * @param p_oAttrs
	 *            The attribtues set containing the Workspace's customization values.
	 * @param p_iDefStyle
	 *            Unused.
	 */
	public MMPagedView(Context p_oContext, AttributeSet p_oAttrs, int p_iDefStyle) {
		super(p_oContext, p_oAttrs, p_iDefStyle);
		if(!isInEditMode()) {
			this.init(p_oAttrs);
		}
	}

	protected final void init(final AttributeSet p_oAttrs) {
		this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);

		this.visibleColumns = p_oAttrs.getAttributeIntValue(AndroidApplication.MOVALYSXMLNS, ATTR_VISIBLE_COLUMNS_COUNT, DEFAULT_VISIBLE_COLUMN_COUNT);

		float fDensity = this.getContext().getResources().getDisplayMetrics().density;

		this.marginBetweenVisibleColumns = Math.round(fDensity * p_oAttrs.getAttributeIntValue(AndroidApplication.MOVALYSXMLNS, "margin-column", DEFAULT_MARGIN));

		this.previousPreviewSize = Math.round(fDensity * p_oAttrs.getAttributeIntValue(AndroidApplication.MOVALYSXMLNS, "previous-preview-size", DEFAULT_PREVIEW_SIZE));
		this.nextPreviewSize = Math.round(fDensity * p_oAttrs.getAttributeIntValue(AndroidApplication.MOVALYSXMLNS, "next-preview-size", DEFAULT_PREVIEW_SIZE));

		this.scroller = new Scroller(getContext());
		this.currentScreen = FIRST_PAGE;

		// Does this do anything for me?
		final ViewConfiguration configuration = ViewConfiguration.get(getContext());
		this.touchSlop = configuration.getScaledTouchSlop();
		this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

		this.setTouchSlop(DISTANCE_TOUCH_SLOP);
		this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		this.setHorizontalFadingEdgeEnabled(this.previousPreviewSize > 0 || this.nextPreviewSize > 0);
		this.setWillNotDraw(this.previousPreviewSize <= 0 && this.nextPreviewSize <= 0);
	}

	@Override
	protected float getLeftFadingEdgeStrength() {
		if(this.isFirstPage()){
			return 0.0F;
		}
		else {
			return this.previousPreviewSize;
		}
	}

	@Override
	protected float getRightFadingEdgeStrength() {
		if(this.isLastPage()){
			return  0.0F;
		}
		else {
			return this.nextPreviewSize;
		}
	}

	@Override
	public int getSolidColor() {
		return Color.rgb(0, 0, 0);
	}

	/**
	 * Measure the workspace AND also children {@inheritDoc}
	 */
	@Override
	protected void onMeasure(int p_iWidthMeasureSpec, int p_iHeightMeasureSpec) {
		final int iCount = getChildCount();
		final int iWidth = MeasureSpec.getSize(p_iWidthMeasureSpec);

		int iMaxHeight = 0;
		int iChildWidth = 0;
		View oChild = null;
		for (int i = 0; i < iCount; i++) {
			oChild = this.getChildAt(i);
			iChildWidth = this.measureChildWidth(iWidth, i < this.visibleColumns, i > iCount - 1 - this.visibleColumns);
			oChild.measure(MeasureSpec.makeMeasureSpec(iChildWidth, MeasureSpec.EXACTLY), p_iHeightMeasureSpec);
			iMaxHeight = Math.max(oChild.getMeasuredHeight(), iMaxHeight);
		}

		this.setMeasuredDimension(iWidth, iMaxHeight);
	}

	protected int measureChildWidth(int p_iWidth, boolean p_bFirstScreen, boolean p_bLastScreen) {
		int r_iChildWidth = 0;

		// Only one page
		if ((p_bFirstScreen && p_bLastScreen)) {
			r_iChildWidth = p_iWidth - (this.visibleColumns - 1) * this.marginBetweenVisibleColumns;
		}
//		// On the first page.
//		else if (p_bFirstScreen) {
//			r_iChildWidth = p_iWidth - this.visibleColumns * this.marginBetweenVisibleColumns - this.nextPreviewSize;
//		}
//		// On the last page.
//		else if (p_bLastScreen) {
//			r_iChildWidth = p_iWidth - this.visibleColumns * this.marginBetweenVisibleColumns - this.previousPreviewSize;
//		}
		// Others
		else {
			r_iChildWidth = p_iWidth - (this.visibleColumns + 1) * this.marginBetweenVisibleColumns - this.previousPreviewSize - this.nextPreviewSize;
		}

		return r_iChildWidth / this.visibleColumns;
	}

	@Override
	protected void onLayout(boolean p_bChanged, int p_iLeft, int p_iTop, int p_iRight, int p_iBottom) {
		final int iChildCount = this.getChildCount();
		if (iChildCount > 0) {
			View oChild = null;
			int iLeft = this.previousPreviewSize; //0;
			for (int i=0; i < iChildCount; i++) {
				oChild = this.getChildAt(i);
				oChild.layout(iLeft, 0, iLeft + oChild.getMeasuredWidth(), oChild.getMeasuredHeight());
				iLeft += oChild.getMeasuredWidth() + this.marginBetweenVisibleColumns;
			}
		}
	}

	/**
	 * Set a new distance that a touch can wander before we think the user is scrolling in pixels slop<br/>
	 * 
	 * @param p_iTouchSlopP
	 *            the distance
	 */
	public final void setTouchSlop(int p_iTouchSlopP) {
		touchSlop = p_iTouchSlopP;
	}

	/**
	 * Returns the index of the currently displayed screen.
	 * 
	 * @return The index of the currently displayed screen.
	 */
	protected int getCurrentScreen() {
		return this.currentScreen;
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
		} else if (nextScreen != INVALID_SCREEN) {
			currentScreen = Math.max(0, Math.min(nextScreen, this.getPageCount() - 1));
			nextScreen = INVALID_SCREEN;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.view.ViewGroup#dispatchUnhandledMove(android.view.View, int)
	 */
	@Override
	public boolean dispatchUnhandledMove(View p_oViewFocused, int p_iDirection) {
		boolean r_bResult = false ;
		if (p_iDirection == View.FOCUS_LEFT) {
			if (getCurrentScreen() > 0) {
				scrollToScreen(getCurrentScreen() - 1);
				r_bResult = true;
			}
		} else if (p_iDirection == View.FOCUS_RIGHT) {
			if (getCurrentScreen() < this.getPageCount() - 1) {
				scrollToScreen(getCurrentScreen() + 1);
				r_bResult = true;
			}
		}
		if ( !r_bResult ){
			r_bResult = super.dispatchUnhandledMove(p_oViewFocused, p_iDirection); 
		}
		return r_bResult ;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent p_oEvt) {
		final int action = p_oEvt.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
			return true;
		}

		final float x = p_oEvt.getX();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(x - this.lastMotionX);
			if (xDiff > touchSlop) {
				mIsBeingDragged = true;
			}
			break;

		case MotionEvent.ACTION_DOWN:
			this.lastMotionX = x;
			this.mActivePointerId = p_oEvt.getPointerId(0);
			this.mIsBeingDragged = !this.scroller.isFinished();
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			/* Release the drag */
			this.mIsBeingDragged = false;
			this.mActivePointerId = INVALID_POINTER;
			break;
		default:
			throw new MobileFwkException("onInterceptTouchEvent", "unknown action type");
		}
		return mIsBeingDragged;
	}

	/**
	 * 
	 * Handle Motion Events for the secondary pointer
	 * 
	 * @param p_oEvt
	 */
	private void onSecondaryPointerUp(MotionEvent p_oEvt) {
		final int iPointerIndex = (p_oEvt.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		final int iPointerId = p_oEvt.getPointerId(iPointerIndex);
		if (iPointerId == mActivePointerId) {
			// This was our active pointer going up. Choose a new
			// active pointer and adjust accordingly.
			// A2A_DEV : Make this decision more intelligent.
			int iNewPointerIndex = 0 ;
			if (iPointerIndex == 0 ) {
				iNewPointerIndex = 1 ;
			}
			lastMotionX = p_oEvt.getX(iNewPointerIndex);
			mActivePointerId = p_oEvt.getPointerId(iNewPointerIndex);
			if (mVelocityTracker != null) {
				mVelocityTracker.clear();
			}
		}
	}

	/**
	 * Track the touch event {@inheritDoc}
	 */
	@Override
	public boolean onTouchEvent(MotionEvent p_oEvt) {
		//Log.e("MMPagedView", "List.onTouchEvent / " + p_oEvt);
		// LoggerHandler.getLog().debug("workspace","caught a touch event");
		if (locked) {
			return true;
		}
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(p_oEvt);

		final int oAction = p_oEvt.getAction();
		final float x = p_oEvt.getX();

		switch (oAction) {
		case MotionEvent.ACTION_DOWN:
			// We can still get here even if we returned false from the intercept function.
			// That's the only way we can get a TOUCH_STATE_REST (0) here.
			// That means that our child hasn't handled the event, so we need to
			// LoggerHandler.getLog().debug("workspace","caught a down touch event and touchstate =" + touchState);

			/*
			 * If being flinged and user touches, stop the fling. isFinished will be false if being flinged.
			 */
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}
			// Remember where the motion event started
			lastMotionX = x;
			mActivePointerId = p_oEvt.getPointerId(0);

			break;
		case MotionEvent.ACTION_MOVE:
			this.handleScrollMove(p_oEvt);
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(VELOCITY_COMPUTED_PER_SECOND, mMaximumVelocity);
			int iVelocityX = (int) velocityTracker.getXVelocity();

			if (iVelocityX > SNAP_VELOCITY && currentScreen > 0) {
				// Fling hard enough to move left
				scrollToScreen(currentScreen - 1);
			} else if (iVelocityX < -SNAP_VELOCITY && currentScreen < this.getPageCount() - 1) {
				// Fling hard enough to move right
				scrollToScreen(currentScreen + 1);
			} else {
				snapToDestination();
			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			mActivePointerId = INVALID_POINTER;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			Application.getInstance().getLog().debug("workspace", "caught a pointer up touch event");
			onSecondaryPointerUp(p_oEvt);
			break;
		default:
			Application.getInstance().getLog().debug("workspace", "default");
		}
		return true;
	}

	/**
	 * Handle the scrool motion event
	 * @param current
	 *            Motion Event
	 */
	private void handleScrollMove(MotionEvent p_oEv) {
		// Scroll to follow the motion event
		final int pointerIndex = p_oEv.findPointerIndex(mActivePointerId);
		if (pointerIndex >= 0 && pointerIndex < p_oEv.getPointerCount()) {
			final float x1 = p_oEv.getX(pointerIndex);
			final int deltaX = (int) (lastMotionX - x1);
			lastMotionX = x1;

			if (deltaX < 0) {
				if (getScrollX() > 0) {
					// Scrollby invalidates automatically
					scrollBy(Math.max(-getScrollX(), deltaX), 0);
				}
			} else if (deltaX > 0) {
				final int availableToScroll = getChildAt(getChildCount() - 1).getRight() - getScrollX() - getWidth();
				if (availableToScroll > 0) {
					// Scrollby invalidates automatically
					scrollBy(Math.min(availableToScroll, deltaX), 0);
				}
			} else {
				awakenScrollBars();
			}
		}
	}

	@Override
	public void addView(View p_oChild) {
		super.addView(p_oChild);
		p_oChild.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View p_oChild, MotionEvent p_oEvent) {
		switch (p_oEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			p_oChild.onTouchEvent(p_oEvent);
			return false;
		default:
			return p_oChild.onTouchEvent(p_oEvent);
		}
	}

	/**
	 * Scroll to the appropriated screen depending of the current position
	 */
	private void snapToDestination() {
		final int screenWidth = getWidth();
		final int whichScreen = (getScrollX() + (screenWidth / 2)) / screenWidth;
		//LoggerHandler.getLog().debug("workspace", StringUtils.concat("snapToDestination", String.valueOf(whichScreen)));
		scrollToScreen(whichScreen);
	}

	/**
	 * Scroll to a specific screen
	 * 
	 * @param p_iWhichScreen
	 */
	public void scrollToScreen(int p_iWhichScreen) {
		scrollToScreen(p_iWhichScreen, false);
	}

	/**
	 * scroll to screen passed in parameter
	 * 
	 * @param p_iWhichScreen
	 *            the screen index
	 * @param p_bImmediate
	 *            if the action have to be done immediately
	 */
	private void scrollToScreen(int p_iWhichScreen, boolean p_bImmediate) {

		boolean bChangingScreens = p_iWhichScreen != currentScreen;

		this.nextScreen = p_iWhichScreen;

		View oViewFocusedChild = getFocusedChild();
		if (oViewFocusedChild != null && bChangingScreens && oViewFocusedChild == getChildAt(currentScreen)) {
			oViewFocusedChild.clearFocus();
		}

		int iStartX = 0, iDX = 0, iMmediate = 0;
		iStartX = this.getScrollX();
		iDX = this.computeDeltaScroll(p_iWhichScreen) - this.getScrollX();
		iMmediate = iDX;

		if (!p_bImmediate) {
			iMmediate = Math.abs(iMmediate) * 2 ;
		}

		this.scroller.startScroll(iStartX, 0, iDX, 0, iMmediate );
		invalidate();

		if (this.listener != null) {
			this.listener.onPageSelected(this, p_iWhichScreen);
		}
	}

	private int computeDeltaScroll(int p_iPage) {
		return (this.getWidth() - this.previousPreviewSize - this.nextPreviewSize - this.marginBetweenVisibleColumns) * p_iPage;
	}

	/**
	 * Unlocks the SlidingDrawer so that touch events are processed.
	 * 
	 * @see #lock()
	 */
	public void unlock() {
		locked = false;
	}

	/**
	 * Locks the SlidingDrawer so that touch events are ignores.
	 * 
	 * @see #unlock()
	 */
	public void lock() {
		locked = true;
	}

	/**
	 * is this instance allow long press event
	 * 
	 * @return True is long presses are still allowed for the current touch
	 */
	public boolean allowLongPress() {
		return allowLongPress;
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
		SavedState oState = new MMPagedView.SavedState(super.onSaveInstanceState(), this.currentScreen);
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

		// Etat du MMWorkspaceView
		MMPagedView.SavedState oState = (MMPagedView.SavedState) ((BaseSavedState) p_oState).getSuperState();
		this.currentScreen = oState.getCurrentColumn();

		// Etat sauvegardé par la classe mère
		superOnRestoreInstanceState(oState.getSuperState());
	}

	public void setOnChangePageListener(OnPageChangeListener p_oListener) {
		this.listener = p_oListener;
	}

	protected void refresh() {
		if (this.listener != null) {
			this.listener.onPageSelected(this, this.currentScreen);
		}
	}

	// ========================= INNER CLASSES ==============================

	/**
	 * A SavedState which save and load the current screen
	 */
	public class SavedState extends AndroidConfigurableVisualComponentSavedState {
		private int currentColumn;

		/**
		 * Internal constructor
		 * 
		 * @param p_oSuperState
		 */
		SavedState(Parcelable p_oSuperState, int p_iCurrentScreen) {
			super(p_oSuperState);
			this.currentColumn = MMPagedView.this.currentScreen;
		}

		/**
		 * Private constructor
		 * 
		 * @param p_oIn
		 */
		private SavedState(Parcel p_oIn) {
			super(p_oIn);
			this.currentColumn = p_oIn.readInt();
		}

		/**
		 * Save the current screen
		 */
		@Override
		public void writeToParcel(Parcel p_oOut, int p_iFlags) {
			super.writeToParcel(p_oOut, p_iFlags);
			p_oOut.writeInt(this.currentColumn);
		}

		public int getCurrentColumn() {
			return this.currentColumn;
		}
	}

	public interface OnPageChangeListener {
		public void onPageSelected(MMPagedView p_oView, int p_iPosition);
	}

	// /////////////////
	// ZONE DELEGATE //
	// /////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivDelegate.setId(p_oId);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VisualComponentConfiguration getConfiguration() {
		return this.aivDelegate.getConfiguration();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isGroup() {
		return this.aivDelegate.isGroup();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationHide(p_bLockModifier);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationUnHide(p_bLockModifier);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(NoneType p_oObject) {
		this.aivDelegate.configurationSetValue(p_oObject);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NoneType configurationGetValue() {
		return null;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetLabel(String p_sLabel) {
		this.aivDelegate.configurationSetLabel(p_sLabel);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLabel() {
		return this.aivDelegate.isLabel();
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAlwaysEnabled() {
		return true;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValue() {
		return this.aivDelegate.isValue();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEdit() {
		return this.aivDelegate.isEdit();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLocalisation() {
		return this.aivDelegate.getLocalisation();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getModel() {
		return this.aivDelegate.getModel();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.aivDelegate.getDescriptor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		this.aivDelegate.setDescriptor(p_oDescriptor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPanel() {
		return this.aivDelegate.isPanel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUnknown() {
		return this.aivDelegate.isUnknown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.aivDelegate.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMaster() {
		return this.aivDelegate.isMaster();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetChanged() {
		this.aivDelegate.resetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(NoneType p_oObject) {
		return p_oObject == null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#hide()
	 */
	@Override
	public void hide() {
		this.aivDelegate.hide();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#unHide()
	 */
	@Override
	public void unHide() {
		this.aivDelegate.unHide();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationPrepareHide(java.util.List)
	 */
	@Override
	public void configurationPrepareHide(List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		this.aivDelegate.configurationPrepareHide(p_oComponentsToHide);
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return NoneType.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getConfigurationSetValueMethod()
	 */
	@Override
	public Method getConfigurationSetValueMethod() {
		return this.aivDelegate.getConfigurationSetValueMethod();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		this.aivDelegate.configurationDisabledComponent();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetCustomValues(java.lang.String[])
	 */
	@Override
	public void configurationSetCustomValues(String[] p_t_sValues) {
		this.aivDelegate.configurationSetCustomValues(p_t_sValues);

	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationGetCustomValues()
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return this.aivDelegate.configurationGetCustomValues();
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyCustomValues(java.lang.String[])
	 */
	@Override
	public boolean isNullOrEmptyCustomValues(String[] p_oObjects) {
		return this.aivDelegate.isNullOrEmptyCustomValues(p_oObjects);
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.aivDelegate.isFilled();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return this.isShown();
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		this.aivDelegate.configurationSetMandatoryLabel();
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		this.aivDelegate.configurationRemoveMandatoryLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnPostAutoBind() {
		//Nothing to do
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		//		this.clearWallpaper();
	}

	@Override
	public void configurationUnsetError() {
		this.aivDelegate.configurationUnsetError();
	}

	@Override
	public void configurationSetError(String p_oError) {
		this.aivDelegate.configurationSetError(p_oError);
	}

	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String p_sNewName) {
		this.aivDelegate.setName(p_sNewName);
	}

	protected boolean isFirstPage() {
		return this.currentScreen == 0;
	}

	protected boolean isLastPage() {
		return this.currentScreen == this.getPageCount() - 1;
	}

	protected int getPageCount() {
		return (this.getChildCount() + this.visibleColumns - 1) / this.visibleColumns;
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	public void registerVM(ViewModel p_oViewModel) {
		this.aivDelegate.registerVM(p_oViewModel);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#unregisterVM()
	 */
	@Override
	public void unregisterVM() {
		this.aivDelegate.unregisterVM();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomConverter(p_oCustomConverter,p_oAttributeSet);		
	}

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		this.aivDelegate.setCustomFormatter(p_oFormatter);
	}
	
	@Override
	public CustomFormatter customFormatter() {
		return this.aivDelegate.customFormatter();
	}
	
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFragmentTag() {
		return this.aivDelegate.getFragmentTag();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFragmentTag(String p_sFragmentTag) {
		this.aivDelegate.setFragmentTag(p_sFragmentTag);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasRules() {
		return this.aivDelegate.hasRules();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHasRules(boolean p_bHasRules) {
		this.aivDelegate.setHasRules(p_bHasRules);
	}
}