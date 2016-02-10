package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMSignatureDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.drawing.AndroidSignatureHelperImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.drawing.MMPoint;

/**
 * <p>
 * This component represents the signature on the form. This component contain
 * the dialog bix to draw or erase the signature. The drawing is realized by
 * {@link MMSignatureDrawing}
 * </p>
 * 
 * <p>
 * Copyright (c) 2010
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 * @author dmaurange, fbourlieux
 */
public class MMSignature extends AbstractMMLinearLayout<String> implements
OnClickListener, OnDismissListener, MMComplexeComponent {

	/** widget to display a signature */
	private MMSignaturePaint uiSignaturePainter;

	/** the current displayed bitmap */
	private Path currentPath = null;
	/** the current customer's signature */
	private List<List<MMPoint>> currentSignature;

	/** the current displayed bitmap */
	private Path temporaryDialogPath = null;
	/** the current customer's signature */
	private List<List<MMPoint>> temporaryDialogSignature;

	/** Component describing the error */
	private TextView errorComponent;

	/** the drawing dialog */
	private MMSignatureDialogFragment drawDialog = null;

	/**
	 * <p>
	 * Construct an object <em>MMSignature</em> with parameters.
	 * </p>
	 * 
	 * @param p_oContext
	 *            the Android context
	 */
	public MMSignature(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.init();
		}
	}


	@Override
	public boolean hasFocus() {
		if(!isInEditMode()) {
			return this.uiSignaturePainter.hasFocus();
		}
		return false;
	}

	/**
	 * <p>
	 * Construct an object <em>MMSignature</em> with parameters.
	 * </p>
	 * 
	 * @param p_oContext
	 *            the Android context
	 * @param p_oAttrs
	 *            some parameters
	 */
	public MMSignature(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.init();
		}
	}

	/**
	 * Inflat the component thanks to the XML fwk_component_signature_imageview
	 */
	protected final void init() {
		LayoutInflater oInflater = LayoutInflater.from(this.getContext());

		AndroidApplication oApplication = (AndroidApplication) Application
				.getInstance();
		oInflater
		.inflate(
				oApplication
				.getAndroidIdByRKey(AndroidApplicationR.fwk_component_signature_imageview),
				this);

		this.uiSignaturePainter = (MMSignaturePaint) this
				.findViewById(oApplication
						.getAndroidIdByRKey(AndroidApplicationR.component_signature__signature__image));
		this.uiSignaturePainter.setBackgroundResource(oApplication
				.getAndroidIdByRKey(AndroidApplicationR.textfield_selector));
		this.errorComponent = (TextView) this
				.findViewById(oApplication
						.getAndroidIdByRKey(AndroidApplicationR.component_signature__error));

		this.setFocusable(true);
		this.setFocusableInTouchMode(true);

		if(this.uiSignaturePainter != null) {
			this.uiSignaturePainter.setClickable(true);
			this.uiSignaturePainter.setOnClickListener(this);
		}	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(String p_sSignature) {
		this.aivDelegate.configurationSetValue(p_sSignature);
		if (p_sSignature != null) {
			this.currentSignature = AndroidSignatureHelperImpl.getInstance()
					.convertFromStringToPoints(p_sSignature);
			this.currentPath = AndroidSignatureHelperImpl.getInstance()
					.convertFromStringToPath(p_sSignature);
			this.uiSignaturePainter.setPath(this.currentPath);
			this.aivDelegate.setFilled(true);
		}
		else {
			this.currentSignature = new ArrayList<>();
			this.currentPath = new Path();
			this.uiSignaturePainter.setPath(this.currentPath);
			this.aivDelegate.setFilled(false);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String configurationGetValue() {
		return AndroidSignatureHelperImpl.getInstance()
				.convertFromPointsToString(this.currentSignature);
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
	public void configurationDisabledComponent() {
		this.uiSignaturePainter.setOnClickListener(null);
		this.setFocusable(false);
		this.setFocusableInTouchMode(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationEnabledComponent() {
		this.uiSignaturePainter.setOnClickListener(this);
		this.setFocusable(false);
		this.setFocusableInTouchMode(true);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout#configurationSetError(java.lang.String)
	 */
	@Override
	public void configurationSetError(String p_sError) {
		this.errorComponent.setError(p_sError);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout#configurationSetError(java.lang.String)
	 */
	@Override
	public void configurationUnsetError() {
		if(this.errorComponent.getError() != null) {
			this.errorComponent.setError(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getValueType() {
		return String.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(String p_oObject) {
		return p_oObject == null || p_oObject.length() == 0;
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
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.currentSignature != null
				&& !this.currentSignature.isEmpty()
				&& !this.currentSignature.iterator().next().isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oParamView) {
		final AndroidApplication oApplication = (AndroidApplication) Application
				.getInstance();
		final int lParamViewId = p_oParamView.getId();

		// clic sur la signature => Ouverture de la zone de dessin
		if (lParamViewId == oApplication
				.getAndroidIdByRKey(AndroidApplicationR.component_signature__signature__image)) {
			this.uiSignaturePainter.requestFocusFromTouch();
			this.configurationUnsetError();
			this.drawDialog = (MMSignatureDialogFragment) createDialogFragment(null);
			prepareDialogFragment(this.drawDialog, null);
			this.drawDialog.show(getFragmentActivityContext()
					.getSupportFragmentManager(), this.drawDialog
					.getFragmentDialogTag());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.content.DialogInterface.OnDismissListener#onDismiss(android.content.DialogInterface)
	 */
	@Override
	public void onDismiss(DialogInterface p_oParamDialogInterface) {
		if (this.temporaryDialogPath != null && this.temporaryDialogSignature != null) {
			this.currentPath = new Path(this.temporaryDialogPath);
			this.currentSignature = copySignature(this.temporaryDialogSignature);
			this.uiSignaturePainter.setPath(new Path(this.temporaryDialogPath));
		}
		this.drawDialog = null;
		this.temporaryDialogPath = null;
		this.temporaryDialogSignature = null;

		String sCurrentSig = AndroidSignatureHelperImpl.getInstance()
				.convertFromPointsToString(this.currentSignature);
		this.configurationSetValue(sCurrentSig);
		this.aivDelegate.changed();
	}

	public Path getPath() {
		return this.currentPath;
	}

	public List<List<MMPoint>> getSignature() {
		return this.currentSignature;
	}

	/**
	 * Returns the fragmentActivit context
	 * 
	 * @return a fragmentActivity context
	 */
	private ActionBarActivity getFragmentActivityContext() {
		return  (ActionBarActivity) ((AndroidApplication) Application.getInstance()).getCurrentActivity();
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState;
		Parcelable oParentParcelable = r_oBundle
				.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		String sCurrentSig = r_oBundle.getString("signature");

		this.configurationSetValue(sCurrentSig);

	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);

		String sCurrentSig = AndroidSignatureHelperImpl.getInstance()
				.convertFromPointsToString(this.currentSignature);
		r_oBundle.putString("signature", sCurrentSig);

		return r_oBundle;
	}

	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		MMDialogFragment oDialogFragment = MMSignatureDialogFragment.newInstance(this);
		return oDialogFragment;
	}

	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		((MMSignatureDialogFragment) p_oDialog)
		.setTemporaryPathValue(new Path(this.currentPath));

		List<List<MMPoint>> oSignatureCopy = copySignature(this.currentSignature);

		((MMSignatureDialogFragment) p_oDialog)
		.setTemporarySignatureValue(oSignatureCopy);
	}

	@Override
	public void onActivityResult(int p_iRequestCode, int p_oResultCode,
			Intent p_oIntent) {
		// Nothing to do
	}

	/**
	 * Allows to do a copy of the signature 
	 * (copying a list of a list of points)
	 * @param p_oSignature The list of list of points to copy
	 * @return The copy of the signature object
	 */
	private List<List<MMPoint>> copySignature(List<List<MMPoint>> p_oSignature) {
		List<List<MMPoint>> oCopy = new ArrayList<>();
		for(List<MMPoint> listOfPoints : p_oSignature) {
			List<MMPoint> listOfPointsCopy = new ArrayList<>();
			for(MMPoint point : listOfPoints) {
				listOfPointsCopy.add(new MMPoint(point));
			}
			oCopy.add(listOfPointsCopy);
		}
		return oCopy;
	}

	public void setTemporaryDialogPath(Path p_oTemporaryDialogPath) {
		this.temporaryDialogPath = p_oTemporaryDialogPath;
	}

	public void setTemporaryDialogSignature(
			List<List<MMPoint>> p_oTemporaryDialogSignature) {
		this.temporaryDialogSignature = p_oTemporaryDialogSignature;
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
	
	@Override
	public void setFragmentTag(String p_sFragmentTag) {
		super.setFragmentTag(p_sFragmentTag);
	}
}
