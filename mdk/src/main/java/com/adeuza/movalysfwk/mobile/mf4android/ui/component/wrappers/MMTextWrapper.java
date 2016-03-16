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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.wrappers;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.AbstractEntityFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.AbstractComponentWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MMTextWrapper extends AbstractComponentWrapper<TextView> implements TextWatcher, ComponentValidator {

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
				MMTextWrapper.this.aivFwkDelegate.changed();
			}
		}
	};
	
	public MMTextWrapper() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setComponent(View p_oComponent, boolean p_bIsRestoring) {
		super.setComponent(p_oComponent, p_bIsRestoring);

		this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<String>) DelegateInitialiserHelper.initDelegate(this, 
				new Class[]{Class.class, AttributeSet.class}, 
				new Object[]{String.class, null});
		this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
				new Class[]{AttributeSet.class}, 
				new Object[]{null});
		
		if (ComponentsAttributesHelper.getInstance().hasAttributesForComponent(((View)p_oComponent).getId())) {
			defineParameters(ComponentsAttributesHelper.getInstance().getAttributesForComponent(((View)p_oComponent).getId()));	
			oValidator.addParametersToConfiguration( this.aivFwkDelegate.getAttributes() , (BasicComponentConfiguration)this.aivFwkDelegate.getConfiguration() );
		}
		
		this.addTextChangedListener(this);		
	}

	@Override
	public void unsetComponent() {
		this.removeTextChangedListener(this);
	}
	
	@Override
	public AndroidConfigurableVisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public AndroidConfigurableVisualComponentDelegate<String> getComponentDelegate() {
		return this.aivDelegate;
	}

	/**
	 * Récupère et conserve les données du paramètrage XML du composant
	 * Spécifie le type de données lié et son validateur
	 * @param p_oAttrs paramétrage XML du composant
	 */
	private void defineParameters( Map<ConfigurableVisualComponent.Attribute,String> p_oAttrs ){
		this.aivFwkDelegate.defineParameters(p_oAttrs);
		String sType = this.aivFwkDelegate.getAttributes().get(ConfigurableVisualComponent.Attribute.DATA_TYPE_ATTRIBUTE) ;
		if (sType != null && sType.length() > 0) {
			this.dataType = AbstractEntityFieldConfiguration.DataType.valueOf( sType.toUpperCase(Locale.getDefault()) );
		}
		if ( this.dataType != null){
			oValidator = BeanLoader.getInstance().getBean( this.dataType.getValidatorClass() ) ;
		}
	}
	
	
	public void setId(int p_oId) {
		this.component.get().setId(p_oId);
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
	public void onTextChanged(CharSequence p_oText, int p_oStart, int p_oBefore, int p_oAfter) {
		if(this.component.get().getText() instanceof Editable) {
			Editable r_oEditable = (Editable) this.component.get().getText();
			if (r_oEditable != null && r_oEditable.length() > 0
					&& (this.component.get().getInputType() & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_NUMBER
					&& (this.component.get().getInputType() & InputType.TYPE_MASK_FLAGS) == InputType.TYPE_NUMBER_FLAG_DECIMAL
					&& r_oEditable.charAt(0) == '.') {
				r_oEditable.insert(0, "0");
			}
		}
		
		if ( this.aivDelegate!=null && !this.aivDelegate.isWritingData() ) {
			mDelayTextChangedHandler.removeMessages(MESSAGE_TEXT_CHANGED);
			final Message msg = Message.obtain(mDelayTextChangedHandler, MESSAGE_TEXT_CHANGED, p_oText);
			mDelayTextChangedHandler.sendMessageDelayed(msg, UPDATEVM_DELAY);
		}
	}
	
	public void setInputType(int p_oType) {

		// aivFwkDelegate is null at init of aivDelegate
		if (  this.aivFwkDelegate != null ) {
			// Le setInputType provoque un changement sur le composant.
			// S'il n'y en avait pas avant, on reset le flag de changement
			final boolean bChanged = this.aivFwkDelegate.isChanged();
			this.component.get().setInputType(p_oType);
			if (!bChanged) {
				this.aivFwkDelegate.resetChanged();
			}
		}
		else {
			this.component.get().setInputType(p_oType);
		}
	}

	
	public void addTextChangedListener(TextWatcher p_oWatcher)
	{       
		if (textWatchers == null) 
		{
			textWatchers = new ArrayList<>();
		}
		textWatchers.add(p_oWatcher);

		this.component.get().addTextChangedListener(p_oWatcher);
	}

	public void removeTextChangedListener(TextWatcher p_oWatcher)
	{       
		if (textWatchers != null) {
			textWatchers.remove(p_oWatcher);
		}
		this.component.get().removeTextChangedListener(p_oWatcher);
	}

	/**
	 *  Nettoie les inspecteurs de texte
	 */
	public void clearTextChangedListeners() {
		if(textWatchers != null)
		{
			for(TextWatcher oWatcher : textWatchers)
			{
				this.component.get().removeTextChangedListener(oWatcher);
			}

			textWatchers.clear();
			textWatchers = null;
		}
	}
	
	public IFormFieldValidator getValidator() {
		return this.oValidator;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// nothing to do
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// nothing to do
	}
	
}
