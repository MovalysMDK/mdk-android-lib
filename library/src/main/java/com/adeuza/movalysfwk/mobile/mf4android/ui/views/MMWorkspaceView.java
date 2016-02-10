package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractWorkspaceMasterDetailMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentSavedState;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * The workspace is a wide area with a infinite number of screens. Each screen contains a view. 
 * <br/>
 * This code has been done by using com.android.launcher.Workspace.java
 */
public class MMWorkspaceView extends ViewGroup implements ConfigurableVisualComponent<NoneType>, MMIdentifiableView, InstanceStatable {

	private static final String TAG = "MMWorkspaceView";
	/** Flag to allow velocity computing per second (see {@link VelocityTracker}) */
	private static final int VELOCITY_COMPUTED_PER_SECOND = 1000;
	/** tab indicator border radius */
	private static final int TAB_INDICATOR_RADIUS = 2;
	/** tab indicator background radius */
	private static final int TAB_INDICATOR_BACKGROUND_RADIUS = 0;
	/** wallpaper default offset */
	private static final float WALLPAPER_DEFAULT_OFFSET = 1.0f;
	/** just to compute percentage */
	private static final int CENT = 100;
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<NoneType> aivDelegate = null;
	/** the invalid pointer flag */
	private static final int INVALID_POINTER = -1;
	/** the current pointer */
	private int mActivePointerId = INVALID_POINTER;
	/** invalid screen flag */
	private static final int INVALID_SCREEN = -1;

	/** The velocity at which a fling gesture will cause us to snap to the next screen */
	private static final int SNAP_VELOCITY = 500;

	private static final int HEXA_RADIX = 16 ;
	/** the default screen index */
	private int defaultScreen;
	/** The current screen index */
	private int currentScreen;
	/** the Id of the first column of the currentscreent (to deal with screen orientation change)**/
	private int currentColumnId;

	/** The next screen index */
	private int nextScreen = INVALID_SCREEN;
	/** Wallpaper properties */
	private Bitmap wallpaper;
	/** the paint */
	private Paint paint;
	/** wallpaper caracteristics Width */
	private int wallpaperWidth;
	/** wallpaper caracteristics Height */
	private int wallpaperHeight;
	/** wallpaper caracteristics Offset */
	private float wallpaperOffset;
	/** wallpaper caracteristics is loaded */
	private boolean wallpaperLoaded;
	/** wallpaper caracteristics is the first one */
	private boolean firstWallpaperLayout = true;

	/** the tab indication height flag */
	private static final int TAB_INDICATOR_HEIGHT_PCT = 1;
	/** the selected tab zone */
	private RectF selectedTab;
	/** the bitmap representation of the tab indicator */
	private Bitmap bitmap;

	/** The scroller which scroll each view */
	private Scroller scroller;
	/** A tracker which to calculate the velocity of a mouvement */
	private VelocityTracker mVelocityTracker;

	/** The last known values of X position of the gesture */
	private float lastMotionX;
	/** The last known values of Y position of the gesture */
	private float lastMotionY;

	/** touch state flags */
	private static final int TOUCH_STATE_REST = 0;
	/** touch state flags */
	private static final int TOUCH_STATE_SCROLLING = 1;

	/** The current touch state */
	private int touchState = TOUCH_STATE_REST;
	/** The minimal distance of a touch slop */
	private int touchSlop;

	/** The width of the component */
	private int width;

	/** An internal flag to reset long press when user is scrolling */
	private boolean allowLongPress;
	/** A flag to know if touch event have to be ignored. Used also in internal */
	private boolean locked;

	/** The interpolator for this workspace to deal with animations changes */
	private WorkspaceOvershootInterpolator mScrollInterpolator;
	/** Maximum velocity to initiate a fling, as measured in pixels per second. */
	private int mMaximumVelocity;
	/** the selected tab paint */
	private Paint selectedTabPaint;
	/** the current canvas for painting */
	private Canvas canvas;
	/** the tab indicator bar */
	private RectF bar;
	/** the tab indicator background */
	private Paint tabIndicatorBackgroundPaint;
	/** this flag is set to true when creationg after orientation change */
	private boolean bOrientationChange = false;

	/** The interpolator Declaration for this workspace to deal with animations changes */
	private static class WorkspaceOvershootInterpolator implements Interpolator {
		/** default tension */
		private static final float DEFAULT_TENSION = 1.3f;
		/** computed tension */
		private float mTension;

		/** default constructor */
		public WorkspaceOvershootInterpolator() {
			mTension = DEFAULT_TENSION;
		}

		/**
		 * change tension value based on distance
		 * 
		 * @param p_iDistance
		 *            the distance
		 */
		@SuppressWarnings("unused")
		public void setDistance(int p_iDistance) {
			if (p_iDistance > 0) {
				mTension = DEFAULT_TENSION / p_iDistance;
			} else {
				mTension = DEFAULT_TENSION;
			}
		}

