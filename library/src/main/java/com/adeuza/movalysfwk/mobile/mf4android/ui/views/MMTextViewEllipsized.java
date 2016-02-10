package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>TextView Ellipsized widget used in the Movalys Mobile product for Android</p>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * @author dmaurange
 */

public class MMTextViewEllipsized extends TextView implements ConfigurableVisualComponent<String>, MMIdentifiableView, InstanceStatable {

	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentDelegate<String> aivDelegate = null;

	private boolean autoHide;

	private static final String ELLIPSIS = "...";

	private static final int MAX_LINES_NUMBER = 4;

	public interface EllipsizeListener {
		void ellipsizeStateChanged(boolean p_bEllipsized);
	}

	private final List<EllipsizeListener> ellipsizeListeners = new ArrayList<>();
	private boolean isEllipsized;
	private boolean isStale;
	private boolean programmaticChange;
	private String fullText;
	private int maxLines = -1;
	private float lineSpacingMultiplier = 1.0F;
	private float lineAdditionalVerticalPadding = 0.0F;


	/**
	 * Constructs a new MMTextView
	 * @param p_oContext the context to use
	 * @see TextView#TextView(Context)
	 */
	public MMTextViewEllipsized(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
			this.autoHide = true;
		}
	}

	/**
	 * Constructs a new MMTextView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see TextView#TextView(Context, AttributeSet)
	 */
	public MMTextViewEllipsized(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
			this.autoHide = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "auto-hide", true);
			this.setMaxLines(MAX_LINES_NUMBER);
			this.setVerticalScrollBarEnabled(true);
			this.setMovementMethod(new ScrollingMovementMethod());
			//LMI: la ligne ci-dessous fait planter le sliding dans le workspace:
			//		this.setScrollbarFadingEnabled(false);
		}
	}


	/**
	 * Constructs a new MMTextView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see TextView#TextView(Context, AttributeSet, int)
	 */
	public MMTextViewEllipsized(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
			this.autoHide = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "auto-hide", true);
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
	public void configurationSetValue(String p_oObject) {
		this.aivDelegate.configurationSetValue(p_oObject);
		if (p_oObject!=null) {
			this.setText(p_oObject);
		}
		else {
			this.setText(StringUtils.EMPTY);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		if (p_oObjectsToSet != null && p_oObjectsToSet.length == 1) {
			this.configurationSetValue(p_oObjectsToSet[0]);
		}
		else {
			this.setText(StringUtils.EMPTY);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String configurationGetValue() {
		return this.fullText;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return new String[] { this.configurationGetValue() };
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
		return false;
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(String p_oObject) {
		return (p_oObject == null || p_oObject.length() == 0) && this.autoHide;
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#hide()
	 */
	@Override
	public void hide() {
		this.aivDelegate.hide();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#unHide()
	 */
	@Override
	public void unHide() {
		this.aivDelegate.unHide();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationPrepareHide(java.util.List)
	 */
	@Override
	public void configurationPrepareHide(List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		this.aivDelegate.configurationPrepareHide(p_oComponentsToHide);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return String.class;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getConfigurationSetValueMethod()
	 */
	@Override
	public Method getConfigurationSetValueMethod() {
		return this.aivDelegate.getConfigurationSetValueMethod();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		this.aivDelegate.configurationDisabledComponent();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.getText().toString().length()>0;
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		if (this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationRemoveMandatoryLabel();
			this.setText(
					this.getText().toString().substring(0, this.getText().toString().length()-"(*)".length()));
		}
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		if (!this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationSetMandatoryLabel();
			if ( !this.getText().toString().endsWith("(*)") ) {
				this.setText(this.getText().toString() + "(*)");
			}
		}
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
		if(this.getError() != null) {
			this.setError(null);
		}
	}

	@Override
	public void configurationSetError(String p_oError) {
		this.setError(p_oError);
	}

	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
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
	 * {@inheritDoc}
	 * @see android.widget.TextView#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivDelegate.onRestoreInstanceState(p_oState);
	}


	public void addEllipsizeListener(EllipsizeListener p_oListener) {
		if (p_oListener == null) {
			throw new NullPointerException();
		}
		ellipsizeListeners.add(p_oListener);
	}

	public void removeEllipsizeListener(EllipsizeListener p_oListener) {
		ellipsizeListeners.remove(p_oListener);
	}

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

	private Layout createWorkingLayout(String p_sWorkingText) {
		return new StaticLayout(p_sWorkingText, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(),
				Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineAdditionalVerticalPadding, false);
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
