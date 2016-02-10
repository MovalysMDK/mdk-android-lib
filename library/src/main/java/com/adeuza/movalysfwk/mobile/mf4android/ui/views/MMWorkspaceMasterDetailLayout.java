package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers.WorkspaceTabDelegate2;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.VMTabConfiguration;

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
 * @deprecated Replaced by {@link MMWorkspaceDetailFragmentLayout}
 */
public class MMWorkspaceMasterDetailLayout extends MMBaseWorkspaceLayout<VMTabConfiguration> {
			
	/**
	 * Tab delegate
	 */
	private WorkspaceTabDelegate2<MMWorkspaceMasterDetailLayout> tabDelegate ;
	
	/**
	 * Constructs a new MMLinearLayout
	 * 
	 * @param p_oContext the context to use
	 * @see LinearLayout#LinearLayout(Context)
	 */
	public MMWorkspaceMasterDetailLayout(Context p_oContext) {
		super(p_oContext);
	}
	
	/**
	 * Constructs a new MMLinearLayout.
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see LinearLayout#LinearLayout(Context)
	 */
	public MMWorkspaceMasterDetailLayout(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, VMTabConfiguration.class );
	}
	
	/**
	 * Init the component.
	 * @param p_oAttrs inflated attributes to use
	 */
	@Override
	public void customInit() {
		
		// Dans le cas de la rotation, le tabDelegate est déjà instancié.
		if ( this.tabDelegate == null ) {
			this.tabDelegate = new WorkspaceTabDelegate2<>(this);
		}
	
		if (this.getWorkspaceConfig() != null) {
			this.tabDelegate.init(this.getWorkspaceConfig().getVisibleGroups().iterator().next().getVisibleZones().iterator().next());
		}
	}
	
	/**
	 * Compute columns
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMBaseWorkspaceLayout#computeColumn(int, android.view.LayoutInflater, com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup)
	 */
	@Override
	public MMWorkspaceColumn computeColumn(int p_iColumnIndex, LayoutInflater p_oInflater,
			ManagementGroup p_oManagementGroup) {
		
		MMWorkspaceColumn r_oMMWorkspaceColumn = null ;
		
		if ( p_iColumnIndex == 0 ) {
			r_oMMWorkspaceColumn = this.computeListColumn(p_oInflater, p_oManagementGroup);
		}
		else {
			r_oMMWorkspaceColumn = this.computeDetailColumn(p_oInflater, p_oManagementGroup);
		}
		
		return r_oMMWorkspaceColumn ;
	}
	
	/**
	 * Construct the list column (mainly a list)
	 * 
	 * @param p_oManagementGroups
	 *            {@link List<ManagementGroup>} that must contain the planning panel on first position
	 * @param p_oInflater
	 *            the current Layout inflater
	 * @return {@link MMLinearLayout} containing the planning panel
	 */
	private MMWorkspaceColumn computeListColumn(LayoutInflater p_oInflater, ManagementGroup p_oGroup) {
		MMWorkspaceColumn r_oMMWorkspaceColumn = this.getColumnByGroup(p_oGroup);
		if (r_oMMWorkspaceColumn == null) {
			r_oMMWorkspaceColumn = new MMWorkspaceColumn();
			r_oMMWorkspaceColumn.setList(true);
			
			MMLinearLayout oLayoutColumn = new MMLinearLayout(this.getContext());
			oLayoutColumn.setFocusableInTouchMode(true);
			
			LinearLayout.LayoutParams oParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			oLayoutColumn.setLayoutParams(oParams);
			oLayoutColumn.setOrientation(VERTICAL);

			// traitement de la liste qui est en première position
			ManagementZone oMainWorkspaceZone = p_oGroup.getVisibleZones().get(0);
			p_oInflater.inflate(this.getAndroidApplication().getAndroidIdByStringKey(ApplicationRGroup.LAYOUT, oMainWorkspaceZone.getSource()), oLayoutColumn);
			this.getAndroidApplication().computeId(oLayoutColumn, oMainWorkspaceZone.getName());

			r_oMMWorkspaceColumn.setViewGroup(oLayoutColumn);
		}
		else {
			// il faut retailler le pannel
			r_oMMWorkspaceColumn.getViewGroup().setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		if (r_oMMWorkspaceColumn.getViewGroup().getParent()!=null) {
			((ViewGroup)r_oMMWorkspaceColumn.getViewGroup().getParent()).removeView(r_oMMWorkspaceColumn.getViewGroup());
		}
		return r_oMMWorkspaceColumn;
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
	private MMWorkspaceColumn computeDetailColumn(LayoutInflater p_oInflater, ManagementGroup p_oGroup) {
		MMWorkspaceColumn r_oMMWorkspaceColumn = this.getColumnByGroup(p_oGroup);
		if ( r_oMMWorkspaceColumn == null ) {
			
			r_oMMWorkspaceColumn = new MMWorkspaceColumn();
			r_oMMWorkspaceColumn.setList(false);
			
			MMLinearLayout oLayoutColumn = new MMLinearLayout(this.getContext());
			oLayoutColumn.setFocusableInTouchMode(true);
			LayoutParams oParamsLayout = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			oLayoutColumn.setLayoutParams(oParamsLayout);
			oLayoutColumn.setOrientation(VERTICAL);
			this.getAndroidApplication().computeId(oLayoutColumn, p_oGroup.getName());
			List<ManagementZone> listZones = p_oGroup.getVisibleZones();
			View oSectionLayout = null ;
			for (ManagementZone oZone : listZones) {
				oSectionLayout = p_oInflater.inflate(this.getAndroidApplication().getAndroidIdByStringKey(ApplicationRGroup.LAYOUT,oZone.getSource()),null);
				oSectionLayout.setPadding(1, 1, 1, 1);
				oLayoutColumn.addView(oSectionLayout);
			}
			r_oMMWorkspaceColumn.setViewGroup(oLayoutColumn);
		}
		else {
			// il faut retailler le pannel
			r_oMMWorkspaceColumn.getViewGroup().setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		if ( r_oMMWorkspaceColumn.getViewGroup().getParent() != null ) {
			((ViewGroup)r_oMMWorkspaceColumn.getViewGroup().getParent()).removeView(r_oMMWorkspaceColumn.getViewGroup());
		}
		return r_oMMWorkspaceColumn;
	}
	
	/**
	 * Hide the detail columns
	 * @param p_iColumn the number of the detail column to hide
	 * @param p_bNeedRecompute if true, this method launch recomputeDisplay
	 */
	public void hideDetailColumn(int p_iDetailColumnIndex, boolean p_bNeedRecompute) {
		super.hideColumn(p_iDetailColumnIndex, p_bNeedRecompute);
	}
	
	
	/**
	 * Hide all detail columns
	 */
	public void hideAllDetailColumn(boolean p_bNeedRecompute) {
		// first column is not a detail
		for( int i = 1 ; i < this.getColumns().size(); i++) {
			this.hideDetailColumn(i, false);
		}
		if (p_bNeedRecompute){
			this.recomputeDisplay();
		}
	}
	
	/**
	 * Unhide all detail columns
	 */
	public void unHideDetailColumns(boolean p_bNeedRecompute) {
		this.unHideAllColumns(p_bNeedRecompute);
	}

	//**
	//** EN PLUS
	//**
	
	/**
	 * @return
	 */
	public WorkspaceTabDelegate2<MMWorkspaceMasterDetailLayout> getTabDelegate() {
		return this.tabDelegate;
	}
	
	/**
	 * 
	 */
	public void doAfterSetContentView() {
		this.tabDelegate.doAfterSetContentView();
	}

	//**
	//** DIFFERENT
	//**
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(VMTabConfiguration p_oVMTabConfiguration) {
		super.configurationSetValue(p_oVMTabConfiguration);
		this.tabDelegate.applyTabConfiguration(p_oVMTabConfiguration);
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
		// Nothing to do
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		// nothing to do
	}
	
	@Override
	public CustomFormatter customFormatter() {
		return null;
	}
	
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		//Nothing to do
	}
}
