package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDoublePickerDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.DoubleFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
/**
 * NE PAS UTILISER COMPOSANT BUGGÉ: -0.5 NON SAISISSABLE AVEC LES PICKER, SAISIR 1 ET 10 DANS LE PICKER DES DECIMALES ABOUTIT AU MEME RÉSULTAT.
 * This view group represents a double text view, its label and its button to use
 * the doublePickerDialog,
 * <ul>
 * <li>inflate layout named "fwk_component_double_edittext"</li>
 * <li>use a DoubleFormFieldValidator validator</li>
 * <li>open a "MMDoublePickerDialog" to choose a float data</li>
 * </ul>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * @since Annapurna
 */
public class MMDoubleEditText extends AbstractMMTableLayout<Double>	
implements MMIdentifiableView, OnClickListener, OnDismissListener, MMComplexeComponent {


	/** id of the edit date dialog displayed the date picker */
	public static final int DOUBLE_DIALOG_ID = 1;

	/** the application */
	private AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
	/** Objet de validation de la valeur selon les paramètres liés au type de données */
	private DoubleFormFieldValidator oValidator = BeanLoader.getInstance().getBean(DoubleFormFieldValidator.class) ;
	/** the date field */
	private EditText oUiDoubleEdit;
	/** the label above the date */
	private TextView oUiLabel;
	/** the date button */
	private ImageButton oUiButton;
	/** Nombre de décimales après la virgule.*/
	private int decimalCount;
	/** the dialog to edit this component */
	private MMDoublePickerDialogFragment dialog;
	/** the key used to retain the value during configuration changes */
	private static final String DOUBLE_EDIT_TEXT_VALUE_KEY = "doubleEditTextValueKey";

	/**
	 * Construct a MMDoubleEditText
	 * @param p_oContext  the context
	 */
	public MMDoubleEditText(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.inflateMyLayout() ;
			this.defineParameters(null);
		}
		//		throw new RuntimeException("Composant comportant des erreurs: impossible de saisir la valeur -0.5, saisir un dans le picker décimal devrait aboutir à 0.01");
	}
	/**
	 * Construct a MMDoubleEditText
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMDoubleEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.inflateMyLayout() ;
			this.linkChildrens(p_oAttrs);
			this.defineParameters(p_oAttrs);
		}
		//		throw new RuntimeException("Composant comportant des erreurs: impossible de saisir la valeur -0.5, saisir un dans le picker décimal devrait aboutir à 0.01");
	}

	private void inflateMyLayout(){
		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
		oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_double_edittext),this);
		oUiLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_double__double__label));
		oUiDoubleEdit = (EditText) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_double__double__edit));
		oUiButton = (ImageButton) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_double__button));
		oUiButton.setOnClickListener(this);
	}
	/**
	 * link the child views with custom attributes 
	 * @param p_oAttrs attributes of XML Component MMdoubleViewGroup
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		try {
			oUiLabel.setText(this.getResources().getString(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0)));
		} catch (NotFoundException e) {
			oUiLabel.setVisibility(View.GONE);
			oApplication.getLog().debug(this.getClass().getSimpleName(),
					StringUtils.concat(this.getConfiguration().getName(),
							"Ressource not found", 
							String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS,"label", 0))));
		}		
	}
	/**
	 * Récupère et conserve les données du paramètrage XML du composant
	 * Spécifie le type de données lié et son validateur
	 * @param p_oAttrs paramétrage XML du composant
	 */
	private void defineParameters( AttributeSet p_oAttrs ){
		this.aivDelegate.defineParameters(p_oAttrs);
		if (p_oAttrs == null) {
			this.setDecimalCount(DecimalDigitsInputFilter.DEFAULT_DECIMAL_COUNT);
		} else {
			this.setDecimalCount(p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "decimalCount", DecimalDigitsInputFilter.DEFAULT_DECIMAL_COUNT));
		}
	}
	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()) {
			final BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.oUiLabel.setText(oConfiguration.getLabel());
			}
			if (oValidator!= null){
				oValidator.addParametersToConfiguration( this.aivDelegate.getAttributes() , (BasicComponentConfiguration)this.getConfiguration() );
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDismiss(DialogInterface p_oDimissedDialog) {
		this.requestFocusFromTouch();
		Double newDialogValue = this.dialog.getTemporaryDialogValue();
		if(newDialogValue != null) {
			this.configurationSetValue(newDialogValue);
			this.aivDelegate.changed();
		}
	}

	/**
	 * On click listener to deal with the click on the date and time button
	 * @param p_oClickedView
	 *            the view that was clicked
	 */
	@Override
	public void onClick(View p_oClickedView) {
		this.dialog = (MMDoublePickerDialogFragment) createDialogFragment(null);
		prepareDialogFragment(this.dialog, null);
		this.dialog.show(getFragmentActivityContext().getSupportFragmentManager(), this.dialog.getFragmentDialogTag());
	}

	/**
	 * Returns the fragmentActivit context
	 * 
	 * @return a fragmentActivity context
	 */
	private ActionBarActivity getFragmentActivityContext() {
		return  (ActionBarActivity) ((AndroidApplication) Application.getInstance()).getCurrentActivity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(Double p_oObjectToSet) {
		this.aivDelegate.configurationSetValue(p_oObjectToSet);
		if (p_oObjectToSet==null || p_oObjectToSet.equals(-1D)) {
			oUiDoubleEdit.setText(StringUtils.EMPTY);
			aivDelegate.setFilled(false);
		} else {
			DecimalFormatSymbols oSymbols = new DecimalFormatSymbols();
			oSymbols.setDecimalSeparator('.');
			DecimalFormat oFormater = new DecimalFormat("0.0", oSymbols);
			oUiDoubleEdit.setText(oFormater.format(p_oObjectToSet));
			aivDelegate.setFilled(true);
			this.resetChanged();
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override 
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		this.aivDelegate.configurationSetCustomValues(p_oObjectsToSet);
		boolean isFilled = false;

		if (p_oObjectsToSet == null || p_oObjectsToSet.length == 0) {
			oUiDoubleEdit.setText(StringUtils.EMPTY);
		} else {
			String sFirstValue = p_oObjectsToSet[0];			
			if ( sFirstValue!= null && sFirstValue.length() > 0 ) {
				oUiDoubleEdit.setText(sFirstValue);
				isFilled = true;
			} else {
				oUiDoubleEdit.setText(StringUtils.EMPTY);
			}
		}
		this.setCustomLabel();
		this.aivDelegate.setFilled(isFilled);
	}
	/**
	 * Définit l'étiquette du champ perso
	 */
	private void setCustomLabel(){
		String sNewCustomLabel = null  ;
		if ( this.aivDelegate.getAttributes().containsKey(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE) ) {
			sNewCustomLabel = this.aivDelegate.getAttributes().get(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE) ;
		}
		if ( sNewCustomLabel == null && this.getConfiguration() != null ) {
			Object oLabelFromGraph = ((BasicComponentConfiguration)this.getConfiguration()).getParameter(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE.getName());
			if ( oLabelFromGraph != null){
				sNewCustomLabel = oLabelFromGraph.toString() ;
			}
			if ( sNewCustomLabel == null && ((BasicComponentConfiguration)this.getConfiguration()).getEntityFieldConfiguration() != null ) {
				Object oLabelFromEntity = ((BasicComponentConfiguration)this.getConfiguration()).getEntityFieldConfiguration().getParameter(ConfigurableVisualComponent.Attribute.LABEL_ATTRIBUTE.getName());
				if ( oLabelFromEntity != null){
					sNewCustomLabel = oLabelFromEntity.toString() ;
				}		
			}
		}
		if (sNewCustomLabel!= null && sNewCustomLabel.length() > 0){
			this.oUiLabel.setText(sNewCustomLabel);
		}else{
			this.oUiLabel.setVisibility(GONE);
		}
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double configurationGetValue() {
		// Si la valeur du champ est nulle, le composant ne doit pas renvoyer de valeur.
		String sValue = this.oUiDoubleEdit.getText().toString();
		
		if (sValue.length() == 0) {
			sValue = null;
		}
		
		if (sValue != null && sValue.trim().length() > 0) {
			return Double.valueOf(sValue.trim());
		}		
		return null;
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationDisabledComponent() {
		super.configurationDisabledComponent();
		oUiDoubleEdit.setEnabled(false);
		oUiButton.setEnabled(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationEnabledComponent() {
		super.configurationEnabledComponent();
		oUiDoubleEdit.setEnabled(true);
		oUiButton.setEnabled(true);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return new String[] {oUiDoubleEdit.getText().toString()};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getValueType() {
		return Double.class;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(Double p_oObject) {
		return p_oObject==null||0d==p_oObject.doubleValue();
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
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#resetChanged()
	 */
	@Override
	public void resetChanged() {
		super.resetChanged();
		this.aivDelegate.resetChanged();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public boolean isFilled() {
		return aivDelegate.isFilled();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		if(this.oUiDoubleEdit.getError() != null) {
			oUiDoubleEdit.setError(null);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		oUiDoubleEdit.setError(p_oError);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters ,StringBuilder p_oErrorBuilder) {
		if ( super.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder) ) {
			return oValidator.validate(this, p_oConfiguration, p_oErrorBuilder) ;
		}
		return false;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#configurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		if (this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationRemoveMandatoryLabel();
		}
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout#configurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		if (!this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationSetMandatoryLabel();
		}
	}

	/**
	 * Définit le nombre de décimales après la virgule.
	 * @param p_iDecimalCount Nombre de décimales après la virgule.
	 */
	public final void setDecimalCount(final int p_iDecimalCount) {
		this.decimalCount = p_iDecimalCount;
		this.oUiDoubleEdit.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(p_iDecimalCount) });
	}

	/**
	 * <p>Filtre pour les chiffres décimaux.</p>
	 *
	 * <p>Copyright (c) 2011</p>
	 * <p>Company: Adeuza</p>
	 *
	 * @author emalespine
	 */
	private class DecimalDigitsInputFilter extends DigitsKeyListener {
		/**
		 * Nombre de décimal après la virgule par défaut
		 */
		private static final int DEFAULT_DECIMAL_COUNT = 2;

		/** nombre de chiffres après la virgule */
		private int decimalCount = 2;

		/**
		 * Construit un nouveau filtre.
		 * @param p_iIntegerCount le nombre de chiffre avant la virgule
		 * @param p_iDecimalCount le nombre de chiffre après la virgule
		 */
		public DecimalDigitsInputFilter(int p_iDecimalCount) {
			super(true, true);
			this.decimalCount = p_iDecimalCount;
		}

		/**
		 * Construit un nouveau filtre.
		 * @param p_iIntegerCount le nombre de chiffre avant la virgule
		 * @param p_iDecimalCount le nombre de chiffre après la virgule
		 */
		public DecimalDigitsInputFilter() {
			this(DEFAULT_DECIMAL_COUNT);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public CharSequence filter(CharSequence p_sSource, int p_iStart, int p_iEnd,
				Spanned p_sDest, int p_iDstart, int p_iDend) {

			CharSequence r_oSource = super.filter(p_sSource, p_iStart, p_iEnd, p_sDest, p_iDstart, p_iDend);

			// if changed, replace the source
			int iStart	= p_iStart;
			int iEnd	= p_iEnd; 
			if (r_oSource == null) {
				r_oSource = p_sSource;
			}
			else {
				iStart	= 0;
				iEnd	= r_oSource.length();
			}

			StringBuilder oIntPart = new StringBuilder();
			int iDecimalCount = 0;
			int iDecimalSeparatorIndex = -1;

			for (int i=0; i < p_iDstart; i++) {
				if (p_sDest.charAt(i) == '.') {
					iDecimalSeparatorIndex = i;
				}
				else if (iDecimalSeparatorIndex >= 0) {
					iDecimalCount++;
				}	
				else {
					oIntPart.append(p_sDest.charAt(i));
				}
			}

			for (int i=iStart; i < iEnd; i++) {
				if (iDecimalSeparatorIndex >= 0) {
					iDecimalCount++;
				}
				else if (r_oSource.charAt(i) == '.') {
					iDecimalSeparatorIndex = i;
				}
				else {
					oIntPart.append(r_oSource.charAt(i));
				}
			}

			for (int i=p_iDend; i < p_sDest.length(); i++) {
				if (iDecimalSeparatorIndex >= 0) {
					iDecimalCount++;
				}
				else if (p_sDest.charAt(i) == '.') {
					iDecimalSeparatorIndex = i;
				}
				else {
					oIntPart.append(p_sDest.charAt(i));
				}
			}

			double dIntPart;
			if( oIntPart.length() == 0 || oIntPart.length() == 1 && oIntPart.charAt(0) == '-'){
				dIntPart = 0D;
			}
			else {
				dIntPart = Double.parseDouble(oIntPart.toString());
			}
			if (iDecimalCount > this.decimalCount || dIntPart > Integer.MAX_VALUE || dIntPart < Integer.MIN_VALUE) {
				r_oSource = StringUtils.EMPTY;
			}

			return r_oSource;
		}
	}

	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		MMDialogFragment oDialogFragment  = MMDoublePickerDialogFragment.newInstance(this);
		return oDialogFragment;
	}

	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		((MMDoublePickerDialogFragment) p_oDialog).setTemporaryDialogValue(this.configurationGetValue());
	}
	@Override
	public void onActivityResult(int p_iRequestCode, int p_oResultCode,
			Intent p_oIntent) {
		// Nothing to do

	}


	@Override
	/**
	 * {@inheritDoc}
	 */
	public Parcelable superOnSaveInstanceState() {

		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();

		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString(DOUBLE_EDIT_TEXT_VALUE_KEY, this.oUiDoubleEdit.getText().toString());

		return r_oBundle;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;

		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		String retainValue = r_oBundle.getString(DOUBLE_EDIT_TEXT_VALUE_KEY);
		this.oUiDoubleEdit.setText(retainValue);
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

	public CustomFormatter customFormatter() {
		return this.aivDelegate.customFormatter();
	}

	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}
}