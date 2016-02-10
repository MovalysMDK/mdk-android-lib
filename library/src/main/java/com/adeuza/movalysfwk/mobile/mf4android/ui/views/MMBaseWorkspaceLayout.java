package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableLayoutComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * @author lmichenaud
 *
 * @param <VALUE>
 * @deprecated Replaced by {@link MMBaseWorkspaceFragmentLayout}
 */
public abstract class MMBaseWorkspaceLayout<VALUE> extends LinearLayout implements ConfigurableLayoutComponent<VALUE>, MMIdentifiableView {

	/**
	 * name of the MMWorkspaceView
	 */
	public static final String MAIN_MMWORKSPACEVIEW_NAME = "Main_MMWorkspaceView";

	/**
	 * 
	 */
	public static final int DISTANCE_TOUCH_SLOP = 32;

	/**
	 * Padding of detail pannel
	 */
	public static final int STD_PADDING = 2;

	/**
	 * Left padding of the detail panel
	 */
	public static final int PADDING_LEFT = 4;

	/**
	 * the application to access to centralized resources
	 */
	private AndroidApplication application = (AndroidApplication) Application.getInstance();

	/**
	 * configurable view delegate
	 */
	private AndroidConfigurableLayoutComponentDelegate<VALUE> aivDelegate;

	/**
	 * the workspace view that contain all columns
	 */
	private MMWorkspaceView workspaceView;

	/**
	 * Management Configuration
	 */
	private ManagementConfiguration workspaceConfig;

	/**
	 * Initial attributes
	 */
	private AttributeSet initAttributes;

	/**
	 * Background resource id
	 */
	private int backgroundResId;

	/**
	 * 
	 */
	private boolean orientationChanged = false;

	/**
	 * 
	 */
	private Class<VALUE> valueClass ;

	/**
	 * Columns
	 */
	private List<MMWorkspaceColumn> columns;

	/**
	 * Columns map
	 * Key: hashcode of group
	 * Value: column
	 */
	private SparseArrayCompat<MMWorkspaceColumn> mapColumns;

	/**
	 * Hidden columns
	 */
	private Set<Integer> hiddenColumns ;

