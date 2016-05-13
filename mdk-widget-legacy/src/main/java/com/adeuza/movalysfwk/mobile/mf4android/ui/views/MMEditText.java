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
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.adeuza.movalysfwk.mobile.mf4android.ui.annotations.BaseComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentValidator;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.base.MMBaseEditText;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.AbstractEntityFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * <p>EditText widget used in the Movalys Mobile product for Android</p>
 */
@BaseComponent(baseName="MMBaseEditText", baseClass="android.widget.EditText", appCompatClass="android.support.v7.widget.AppCompatEditText")
public class MMEditText extends MMBaseEditText implements ConfigurableVisualComponent, InstanceStatable,
		ComponentValidator {

	/** DELAY used before value update in viewmodel */
	private static final int UPDATEVM_DELAY = 200;

	/** identifier of the message "text changed" */
	private static final int MESSAGE_TEXT_CHANGED = 0;

	/** configurable view framework delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<String> aivDelegate = null;

	/** Objet de validation de la valeur selon les paramètres liés au type de données */
	private IFormFieldValidator oValidator ;

	/** Id of the type conforms to */
	private AbstractEntityFieldConfiguration.DataType dataType = AbstractEntityFieldConfiguration.DataType.TYPE_STRING;

	/** Liste des surveillants d'écriture */
	private List<TextWatcher> textWatchers = null;

	/**
	 * Handler for text change and send message to ViewModel
	 */
	private Handler mDelayTextChangedHandler = new Handler() {
		@Override
		public void handleMessage(Message p_oMsg) {
			if (p_oMsg.what == MESSAGE_TEXT_CHANGED) {
				MMEditText.this.aivFwkDelegate.changed();
			}
		}
	};

	/**
	 * Constructs a new MMEditText
	 * @param p_oContext the context to use
	 * @see EditText#EditText(Context)
	 */
	@SuppressWarnings("unchecked")
	public MMEditText(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<String>) DelegateInitialiserHelper.initDelegate(this,
					new Class[]{Class.class, AttributeSet.class},
					new Object[]{String.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this,
					new Class[]{AttributeSet.class},
					new Object[]{null});
			this.defineParameters(null);
		}
	}
	/**
	 * Constructs a new MMEditText
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see EditText#EditText(Context, AttributeSet)
	 */
	@SuppressWarnings("unchecked")
	public MMEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<String>) DelegateInitialiserHelper.initDelegate(this,
					new Class[]{Class.class, AttributeSet.class},
					new Object[]{String.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this,
					new Class[]{AttributeSet.class},
					new Object[]{p_oAttrs});
			this.defineParameters(p_oAttrs);
		}
	}

	/**
	 * Constructs a new MMEditText
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see EditText#EditText(Context, AttributeSet, int)
	 */
	@SuppressWarnings("unchecked")
	public MMEditText(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<String>) DelegateInitialiserHelper.initDelegate(this,
					new Class[]{Class.class, AttributeSet.class},
					new Object[]{String.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this,
					new Class[]{AttributeSet.class},
					new Object[]{p_oAttrs});
			this.defineParameters(p_oAttrs);
		}
	}

	/**
	 * Récupère et conserve les données du paramètrage XML du composant
	 * Spécifie le type de données lié et son validateur
	 * @param p_oAttrs paramétrage XML du composant
	 */
	private void defineParameters( AttributeSet p_oAttrs ){
		this.aivFwkDelegate.defineParameters(p_oAttrs);
		String sType = this.aivFwkDelegate.getAttributes().get(ConfigurableVisualComponent.Attribute.DATA_TYPE_ATTRIBUTE) ;
		if (sType != null && sType.length() > 0) {
			this.dataType = AbstractEntityFieldConfiguration.DataType.valueOf( sType.toUpperCase(Locale.getDefault()) );
		}
		if ( this.dataType != null && !isInEditMode()){
			oValidator = BeanLoader.getInstance().getBean( this.dataType.getValidatorClass() ) ;
		}
	}

	/**
	 * called wen the inflator finished the job
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode() && oValidator!= null) {
			oValidator.addParametersToConfiguration( this.aivFwkDelegate.getAttributes() , (BasicComponentConfiguration)this.aivFwkDelegate.getConfiguration() );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}
	/**
	 * Modifie le type paramétré et récupère les attribus adéquat pour le validateur
	 * @param p_iDataType nouvelle valeur du type paramétré
	 */
	public void setDataType(AbstractEntityFieldConfiguration.DataType p_iDataType) {
		this.dataType = p_iDataType;
		oValidator = BeanLoader.getInstance().getBean( this.dataType.getValidatorClass() ) ;
		oValidator.addParametersToConfiguration( this.aivFwkDelegate.getAttributes() , (BasicComponentConfiguration)this.aivFwkDelegate.getConfiguration() );
	}

	/**
	 * Set a seletion on the parameter {@link EditText}
	 * @param p_oEditText the {@link EditText} to set selection on
	 * @param p_iStart the start selection index
	 * @param p_iStop the en selection index
	 */
	public void setEditTextSelection(EditText p_oEditText, int p_iStart, int p_iStop) {
		if (p_iStart != -1 && p_iStop != -1) {
			p_oEditText.setSelection(p_iStart, p_iStop);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	protected void onTextChanged(CharSequence p_oText, int p_oStart, int p_oBefore, int p_oAfter) {
		super.onTextChanged(p_oText, p_oStart, p_oBefore, p_oAfter);

		// Dans le cas des nombres décimaux, android autorise la saisie ".1234" mais l'appel à Double.parseDouble(".1234") provoque une exception.
		// On corrige ce point au niveau composant, android ne gérant pas correctement le séparateur décimal en fonction de la local.
		Editable r_oEditable = super.getText();
		if (r_oEditable != null && r_oEditable.length() > 0
				&& (this.getInputType() & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_NUMBER
				&& (this.getInputType() & InputType.TYPE_MASK_FLAGS) == InputType.TYPE_NUMBER_FLAG_DECIMAL
				&& r_oEditable.charAt(0) == '.' ) {
			r_oEditable.insert(0, "0");
		}

		if ( this.aivDelegate!=null && !this.aivDelegate.isWritingData() ) {
			mDelayTextChangedHandler.removeMessages(MESSAGE_TEXT_CHANGED);
			final Message msg = Message.obtain(mDelayTextChangedHandler, MESSAGE_TEXT_CHANGED, p_oText);
			mDelayTextChangedHandler.sendMessageDelayed(msg, UPDATEVM_DELAY);
		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Parcelable superOnSaveInstanceState() {

		Parcelable superParcel = super.onSaveInstanceState();
		Bundle r_oBundle = new Bundle();

		r_oBundle.putParcelable("parentParcelable", superParcel);
		String valueToSave = this.getText().toString();
		if(this.aivFwkDelegate.customFormatter() != null) {
			valueToSave = (String) this.aivFwkDelegate.customFormatter().unformat(valueToSave);
		}
		r_oBundle.putString("edittext", valueToSave);

		return r_oBundle;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;

		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.onRestoreInstanceState(oParentParcelable);
		this.aivDelegate.configurationSetValue(r_oBundle.getString("edittext"));

	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivFwkDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}
	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {

		// On désactive les watchers, sinon leur méthode afterTextChanged sera invoquée
		// et ils seront marqués comme modifiés alors que nous sommes en mode restauration.
		List<TextWatcher> listBackupWatchers = null ;
		if ( this.textWatchers != null ) {
			listBackupWatchers = new ArrayList<>(this.textWatchers);
			this.clearTextChangedListeners();
		}

		//super.onRestoreInstanceState(((BaseSavedState) p_oState).getSuperState());
		this.aivFwkDelegate.onRestoreInstanceState(p_oState);

		// Réactivation des watchers
		if ( listBackupWatchers != null ) {
			for( TextWatcher oWatcher: listBackupWatchers ) {
				this.addTextChangedListener(oWatcher);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#setInputType(int)
	 */
	@Override
	public void setInputType(int p_oType) {

		// aivFwkDelegate is null at init of aivDelegate
		if (  this.aivFwkDelegate != null ) {
			// Le setInputType provoque un changement sur le composant.
			// S'il n'y en avait pas avant, on reset le flag de changement
			final boolean bChanged = this.aivFwkDelegate.isChanged();
			super.setInputType(p_oType);
			if (!bChanged) {
				this.aivFwkDelegate.resetChanged();
			}
		}
		else {
			super.setInputType(p_oType);
		}
	}

	/**
	 *
	 * {@inheritDoc}
	 * @see android.widget.TextView#addTextChangedListener(android.text.TextWatcher)
	 */
	@Override
	public void addTextChangedListener(TextWatcher p_oWatcher)
	{
		if (textWatchers == null)
		{
			textWatchers = new ArrayList<>();
		}
		textWatchers.add(p_oWatcher);

		super.addTextChangedListener(p_oWatcher);
	}
	/**
	 *
	 * {@inheritDoc}
	 * @see android.widget.TextView#removeTextChangedListener(android.text.TextWatcher)
	 */
	@Override
	public void removeTextChangedListener(TextWatcher p_oWatcher)
	{
		if (textWatchers != null) {
			textWatchers.remove(p_oWatcher);
		}
		super.removeTextChangedListener(p_oWatcher);
	}

	/**
	 *  Nettoie les inspecteurs de texte
	 */
	public void clearTextChangedListeners() {
		if(textWatchers != null)
		{
			for(TextWatcher oWatcher : textWatchers)
			{
				super.removeTextChangedListener(oWatcher);
			}

			textWatchers.clear();
			textWatchers = null;
		}
	}

	/**
	 *
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getComponentFwkDelegate()
	 */
	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}
	/**
	 *
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getComponentDelegate()
	 */
	@Override
	public VisualComponentDelegate<String> getComponentDelegate() {
		return this.aivDelegate;
	}


	/************************************************************************************************
	 ******************************** Framework delegate callback ***********************************
	 ************************************************************************************************/

	/**
	 * {@inheritDoc}
	 * @see ComponentValidator#getValidator()
	 */
	@Override
	public IFormFieldValidator getValidator() {
		return this.oValidator;
	}

}
