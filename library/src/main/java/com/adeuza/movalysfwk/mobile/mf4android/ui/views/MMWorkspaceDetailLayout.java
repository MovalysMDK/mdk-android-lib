package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;

/**
 * Workspace with only detail columns
 * 
 * @author lmichenaud
 * @deprecated Replaced by {@link MMWorkspaceDetailFragmentLayout}
 * 
 */
public class MMWorkspaceDetailLayout extends MMBaseWorkspaceLayout<NoneType> {

	/**
	 * @param p_oContext
	 */
	public MMWorkspaceDetailLayout(Context p_oContext) {
		super(p_oContext);
	}

	/**
	 * @param p_oContext
	 * @param p_oAttrs
	 */
	public MMWorkspaceDetailLayout(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, NoneType.class);
	}

	/**
	 * Workspace initialization
	 */
	@Override
	public void customInit() {
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMBaseWorkspaceLayout#computeColumn(int, android.view.LayoutInflater, com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup)
	 */
	@Override
	protected MMWorkspaceColumn computeColumn( int p_iColumnIndex, LayoutInflater p_oInflater,
			ManagementGroup p_oGroup) {
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
		// Nothing to do 
	}
	
	@Override
	public CustomFormatter customFormatter() {
		return null;
	}
	
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		//Nothing to do
	}
}
