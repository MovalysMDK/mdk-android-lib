/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
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
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentError;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMSignatureDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.drawing.AndroidSignatureHelperImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.drawing.MMPoint;

/**
 * <p>
 * This component represents the signature on the form. This component contain
 * the dialog bix to draw or erase the signature. The drawing is realized by
 * {@link MMSignatureDrawing}
 * </p>
 * 
 */
public class MMSignature extends AbstractMMLinearLayout<String> implements OnClickListener, OnDismissListener, MMComplexeComponent, 
	ComponentError, ComponentEnable, 
	ComponentReadableWrapper<String>, ComponentWritableWrapper<String> {

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
	 * Component request code
	 */
	private int requestCode = -1;
	
	/**
	 * <p>
	 * Construct an object <em>MMSignature</em> with parameters.
	 * </p>
	 * 
	 * @param p_oContext
	 *            the Android context
	 */
	public MMSignature(Context p_oContext) {
		super(p_oContext, String.class);
		if(!isInEditMode()) {
			this.init();
		}
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
		super(p_oContext, p_oAttrs, String.class);
		if(!isInEditMode()) {
			this.init();
		}
	}

	/**
	 * Inflates the component thanks to the XML fwk_component_signature_imageview
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
			this.uiSignaturePainter.setOnClickListener(this);
		}	
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
			this.aivDelegate.configurationUnsetError();
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
		this.aivDelegate.configurationSetValue(sCurrentSig);
		this.aivFwkDelegate.changed();
	}

	/**
	 * Get the current path
	 * @return the current path in the component
	 */
	public Path getPath() {
		return this.currentPath;
	}

	/**
	 * Get the current signature
	 * @return the current signature (a List<List<MMPoint>>)
	 */
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

		this.aivDelegate.configurationSetValue(sCurrentSig);

		this.requestCode = r_oBundle.getInt("requestCode");
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);

		String sCurrentSig = AndroidSignatureHelperImpl.getInstance()
				.convertFromPointsToString(this.currentSignature);
		r_oBundle.putString("signature", sCurrentSig);
		r_oBundle.putInt("requestCode", this.requestCode);

		return r_oBundle;
	}

	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		return MMSignatureDialogFragment.newInstance(this);
	}

	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog, Bundle p_oDialogFragmentArguments) {
		
		if(p_oDialog instanceof MMSignatureDialogFragment) {
			((MMSignatureDialogFragment) p_oDialog).setTemporaryPathValue(new Path(this.currentPath));
		}

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

	/**
	 * Set temporary dialog path
	 * @param p_oTemporaryDialogPath the path to set
	 */
	public void setTemporaryDialogPath(Path p_oTemporaryDialogPath) {
		this.temporaryDialogPath = p_oTemporaryDialogPath;
	}

	/**
	 * Set temporary dialog signature
	 * @param p_oTemporaryDialogSignature the signature to set
	 */
	public void setTemporaryDialogSignature(List<List<MMPoint>> p_oTemporaryDialogSignature) {
		this.temporaryDialogSignature = p_oTemporaryDialogSignature;
	}

	/***************************************************************************************
	 ****************************** Framework delegate callback ****************************
	 ***************************************************************************************/
	

	/**
	 * {@inheritDoc}
	 * @see ComponentError#getErrorView()
	 */
	@Override
	public TextView getErrorView() {
		return this.errorComponent;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentEnable#enableComponent(boolean)
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		this.uiSignaturePainter.setEnabled(p_bEnable);
		if (p_bEnable) {
			this.uiSignaturePainter.setOnClickListener(this);
		} else {
			this.uiSignaturePainter.setOnClickListener(null);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public String configurationGetValue() {
		return AndroidSignatureHelperImpl.getInstance().convertFromPointsToString(this.currentSignature);
	}
	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		if (p_oObjectToSet != null) {
			this.currentSignature = AndroidSignatureHelperImpl.getInstance().convertFromStringToPoints((String) p_oObjectToSet);
			this.currentPath = AndroidSignatureHelperImpl.getInstance().convertFromStringToPath((String) p_oObjectToSet);
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
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.currentSignature != null
				&& !this.currentSignature.isEmpty()
				&& !this.currentSignature.iterator().next().isEmpty();
	}

	@Override
	public int getRequestCode() {
		if (this.requestCode == -1) {
			this.requestCode = this.hashCode() & REQUEST_CODE_MASK;
		}
		return this.requestCode;
	}
}
