package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;

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
 */
public class MMWorkspaceDetailFragmentLayout extends MMBaseWorkspaceFragmentLayout<NoneType> {
	
	/**
	 * Constructs a new MMLinearLayout
	 * 
	 * @param p_oContext the context to use
	 * @see LinearLayout#LinearLayout(Context)
	 */
	public MMWorkspaceDetailFragmentLayout(Context p_oContext) {
		super(p_oContext);
	}
	
	/**
	 * Constructs a new MMLinearLayout.
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see LinearLayout#LinearLayout(Context)
	 */
	public MMWorkspaceDetailFragmentLayout(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, NoneType.class );
	}
	
	/**
	 * Init the component.
	 * @param p_oAttrs inflated attributes to use
	 */
	@Override
	public void customInit() {

		// nothing
		
	}
	
	/**
	 * Unhide all detail columns
	 */
	public void unHideDetailColumns(boolean p_bNeedRecompute) {
//		this.unHideAllColumns(p_bNeedRecompute);
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
	@Override
	public CustomFormatter customFormatter() {
		return null;
	}
	
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		//Nothing to do
	}

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		// Nothing to do
	}

	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		// Nothing to do
	}
}
