package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableLayoutComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IntegerFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
/**
 * This class has been pulled from the Android platform source code, its an internal widget that hasn't been made public so its included in
 * the project in this fashion for use with the preferences screen; I have made a few slight modifications to the code here, I simply put a
 * MAX and MIN default in the code but these values can still be set publically by calling code.
 */
public class MMNumberPicker extends AbstractMMLinearLayout<Integer> implements OnClickListener, OnLongClickListener,
MMIdentifiableView {
	/**
	 * Valeur par défaut du minimum
	 */
	private static final int DEFAULT_MIN = Integer.MIN_VALUE;
	/**
	 * Valeur par défaut du maximum
	 */
	private static final int DEFAULT_MAX = Integer.MAX_VALUE;
	/**
	 * Valeur par défaut de la variation du click long sur le bouton
	 */
	private static final int DEFAULT_DELTA_ON_LONG_CLICK = 10 ;
	/**
	 * Valeur par défaut de la variation du click court sur le bouton
	 */
	private static final int DEFAULT_DELTA_ON_CLICK = 1 ;
	/**
	 * Valeur par défaut de la vitesse de défilement
	 */
	private static final int DEFAULT_SPEED = 300 ;
	/**
	 * Variation de la valeur sur un click court 
	 */
	private int smallDelta = DEFAULT_DELTA_ON_CLICK;
	/**
	 * Variation de la valeur sur un click long 
	 */
	private int bigDelta = DEFAULT_DELTA_ON_LONG_CLICK;	
	/**
	 * Outil validant la qualité des données selon le paramétrage 
	 */
	private static IntegerFormFieldValidator VALIDATOR;

	/*
	public interface OnChangedListener {
		void onChanged(MMNumberPicker picker, int oldVal, int newVal);
	}
	 */
	/*
	public interface Formatter {
		String toString(int value);
	}
	 */
	/*
	 * Use a custom NumberPicker formatting callback to use two-digit minutes strings like "01". Keeping a static formatter etc. is the most
	 * efficient way to do this; it avoids creating temporary objects on every call to format().
	 */
	// A2A_DEV DMA a finir
	/*
	public static final MMNumberPicker.Formatter TWO_DIGIT_FORMATTER = new MMNumberPicker.Formatter() {
		final StringBuilder mBuilder = new StringBuilder();
		final java.util.Formatter mFmt = new java.util.Formatter(mBuilder);
		final Object[] mArgs = new Object[1];
		public String toString(int value) {
			mArgs[0] = value;
			mBuilder.delete(0, mBuilder.length());
			mFmt.format("%02d", mArgs);
			return mFmt.toString();
		}
	};
	 */
	private Handler mHandler;
	private final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			if (bCanIncrement) {
				changeCurrent(mCurrent + bigDelta);
				mHandler.postDelayed(this, mSpeed);
			} else if (bCanDecrement) {
				changeCurrent(mCurrent - bigDelta);
				mHandler.postDelayed(this, mSpeed);
			}
		}
	};
	private MMEditText oUiText;
	private InputFilter oNumberInputFilter;
	private String[] mDisplayedValues;
	/** Valeur minimale du picker */ 
	private int mStart = DEFAULT_MIN;
	/** Valeur maximale du picker */
	private int mEnd   = DEFAULT_MAX;
	/** valeur courant affichée */
	private int mCurrent;
	//private int mPrevious;
	//private OnChangedListener mListener;
	//private Formatter mFormatter;
	/** Vitesse de défilement */
	private long mSpeed = DEFAULT_SPEED;
	/** Le picker peut il décrémenter */
	private boolean bCanIncrement;
	/** Le picker peut il décrémenter */
	private boolean bCanDecrement;
	/** Tableau des caractères autorisées */
	private static final char[] DIGIT_CHARACTERS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	/** Bouton permettant d'incrémenter le nombre */
	private MMNumberPickerButton oUiIncrementButton;
	/** Bouton permettant de decrémenter le nombre */
	private MMNumberPickerButton oUiDecrementButton;
	
	/**
	 * Constructeur d'un objet
	 * @param p_oContext contexte de l'objet
	 */
	public MMNumberPicker(Context p_oContext) {
		this(p_oContext, null);

	}
	/**
	 * Constructeur d'un objet
	 * @param p_oContext contexte de l'objet
	 * @param p_oAttrs attributs de paramétrage XML
	 */
	public MMNumberPicker(Context p_oContext, AttributeSet p_oAttrs) {
		this(p_oContext, p_oAttrs, 0);

	}
	/**
	 * Constructeur d'un objet
	 * @param p_oContext contexte de l'objet
	 * @param p_oAttrs attributs de paramétrage XML
	 * @param p_iDefStyle style par défaut
	 */
	public MMNumberPicker(Context p_oContext, AttributeSet p_oAttrs, int p_iDefStyle) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(this) ;

			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

			LayoutInflater oInflater = LayoutInflater.from(this.getContext());

			// selon l'orientation désirée on inflate le XML différent
			String sOrientation = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "orientation") ;
			ApplicationR oXmlLayout = AndroidApplicationR.fwk_number_picker ;
			if ( sOrientation != null && sOrientation.equalsIgnoreCase("horizontal")){
				oXmlLayout = AndroidApplicationR.fwk_numberhorizontal_picker ; 
			}
			oInflater.inflate(oApplication.getAndroidIdByRKey(oXmlLayout), this);
			mHandler = new Handler(Looper.getMainLooper()); // sur thread principal

			oNumberInputFilter = new NumberRangeKeyListener();
			oUiIncrementButton = (MMNumberPickerButton) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__increment__button));
			oUiIncrementButton.setNumberPicker(this);
			oUiIncrementButton.setOnClickListener(this);
			oUiIncrementButton.setOnLongClickListener(this);
			oUiDecrementButton = (MMNumberPickerButton) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__decrement__button));
			oUiDecrementButton.setNumberPicker(this);
			oUiDecrementButton.setOnClickListener(this);
			oUiDecrementButton.setOnLongClickListener(this);

			//oUiLabel = (MMTextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__number__label));
			oUiText = (MMEditText) findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__number__edit));
			//mText.setOnFocusChangeListener(this);
			InputFilter oInputFilter = new NumberPickerInputFilter();
			oUiText.setFilters(new InputFilter[] { oInputFilter });
			
			if (!isEnabled()) {
				setEnabled(false);
			}
			this.defineParameters(p_oAttrs);
			VALIDATOR = BeanLoader.getInstance().getBean( IntegerFormFieldValidator.class );
		}
	}
	/**
	 * Retrieve parameters from layout to put in the local map for validation
	 * @param p_oAttrs XML attribute set
	 */
	private void defineParameters( AttributeSet p_oAttrs ){
		this.aivDelegate.defineParameters(p_oAttrs); 
	}	
	/**
	 * called wen the inflator finished the job before setting value
	 */	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()) {
			if ( VALIDATOR != null) {
				VALIDATOR.addParametersToConfiguration( this.aivDelegate.getAttributes() , (BasicComponentConfiguration)this.getConfiguration() );
			}
		}
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see android.view.View#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean p_bEnabled) {
		super.setEnabled(p_bEnabled);
		oUiIncrementButton.setEnabled(p_bEnabled);
		oUiDecrementButton.setEnabled(p_bEnabled);
		oUiText.setEnabled(p_bEnabled);
		if (oUiText.isEnabled()) {
			oUiText.setTextColor(Color.BLACK);
		} else {
			oUiText.setTextColor(Color.WHITE);
		}
	}

	public MMEditText getUiText() {
		return oUiText;
	}

	/*public void setOnChangeListener(OnChangedListener listener) {
		mListener = listener;
	}*/
	/*
	public void setFormatter(Formatter formatter) {
		mFormatter = formatter;
	}*/
	/**
	 * Set the range of numbers allowed for the number picker. The current value will be automatically set to the start.
	 * @param start the start of the range (inclusive)
	 * @param end the end of the range (inclusive)

	public void setRange(int start, int end) {
		mStart = start;
		mEnd = end;
		mCurrent = start;
		updateView();
	}*/
	/**
	 * Set the range of numbers allowed for the number picker. The current value will be automatically set to the start. Also provide a
	 * mapping for values used to display to the user.
	 * @param start the start of the range (inclusive)
	 * @param end the end of the range (inclusive)
	 * @param displayedValues the values displayed to the user.

	public void setRange(int start, int end, String[] displayedValues) {
		mDisplayedValues = displayedValues;
		mStart = start;
		mEnd = end;
		mCurrent = start;
		updateView();
	}
	public void setCurrent(int current) {
		mCurrent = current;
		updateView();
	}*/
	/**
	 * The speed (in milliseconds) at which the numbers will scroll when the the +/- buttons are longpressed. Default is 300ms.

	public void setSpeed(long speed) {
		mSpeed = speed;
	}*/
	@Override
	public void onClick(View p_oView){
		if (!oUiText.hasFocus()){
			oUiText.requestFocus();
		}
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

		if ( oUiText.isChanged()){
			//mPrevious = mCurrent;
			String sValue = String.valueOf(oUiText.getText()).trim().replace(" ", "");
			if (sValue.length() > 0 ){
				try {
					mCurrent = Integer.valueOf(sValue);
				} catch (NumberFormatException oOException) {
					mCurrent = 0 ;
				}
			} else {
				mCurrent = 0 ;
			}
		}
		// now perform the increment/decrement
		if (oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__increment__button) == p_oView.getId()) {
			this.changeCurrent(mCurrent + smallDelta);
		} else if (oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__decrement__button) == p_oView.getId()) {
			this.changeCurrent(mCurrent - smallDelta);
		}
		// on change la valeur
		if (aivDelegate!=null) {
			this.aivDelegate.changed();
		}
	}
	/*
	private String formatNumber(int value) {
		return (mFormatter != null) ? mFormatter.toString(value) : String.valueOf(value);
	}
	 */
	protected void changeCurrent(int p_iNewCurrent) {
		int iNewCurrent = p_iNewCurrent ;
		// Wrap around the values if we go past the start or end
		if (p_iNewCurrent > mEnd) {
			iNewCurrent = mStart;
		} else if (p_iNewCurrent < mStart) {
			iNewCurrent = mEnd;
		}
		//mPrevious = mCurrent;
		mCurrent = iNewCurrent;
		//notifyChange();
		updateView();
	}
	/*
	protected void notifyChange() {
		if (mListener != null) {
			mListener.onChanged(this, mPrevious, mCurrent);
		}
	}*/
	protected void updateView() {
		/*
		 * If we don't have displayed values then use the current number else find the correct value in the displayed values for the current
		 * number.
		 */
		
		if (mDisplayedValues == null) {
			this.configurationSetValue(Integer.valueOf(mCurrent));
		} else {
			oUiText.setText(mDisplayedValues[mCurrent - mStart]);
		}
		oUiText.setSelection(oUiText.getText().length());
		this.aivDelegate.changed();
	}
	/*
	private void validateCurrentView(CharSequence str) {
		int val = getSelectedPos(str.toString());
		if ((val >= mStart) && (val <= mEnd)) {
			if (mCurrent != val) {
				mPrevious = mCurrent;
				mCurrent = val;
				notifyChange();
			}
		}
		updateView();
	}

	public void onFocusChange(View v, boolean hasFocus) {
		// When focus is lost check that the text field has valid values.

		if (!hasFocus) {
			validateInput(v);
		}
	}

	private void validateInput(View v) {
		String str = String.valueOf(((TextView) v).getText());
		if ("".equals(str)) {
			// Restore to the old value as we don't allow empty values
			updateView();
		} else {
			// Check the new value and ensure it's in range
			validateCurrentView(str);
		}
	}*/
	/**
	 * We start the long click here but rely on the {@link NumberPickerButton} to inform us when the long click has ended.
	 */
	@Override
	public boolean onLongClick(View p_oViewv) {
		/*
		 * The text view may still have focus so clear it's focus which will trigger the on focus changed and any typed values to be pulled.
		 */
		oUiText.clearFocus();
		if ( oUiText.isChanged()){
			//mPrevious = mCurrent;
			mCurrent = Integer.valueOf(String.valueOf(oUiText.getText()));
		}
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		if (oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__increment__button) == p_oViewv.getId()) {
			bCanIncrement = true;
			mHandler.post(mRunnable);
		} else if (oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__decrement__button) == p_oViewv.getId()) {
			bCanDecrement = true;
			mHandler.post(mRunnable);
		}
		return true;
	}
	/**
	 * Annule l'incrementation
	 */
	public void cancelIncrement() {
		bCanIncrement = false;
	}
	/**
	 * Annule la décrementation
	 */
	public void cancelDecrement() {
		bCanDecrement = false;
	}

	private class NumberPickerInputFilter implements InputFilter {
		@Override
		public CharSequence filter(CharSequence p_sSource, int p_iStart, int p_iEnd, Spanned p_oDest, int p_oDstart, int p_oDend) {
			if (mDisplayedValues == null) {
				return oNumberInputFilter.filter(p_sSource, p_iStart, p_iEnd, p_oDest, p_oDstart, p_oDend);
			}
			CharSequence oFiltered = p_sSource.subSequence(p_iStart, p_iEnd);
			String sResult = new StringBuilder( p_oDest.subSequence(0, p_oDstart) ).append(oFiltered)
					.append(p_oDest.subSequence(p_oDend, p_oDest.length())).toString().toLowerCase(Locale.getDefault());
			CharSequence r_sResult = "" ;
			for (String sValue : mDisplayedValues) {
				sValue = sValue.toLowerCase(Locale.getDefault());
				if (sValue.startsWith(sResult)) {
					r_sResult = oFiltered;
					break ;
				}
			}
			return r_sResult;
		}
	}
	private class NumberRangeKeyListener extends NumberKeyListener {
		@Override
		public int getInputType() {
			return InputType.TYPE_CLASS_NUMBER;
		}
		@Override
		protected char[] getAcceptedChars() {
			return DIGIT_CHARACTERS;
		}
		@Override
		public CharSequence filter(CharSequence p_oSource, int p_oStart, int p_oEnd, Spanned p_oDest, int p_iStart, int p_iEnd) {
			CharSequence sFiltered = super.filter(p_oSource, p_oStart, p_oEnd, p_oDest, p_iStart, p_iEnd);
			if (sFiltered == null) {
				sFiltered = p_oSource.subSequence(p_oStart, p_oEnd);
			}
			String sResult = new StringBuilder( p_oDest.subSequence(0, p_iStart) ).append( sFiltered )
					.append( p_oDest.subSequence(p_iEnd, p_oDest.length())).toString().toLowerCase(Locale.getDefault());

			if ("".equals(sResult)) {
				return sResult;
			}
			int iVal = getSelectedPos(sResult);
			/*
			 * Ensure the user can't type in a value greater than the max allowed. We have to allow less than min as the user might want to
			 * delete some numbers and then type a new number.
			 */
			CharSequence r_sResult = ""; 
			if (iVal <= mEnd) {
				r_sResult = sFiltered ;
			}
			return r_sResult ;
		}
	}
	private int getSelectedPos(String p_sText) {
		int r_iPos ;
		if (mDisplayedValues == null) {
			r_iPos = Integer.parseInt(p_sText);
		} else {
			for (int iNdex = 0; iNdex < mDisplayedValues.length; iNdex++) {
				/* Don't force the user to type in jan when ja will do */
				if (mDisplayedValues[iNdex].toLowerCase(Locale.getDefault()).startsWith(p_sText)) {
					return mStart + iNdex;
				}
			}
			/*
			 * The user might have typed in a number into the month field i.e. 10 instead of OCT so support that too.
			 */
			try {
				r_iPos = Integer.parseInt(p_sText);
			} catch (NumberFormatException e) {
				/* Ignore as if it's not a number we don't care */
				Log.d("MMNumberPicker", p_sText+" pas un entier");
				r_iPos = mStart ; 
			}
		}		
		return r_iPos;
	}
	/**
	 * @return the current value.
	 */
	public int getCurrent() {
		return mCurrent;
	}

	/**
	 * {@inheritDoc}
	 */ 
	@Override
	public void configurationSetValue(Integer p_oObjectToSet) {
		super.configurationSetValue(p_oObjectToSet);
		this.retrieveMinMaxValues();
		if( p_oObjectToSet != null) {
			oUiText.setText(String.valueOf(p_oObjectToSet));
			mCurrent = p_oObjectToSet.intValue();
		} else{
			oUiText.setText("");
		}

		// définit le nombre max de caractères
		oUiText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(String.valueOf(mStart).length()) });
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		this.retrieveMinMaxValues();
		if (p_oObjectsToSet == null || p_oObjectsToSet.length == 0) {
			this.configurationSetValue(null);
		} else {
			Integer sFirstValue ;
			try {
				sFirstValue = Integer.parseInt(p_oObjectsToSet[0]);
			} catch (NumberFormatException e) {
				sFirstValue = null ; //par défaut si erreur
			}
			if (sFirstValue == null) {
				this.configurationSetValue(null);
			}
			else {
				this.configurationSetValue(sFirstValue);
			}
		}
	}	

	private void retrieveMinMaxValues(){
		Integer oMinValue = VALIDATOR.getMinValue((BasicComponentConfiguration)this.getConfiguration()) ;
		if ( oMinValue != null){
			mStart  = oMinValue.intValue() ;
		} 
		Integer oMaxValue = VALIDATOR.getMaxValue((BasicComponentConfiguration)this.getConfiguration()) ;
		if ( oMaxValue != null){
			mEnd  = oMaxValue.intValue() ;
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer configurationGetValue() {
		Integer lValue ;
		try {
			lValue = Integer.valueOf(oUiText.getText().toString().trim() );
		} catch (NumberFormatException e) {
			lValue = null ; //par défaut si erreur
		}
		return lValue ;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#getValueType()
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return new String[] { String.valueOf(this.configurationGetValue()) };
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getValueType() {
		return Integer.class;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		if ( super.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder) ) {
			return VALIDATOR.validate(this, p_oConfiguration, p_oErrorBuilder) ;
		}
		return false ;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		oUiText.setError(p_oError);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		if(this.oUiText.getError() != null) {
			oUiText.setError(null);
		}
	}
	/**
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		super.configurationEnabledComponent();
		oUiText.configurationEnabledComponent();
		oUiIncrementButton.configurationEnabledComponent();
		oUiDecrementButton.configurationEnabledComponent();

		oUiIncrementButton.setOnClickListener(this);
		oUiIncrementButton.setOnLongClickListener(this);
		oUiIncrementButton.setNumberPicker(this);
		oUiDecrementButton.setOnClickListener(this);
		oUiDecrementButton.setOnLongClickListener(this);
		oUiDecrementButton.setNumberPicker(this);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.AbstractMMTableLayout#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		super.configurationDisabledComponent();
		oUiText.configurationDisabledComponent();
		oUiIncrementButton.configurationDisabledComponent();
		oUiDecrementButton.configurationDisabledComponent();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(Integer p_oObject) {
		return configurationGetValue() == null ;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.configurationGetValue() != null ;
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetChanged() {
		this.oUiText.resetChanged()  ;
		this.aivDelegate.resetChanged();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged() || this.oUiText.isChanged() ;
	}
	/**
	 * Permet de définir la valeur maximale à valider
	 * @param p_iEnd entier 
	 */
	public void setMaximalValue(int p_iEnd) {
		this.mEnd = p_iEnd;
	}
	/**
	 * Permet de définir la valeur minimale à valider
	 * @param p_iStart entier 
	 */	
	public void setMinimalValue(int p_iStart) {
		this.mStart = p_iStart;
	}
	/**
	 * Permet de modifier la variation réalisée par le clic court
	 * @param bigDelta entier 
	 */
	public void setSmallDelta(int p_iSmallDelta) {
		this.smallDelta = p_iSmallDelta;
	}
	/**
	 * Permet de modifier la variation réalisée par le clic long
	 * @param bigDelta entier 
	 */
	public void setBigDelta(int p_iBigDelta) {
		this.bigDelta = p_iBigDelta;
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

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
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
}