		/** reset tension */
		@SuppressWarnings("unused")
		public void disableSettle() {
			mTension = 0.f;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see android.view.animation.Interpolator#getInterpolation(float)
		 */
		@Override
		public float getInterpolation(float p_fTension) {
			float r_fTention = p_fTension - WALLPAPER_DEFAULT_OFFSET;
			return r_fTention * r_fTention * ((mTension + 1) * r_fTention + mTension) + WALLPAPER_DEFAULT_OFFSET;
		}
	}

	// A2A_DEV DMA ajouter le calcul du nombre de colonnes pour déterminer si on met le tab scroll en bas (barre rouge) ou si on gagne la place

	/**
	 * Used to contruct an empty Workspace.
	 * 
	 * @param p_oContext
	 *            The application's context.
	 */
	public MMWorkspaceView(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
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
	public MMWorkspaceView(Context p_oContext, AttributeSet p_oAttrs) {
		this(p_oContext, p_oAttrs, 0);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
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
	public MMWorkspaceView(Context p_oContext, AttributeSet p_oAttrs, int p_iDefStyle) {
		super(p_oContext, p_oAttrs, p_iDefStyle);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
			defaultScreen = 0;
			initWorkspace();
		}
	}

	/**
	 * Initializes various states for this workspace.
	 */
	private void initWorkspace() {
		mScrollInterpolator = new WorkspaceOvershootInterpolator();
		scroller = new Scroller(getContext(), mScrollInterpolator);
		currentScreen = defaultScreen;
		//currentColumnId à -1 renverra le workspace su la première colonne
		currentColumnId = -1;

		paint = new Paint();
		paint.setDither(false);

		// Does this do anything for me?
		final ViewConfiguration configuration = ViewConfiguration.get(getContext());
		touchSlop = configuration.getScaledTouchSlop();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

		selectedTabPaint = new Paint();
		selectedTabPaint.setColor(this.getResources().getColor(this.aivDelegate.getAndroidApplication().getAndroidIdByRKey(DefaultApplicationR.red)));
		selectedTabPaint.setStyle(Paint.Style.FILL_AND_STROKE);

		tabIndicatorBackgroundPaint = new Paint();
		tabIndicatorBackgroundPaint.setColor(this.getResources().getColor(this.aivDelegate.getAndroidApplication().getAndroidIdByRKey(DefaultApplicationR.dark_grey)));
		tabIndicatorBackgroundPaint.setStyle(Paint.Style.FILL);
	}


	/**
	 * return the column view ID for the screen (or flip)
	 * @param currentScreen2
	 * @return
	 */
	public int getFirstColumnforScreen(int p_iScreen) {
		int iFirstcolumnId=((ViewGroup)((ViewGroup)this.getChildAt(currentScreen)).getChildAt(0)).getChildAt(0).getId();
		return iFirstcolumnId;
	}

	/**
	 * the first column id of the currrent screen
	 * @return
	 */
	public int getCurrentColumnId(){
		return currentColumnId;
	}

	/**
	 * Set a new distance that a touch can wander before we think the user is scrolling in pixels slop<br/>
	 * 
	 * @param p_iTouchSlopP
	 *            the distance
	 */
	public void setTouchSlop(int p_iTouchSlopP) {
		touchSlop = p_iTouchSlopP;
	}

	public void clearWallpaper() {
		if ( this.wallpaper != null ) {
			this.wallpaper.recycle();
			this.wallpaper = null ;
		}
	}

	/**
	 * Set the background's wallpaper.
	 * 
	 * @param p_oBitmap
	 *            the bitmap to load
	 */
	public void loadWallpaper(Bitmap p_oBitmap) {
		wallpaper = p_oBitmap;
		wallpaperLoaded = true;
		requestLayout();
		invalidate();
	}

	/**
	 * set the width of the component
	 */
	public void setWidth(int p_oWidth){
		width=p_oWidth;
	}
	/**
	 * 
	 * return true if the current screen is the default screen
	 * 
	 * @return true if current screen is the default one
	 */
	boolean isDefaultScreenShowing() {
		return currentScreen == defaultScreen;
	}

	/**
	 * Returns the index of the currently displayed screen.
	 * 
	 * @return The index of the currently displayed screen.
	 */
	protected int getCurrentScreen() {
		return currentScreen;
	}

	/**
	 * compute the index of the current column
	 * @return
	 *  @deprecated use the column id to interact with MMWorkspaceView or call {@link MMWorkspaceLayout}
	 */
	public int getCurrentColumn() {
		// il faut calculer la colonne courrante en fonction de l'écran
		int iCurrentColumn = 0;
		for(int i = 0; i<this.currentScreen-1; i++) {
			iCurrentColumn = iCurrentColumn + ((ViewGroup)this.getChildAt(i)).getChildCount();
		}
		return iCurrentColumn;
	}


	/**
	 * Sets the current screen.
	 * 
	 * @param p_iCurrentScreenIndex
	 *            the screen index
	 */
	private void setCurrentScreen(int p_iCurrentScreenIndex) {

		if (!scroller.isFinished()) {
			scroller.abortAnimation();
		}
		int iCurrentScreenIndex = Math.max(0, Math.min(p_iCurrentScreenIndex, getChildCount()));
		scrollTo(iCurrentScreenIndex * getWidth(), 0);
		//LoggerHandler.getLog().debug("workspace", "setCurrentScreen: width is " + getWidth());
		invalidate();
	}

	/**
	 * Shows the default screen (defined by the firstScreen attribute in XML.)
	 */
	void showDefaultScreen() {
		setCurrentScreen(defaultScreen);
	}

	/**
	 * Registers the specified listener on each screen contained in this workspace.
	 * 
	 * @param p_oOnLongClickListener
	 *            The listener used to respond to long clicks.
	 */
	@Override
	public void setOnLongClickListener(OnLongClickListener p_oOnLongClickListener) {
		final int count = getChildCount();
		// A2A_DEV DMA longclic pour changement d'état
		for (int i = 0; i < count; i++) {
			getChildAt(i).setOnLongClickListener(p_oOnLongClickListener);
		}
	}

	/**
	 * Registers the specified listener on each screen contained in this workspace.
	 * 
	 * @param p_oOnCreateContextMenuListener
	 *            The listener used to respond to long clicks.
	 */
	@Override
	public void setOnCreateContextMenuListener(OnCreateContextMenuListener p_oOnCreateContextMenuListener) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			this.getChildAt(i).setOnCreateContextMenuListener(p_oOnCreateContextMenuListener);
		}
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();

