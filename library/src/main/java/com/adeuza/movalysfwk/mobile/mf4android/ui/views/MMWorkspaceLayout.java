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
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableLayoutComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers.WorkspaceTabDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.VMTabConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>
 * 	LinearLayout widget used only for planning and intervention layouting in the Movalys Mobile product for Android
 * </p>
 * 
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * 
 * @author smaitre
 * @author fbourlieux
 * @deprecated Replaced by {@link MMBaseWorkspaceFragmentLayout}
 */
public class MMWorkspaceLayout extends LinearLayout implements ConfigurableLayoutComponent<VMTabConfiguration>, MMIdentifiableView {

	/**
	 * 
	 */
	private static final String TAG = "MMWorkspaceLayout";

	/**
	 * Padding of the intervention detail pannel
	 */
	private static final int STD_PADDING = 2;
	/**
	 * Padding of the intervention detail pannel
	 */
	private static final int DISTANCE_TOUCH_SLOP = 32;

	/**
	 * Left paddin of the intervention detail pannel used to delimit the planning
	 */
	private static final int PADDING_LEFT = 4;

	/**
	 * name of the MMWorkspaceView
	 */
	public static final String MAIN_MMWORKSPACEVIEW_NAME = "Main_MMWorkspaceView";

	/**
	 * 
	 */
	private boolean bOrientationChanged=false;

	/** configurable view delegate */
	private AndroidConfigurableLayoutComponentDelegate<VMTabConfiguration> aivDelegate = null;

	/**
	 * the workspace view that contain all columns
	 */
	private MMWorkspaceView workspaceView;

	/** first column */
	private ViewGroup firstColumn = null;
	/** columns details */
	private List<MMLinearLayout> detailColumns = null;
	/** columns hidden */
	private Set<Integer> hiddenColumns = null;
	/** columns details */
	private SparseArrayCompat<MMLinearLayout> mapDetailColumns = null;
	/** the graphical list */
	private MMLinearLayout mainWorkspacePanel = null;
	/** configuration */
	private ManagementConfiguration manager = null;

	private AttributeSet initAttributes ;

	/**
	 * Background resource id 
	 */
	private int backgroundResId ;
	/**
	 * the application to access to centralized resources
	 */
	private AndroidApplication application = (AndroidApplication) Application.getInstance();

	/**
	 * 
	 */
	private WorkspaceTabDelegate tabDelegate ;