	/**
	 * @param p_oContext
	 */
	public MMBaseWorkspaceLayout(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(this);
		}
	}

	/**
	 * @param p_oContext
	 * @param p_oAttrs
	 */
	public MMBaseWorkspaceLayout(Context p_oContext, AttributeSet p_oAttrs, Class<VALUE> p_oValueClass ) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(this);
			this.initAttributes = p_oAttrs;
			this.backgroundResId = p_oAttrs.getAttributeResourceValue(AndroidApplication.MOVALYSXMLNS, "background", 0);
			this.valueClass = p_oValueClass ;
			this.columns = new ArrayList<>();
			this.mapColumns = new SparseArrayCompat<>();
			this.hiddenColumns = new HashSet<>();
			this.init();
		}
	}

	/**
	 * 
	 */
	public abstract void customInit();


	/**
	 * @param p_iColumnIndex column index
	 * @param p_oInflater inflater
	 * @param p_oManagementGroup management group
	 */
	protected abstract MMWorkspaceColumn computeColumn( int p_iColumnIndex, LayoutInflater p_oInflater, ManagementGroup p_oManagementGroup);


	/**
	 * 
	 */
	public final void init() {
		// Retrieve workspace configuration
		this.workspaceConfig = ConfigurationsHandler.getInstance().getManagementConfiguration(this.getModel());
		if ( workspaceConfig == null ) {
			throw new MobileFwkException(Application.LOG_TAG,"Can't find management configuration for workspace.");
		}

		if ( this.workspaceView != null && workspaceView.getParent() != null ) {
			((ViewGroup) workspaceView.getParent()).removeView(this.workspaceView);
		}
		this.workspaceView = new MMWorkspaceView(this.getContext(), this.initAttributes);
		this.getAndroidApplication().computeId(workspaceView, MAIN_MMWORKSPACEVIEW_NAME);
		// Car il y a toujours un petit décalage du doigt même lors d'un scrolling vertical
		this.workspaceView.setTouchSlop( DISTANCE_TOUCH_SLOP);
		this.initBackground();

		this.customInit();

		LayoutInflater oInflater = LayoutInflater.from(this.getContext());
		int iColumnIndex = 0 ;
		for (ManagementGroup oGroup : this.workspaceConfig.getVisibleGroups()) {
			MMWorkspaceColumn oMMWorkspaceColumn = this.computeColumn( iColumnIndex++, oInflater, oGroup);
			if ( this.mapColumns.indexOfKey(oGroup.hashCode()) == -1) {
				this.columns.add(oMMWorkspaceColumn);
				this.mapColumns.put(oGroup.hashCode(), oMMWorkspaceColumn);	
			}
		}

		this.addView( this.getWorkspaceView());
		this.recomputeDisplay();
	}

	/**
	 * 
	 */
	protected void initBackground() {
		if (this.backgroundResId > 0) {
			this.workspaceView.clearWallpaper();

			BitmapFactory.Options oPtions = new BitmapFactory.Options();
			oPtions.inPurgeable = true;
			final Bitmap backGd = BitmapFactory.decodeResource(getResources(),
					this.backgroundResId, oPtions);
			this.workspaceView.loadWallpaper(backGd);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.view.View#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	protected void onConfigurationChanged(Configuration p_oNewConfig) {
		super.onConfigurationChanged(p_oNewConfig);
		this.orientationChanged = true;
		this.recomputeDisplay();
		this.orientationChanged = false;
		  
	}

	/**
	 * @return
	 */
	protected ManagementConfiguration getWorkspaceConfig() {
		return this.workspaceConfig;
	}

	/**
	 * Return ScrollView position of the column
	 * @param p_oWorkspaceView workspace view
	 * @param p_oViewToCompare column view
	 */
	public int getRealFlipNumber(MMWorkspaceColumn p_oWorkspaceColumn) {
		int r_iReal = 0;

		if (p_oWorkspaceColumn.getRootViewGroup() != null) {
			int iFlipIndex = 0;
			int iMaxFlipNumber = this.workspaceView.getChildCount();
			boolean bNotFinded = true;

			while (bNotFinded && iFlipIndex <= iMaxFlipNumber) {
				if (this.workspaceView.getChildAt(iFlipIndex) == p_oWorkspaceColumn.getRootViewGroup()) {
					bNotFinded = false;
				} else {
					iFlipIndex++;
				}
			}
			if (!bNotFinded) {
				r_iReal = iFlipIndex;
			}
		} else {
			this.application.getLog().debug(Application.LOG_TAG,"getRealFlipNumber for null is " + r_iReal);
		}
		return r_iReal;
	}

	/**
	 * @param p_oWorkspaceView workspace
	 * @param p_iPosition position to flip to
	 * @param p_bImmediate
	 */
	public void flipTo( int p_iPosition, boolean p_bImmediate) {
		this.workspaceView.flipTo( p_iPosition, p_bImmediate);
	}

	/**
	 * 
	 */
	public void goToCurrentColumn() {
		this.goToColumnById(this.workspaceView.getCurrentColumnId());
	}

	/**
	 * flip the WorkspaceView to the column saved in {@link AndroidControllerImpl#getCurrentColumnId()} 
	 * this is used to show the column displayed before
	 * the orientation change of the device
	 */
	public void goToColumnById(int p_iColumnId) {
		MMWorkspaceColumn oColumn = this.getColumnById(p_iColumnId);
		int iFlipNumber= this.getRealFlipNumber( oColumn);
		this.flipTo( iFlipNumber, this.isOrientationChanged());		
	}

	/**
	 * Flip to a column
	 * 
	 * @param p_iColumnIndex
	 *            column index
	 */
	public void goToColumn(int p_iColumnIndex) {
		MMWorkspaceColumn oColumn = this.getColumnByIndex(p_iColumnIndex);
		int iFlipNumber = this.getRealFlipNumber(oColumn);
		this.flipTo( iFlipNumber, this.isOrientationChanged());
	}

	/**
	 * {@inheritDoc}
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMBaseWorkspaceLayout#getColumnViewByIndex(int)
	 */
	public MMWorkspaceColumn getColumnByGroup( ManagementGroup p_oGroup ) {
		return this.mapColumns.get(p_oGroup.hashCode());
	}

	/**
	 * {@inheritDoc}
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMBaseWorkspaceLayout#getColumnViewByIndex(int)
	 */
	public MMWorkspaceColumn getColumnById( int p_iColumnId ) {
		MMWorkspaceColumn r_oColumn = null;
		try {
			// if -1, return first column
			if ( p_iColumnId == -1 ) {
				r_oColumn = this.columns.get(0);
			}
			else {
				// Else, do search
				for (MMWorkspaceColumn oColumn : this.columns) {
					if (oColumn.getViewGroup().getId() == p_iColumnId) {
						r_oColumn = oColumn;
						break;
					}
				}
			}
		} catch (Exception oException) {
			this.getAndroidApplication()
			.getLog()
			.error(Application.LOG_TAG,
					"The ViewFlipper doesn't contain the correct structure : findViewById(iColumnId).getParent().getParent()  Expected...");
		}
		return r_oColumn ;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMBaseWorkspaceLayout#getColumnViewByIndex(int)
	 */
	public MMWorkspaceColumn getColumnByIndex( int p_iIndex ) {
		return(MMWorkspaceColumn) this.columns.get(p_iIndex);
	}

	/**
	 * Hide the detail columns
	 * @param p_iColumn the number of the detail column to hide
	 * @param p_bNeedRecompute if true, this method launch recomputeDisplay
	 */
	public void hideColumn(int p_iColumnIndex, boolean p_bNeedRecompute) {
		MMWorkspaceColumn oColumn = this.getColumnByIndex(p_iColumnIndex);
		if ( oColumn.canBeHidden()) {
			this.hiddenColumns.add(p_iColumnIndex);
			this.getWorkspaceView().removeView(oColumn.getRootViewGroup());
			if (p_bNeedRecompute){
				this.recomputeDisplay();
			}
		}
	}

	/**
	 * Unhide all detail columns
	 */
	public void unHideAllColumns(boolean p_bNeedRecompute) {
		if ( !this.hiddenColumns.isEmpty()) {
			this.hiddenColumns.clear();
			if(p_bNeedRecompute){
				this.recomputeDisplay();
			}
		}
	}

	/**
	 * Hide all detail columns
	 */
	public void hideAllColumns(boolean p_bNeedRecompute) {
		int i = 0;
		for (MMWorkspaceColumn oColumn : this.getColumns()) {
			if ( oColumn.canBeHidden()) {
				this.hiddenColumns.add(i);
				this.getWorkspaceView().removeView(oColumn.getViewGroup());
			}
			i++;
		}
		if (p_bNeedRecompute) {
			this.recomputeDisplay();
		}
	}

	/**
	 * retrieve the section layout in uhiddens columns
	 * @param p_iSectionId the ViewId of the section
	 * @return the section Layout
	 */
	public MMMasterRelativeLayout getSectionLayoutByIdInHiddenColumns(int p_iSectionId){
		MMMasterRelativeLayout r_oSectionLayout = null;
		for ( int iIdColumn : this.hiddenColumns ) {
			r_oSectionLayout= (MMMasterRelativeLayout)this.getColumnByIndex(iIdColumn).getRootViewGroup().findViewById(p_iSectionId);
			if ( r_oSectionLayout !=null ) {
				return r_oSectionLayout;
			}
		}
		return r_oSectionLayout;
	}

	/**
	 * @return
	 */
	protected MMScrollView createScrollViewForFlipZone( MMLinearLayout p_oScrollViewLayout ) {

		MMScrollView r_oUiScrollView = new MMScrollView(this.getContext());
		FrameLayout.LayoutParams oParamsScrollView = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		r_oUiScrollView.setLayoutParams(oParamsScrollView);
		r_oUiScrollView.addView(p_oScrollViewLayout);

		return r_oUiScrollView ;
	}

	/**
	 * @return
	 */
	protected MMLinearLayout createLayoutForScrollView() {
		MMLinearLayout oScrollViewLayout = new MMLinearLayout(this.getContext());
		LinearLayout.LayoutParams oParamsLayoutFlipZone = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		oScrollViewLayout.setLayoutParams(oParamsLayoutFlipZone);
		oScrollViewLayout.setOrientation(HORIZONTAL);
		oScrollViewLayout.setPadding(PADDING_LEFT, 0, STD_PADDING, 0);
		return oScrollViewLayout ;
	}

	/**
	 * Calcul l'affichage des colonnes
	 */
	public void recomputeDisplay() {
		// number max of column
		int iNbMax = this.getWorkspaceConfig().getVisibleGroups().size();

		// number of columns that can be displayed on the current screen size
		int iNbColumnToDisplay = this.getWorkspaceConfig().getVisibleGroups().size()
				- this.countHiddenColumns();

		// number of columns to display
		int iNbColumnOnTheScreen = this.getAndroidApplication().getScreenColumnNumber();

		// width of the columns that consider the sreen size and the number of
		// columns on the screen
		int iComputedWidth = this.getAndroidApplication().getScreenColumnWidth(
				iNbColumnToDisplay, iNbColumnOnTheScreen,
				STD_PADDING, PADDING_LEFT);

		// width of the screen
		int iScreenWidth = this.getAndroidApplication().getScreenWidth();
		LinearLayout.LayoutParams oWorkspaceParam = new LinearLayout.LayoutParams(
				iScreenWidth, LayoutParams.MATCH_PARENT);
		this.getWorkspaceView().setLayoutParams(oWorkspaceParam);

		for (MMWorkspaceColumn oColumn : this.getColumns()) {
			oColumn.getViewGroup().setLayoutParams(new LinearLayout.LayoutParams(iComputedWidth,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			if (oColumn.getViewGroup().getParent() != null) {
				((ViewGroup) oColumn.getViewGroup().getParent()).removeView(oColumn.getViewGroup());
			}
		}

		// Reconstruction
		this.getWorkspaceView().setWidth(iScreenWidth);
		this.getWorkspaceView().removeAllViews();

		// calcul du nombre de flipZones
		int iMaxFlipZoneNumber = (int) Math.ceil(((double) iNbColumnToDisplay)
				/ ((double) iNbColumnOnTheScreen));

		// Compute first flip zone layout : used when first column is a list. 
		MMLinearLayout oFirstFlipZoneLayout = new MMLinearLayout(this.getContext());
		LinearLayout.LayoutParams oParamsLayoutFirstFlipZone = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		oFirstFlipZoneLayout.setLayoutParams(oParamsLayoutFirstFlipZone);
		oFirstFlipZoneLayout.setOrientation(HORIZONTAL);

		int iIndexColumn = 0;
		int iIndexColumnCurrentFlipView = 0;		

		boolean bFirstColumn = true ;
		boolean bUseSuperLayout = false ;

		// Add flip zone
		for (int iFlipZoneNumber = 0; iFlipZoneNumber < iMaxFlipZoneNumber; iFlipZoneNumber++) {

			MMLinearLayout oScrollViewLayout = createLayoutForScrollView();


			// In landscape mode, screen can display more than more columns
			while (iIndexColumnCurrentFlipView < iNbColumnOnTheScreen
					&& iIndexColumn < iNbMax) {
				if (!this.isColumnHidden(iIndexColumn)) {
					// dans les suivantes ajout des colonnes pour completer
					MMWorkspaceColumn oColumn = this.getColumnByIndex(iIndexColumn);
					if ( bFirstColumn && oColumn.isList()) {
						bUseSuperLayout = true ;
						//						oFirstFlipZoneLayout.addView(oColumn.getViewGroup());
					}
					//					else {
					oScrollViewLayout.addView(oColumn.getViewGroup());
					//					}
					// l'avancement dans les colonnes
					iIndexColumnCurrentFlipView++;
					bFirstColumn = false ;					
				}
				iIndexColumn++;
			}

			if ( bUseSuperLayout ) {
				oFirstFlipZoneLayout.addView(oScrollViewLayout);
				this.getWorkspaceView().addView(oFirstFlipZoneLayout);
				bUseSuperLayout = false ;
			} else {
				MMScrollView oScrollView = this.createScrollViewForFlipZone( oScrollViewLayout );
				//on est dans les autres flips, on ajoute le scrollView sans le layout (oFirstFlipZoneLayout)
				this.getWorkspaceView().addView(oScrollView);
			}
			iIndexColumnCurrentFlipView = 0;
		}
		this.goToCurrentColumn();
	}

	/**
	 * @return
	 */
	public boolean isFirstScreenSelected() {
		return this.getWorkspaceView().getCurrentScreen() == 0;
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#setOnCreateContextMenuListener(android.view.View.OnCreateContextMenuListener)
	 */
	@Override
	public void setOnCreateContextMenuListener(OnCreateContextMenuListener p_oListener) {
		for (MMWorkspaceColumn oMMWorkspaceColumn : this.getColumns()) {
			oMMWorkspaceColumn.getViewGroup().setOnCreateContextMenuListener(p_oListener);
		}
	}

	/**
	 * Return number of hidden columns
	 * @return
	 */
	public int countHiddenColumns() {
		return this.hiddenColumns.size();
	}

	/**
	 * @param p_iColumnIndex
	 * @return
	 */
	public boolean isColumnHidden( int p_iColumnIndex ) {
		return this.hiddenColumns.contains(p_iColumnIndex);
	}

	/**
	 * @return
	 */
	public boolean isOrientationChanged() {
		return this.orientationChanged;
	}

	/**
	 * @return
	 */
	public AndroidApplication getAndroidApplication() {
		return this.application;
	}

	/**
	 * @return
	 */
	protected MMWorkspaceView getWorkspaceView() {
		return this.workspaceView;
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
	 * @return
	 */
	public List<MMWorkspaceColumn> getColumns() {
		return this.columns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		// Nothing to do
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
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(VALUE p_oObject) {
		return p_oObject == null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyCustomValues(String[] p_oObjects) {
		return this.aivDelegate.isNullOrEmptyCustomValues(p_oObjects);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void hide() {
		this.aivDelegate.hide();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unHide() {
		this.aivDelegate.unHide();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationPrepareHide(List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		this.aivDelegate.configurationPrepareHide(p_oComponentsToHide);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Method getConfigurationSetValueMethod() {
		return this.aivDelegate.getConfigurationSetValueMethod();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationDisabledComponent() {
		this.aivDelegate.configurationDisabledComponent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		return this.aivDelegate.isFilled();
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
	 */
	@Override
	public void configurationUnsetError() {
		this.aivDelegate.configurationUnsetError();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		this.aivDelegate.configurationSetError(p_oError);
	}

	/**
	 * {@inheritDoc}
	 */
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
	public final String getModel() {
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
	public String[] configurationGetCustomValues() {
		return this.aivDelegate.configurationGetCustomValues();
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
	public ConfigurableVisualComponent<?> configurationAppendCustomDateTime(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomDateTime(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(VALUE p_oVALUE) {
		this.aivDelegate.configurationSetValue(p_oVALUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		this.aivDelegate.configurationSetCustomValues(p_oObjectsToSet);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VALUE configurationGetValue() {
		return this.aivDelegate.configurationGetValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomLabel(final BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomLabel(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomTextField(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomTextField(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomCheckBoxes(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomCheckBoxes(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomRadioButtons(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomRadioButtons(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomComboBox(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomComboBox(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomURL(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomURL(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomEmail(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomEmail(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomInteger(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomInteger(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomDuration(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomDuration(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomDate(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomDate(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomTime(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomTime(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent#configurationAppendCustomPhone(com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration)
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomPhone(BasicComponentConfiguration p_oConfiguration) {
		return this.aivDelegate.configurationAppendCustomPhone(p_oConfiguration);
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
	public void removeChild(ConfigurableVisualComponent<?> p_oChildToDelete) {
		this.aivDelegate.removeChild(p_oChildToDelete);
	}

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
	public Class<?> getValueType() {
		return this.valueClass;
	}


	/**
	 * @author lmichenaud
	 *
	 */
	public class MMWorkspaceColumn {

		/**
		 * Can be hidden
		 */
		private boolean canBeHidden = false ;

		/**
		 * Layout
		 */
		private ViewGroup viewGroup ;

		/**
		 * 
		 */
		private boolean list = false ;

		/**
		 * @return
		 */
		public boolean canBeHidden() {
			return this.canBeHidden;
		}

		/**
		 * @param p_bCanBeHidden
		 */
		public void setCanBeHidden(boolean p_bCanBeHidden) {
			this.canBeHidden = p_bCanBeHidden;
		}

		/**
		 * @return
		 */
		public ViewGroup getViewGroup() {
			return this.viewGroup;
		}

		/**
		 * Return root view group.
		 * The root view is generally the scroll view that contains
		 * the view group of the column.
		 * @return
		 */
		public ViewGroup getRootViewGroup() {
			return (ViewGroup) this.viewGroup.getParent().getParent();
		}

		/**
		 * @param p_oViewGroup
		 */
		public void setViewGroup(ViewGroup p_oViewGroup) {
			this.viewGroup = p_oViewGroup;
		}

		/**
		 * @return
		 */
		public boolean isList() {
			return this.list;
		}

		/**
		 * @param p_bList
		 */
		public void setList(boolean p_bList) {
			this.list = p_bList;
		}
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
