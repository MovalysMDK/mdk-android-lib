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

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentSavedState;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentError;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.common.AbstractNamableObject;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * <p>RadioGroup widget used in the Movalys Mobile product for Android</p>
 */
public class MMRadioGroup extends RadioGroup implements ConfigurableVisualComponent, OnCheckedChangeListener, InstanceStatable, 
ComponentError, ComponentEnable, ComponentReadableWrapper<Enum<?>>, ComponentWritableWrapper<Enum<?>> {

	/** VALUE none */
	public static final String FWK_NONE = "FWK_NONE";
	
	/** initial value */
	public static final int INIT_VALUE = -1 ;

	/** local storage of a FWK_NONE if initialized with no radioButton selected*/
	private Enum<?> noneValue=null;

	/** the id of the component on which error are displayed */
	private int errorComponentId;
	
	/** View on which errors are displayed */
	private TextView errorComponent;

	/** configurable view framework delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentDelegate<Enum<?>> aivDelegate = null;
	
	/** map of radio buttons by key */
	private Map<String, MMRadioButton> radioByKey ;
	
	/** listener on check change */
	private OnCheckedChangeListener onCheckedChangeListener;
	
	/**
	 * Constructs a new MMRadioGroup
	 * @param p_oContext
	 *            the context to use
	 * @see RadioGroup#RadioGroup(Context, AttributeSet)
	 */
	public MMRadioGroup(Context p_oContext) {
		this(p_oContext,null);
	}
	
	/**
	 * Constructs a new MMRadioGroup
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @see RadioGroup#RadioGroup(Context, AttributeSet)
	 */
	@SuppressWarnings("unchecked")
	public MMRadioGroup(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			if( p_oAttrs != null ){
				this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Enum<?>>) DelegateInitialiserHelper.initDelegate(this, 
						new Class[]{Class.class, AttributeSet.class}, 
						new Object[]{Enum.class, p_oAttrs});
				this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
						new Class[]{AttributeSet.class}, 
						new Object[]{p_oAttrs});
				this.errorComponentId =  p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "error-component", 0);
			}
			this.radioByKey = new HashMap<>();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onFinishInflate() {
		if(!isInEditMode()) {
			super.setOnCheckedChangeListener(this);
			if (this.errorComponentId > 0) {
				this.errorComponent = (TextView) ((View) this.getParent()).findViewById(this.errorComponentId);
			}
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
	 * 
	 * {@inheritDoc}
	 * @see android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android.widget.RadioGroup, int)
	 */
	@Override
	public void onCheckedChanged(RadioGroup p_oRadioGroup, int p_ibCheckedId) {
		if ( this.aivDelegate != null && !this.aivDelegate.isWritingData() ) {
			this.aivFwkDelegate.changed();
			if (this.onCheckedChangeListener != null) {
				this.onCheckedChangeListener.onCheckedChanged(p_oRadioGroup, p_ibCheckedId);
			}
		}
		this.aivDelegate.configurationUnsetError();
	}

	/**
	 * Adds a new radio button  to this group. A radio button is defined by its label and its value.
	 * @param p_oKeyValue Value of the radio button to add. Possibly a null or empty value.
	 * @param p_sLabel Label of the radio button to add. Possibly a empty string.
	 */
	public void addRadioButton(String p_oKeyValue, String p_sLabel) {
		MMRadioButton oRadioButton = new MMRadioButton(this.getContext());
		((AndroidApplication) Application.getInstance()).computeId(oRadioButton, this.aivFwkDelegate.getName() + AbstractNamableObject.KEY_SEPARATOR + this.getChildCount());
		oRadioButton.setText(p_sLabel);
		this.addView(oRadioButton);
		this.radioByKey.put(p_oKeyValue, oRadioButton);
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		return super.onSaveInstanceState();
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
	}
	
	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState=superOnSaveInstanceState();
		MMRadioGroupSavedState oState=new MMRadioGroupSavedState(superState);
		oState.setChanged(this.aivFwkDelegate.isChanged());
		oState.setSelectedRadioButtonId(this.getCheckedRadioButtonId());
		oState.setSelectedRadioButtonValue(this.aivDelegate.configurationGetValue());

		return oState;
	}
	
	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		try{
			if(! (p_oState instanceof MMRadioGroupSavedState)){
				superOnRestoreInstanceState(p_oState);
				return;
			}
	
			this.aivDelegate.setWritingData(true);
			
			MMRadioGroupSavedState savedState = (MMRadioGroupSavedState) p_oState;
			// Etat sauvegardé par la classe mère
			superOnRestoreInstanceState(savedState.getSuperState());
			
			//on sélectionne le bouton qui a été sauvegardé
			this.check(savedState.getSelectedRadioButtonId());
			
			this.configurationSetValue(savedState.getSelectedRadioButtonValue());
			
			//on repose l'état
			if (savedState.isChanged()){
				this.aivFwkDelegate.changed();
			}
			else {
				this.aivFwkDelegate.resetChanged();
			}	

	
			this.aivDelegate.setWritingData(false);
			
		}
		catch (Exception e){
			Log.e("MMRadioGroup","Exception dans le onRestore",e);
		}
	
	}

	/**
	 * Object to save the state of the MMRadioGroupComponent
	 * @see AndroidConfigurableVisualComponentSavedState
	 */
	protected static class MMRadioGroupSavedState extends  AndroidConfigurableVisualComponentSavedState{
		/** the selected radiobutton id */
		private int selectedRadioButtonId;
		
		/** the current enum value */
		private Enum<?> selectedRadioButtonValue;

		/**
		 * Create a new save object from parcelable
		 * @param p_oSuperState the parcelable to build on
		 */
		public MMRadioGroupSavedState(Parcelable p_oSuperState) {
			super(p_oSuperState);
		}
		
		/**
		 * Create a new save object from parcel
		 * @param p_oInParcel the pacel to write in
		 */
		public MMRadioGroupSavedState(Parcel p_oInParcel) {
			super(p_oInParcel);
			this.selectedRadioButtonId = p_oInParcel.readInt();
			this.selectedRadioButtonValue = (Enum<?>) p_oInParcel.readSerializable();
		}
		
		/**
		 * Getter for current enum value
		 * @return the current enum value
		 */
		public Enum<?> getSelectedRadioButtonValue() {
			return selectedRadioButtonValue;
		}
		
		/**
		 * Setter for current enum value
		 * @param p_oSelectedRadioButtonValue the value to set
		 */
		public void setSelectedRadioButtonValue(Enum<?> p_oSelectedRadioButtonValue) {
			this.selectedRadioButtonValue = p_oSelectedRadioButtonValue;
		}

		/**
		 * Getter for current check button
		 * @return the selectedRadioButtonId
		 */
		public int getSelectedRadioButtonId() {
			return selectedRadioButtonId;
		}

		/**
		 * Setter for current check button id
		 * @param p_iSelectedRadioButtonId the selectedRadioButtonId to set
		 */
		public void setSelectedRadioButtonId(int p_iSelectedRadioButtonId) {
			this.selectedRadioButtonId = p_iSelectedRadioButtonId;
		}

		@Override
		public void writeToParcel(Parcel p_oOutParcel, int p_iFlags) {
			super.writeToParcel(p_oOutParcel, p_iFlags);
			p_oOutParcel.writeInt(selectedRadioButtonId);
			p_oOutParcel.writeSerializable(selectedRadioButtonValue);
		} 

		/**
		 * CREATOR for this save object
		 */
		public static final Parcelable.Creator<MMRadioGroupSavedState> CREATOR
		= new Parcelable.Creator<MMRadioGroupSavedState>() {
			@Override
			public MMRadioGroupSavedState createFromParcel(Parcel p_oInParcel) {
				return new MMRadioGroupSavedState(p_oInParcel);
			}

			@Override
			public MMRadioGroupSavedState[] newArray(int p_iSize) {
				return new MMRadioGroupSavedState[p_iSize];
			}
		};
	}
	
	@Override
	public void setOnCheckedChangeListener(OnCheckedChangeListener p_oListener) {
		this.onCheckedChangeListener = p_oListener;
	}
	
	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}
	
	@Override
	public VisualComponentDelegate<Enum<?>> getComponentDelegate() {
		return this.aivDelegate;
	}
	
	/**
	 * Get none value
	 * @return the noneValue
	 */
	public Enum<?> getNoneValue() {
		return noneValue;
	}
	
	/**
	 * Set none value
	 * @param p_oNoneValue the noneValue to set
	 */
	public void setNoneValue(Enum<?> p_oNoneValue) {
		this.noneValue = p_oNoneValue;
	}
	
	/*********************************************************************************
	 ************************** Framework delegate callback **************************
	 *********************************************************************************/
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextView getErrorView() {
		return this.errorComponent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		MMRadioButton oCurrentRadioButton;
		for (int iNdexRadio=0;iNdexRadio<this.getChildCount();iNdexRadio++){
			try{
				oCurrentRadioButton=(MMRadioButton)this.getChildAt(iNdexRadio);
				
				if (p_bEnable) {
					oCurrentRadioButton.getComponentDelegate().configurationEnabledComponent();
				} else {
					oCurrentRadioButton.getComponentDelegate().configurationDisabledComponent();
				}
			}
			catch (ClassCastException oE) {
				Application.getInstance().getLog().error("MMRadioGroup", StringUtils.concat("The radiogroup ",this.getComponentFwkDelegate().getName(),"have forbiden childs (Not MMRadioButton) at position ",String.valueOf(iNdexRadio)));
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enum<?> configurationGetValue() {
		MMRadioButton oCheckedRadioButton = (MMRadioButton) this.findViewById(this.getCheckedRadioButtonId());
		if (oCheckedRadioButton!=null){
			return oCheckedRadioButton.getComponentDelegate().configurationGetValue();
		} else {
			//aucun radio n'est coché, cela n'est possible que si init à FWK_NONE
			return this.getNoneValue();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(Enum<?> p_oObjectToSet) {
		// le clear check provoque un onCheckChanged()
		this.clearCheck();
		//affectation des values aux radioBouttons fils
		Enum<?>[] oEnums;
		if (p_oObjectToSet!=null){
			if ( MMRadioGroup.FWK_NONE.equals(((Enum<?>)p_oObjectToSet).name())){
				this.setNoneValue((Enum<?>) p_oObjectToSet);
			}
			oEnums=(Enum<?>[]) p_oObjectToSet.getClass().getEnumConstants();

			MMRadioButton oCurrentRadio = null;
			String sCurrentRadioName = null;
			for(int iCompteurEnum = 0;iCompteurEnum<oEnums.length; iCompteurEnum++) {
				for(int iCompteurRadio = 0; iCompteurRadio< this.getChildCount(); iCompteurRadio++) {
					oCurrentRadio=(MMRadioButton) this.getChildAt(iCompteurRadio);
					sCurrentRadioName = "radio_" + oEnums[iCompteurEnum].name();
					if (oCurrentRadio.getComponentFwkDelegate().getName().equals(sCurrentRadioName)){
						oCurrentRadio.getComponentDelegate().configurationSetValue(oEnums[iCompteurEnum]);
						if (p_oObjectToSet.equals(oCurrentRadio.getComponentDelegate().configurationGetValue())){
							oCurrentRadio.setChecked(true);
						}						
					}
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		return this.getCheckedRadioButtonId() > 0;
	}
}