	/**
	 * Constructs a new MMLinearLayout
	 * 
	 * @param p_oContext the context to use
	 * @see LinearLayout#LinearLayout(Context)
	 */
	public MMWorkspaceLayout(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(this);
		}
	}
	/**
	 * Constructs a new MMLinearLayout.
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see LinearLayout#LinearLayout(Context)
	 */
	public MMWorkspaceLayout(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {			
			this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(this);
			this.initAttributes = p_oAttrs ;
			this.detailColumns = new ArrayList<>();
			this.mapDetailColumns = new SparseArrayCompat<>();
			this.hiddenColumns = new HashSet<>();
			this.backgroundResId = p_oAttrs.getAttributeResourceValue(AndroidApplication.MOVALYSXMLNS, "background", 0);
			this.init();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	protected void onConfigurationChanged(Configuration p_oNewConfig) {
		super.onConfigurationChanged(p_oNewConfig);
		bOrientationChanged=true;
		this.recomputeDisplay();
		bOrientationChanged=false;
		
	}

	/**
	 * Init the component.
	 * @param p_oAttrs inflated attributes to use
	 */
	public final void init() {

		//récupération de la configuration du workspace et ajout au ConfigurationHandler
		this.manager = ConfigurationsHandler.getInstance().getManagementConfiguration(this.getModel());

		// Dans le cas de la rotation, le tabDelegate est déjà instancié.
		if ( this.tabDelegate == null ) {
			this.tabDelegate = new WorkspaceTabDelegate(this);
		}

		if (this.manager != null) {
			this.tabDelegate.init(this.manager.getVisibleGroups().iterator().next().getVisibleZones().iterator().next());
		}

		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());

		if ( this.workspaceView != null && workspaceView.getParent() != null ) {
			((ViewGroup) workspaceView.getParent()).removeView(this.workspaceView);
		}
		this.workspaceView = new MMWorkspaceView(this.getContext(), this.initAttributes);
		this.application.computeId(workspaceView, MAIN_MMWORKSPACEVIEW_NAME);

		// Car il y a toujours un petit décalage du doigt même lors d'un scrolling vertical
		this.workspaceView.setTouchSlop(DISTANCE_TOUCH_SLOP);

		if ( this.backgroundResId > 0) {
			this.workspaceView.clearWallpaper();

			BitmapFactory.Options oPtions = new BitmapFactory.Options();
			oPtions.inPurgeable = true ;
			final Bitmap backGd = BitmapFactory.decodeResource(getResources(), this.backgroundResId, oPtions);
			this.workspaceView.loadWallpaper(backGd);
		}

		this.firstColumn = this.computeFirstColumn(oLayoutInflater);
		int iIndex = 0;
		for(ManagementGroup oGroup : this.manager.getVisibleGroups()) {
			if ( iIndex != 0 ) {
				this.computeOtherColumn(oLayoutInflater, oGroup);
			}
			iIndex++;
		}
		this.addView(workspaceView);
		// DMA : 20120419 semble non necessaire à l'init, le hideall apellé plus tard fait cet appel
		this.recomputeDisplay();
	}

	/**
	 * Calcul l'affichage des colonnes
	 */
	public void recomputeDisplay() {
		//number max of column
		int iNbMax = this.manager.getVisibleGroups().size();
		//number of columns that can be displayed on the current screen size
		int iNbColumnToDisplay = this.manager.getVisibleGroups().size()- this.hiddenColumns.size();
		//number of columns to display
		int iNbColumnOnTheScreen = this.application.getScreenColumnNumber();
		//width of the columns that consider the sreen size and the number of columns on the screen
		int iComputedWidth = this.application.getScreenColumnWidth(iNbColumnToDisplay, iNbColumnOnTheScreen, STD_PADDING, PADDING_LEFT);
		//width of the screen
		int iScreenWidth = this.application.getScreenWidth();
		LinearLayout.LayoutParams oWorkspaceParam=new LinearLayout.LayoutParams(iScreenWidth,LayoutParams.MATCH_PARENT);
		this.workspaceView.setLayoutParams(oWorkspaceParam);

		//remettre la taille des éléments de colonne + détachement du parent s'il existe
		this.firstColumn.setLayoutParams(new LinearLayout.LayoutParams(iComputedWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
		if (this.firstColumn.getParent()!=null) {
			((ViewGroup)this.firstColumn.getParent()).removeView(firstColumn);
		}
		for(View oView : this.detailColumns) {
			oView.setLayoutParams(new LinearLayout.LayoutParams(iComputedWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
			if (oView.getParent()!=null) {
				((ViewGroup)oView.getParent()).removeView(oView);
			}
		}

		//Reconstruction
		this.workspaceView.setWidth(iScreenWidth);
		this.workspaceView.removeAllViews();
		// calcul du nombre de flipZones
		int iMaxFlipZoneNumber = (int) Math.ceil(((double) iNbColumnToDisplay) / ((double) iNbColumnOnTheScreen));

		MMLinearLayout oFirstFlipZoneLayout = new MMLinearLayout(this.getContext());
		LinearLayout.LayoutParams oParamsLayoutFirstFlipZone = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		oFirstFlipZoneLayout.setLayoutParams(oParamsLayoutFirstFlipZone);
		oFirstFlipZoneLayout.setOrientation(HORIZONTAL);

		// Calcul du panel de la liste
		oFirstFlipZoneLayout.addView(this.firstColumn);

		MMScrollView oUiScrollView;
		//l'avancement dans les colonnes, il commence e 1 car on a deja pose la première page du workspace 
		int iIndexColumn = 1;
		int iIndexColumnCurrentFlipView = 1;

		LinearLayout.LayoutParams oParamsLayoutFlipZone;
		MMLinearLayout oScrollViewLayout;
		FrameLayout.LayoutParams oParamsScrollView;

		// ajout des flipzones
		for (int iFlipZoneNumber = 0; iFlipZoneNumber < iMaxFlipZoneNumber; iFlipZoneNumber++) {

			oUiScrollView = new MMScrollView(this.getContext());
			oParamsScrollView = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			oUiScrollView.setLayoutParams(oParamsScrollView);
			//oUiScrollView.setBackgroundColor(R.color.background_mainscreen);

			oScrollViewLayout = new MMLinearLayout(oUiScrollView.getContext());
			oParamsLayoutFlipZone = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			oScrollViewLayout.setLayoutParams(oParamsLayoutFlipZone);
			oScrollViewLayout.setOrientation(HORIZONTAL);
			//oScrollViewLayout.setBackgroundColor(Color.LTGRAY);
			oScrollViewLayout.setPadding(PADDING_LEFT, 0, STD_PADDING, 0);
			while (iIndexColumnCurrentFlipView < iNbColumnOnTheScreen && iIndexColumn < iNbMax) {
				if (!this.hiddenColumns.contains(iIndexColumn)) {
					// dans les suivantes ajout des colonnes pour completer
					oScrollViewLayout.addView(this.detailColumns.get(iIndexColumn-1));
					// l'avancement dans les colonnes
					iIndexColumnCurrentFlipView++;
				}
				iIndexColumn++;
			}
			oUiScrollView.addView(oScrollViewLayout);
			if (iFlipZoneNumber == 0) {
				// on est sur le planning, le oFirstFlipZoneLayout inclue le planning + le scroll View
				oFirstFlipZoneLayout.addView(oUiScrollView);
				this.workspaceView.addView(oFirstFlipZoneLayout);
			} else {
				//on est dans les autres flips, on ajoute le scrollView sans layout
				this.workspaceView.addView(oUiScrollView);
			}
			iIndexColumnCurrentFlipView = 0;
		}
		this.goToCurrentColumn();
	}

	/**
	 * 
	 */
	public void goToCurrentColumn() {
		this.goToColumnById(this.workspaceView.getCurrentColumnId());
	}

	/**
	 * @return
	 */
	public boolean isFirstScreenSelected() {
		return this.workspaceView.getCurrentScreen() == 0;
	}

	/**
	 * flip the WorkspaceView to the column  
	 * this is used to show the column by location in the workspace
	 */
	public void goToColumn(int p_iColumnId) {
		View oViewToCompare = null;
		if (p_iColumnId == 0 || p_iColumnId==-1) {
			oViewToCompare = (View) this.firstColumn.getParent().getParent();
		}
		else {
			try {
				oViewToCompare = (View) this.detailColumns.get(p_iColumnId-1).getParent().getParent();
			} catch (NullPointerException e) {
				// nothing to do
				Application.getInstance().getLog().error(TAG,
						"The Workspace doesn't contain the correct structure : findViewById(iColumnId).getParent().getParent()  Expected...");
			}
		}


		int iFlipNumber= this.getRealFlipNumber(oViewToCompare);
		this.flipTo(iFlipNumber, bOrientationChanged); 
		//oApplication.getLog().debug(TAG, "goToColumn flipTo colummn num "+p_iColumnId+" FlipNumber "+iFlipNumber+" Orientation "+bOrientationChanged);

	}

	/**
	 * flip the WorkspaceView to the column saved in {@link AndroidControllerImpl#getCurrentColumnId()} 
	 * this is used to show the column displayed before
	 * the orientation change of the device
	 */
	public void goToColumnById(int p_iColumnId) {
		View oViewToCompare = null;
		if (p_iColumnId == -1 || p_iColumnId==firstColumn.getChildAt(0).getId()) {
			oViewToCompare = (View) firstColumn.getParent().getParent();
		}
		else {
			try {
				//recup la viewToCompare = le flip de la colonne d'id param
				for (View oCurrentFlip : this.detailColumns){
					if (oCurrentFlip.getId()==p_iColumnId){
						oViewToCompare = (View) oCurrentFlip.getParent().getParent();
					}
				}
			} catch (Exception e) {
				// nothing to do
				this.application.getLog().error(TAG,
						"The ViewFlipper doesn't contain the correct structure : findViewById(iColumnId).getParent().getParent()  Expected...");
			}
		}
		int iFlipNumber= this.getRealFlipNumber(oViewToCompare);
		//oApplication.getLog().debug(TAG, "goToColumnById flipTo colummn Id "+p_iColumnId+" FlipNumber "+iFlipNumber+" Orientation "+bOrientationChanged);
		this.flipTo(iFlipNumber, bOrientationChanged); 

	}

	/**
	 * Donne la position du scroll view pour la colonne donnée 
	 */
	public int getRealFlipNumber(View p_oViewToCompare) {
		int r_iReal = 0;

		if (p_oViewToCompare!=null) {
			int iFlipIndex = 0;
			int iMaxFlipNumber = workspaceView.getChildCount();
			boolean bNotFinded = true;

			while (bNotFinded && iFlipIndex <= iMaxFlipNumber) {
				if (this.workspaceView.getChildAt(iFlipIndex) == p_oViewToCompare) { //c'est bien une comparaison d'adresse que l'on veut
					bNotFinded = false;
				} else {
					iFlipIndex++;
				}
			}
			if (!bNotFinded) {
				r_iReal = iFlipIndex; 
			}
			//oApplication.getLog().debug(TAG,"getRealFlipNumber for "+p_oViewToCompare.getId()+" is "+real);
		}
		else {
			this.application.getLog().debug(TAG,"getRealFlipNumber for null is "+r_iReal);
		}
		return r_iReal;
	}


	/**
	 * Compute the content of a detail Column
	 * 
	 * @param p_oInflater
	 *            the layout inflater
	 * @param p_oParentMMLinearLayout
	 *            the parent view
	 * @param p_oGroup
	 *            the {@link ManagementGroup} in witch the {@link ManagementGroup#getVisibleZones()} are used to construct the content of the columns
	 * @return the vertical {@link MMLinearLayout} cointaining all columns of an intervention detail
	 */
	private MMLinearLayout computeOtherColumn(LayoutInflater p_oInflater, ManagementGroup p_oGroup) {
		View oLayoutZone;
		MMLinearLayout r_oLayoutColumn = this.mapDetailColumns.get(p_oGroup.hashCode());
		if (r_oLayoutColumn==null) {
			r_oLayoutColumn = new MMLinearLayout(this.getContext());
			r_oLayoutColumn.setFocusableInTouchMode(true);
			LinearLayout.LayoutParams oParamsLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			r_oLayoutColumn.setLayoutParams(oParamsLayout);
			r_oLayoutColumn.setOrientation(VERTICAL);
			this.application.computeId(r_oLayoutColumn, p_oGroup.getName());
			List<ManagementZone> listZones = p_oGroup.getVisibleZones();
			for (ManagementZone oZone : listZones) {
				oLayoutZone = p_oInflater.inflate(this.application.getAndroidIdByStringKey(ApplicationRGroup.LAYOUT,oZone.getSource()),null);
				oLayoutZone.setPadding(1, 1, 1, 1);

				r_oLayoutColumn.addView(oLayoutZone);
			}
			this.detailColumns.add(r_oLayoutColumn);
			this.mapDetailColumns.put(p_oGroup.hashCode(), r_oLayoutColumn);
		}
		else {
			// il faut retailler le pannel
			r_oLayoutColumn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		}
		if (r_oLayoutColumn.getParent()!=null) {
			((ViewGroup)r_oLayoutColumn.getParent()).removeView(r_oLayoutColumn);
		}
		return r_oLayoutColumn;
	}

	/**
	 * Construct the first column (mainly a list)
	 * 
	 * @param p_oManagementGroups
	 *            {@link List<ManagementGroup>} that must contain the planning panel on first position
	 * @param p_oInflater
	 *            the current Layout inflater
	 * @return {@link MMLinearLayout} containing the planning panel
	 */
	private MMLinearLayout computeFirstColumn(LayoutInflater p_oInflater) {
		MMLinearLayout r_oLayoutColumn = this.mainWorkspacePanel;
		if (r_oLayoutColumn == null) {
			r_oLayoutColumn = new MMLinearLayout(this.getContext());
			r_oLayoutColumn.setFocusableInTouchMode(true);
			ManagementGroup oGroup = this.manager.getVisibleGroups().get(0);

			LinearLayout.LayoutParams oParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			r_oLayoutColumn.setLayoutParams(oParams);
			r_oLayoutColumn.setOrientation(VERTICAL);

			// traitement de la liste qui est en première position
			ManagementZone oMainWorkspaceZone = oGroup.getVisibleZones().get(0);
			MMLinearLayout oLayoutZone = new MMLinearLayout(this.getContext());
			p_oInflater.inflate(this.application.getAndroidIdByStringKey(ApplicationRGroup.LAYOUT, oMainWorkspaceZone.getSource()), oLayoutZone);
			this.application.computeId(oLayoutZone, oMainWorkspaceZone.getName());
			r_oLayoutColumn.addView(oLayoutZone);
			this.mainWorkspacePanel = r_oLayoutColumn;
		}
		else {
			// il faut retailler le pannel
			r_oLayoutColumn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		}
		if (r_oLayoutColumn.getParent()!=null) {
			((ViewGroup)r_oLayoutColumn.getParent()).removeView(r_oLayoutColumn);
		}
		return r_oLayoutColumn;
	}

	/**
	 * @param p_iPosition
	 * @param p_bImmediate
	 */
	private void flipTo(int p_iPosition, boolean p_bImmediate) {
		workspaceView.flipTo(p_iPosition, p_bImmediate);
	}

	/**
	 * Hide the detail columns
	 * @param p_iColumn the number of the column to hide
	 * @param p_bNeedRecompute if true, this method launch recomputeDisplay
	 */
	public void hideDetailColumn(int p_iColumn, boolean p_bNeedRecompute) {
		this.hiddenColumns.add(p_iColumn+1);
		workspaceView.removeView(this.detailColumns.get(p_iColumn));
		if (p_bNeedRecompute){
			this.recomputeDisplay();
		}
	}


	/**
	 * Hide all detail columns
	 */
	public void hideAllDetailColumn(boolean p_bNeedRecompute) {
		int i = 1;
		for(ViewGroup oView : this.detailColumns) {
			this.hiddenColumns.add(i);
			workspaceView.removeView(oView);
			i++;
		}
		if (p_bNeedRecompute){
			this.recomputeDisplay();
		}
	}
	/**
	 * Unhide all detail columns
	 */
	public void unHideDetailColumn(boolean p_bNeedRecompute) {
		if ( !this.hiddenColumns.isEmpty()) {
			this.hiddenColumns.clear();
			if(p_bNeedRecompute){
				this.recomputeDisplay();
			}
		}
	}

	/**
	 * retrieve the section layout in uhiddens columns
	 * @param p_iSectionId the ViewId of the section
	 * @return the section Layout
	 */
	public MMMasterRelativeLayout getSectionLayoutByIdInHiddenColumns(int p_iSectionId){
		MMMasterRelativeLayout r_oSectionLayout=null;
		for (int iIdColumn:this.hiddenColumns){
			r_oSectionLayout= (MMMasterRelativeLayout)this.detailColumns.get(iIdColumn-1).findViewById(p_iSectionId);
			if (r_oSectionLayout!=null){
				return r_oSectionLayout;
			}
		}
		return r_oSectionLayout;
	}


	// ////////////////
	// ZONE DELEGATE //
	// ////////////////

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
	public void removeChild(ConfigurableVisualComponent<?> p_oChildToDelete) {
		this.aivDelegate.removeChild(p_oChildToDelete);
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
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomDateTime(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomDateTime(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomPhone(BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate.configurationAppendCustomPhone(p_oFieldConfiguration);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(VMTabConfiguration p_oVMTabConfiguration) {
		this.aivDelegate.configurationSetValue(p_oVMTabConfiguration);
		this.tabDelegate.applyTabConfiguration(p_oVMTabConfiguration);
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
	public VMTabConfiguration configurationGetValue() {
		return this.aivDelegate.configurationGetValue();
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
	 */
	@Override
	public boolean isNullOrEmptyValue(VMTabConfiguration p_oObject) {
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
	public Class<?> getValueType() {
		return NoneType.class;
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
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return this.isShown();
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
	 * 
	 */
	public void doAfterSetContentView() {
		this.tabDelegate.doAfterSetContentView();
	}

	/**
	 * @return
	 */
	public WorkspaceTabDelegate getTabDelegate() {
		return tabDelegate;
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
	 * @see android.view.View#setOnCreateContextMenuListener(android.view.View.OnCreateContextMenuListener)
	 */
	@Override
	public void setOnCreateContextMenuListener(OnCreateContextMenuListener p_oListener) {
		this.firstColumn.setOnCreateContextMenuListener(p_oListener);

		for (View oView : this.detailColumns) {
			oView.setOnCreateContextMenuListener(p_oListener);
		}
	}

	public int getTabCount() {
		return this.tabDelegate.getTabCount();
	}

	/**
	 * @param p_oOnTabChangedListener
	 */
	public void setOnTabChangedListener( OnTabChangeListener p_oOnTabChangedListener ) {
		this.tabDelegate.setOnTabChangedListener(p_oOnTabChangedListener);
	}

	/**
	 * @return
	 */
	public String getSelectedTab() {
		return this.tabDelegate.getSelectedTab();
	}

	/**
	 * @param p_sTabId
	 */
	public void setSelectedTab( String p_sTabId ) {
		this.tabDelegate.setSelectedTab(p_sTabId);
	}

	/**
	 * {@inheritDoc}
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