			// Pour corriger une anomalie sur android 2.2:
			// scroller.computeScrollOffset() renvoie true, le scroll doit être fait
			// mais le currentScreen doit aussi être mis à jour
			if (bOrientationChange && nextScreen != INVALID_SCREEN) {
				currentScreen = Math.max(0, Math.min(nextScreen, getChildCount() - 1));
				currentColumnId=getFirstColumnforScreen(currentScreen);
				nextScreen = INVALID_SCREEN;
			}
		} else if (nextScreen != INVALID_SCREEN) {
			currentScreen = Math.max(0, Math.min(nextScreen, getChildCount() - 1));
			currentColumnId=getFirstColumnforScreen(currentScreen);
			nextScreen = INVALID_SCREEN;
		}
	}

	/**
	 * ViewGroup.dispatchDraw() supports many features we don't need: clip to padding, layout animation, animation listener, disappearing children,
	 * etc. The following implementation attempts to fast-track the drawing dispatch by drawing only what we know needs to be drawn. {@inheritDoc}
	 */
	@Override
	protected void dispatchDraw(Canvas p_oCanvas) {
		// First draw the wallpaper if needed
		if (this.wallpaper != null) {
			float x = getScrollX() * wallpaperOffset;
			if (x + wallpaperWidth < getRight() - getLeft()) {
				x = getRight() - getLeft() - wallpaperWidth;
			}
			p_oCanvas.drawBitmap(wallpaper, x, (getBottom() - getTop() - wallpaperHeight) / 2, paint);
		}
		final long drawingTime = getDrawingTime();
		// Determine if we need to draw every child or only the current screen
		boolean bFastDraw = touchState != TOUCH_STATE_SCROLLING && nextScreen == INVALID_SCREEN;
		// If we are not scrolling or flinging, draw only the current screen
		if (bFastDraw && !bOrientationChange) {
			this.drawChild(p_oCanvas, getChildAt(currentScreen), drawingTime);
			//LoggerHandler.getLog().debug("workspace", StringUtils.concat("fastview  ",String.valueOf(currentScreen)));
		} else {

			//LoggerHandler.getLog().debug("workspace", StringUtils.concat("bOrientationChange ",String.valueOf(bOrientationChange)));
			if (bOrientationChange) {
				//				currentScreen = nextScreen != INVALID_SCREEN ? nextScreen : 0;
				//				nextScreen = INVALID_SCREEN;
				drawChild(p_oCanvas, getChildAt(currentScreen), drawingTime);
				//LoggerHandler.getLog().debug("workspace", StringUtils.concat("draw child ",String.valueOf(currentScreen)));
				bOrientationChange = false;
			}
			else{
				// If we are flinging, draw only the current screen and the target screen
				if (nextScreen >= 0 && nextScreen < getChildCount() && Math.abs(currentScreen - nextScreen) == 1) {
					//LoggerHandler.getLog().debug("workspace", "flinging");
					try {
						drawChild(p_oCanvas, getChildAt(currentScreen), drawingTime);
						drawChild(p_oCanvas, getChildAt(nextScreen), drawingTime);
					} catch (NullPointerException e) {
						Application.getInstance().getLog().debug("workspace", "nullPointerException");
					}
				} else {
					// If we are scrolling, draw all of our children
					//LoggerHandler.getLog().debug("workspace", "scrolling");
					final int count = getChildCount();
					for (int i = 0; i < count; i++) {
						drawChild(p_oCanvas, getChildAt(i), drawingTime);
					}
				}
			}
		}
		updateTabIndicator();
		p_oCanvas.drawBitmap(bitmap, getScrollX(), getMeasuredHeight() * (CENT - TAB_INDICATOR_HEIGHT_PCT) / CENT, paint);

	}

	/**
	 * Measure the workspace AND also children {@inheritDoc}
	 */
	@Override
	protected void onMeasure(int p_iWidthMeasureSpec, int p_iHeightMeasureSpec) {
		super.onMeasure(p_iWidthMeasureSpec, p_iHeightMeasureSpec);

		final int iWidth = MeasureSpec.getSize(p_iWidthMeasureSpec);
		final int iHeight = MeasureSpec.getSize(p_iHeightMeasureSpec);
		final int heightiHode = MeasureSpec.getMode(p_iHeightMeasureSpec);

		// The children are given the same width and height as the workspace
		final int iCount = getChildCount();
		int iAdjustedHeightMeasureSpec ;
		for (int i = 0; i < iCount; i++) {
			iAdjustedHeightMeasureSpec = MeasureSpec.makeMeasureSpec(iHeight * (CENT - TAB_INDICATOR_HEIGHT_PCT) / CENT, heightiHode);
			getChildAt(i).measure(p_iWidthMeasureSpec, iAdjustedHeightMeasureSpec);

		}

		// Compute wallpaper
		if (wallpaperLoaded) {
			wallpaperLoaded = false;
			wallpaper = centerToFit(wallpaper, iWidth, iHeight, getContext());
			wallpaperWidth = wallpaper.getWidth();
			wallpaperHeight = wallpaper.getHeight();
		}
		if (wallpaperWidth > iWidth && iCount > 1 /* Si Count == 1, le résultat de l'opération ci-dessous est -Infinity */) {
			wallpaperOffset = (iCount * iWidth - wallpaperWidth) / ((iCount - 1) * (float) iWidth);
		} else {
			wallpaperOffset = WALLPAPER_DEFAULT_OFFSET;
		}
		if (firstWallpaperLayout) {
			this.scrollTo(currentScreen * iWidth, 0);
			firstWallpaperLayout = false;
		}

		// LoggerHandler.getLog().debug("workspace","Top is "+getTop()+", bottom is "+getBottom()+", left is "+getLeft()+", right is "+getRight());

		this.updateTabIndicator();
		this.invalidate();
	}

	/**
	 * 
	 * update the tab indicator
	 */
	private void updateTabIndicator() {
		int iWidth = getMeasuredWidth();
		int iHeight = getMeasuredHeight();
		//a priori on peut récupérer des valeurs <0 dans des cas limites...
		int iNewIHeight =TAB_INDICATOR_HEIGHT_PCT * iHeight / CENT;
		if (iWidth>0 && iNewIHeight>0){
			// For drawing in its own bitmap:
			bar = new RectF(0, 0, iWidth, iNewIHeight);

			int iStartPos = 0;
			if (this.getChildCount() > 0 ){
				iStartPos = this.getScrollX() / this.getChildCount() ;
			}
			int iWidt = iWidth ;
			if (this.getChildCount() > 0) {
				iWidt = iWidth / getChildCount() ;
			}
			selectedTab = new RectF(iStartPos, 0, iStartPos + iWidt, (TAB_INDICATOR_HEIGHT_PCT * iHeight / CENT));

			bitmap = Bitmap.createBitmap(iWidth, iNewIHeight, Bitmap.Config.ARGB_8888);
			canvas = new Canvas(bitmap);
			canvas.drawRoundRect(bar, TAB_INDICATOR_BACKGROUND_RADIUS, TAB_INDICATOR_BACKGROUND_RADIUS, tabIndicatorBackgroundPaint);
			canvas.drawRoundRect(selectedTab, TAB_INDICATOR_RADIUS, TAB_INDICATOR_RADIUS, selectedTabPaint);
		}

	}

	/**
	 * Overrided method to layout child {@inheritDoc}
	 */
	@Override
	protected void onLayout(boolean p_bChanged, int p_iLeft, int p_iTop, int p_iRight, int p_iBottom) {
		int iChildLeft = 0;
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				final int childWidth = child.getMeasuredWidth();
				child.layout(iChildLeft, 0, iChildLeft + childWidth, child.getMeasuredHeight());
				iChildLeft += childWidth;
			}
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
			if (getCurrentScreen() < getChildCount() - 1) {
				scrollToScreen(getCurrentScreen() + 1);
				r_bResult = true;
			}
		}
		if ( !r_bResult ){
			r_bResult = super.dispatchUnhandledMove(p_oViewFocused, p_iDirection); 
		}
		return r_bResult ;
	}

	/**
	 * This method JUST determines whether we want to intercept the motion.
	 * If we return true, onTouchEvent will be called and we do the actual
	 * scrolling there. {@inheritDoc}
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent p_oEvt) {
		/*
		 * Shortcut the most recurring case: the user is in the dragging state and he is moving his finger.
		 *  We want to intercept this motion.
		 */
		if ( locked || (p_oEvt.getAction() == MotionEvent.ACTION_MOVE && touchState != TOUCH_STATE_REST) ) {
			return true;
		}

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(p_oEvt);

		// switch (action & MotionEvent.ACTION_MASK) {
		switch (p_oEvt.getAction()) {
		case MotionEvent.ACTION_MOVE:
			/*
			 * Locally do absolute value. mLastMotionX is set to the y value of the down event.
			 */
			handleInterceptMove(p_oEvt);
			break;
		case MotionEvent.ACTION_DOWN:
			// Remember location of down touch
			final float x1 = p_oEvt.getX();
			final float y1 = p_oEvt.getY();
			lastMotionX = x1;
			lastMotionY = y1;
			allowLongPress = true;
			mActivePointerId = p_oEvt.getPointerId(0);

			/*
			 * If being flinged and user touches the screen, initiate drag; otherwise don't. 
			 * mScroller.isFinished should be false when being flinged.
			 */
			if (scroller.isFinished()) {
				touchState = TOUCH_STATE_REST;
			} else {
				touchState = TOUCH_STATE_SCROLLING;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mActivePointerId = INVALID_POINTER;
			allowLongPress = false;

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			touchState = TOUCH_STATE_REST;
			break;

		case MotionEvent.ACTION_POINTER_UP:
			onSecondaryPointerUp(p_oEvt);
			break;
		default :
			break;
		}
		/*
		 * The only time we want to intercept motion events is if we are in the drag mode.
		 */
		return touchState != TOUCH_STATE_REST;
	}

	/**
	 * Handle the Motion event
	 * 
	 * @param p_oEvt
	 */
	private void handleInterceptMove(MotionEvent p_oEvt) {
		final int iPointerIndex = p_oEvt.findPointerIndex(mActivePointerId);
		if(iPointerIndex!=INVALID_POINTER){
			final float fXposition = p_oEvt.getX(iPointerIndex);
			final float fYposition = p_oEvt.getY(iPointerIndex);
			final int iXDiff = (int) Math.abs(fXposition - lastMotionX);
			final int iYDiff = (int) Math.abs(fYposition - lastMotionY);
			boolean bXMoved = iXDiff > touchSlop;
			boolean bYMoved = iYDiff > touchSlop;

			if (bXMoved || bYMoved) {
				// LoggerHandler.getLog().debug("workspace","Detected move.  Checking to scroll.");
				if (bXMoved && !bYMoved) {
					// LoggerHandler.getLog().debug("workspace","Detected X move.  Scrolling.");
					// Scroll if the user moved far enough along the X axis
					touchState = TOUCH_STATE_SCROLLING;
					lastMotionX = fXposition;
				}
				// Either way, cancel any pending longpress
				if (allowLongPress) {
					allowLongPress = false;
					// Try canceling the long press. It could also have been scheduled
					// by a distant descendant, so use the mAllowLongPress flag to block
					// everything
					final View currentView = getChildAt(currentScreen);
					currentView.cancelLongPress();
				}
			}
		}
	}

	/**
	 * 
	 * Handle Motion Events for the secondary pointer
	 * 
	 * @param p_oEvt
	 */
	private void onSecondaryPointerUp(MotionEvent p_oEvt) {
		final int iPointerIndex = (p_oEvt.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
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
			lastMotionY = p_oEvt.getY(iNewPointerIndex);
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

			if (touchState != TOUCH_STATE_REST) {
				/*
				 * If being flinged and user touches, stop the fling. isFinished will be false if being flinged.
				 */
				if (!scroller.isFinished()) {
					scroller.abortAnimation();
				}
				// Remember where the motion event started
				lastMotionX = x;
				mActivePointerId = p_oEvt.getPointerId(0);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (touchState == TOUCH_STATE_SCROLLING) {
				handleScrollMove(p_oEvt);
			} else {
				// LoggerHandler.getLog().debug("workspace","caught a move touch event but not scrolling");
				// NOTE: We will never hit this case in Android 2.2. This is to fix a 2.1 bug.
				// We need to do the work of interceptTouchEvent here because we don't intercept the move
				// on children who don't scroll.
				Application.getInstance().getLog().debug("workspace", "handling move from onTouch");
				if (onInterceptTouchEvent(p_oEvt) && touchState == TOUCH_STATE_SCROLLING) {
					handleScrollMove(p_oEvt);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (touchState == TOUCH_STATE_SCROLLING) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(VELOCITY_COMPUTED_PER_SECOND, mMaximumVelocity);
				int iVelocityX = (int) velocityTracker.getXVelocity();

				if (iVelocityX > SNAP_VELOCITY && currentScreen > 0) {
					// Fling hard enough to move left
					scrollToScreen(currentScreen - 1);
				} else if (iVelocityX < -SNAP_VELOCITY && currentScreen < getChildCount() - 1) {
					// Fling hard enough to move right
					scrollToScreen(currentScreen + 1);
				} else {
					snapToDestination();
				}
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}			}
			touchState = TOUCH_STATE_REST;
			mActivePointerId = INVALID_POINTER;
			break;
		case MotionEvent.ACTION_CANCEL:
			Application.getInstance().getLog().debug("workspace", "caught a cancel touch event");
			touchState = TOUCH_STATE_REST;
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
		//LoggerHandler.getLog().debug("workspace", "snapToScreen=" + p_iWhichScreen);

		boolean bChangingScreens = p_iWhichScreen != currentScreen;

		nextScreen = p_iWhichScreen;

		View oViewFocusedChild = getFocusedChild();
		if (oViewFocusedChild != null && bChangingScreens && oViewFocusedChild == getChildAt(currentScreen)) {
			oViewFocusedChild.clearFocus();
		}

		final int newX = p_iWhichScreen * width;
		final int delta = newX - getScrollX();
		//LoggerHandler.getLog().debug("workspace", "newX=" + newX + " scrollX=" + getScrollX() + " delta=" + delta);
		int iMmediate = 0 ;
		if (!p_bImmediate){
			iMmediate = Math.abs(delta) * 2 ;
		}
		scroller.startScroll(getScrollX(), 0, delta, 0, iMmediate );
		invalidate();
		//
	}

	/**
	 * Scroll to the screen passed in parameter immediately
	 * @param p_iWhichScreen
	 *            the screen index
	 */
	private void scrollToScreenImmediate(int p_iWhichScreen) {
		scrollToScreen(p_iWhichScreen, true);
	}
	/**
	 * {@inheritDoc}
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
	}
	/**
	 * {@inheritDoc}
	 * @see android.view.View#onSaveInstanceState()
	 */
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
		this.clearWallpaper();

		Parcelable superState=super.onSaveInstanceState();
		MMWorkspaceViewSavedState oState=new MMWorkspaceViewSavedState(superState);
		oState.setChanged(this.aivDelegate.isChanged());
		oState.setiCurrentScreenNum(currentScreen);
		oState.setiCurrentColumnId(currentColumnId);

		Log.d(TAG,"[onSaveInstanceState] currentScreen [" + this.currentScreen + "], currentColumnId [" + this.currentColumnId + "]");
		return oState;
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		try{
			if(!(p_oState instanceof MMWorkspaceViewSavedState)){
				super.onRestoreInstanceState(p_oState);
				return;
			}

			MMWorkspaceViewSavedState savedState = (MMWorkspaceViewSavedState) p_oState;
			// Etat sauvegardé par la classe mère
			super.onRestoreInstanceState(savedState.getSuperState());

			//on repose l'état
			if (savedState.isChanged()){
				this.aivDelegate.changed();
			}
			else {
				this.aivDelegate.resetChanged();
			}

			this.currentScreen=savedState.getiCurrentScreenNum();
			this.currentColumnId=savedState.getiCurrentColumnId();
			Log.d(TAG,"[onRestoreInstanceState] currentScreen [" + this.currentScreen + "], currentColumnId [" + this.currentColumnId + "]");

			//on reset l'écran sur la bonne colonne
			this.scrollTo(currentScreen);

			if (this.getContext() instanceof AbstractWorkspaceMasterDetailMMActivity) {
				Log.d(TAG,"[onRestoreInstanceState] doRefreshDetail");
				((AbstractWorkspaceMasterDetailMMActivity) (this.getContext())).doRefreshDetail();
			}

		}
		catch (Exception e){
			Log.e(TAG,"Exception dans le onRestore",e);
		}
	}

	///debut DMA
	private static class MMWorkspaceViewSavedState extends BaseSavedState {
		private boolean changed;
		int iCurrentScreenNum= 0;
		int iCurrentColumnId = -1;


		public MMWorkspaceViewSavedState(Parcelable p_oSuperState) {
			super(p_oSuperState);
		}

		public MMWorkspaceViewSavedState(Parcel p_oInParcel) {
			super(p_oInParcel);
			this.changed = p_oInParcel.readInt() == 1;
			this.iCurrentScreenNum = p_oInParcel.readInt();
			this.iCurrentColumnId = p_oInParcel.readInt();
		}

		public boolean isChanged() {
			return changed;
		}

		public void setChanged(boolean p_bChanged) {
			this.changed = p_bChanged;
		}

		/**
		 * @return the iCurrentScreenNum
		 */
		public int getiCurrentScreenNum() {
			return iCurrentScreenNum;
		}

		/**
		 * @param p_iCurrentScreenNum the iCurrentScreenNum to set
		 */
		public void setiCurrentScreenNum(int p_iCurrentScreenNum) {
			this.iCurrentScreenNum = p_iCurrentScreenNum;
		}

		/**
		 * @return the iCurrentColumnId
		 */
		public int getiCurrentColumnId() {
			return iCurrentColumnId;
		}

		/**
		 * @param p_iCurrentColumnId the iCurrentColumnId to set
		 */
		public void setiCurrentColumnId(int p_iCurrentColumnId) {
			this.iCurrentColumnId = p_iCurrentColumnId;
		}

		@Override
		public void writeToParcel(Parcel p_oOutParcel, int p_iFlags) {
			super.writeToParcel(p_oOutParcel, p_iFlags);
			int i = 0 ;
			if (this.changed ){
				i = 1 ;
			}
			p_oOutParcel.writeInt(i);
			p_oOutParcel.writeInt(iCurrentScreenNum);
			p_oOutParcel.writeInt(iCurrentColumnId);
		} 

		public static final Parcelable.Creator<MMWorkspaceViewSavedState> CREATOR
		= new Parcelable.Creator<MMWorkspaceViewSavedState>() {
			@Override
			public MMWorkspaceViewSavedState createFromParcel(Parcel p_oInParcel) {
				return new MMWorkspaceViewSavedState(p_oInParcel);
			}

			@Override
			public MMWorkspaceViewSavedState[] newArray(int p_iSize) {
				return new MMWorkspaceViewSavedState[p_iSize];
			}
		};
	}
	///finDMA

	/**
	 * Scroll to the left right screen
	 */
	private void scrollLeft() {
		if (nextScreen == INVALID_SCREEN && currentScreen > 0 && scroller.isFinished()) {
			scrollToScreen(currentScreen - 1);
		}
	}

	/**
	 * Scroll to the left right screen
	 * 
	 * @param p_iPosition
	 *            the index of the screen
	 */
	private void scrollTo(int p_iPosition) {
		if (scroller.isFinished()) {
			scrollToScreen(p_iPosition);
		}
	}

	/**
	 * Scroll to the next right screen
	 */
	private void scrollRight() {
		if (nextScreen == INVALID_SCREEN && currentScreen < getChildCount() - 1 && scroller.isFinished()) {
			scrollToScreen(currentScreen + 1);
		}
	}

	/**
	 * Return the screen's index where a view has been added to.
	 * 
	 * @param p_oView
	 *            the view to search for
	 * @return the sreen index
	 */
	public int getScreenForView(View p_oView) {
		int r_iIndex = -1;
		if (p_oView != null) {
			ViewParent oViewParent = p_oView.getParent();
			int iChildNumber = getChildCount();
			for (int i = 0; i < iChildNumber; i++) {
				if (oViewParent == getChildAt(i)) {
					return i;
				}
			}
		}
		return r_iIndex;
	}

	/**
	 * Return a view instance according to the tag parameter or null if the view could not be found
	 * 
	 * @param p_oTag
	 * @return the view for this tag
	 */
	public View getViewForTag(Object p_oTag) {
		int iCcreenCount = getChildCount();
		for (int iCurrentScreenIndex = 0; iCurrentScreenIndex < iCcreenCount; iCurrentScreenIndex++) {
			View oChildView = getChildAt(iCurrentScreenIndex);
			if (oChildView.getTag() == p_oTag) {
				return oChildView;
			}
		}
		return null;
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

	/**
	 * Move to the default screen
	 */
	public void moveToDefaultScreen() {
		scrollToScreen(defaultScreen);
		//		getChildAt(defaultScreen).requestFocus();
	}

	// ========================= INNER CLASSES ==============================

	/**
	 * A SavedState which save and load the current screen
	 */
	public class SavedState extends AndroidConfigurableVisualComponentSavedState {
		private int iCurrentScreenNum = 0;
		private int iCurrentColumnId = -1;

		/**
		 * Internal constructor
		 * 
		 * @param p_oSuperState
		 */
		SavedState(Parcelable p_oSuperState, int p_iCurrentScreen, int p_iCurrentColumn) {
			super(p_oSuperState);
			this.iCurrentScreenNum = p_iCurrentScreen;
			this.iCurrentColumnId = p_iCurrentColumn;
		}

		/**
		 * Private constructor
		 * 
		 * @param p_oIn
		 */
		private SavedState(Parcel p_oIn) {
			super(p_oIn);
			iCurrentScreenNum = p_oIn.readInt();
			iCurrentColumnId = p_oIn.readInt();
		}

		/**
		 * Save the current screen
		 */
		@Override
		public void writeToParcel(Parcel p_oOut, int p_iFlags) {
			super.writeToParcel(p_oOut, p_iFlags);
			p_oOut.writeInt(iCurrentScreenNum);
			p_oOut.writeInt(iCurrentColumnId);
		}

		public int getCurrentScreen() {
			return iCurrentScreenNum;
		}

		public int getCurrentColumnId(){
			return iCurrentColumnId;
		}
	}

	/**
	 * 
	 * for Flipper compatibility
	 * 
	 * @return the current child index
	 * @see MMWorkspaceView#getCurrentScreen()
	 */
	public int getDisplayedChild() {
		return getCurrentScreen();
	}

	/**
	 * 
	 * for Flipper compatibility
	 * 
	 * @param p_iChildIndex
	 *            the column index to display
	 * @see MMWorkspaceView#flipTo(int)
	 */
	public void setDisplayedChild(int p_iChildIndex) {
		// setCurrentScreen(i);
		flipTo(p_iChildIndex);
		//		getChildAt(p_iChildIndex).requestFocus();
	}

	/**
	 * Flip to the next column and set the CurentColumnId on the controler to deal with screen orientation change
	 */
	public void flipLeft() {
		scrollLeft();
	}

	/**
	 * Flip to the column at this position and set the CurentColumnId on the controler to deal with screen orientation change
	 * 
	 * @param p_iPostion
	 *            the column index
	 */

	public void flipTo(int p_iPostion) {
		scrollTo(p_iPostion);
	}

	/**
	 * Flip to the column at this position after an orientation change and set the CurentColumnId on the controler to deal with screen orientation
	 * change
	 * 
	 * @param p_oIFlipIndex
	 *            the column index
	 * @param p_oB
	 *            true if orientation change
	 */
	public void flipTo(int p_oIFlipIndex, boolean p_oB) {
		bOrientationChange = p_oB;
		if (bOrientationChange){
			scrollToScreenImmediate(p_oIFlipIndex);
		}
		else{
			scrollToScreen(p_oIFlipIndex);
		}
	}

	/**
	 * Flip to the next column and set the CurentColumnId on the controler to deal with screen orientation change
	 */
	public void flipRight() {
		scrollRight();
	}
	// ======================== UTILITIES METHODS ==========================
	/**
	 * Return a centered Bitmap
	 * 
	 * @param p_oBitmap
	 * @param p_iWidth
	 * @param p_iHeight
	 * @param p_oContext
	 * @return
	 */
	static Bitmap centerToFit(Bitmap p_oBitmap, int p_iWidth, int p_iHeight, Context p_oContext) {
		Bitmap r_oBitmap = p_oBitmap;
		final int bitmapWidth = p_oBitmap.getWidth();
		final int bitmapHeight = p_oBitmap.getHeight();

		if (bitmapWidth < p_iWidth || bitmapHeight < p_iHeight) {
			// Normally should get the window_background color of the context
			// A2A_DEV DMA externaliser la couleur
			int iColor = Integer.valueOf("B71717",HEXA_RADIX);
			int iBitmapWidt = bitmapWidth ;
			if (bitmapWidth < p_iWidth){
				iBitmapWidt = p_iWidth ;
			}
			int iBitmapHei = bitmapHeight ;
			if (bitmapHeight < p_iHeight ){
				iBitmapHei = p_iHeight ;
			}
			Bitmap oCentered = Bitmap.createBitmap(iBitmapWidt , iBitmapHei , Bitmap.Config.RGB_565);
			Canvas oCanvas = new Canvas(oCentered);
			oCanvas.drawColor(iColor);
			oCanvas.drawBitmap(p_oBitmap, (p_iWidth - bitmapWidth) / 2.0f, (p_iHeight - bitmapHeight) / 2.0f, null);
			r_oBitmap = oCentered;
		}
		return r_oBitmap;
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
		this.clearWallpaper();
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
		return null;
	}

	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		this.aivDelegate.setCustomFormatter(p_oFormatter);
	}

	@Override
	public CustomFormatter customFormatter() {
		return null;
	}

	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		//Nothing to do
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
