package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

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
import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
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
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDurationPickerDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.DurationFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.DurationUtils;

/**
 * This view group represents a date text view, its label and its button to use
 * the datePickerDialog,
 * <ul>
 * <li>add label attribute to declare the @string/resource Id to use as label</li>
 * <li>use "fwk_component_duration_edittext" xml layout</li>
 * <li>Use parameterized "DurationFormFieldValidator" validator</li>
 * <li>Open "MMDurationPickerDialog" to edit a duration</li>
 * </ul>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * @since Annapurna
 */
public class MMDurationEditText extends AbstractMMTableLayout<Long>	
implements MMIdentifiableView, MMComplexeComponent, OnClickListener, OnDismissListener {
	/** the application */
	private AndroidApplication app = (AndroidApplication) Application.getInstance();
	/** Objet de validation de la valeur selon les paramètres liés au type de données */
	private DurationFormFieldValidator validator = BeanLoader.getInstance().getBean(DurationFormFieldValidator.class) ;
	/** the date field */
	private EditText oUiDurationEdit;
	/** the label above the date */
	private TextView oUiLabel;
	/** the date button */
	private ImageButton oUiButton;
	/** The dialog to show, to edit the value */
	private MMDurationPickerDialogFragment dialog = null;

	/** The key used to retain the current value of the component during orientation changes */
	private static final String DURATION_PICKER_GROUP_VALUE_KEY = "durationPickerGroupValueKey";

	/**
	 * Construct a MMDurationEditText
	 * @param p_oContext           the context
	 */
	public MMDurationEditText(Context p_oContext) {
		super(p_oContext);
		this.inflateMyLayout() ;
		this.defineParameters(null);
	}
	/**
	 * Construct a MMDurationEditText
	 * @param p_oContext the context
	 * @param p_oAttrs the xml attributes
	 */
	public MMDurationEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.inflateMyLayout() ;
			this.linkChildrens(p_oAttrs);
			this.defineParameters(p_oAttrs);
		}
	}

	private void inflateMyLayout(){
		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
		oLayoutInflater.inflate(app.getAndroidIdByRKey(AndroidApplicationR.fwk_component_duration_edittext),this);
		oUiLabel = (TextView) this.findViewById(app.getAndroidIdByRKey(AndroidApplicationR.component_duration__duration__label));
		oUiDurationEdit = (EditText) this.findViewById(app.getAndroidIdByRKey(AndroidApplicationR.component_duration__duration__edit));
		oUiButton = (ImageButton) this.findViewById(app.getAndroidIdByRKey(AndroidApplicationR.component_duration__button));
		oUiButton.setOnClickListener(this);
		// Le setInputType provoque un changed, on reset le changement s'il n'était pas changé à l'origine.
		oUiDurationEdit.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);

	}
	/**
	 * link the child views with custom attributes 
	 * @param p_oAttrs
	 *            attributes of XML Component MMdurationViewGroup
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {
		try {
			oUiLabel.setText(this.getResources().getString(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0)));
		} catch (NotFoundException e) {
			oUiLabel.setVisibility(View.GONE);
			app.getLog().debug(this.getClass().getSimpleName(),
					StringUtils.concat(this.getConfiguration().getName(),"Ressource not found", String.valueOf(p_oAttrs.getAttributeResourceValue(
							Application.MOVALYSXMLNS,"label", 0))));
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
			this.setHoursDigitsCount(DurationDigitsInputFilter.DEFAULT_HOURS_COUNT);
		} else {
			this.setHoursDigitsCount(p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "hoursCount", DurationDigitsInputFilter.DEFAULT_HOURS_COUNT));
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
			if (validator!= null){
				validator.addParametersToConfiguration( this.aivDelegate.getAttributes() , (BasicComponentConfiguration)this.getConfiguration() );
			}
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDismiss(DialogInterface p_oDimissedDialog) {
		this.requestFocusFromTouch();
		Long newDialogValue = this.dialog.getTemporaryDialogValue();
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

		this.dialog = (MMDurationPickerDialogFragment) createDialogFragment(null);
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
	public void configurationSetValue(Long p_oObjectToSet) {
		this.aivDelegate.configurationSetValue(p_oObjectToSet);
		if (p_oObjectToSet==null || p_oObjectToSet.equals(-1L)) {
			oUiDurationEdit.setText(StringUtils.EMPTY);
			this.aivDelegate.setFilled(false);
		} else {
			oUiDurationEdit.setText(DurationUtils.convertMinutesToDuration(p_oObjectToSet));
			this.aivDelegate.setFilled(true);
			this.resetChanged();
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override 
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		this.aivDelegate.configurationSetCustomValues(p_oObjectsToSet);

		if (p_oObjectsToSet == null || p_oObjectsToSet.length == 0) {
			oUiDurationEdit.setText(StringUtils.EMPTY);
			this.aivDelegate.setFilled(false);
		} else {
			String sFirstValue = p_oObjectsToSet[0];
			if ( sFirstValue!= null && sFirstValue.length() > 0 ) {
				oUiDurationEdit.setText(sFirstValue);
				this.aivDelegate.setFilled(true);
			} else {
				oUiDurationEdit.setText(StringUtils.EMPTY);
				this.aivDelegate.setFilled(false);
			}
		}
		this.setCustomLabel();
	}
	/**
	 * Définit le contenu de l'étiquette spécifique selon le paramétrage XML ou du chp perso
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
	public Long configurationGetValue() {
		// Si la valeur du champ est nulle, le composant ne doit pas renvoyer de valeur.
		String sValue = this.oUiDurationEdit.getText().toString();
		
		if (sValue.length() == 0) {
			sValue = null;
		}
		
		if (sValue != null && sValue.trim().length() > 0) {
			return DurationUtils.convertTimeToDurationInMinutes(sValue.trim());
		}		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationDisabledComponent() {
		super.setEnabled(false);
		oUiDurationEdit.setEnabled(false);
		oUiButton.setEnabled(false);
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationEnabledComponent() {
		super.setEnabled(true);
		oUiDurationEdit.setEnabled(true);
		oUiButton.setEnabled(true);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		String sValue = this.oUiDurationEdit.getText().toString();
		if (sValue != null && sValue.trim().length() > 0) {
			return new String[] { sValue };
		}
		return new String[] {};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getValueType() {
		return Long.class;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(Long p_oObject) {
		return p_oObject==null||p_oObject.longValue() == 0L;
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
		if(this.oUiDurationEdit.getError() != null) {
			oUiDurationEdit.setError(null);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		oUiDurationEdit.setError(p_oError);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration,Map<String, Object> p_mapParameters ,StringBuilder p_oErrorBuilder) {
		if ( super.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder) ) {
			return validator.validate(this, p_oConfiguration, p_oErrorBuilder) ;
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
	 * Définit le nombre de digits des heures.
	 * @param p_iHoursCount Nombre de digits des heures.
	 */
	public final void setHoursDigitsCount(final int p_iHoursCount) {
		this.oUiDurationEdit.setFilters(new InputFilter[] { new DurationDigitsInputFilter(p_iHoursCount) });
	}

	/**
	 * <p>Filtre sur les durées.</p>
	 *
	 * <p>Copyright (c) 2012</p>
	 * <p>Company: Adeuza</p>
	 *
	 * @author ktilhou
	 */
	private class DurationDigitsInputFilter extends NumberKeyListener {
		/**
		 * Nombre de digits pour les heures
		 */
		private static final int DEFAULT_HOURS_COUNT = 3;

		/** Nombre de digits pour les heures */
		private int hoursCount = 3;

		/** Nombre de digits pour les minutes */
		private int minutesCount = 2;

		/**
		 * Construit un nouveau filtre.
		 * @param p_iHoursCount le nombre de digits des heures
		 */
		public DurationDigitsInputFilter(int p_iHoursCount) {
			super();
			this.hoursCount = p_iHoursCount;
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

			int iMinutesCount = 0;
			int iHoursCount = 0;
			int iSeparatorIndex = -1;
			boolean bValidMinutes = true;

			for (int i=0; i < p_iDstart; i++) {
				if (p_sDest.charAt(i) == ':') {
					iSeparatorIndex = i;
				}
				else if (iSeparatorIndex >= 0) {
					iMinutesCount++;
				} else {
					iHoursCount++;
				}
			}

			for (int i=iStart; i < iEnd; i++) {
				if (iSeparatorIndex >= 0) {
					iMinutesCount++;
					if (iMinutesCount == 1) {
						// dizaine des minutes
						bValidMinutes = r_oSource.charAt(i) >= 48 && r_oSource.charAt(i) <= 53;
					}
				}
				else if (r_oSource.charAt(i) == ':') {
					iSeparatorIndex = i;
				} else {
					iHoursCount++;
				}
			}

			for (int i=p_iDend; i < p_sDest.length(); i++) {
				if (iSeparatorIndex >= 0) {
					iMinutesCount++;
				}
				else if (p_sDest.charAt(i) == ':') {
					iSeparatorIndex = i;
				} else {
					iHoursCount++;
				}
			}
			if (iMinutesCount > this.minutesCount || iHoursCount > this.hoursCount || !bValidMinutes) {
				r_oSource = StringUtils.EMPTY;
			}
			return r_oSource;
		}

		/**
		 * {@inheritDoc}
		 * @see android.text.method.KeyListener#getInputType()
		 */
		@Override
		public int getInputType() {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 * @see android.text.method.NumberKeyListener#getAcceptedChars()
		 */
		@Override
		protected char[] getAcceptedChars() {
			return new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':' };
		}
	}

	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		MMDialogFragment oDialogFragment = MMDurationPickerDialogFragment.newInstance(this);
		return oDialogFragment;
	}
	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		((MMDurationPickerDialogFragment) p_oDialog).setTemporaryDialogValue(this.configurationGetValue());
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
		r_oBundle.putString(DURATION_PICKER_GROUP_VALUE_KEY, this.oUiDurationEdit.getText().toString());

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

		String retainValue = r_oBundle.getString(DURATION_PICKER_GROUP_VALUE_KEY);
		this.oUiDurationEdit.setText(retainValue);
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
