package com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable;

import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.TAbstractEntityFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMCheckBoxGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMDateTimeEditText;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMDoubleEditText;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMDurationEditText;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEditText;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEmailSimpleEditTextGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEmailSimpleViewTextGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMNumberPickerGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPhoneEditText;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPhoneTextViewGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMRadioGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSimpleSpinner;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMTextView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMURLEditViewGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMURLTextViewGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.AbstractEntityFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.list.CustomListConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.CustomFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
/**
 * <p>TODO Décrire la classe AndroidConfigurableLayoutComponentDelegate</p>
 * <p>Copyright (c) 2010
 * <p>Company: Adeuza
 * @author emalespine
 */
public class AndroidConfigurableLayoutComponentDelegate<VALUE> extends AndroidConfigurableVisualComponentDelegate<VALUE>
		implements ConfigurableLayoutComponent<VALUE> {
	/**
	 * Constructs a new delegate.
	 * 
	 * @param p_oCurrentView The current view. Mandatory.
	 */
	public AndroidConfigurableLayoutComponentDelegate(View p_oCurrentView) {
		super(p_oCurrentView);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeChild(ConfigurableVisualComponent<?> p_oChildToDelete) {
		if (this.getCurrentView() instanceof ViewGroup && p_oChildToDelete instanceof View) {
			ViewGroup oGroup = (ViewGroup) this.getCurrentView();
			oGroup.removeView((View) p_oChildToDelete);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomLabel(final BasicComponentConfiguration p_oConfiguration) {
		MMTextView r_oLabel = null;

		if (this.getCurrentView() instanceof ViewGroup) {
			ViewGroup oGroup = (ViewGroup) this.getCurrentView();

			r_oLabel = (MMTextView) ((Activity) oGroup.getContext()).getLayoutInflater().inflate(this.getAndroidApplication().getAndroidIdByRKey(AndroidApplicationR.fwk_component__label), null);
			r_oLabel.setText(p_oConfiguration.getLabel());

			StringBuilder oComponentName = new StringBuilder(p_oConfiguration.getName());
			oComponentName.append(KEY_SEPARATOR);
			oComponentName.append(ConfigurableVisualComponent.TYPE_LABEL);
			
			r_oLabel.setName(oComponentName.toString());

			((AndroidApplication)Application.getInstance()).computeId(r_oLabel, oComponentName.toString());
			oGroup.addView(r_oLabel);
		}
		return r_oLabel;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomTextField(final BasicComponentConfiguration p_oConfiguration) {
		ConfigurableVisualComponent<?> r_oTextBox = null;

		if (this.getCurrentView() instanceof ViewGroup) {
			final ViewGroup oGroup = (ViewGroup) this.getCurrentView();
			final Context oContext = oGroup.getContext();
			final boolean bMultiline = ((CustomFieldConfiguration) p_oConfiguration).isMultiLine();

			StringBuilder oComponentName = new StringBuilder(p_oConfiguration.getName());
			oComponentName.append(KEY_SEPARATOR);
			if (p_oConfiguration.isDisabled()) {
				oComponentName.append(ConfigurableVisualComponent.TYPE_VALUE);
			}
			else {
				oComponentName.append(ConfigurableVisualComponent.TYPE_EDIT);
			}

			ApplicationR oLayout = AndroidApplicationR.fwk_component__edittext;
			if (bMultiline) {
				oLayout = AndroidApplicationR.fwk_component__multilinetext;
			}
			else if (p_oConfiguration.isDisabled()) {
				oLayout = AndroidApplicationR.fwk_component__valuetext;
			}

			r_oTextBox = (ConfigurableVisualComponent<String>) ((Activity) oContext).getLayoutInflater().inflate(this.getAndroidApplication()
					.getAndroidIdByRKey(oLayout), null);

			r_oTextBox.setName( oComponentName.toString() );
			((AndroidApplication)Application.getInstance()).computeId((View) r_oTextBox, oComponentName.toString());

			if (!bMultiline && !p_oConfiguration.isDisabled()) {
				((MMEditText) r_oTextBox).setDataType( AbstractEntityFieldConfiguration.DataType.valueOf(
						p_oConfiguration.getEntityFieldConfiguration().getType() ));
			}

			int r_iInputType = FieldTypeUtils.getInstance().getAndroidTypeByConfigType(p_oConfiguration.getEntityFieldConfiguration().getType());
			if (((CustomFieldConfiguration) p_oConfiguration).isMultiLine()) {
				r_iInputType = r_iInputType | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
			}

			// Le setInputType provoque un changement sur le composant.
			// S'il n'y en avait pas avant, on reset le flag de changement
			// => gestion délégué dans MMEditText.setInputType (seul composant impacté)
			((TextView) r_oTextBox).setInputType(r_iInputType);

			AbstractEntityFieldConfiguration oEntityFieldConfiguration = p_oConfiguration.getEntityFieldConfiguration();
			if (oEntityFieldConfiguration.hasMaxLength()) {
				InputFilter[] aFilterArray= new InputFilter[1];
				aFilterArray[0] = new InputFilter.LengthFilter(oEntityFieldConfiguration.getMaxLength());
				((TextView) r_oTextBox).setFilters(aFilterArray);
			}
			oGroup.addView((View) r_oTextBox);
		}
		return r_oTextBox;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableLayoutComponent#configurationAppendCustomCheckBoxes(com.adeuza.movalys.fwk.mobile.javacommons.configuration.visual.BasicComponentConfiguration)
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomCheckBoxes(BasicComponentConfiguration p_oConfiguration) {
		MMCheckBoxGroup<String> r_oCheckBoxes = null;

		if (ViewGroup.class.isAssignableFrom(this.getCurrentView().getClass())) {
			CustomListConfiguration oConfigurableList = ConfigurationsHandler.getInstance().getCustomListConfiguration((String) p_oConfiguration.getParameter("list"));

			if (oConfigurableList != null && !oConfigurableList.getItems().isEmpty()) {
				final ViewGroup oGroup = (ViewGroup) this.getCurrentView();

				r_oCheckBoxes =  new MMCheckBoxGroup<>(oGroup.getContext());

				for (Entry<String, String> oItem : oConfigurableList.getItems().entrySet()) {
					r_oCheckBoxes.addCheckBox(oItem.getKey(), oItem.getValue());
				}

				// A2A_DEV Affecation et generation d'un id unique
				StringBuilder oComponentName = new StringBuilder(p_oConfiguration.getName());
				oComponentName.append(KEY_SEPARATOR);

				if (p_oConfiguration.isDisabled()) {
					oComponentName.append(ConfigurableVisualComponent.TYPE_VALUE);
					r_oCheckBoxes.configurationDisabledComponent();
				}
				else {
					oComponentName.append(ConfigurableVisualComponent.TYPE_EDIT);
				}

				((AndroidApplication) Application.getInstance()).computeId(r_oCheckBoxes, oComponentName.toString());
				oGroup.addView(r_oCheckBoxes);
			}
		}
		return r_oCheckBoxes;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableLayoutComponent#configurationAppendCustomComboBox(com.adeuza.movalys.fwk.mobile.javacommons.configuration.visual.BasicComponentConfiguration)
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomRadioButtons(BasicComponentConfiguration p_oConfiguration) {
		MMRadioGroup r_oRadioGroup = null ;
		if (ViewGroup.class.isAssignableFrom(this.getCurrentView().getClass())) {
			CustomListConfiguration oConfigurableList = ConfigurationsHandler.getInstance().getCustomListConfiguration((String) p_oConfiguration.getParameter("list"));

			if (oConfigurableList != null && !oConfigurableList.getItems().isEmpty()) {
				final ViewGroup oGroup = (ViewGroup) this.getCurrentView();

				r_oRadioGroup =  new MMRadioGroup(oGroup.getContext());
				
				StringBuilder oComponentName = new StringBuilder(p_oConfiguration.getName());
				oComponentName.append(KEY_SEPARATOR);

				if (p_oConfiguration.isDisabled()) {
					oComponentName.append(ConfigurableVisualComponent.TYPE_VALUE);
					r_oRadioGroup.configurationDisabledComponent();
				}
				else {
					oComponentName.append(ConfigurableVisualComponent.TYPE_EDIT);
				}
				((AndroidApplication) Application.getInstance()).computeId(r_oRadioGroup, oComponentName.toString());
				
				for (Entry<String, String> oItem : oConfigurableList.getItems().entrySet()) {
					r_oRadioGroup.addRadioButton( oItem.getKey() , oItem.getValue());
				}
				oGroup.addView(r_oRadioGroup);
			}
		}
		return r_oRadioGroup;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableLayoutComponent#configurationAppendCustomComboBox(com.adeuza.movalys.fwk.mobile.javacommons.configuration.visual.BasicComponentConfiguration)
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomComboBox(BasicComponentConfiguration p_oConfiguration) {
		MMSimpleSpinner r_oSpinner = null;

		if (ViewGroup.class.isAssignableFrom(this.getCurrentView().getClass())) {
			CustomListConfiguration oConfigurableList = ConfigurationsHandler.getInstance().getCustomListConfiguration((String) p_oConfiguration.getParameter("list-ref"));

			if (oConfigurableList != null && !oConfigurableList.getItems().isEmpty()) {
				final ViewGroup oGroup = (ViewGroup) this.getCurrentView();
				r_oSpinner =  new MMSimpleSpinner(oGroup.getContext());
				r_oSpinner.setKeyValues(oConfigurableList.getItems()); // définit l'adapteur et les valeurs affichées
				
				// A2A_DEV Affecation et generation d'un id unique
				StringBuilder oComponentName = new StringBuilder(p_oConfiguration.getName());
				oComponentName.append(KEY_SEPARATOR);

				if (p_oConfiguration.isDisabled()) {
					oComponentName.append(ConfigurableVisualComponent.TYPE_VALUE);
					r_oSpinner.configurationDisabledComponent();
				}else {
					oComponentName.append(ConfigurableVisualComponent.TYPE_EDIT);
				}
				
				((AndroidApplication) Application.getInstance()).computeId(r_oSpinner, oComponentName.toString());
				oGroup.addView(r_oSpinner);
			}
		}
		return r_oSpinner;
	}			
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableLayoutComponent#configurationAppendCustomUrl(com.adeuza.movalys.fwk.mobile.javacommons.configuration.visual.BasicComponentConfiguration)
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomURL(BasicComponentConfiguration p_oConfiguration) {
		AbstractMMTableLayout<?> r_oURLComponent = null ;
		if (ViewGroup.class.isAssignableFrom(this.getCurrentView().getClass())) {
			final ViewGroup oGroup = (ViewGroup) this.getCurrentView();
			StringBuilder oComponentName = new StringBuilder(p_oConfiguration.getName());
			oComponentName.append(KEY_SEPARATOR);
			
			if (p_oConfiguration.isDisabled()) {
				r_oURLComponent =  new MMURLTextViewGroup(oGroup.getContext());
				oComponentName.append(ConfigurableVisualComponent.TYPE_VALUE);
				r_oURLComponent.configurationDisabledComponent();
			} else {
				oComponentName.append(ConfigurableVisualComponent.TYPE_EDIT);
				r_oURLComponent =  new MMURLEditViewGroup(oGroup.getContext());
			}
			r_oURLComponent.setName(oComponentName.toString());
			((AndroidApplication) Application.getInstance()).computeId(r_oURLComponent, oComponentName.toString());
			oGroup.addView(r_oURLComponent);
		}
		return r_oURLComponent ;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableLayoutComponent#configurationAppendCustomEmail(com.adeuza.movalys.fwk.mobile.javacommons.configuration.visual.BasicComponentConfiguration)
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomEmail(BasicComponentConfiguration p_oConfiguration) {
		View r_oEmailComponent = null ;
		if (ViewGroup.class.isAssignableFrom(this.getCurrentView().getClass())) {
			final ViewGroup oGroup = (ViewGroup) this.getCurrentView();
			StringBuilder oComponentName = new StringBuilder(p_oConfiguration.getName());
			oComponentName.append(KEY_SEPARATOR);

			if (p_oConfiguration.isDisabled()) {
				oComponentName.append(ConfigurableVisualComponent.TYPE_VALUE);
				r_oEmailComponent =  new MMEmailSimpleViewTextGroup(oGroup.getContext());
				((MMEmailSimpleViewTextGroup)r_oEmailComponent).configurationDisabledComponent();
			}else {
				oComponentName.append(ConfigurableVisualComponent.TYPE_EDIT);
				r_oEmailComponent =  new MMEmailSimpleEditTextGroup(oGroup.getContext());
			}
			((ConfigurableVisualComponent<?>)r_oEmailComponent).setName(oComponentName.toString());
			((AndroidApplication) Application.getInstance()).computeId(r_oEmailComponent, oComponentName.toString());
			oGroup.addView(r_oEmailComponent);
		}
		return (ConfigurableVisualComponent<?>)r_oEmailComponent ;
	}
	/**
	 * Permet d'ajouter un composant dans la vue 
	 */
	private ConfigurableVisualComponent<?> configurationAppendCustom(BasicComponentConfiguration p_oConfiguration , View p_oNewComponent ) {
		
		if (ViewGroup.class.isAssignableFrom(this.getCurrentView().getClass())) {
			StringBuilder oComponentName = new StringBuilder(p_oConfiguration.getName());
			oComponentName.append(KEY_SEPARATOR);

			if (p_oConfiguration.isDisabled()) {
				oComponentName.append(ConfigurableVisualComponent.TYPE_VALUE);
				((ConfigurableVisualComponent<?>)p_oNewComponent).configurationDisabledComponent();
			}else {
				oComponentName.append(ConfigurableVisualComponent.TYPE_EDIT);
			}
			((ConfigurableVisualComponent<?>)p_oNewComponent).setName(oComponentName.toString());
			((AndroidApplication) Application.getInstance()).computeId( p_oNewComponent, oComponentName.toString());
			((ViewGroup) this.getCurrentView()).addView(p_oNewComponent); 
		}
		return (ConfigurableVisualComponent<?>)p_oNewComponent ;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomInteger(BasicComponentConfiguration p_oConfiguration) {
		View oNewComponent =  new MMNumberPickerGroup(((ViewGroup) this.getCurrentView()).getContext());
		return this.configurationAppendCustom(p_oConfiguration, oNewComponent);
	}
	/**
	 * {@inheritDoc}
	 */
	public ConfigurableVisualComponent<?> configurationAppendCustomDouble(BasicComponentConfiguration p_oConfiguration) {
		MMDoubleEditText oNewComponent =  new MMDoubleEditText(((ViewGroup) this.getCurrentView()).getContext());
		final String sNbDigits = (String) p_oConfiguration.getEntityFieldConfiguration().getParameter("nb-digit");
		if (sNbDigits != null && sNbDigits.length() > 0) {
			oNewComponent.setDecimalCount(Integer.parseInt(sNbDigits));
		}
		return this.configurationAppendCustom(p_oConfiguration, oNewComponent);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomDate(BasicComponentConfiguration p_oConfiguration) {
		MMDateTimeEditText oNewComponent =  new MMDateTimeEditText(((ViewGroup) this.getCurrentView()).getContext());
		this.configurationAppendCustom(p_oConfiguration, (View)oNewComponent);
		oNewComponent.setDataType(AbstractEntityFieldConfiguration.DataType.TYPE_DATE);
		return oNewComponent;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomDateTime(BasicComponentConfiguration p_oConfiguration) {
		MMDateTimeEditText oNewComponent =  new MMDateTimeEditText(((ViewGroup) this.getCurrentView()).getContext());
		this.configurationAppendCustom(p_oConfiguration, (View)oNewComponent);
		oNewComponent.setDataType(AbstractEntityFieldConfiguration.DataType.TYPE_DATETIME);
		return oNewComponent;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomTime(BasicComponentConfiguration p_oConfiguration) {
		MMDateTimeEditText oNewComponent =  new MMDateTimeEditText(((ViewGroup) this.getCurrentView()).getContext());
		this.configurationAppendCustom(p_oConfiguration, (View)oNewComponent);
		oNewComponent.setDataType(AbstractEntityFieldConfiguration.DataType.TYPE_TIME);
		return oNewComponent;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomDuration(BasicComponentConfiguration p_oConfiguration) {
		MMDurationEditText oNewComponent =  new MMDurationEditText(((ViewGroup) this.getCurrentView()).getContext());
		return this.configurationAppendCustom(p_oConfiguration, (View)oNewComponent);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomPhone(BasicComponentConfiguration p_oConfiguration) {
		MMPhoneTextViewGroup r_oComponent = null;
		if (p_oConfiguration.isDisabled()) {
			r_oComponent = new MMPhoneTextViewGroup(((ViewGroup) this.getCurrentView()).getContext());
		}
		else {
			r_oComponent = new MMPhoneEditText(((ViewGroup) this.getCurrentView()).getContext());
		}

		this.configurationAppendCustom(p_oConfiguration, r_oComponent);

		return r_oComponent;
	}

	private static final class FieldTypeUtils {
		private static FieldTypeUtils instance;

		private SparseArrayCompat<Integer> androidTypeByConfigType;

		public static FieldTypeUtils getInstance() {
			if (FieldTypeUtils.instance == null) {
				FieldTypeUtils.instance = new FieldTypeUtils();
			}
			return FieldTypeUtils.instance;
		}

		private FieldTypeUtils() {
			this.androidTypeByConfigType = new SparseArrayCompat<>();

			this.androidTypeByConfigType.put(TAbstractEntityFieldConfiguration.TYPE_STRING, InputType.TYPE_CLASS_TEXT
					+ InputType.TYPE_TEXT_VARIATION_NORMAL
					+ InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

			this.androidTypeByConfigType.put(TAbstractEntityFieldConfiguration.TYPE_INTEGER, InputType.TYPE_CLASS_NUMBER
					+ InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

			this.androidTypeByConfigType.put(TAbstractEntityFieldConfiguration.TYPE_FLOAT, InputType.TYPE_CLASS_NUMBER
					+ InputType.TYPE_NUMBER_FLAG_DECIMAL
					+ InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

			this.androidTypeByConfigType.put(TAbstractEntityFieldConfiguration.TYPE_DATE, InputType.TYPE_CLASS_DATETIME
					+ InputType.TYPE_DATETIME_VARIATION_DATE
					+ InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

			this.androidTypeByConfigType.put(TAbstractEntityFieldConfiguration.TYPE_DATETIME, InputType.TYPE_CLASS_DATETIME
					+ InputType.TYPE_DATETIME_VARIATION_NORMAL
					+ InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

			this.androidTypeByConfigType.put(TAbstractEntityFieldConfiguration.TYPE_DURATION, InputType.TYPE_CLASS_DATETIME
					+ InputType.TYPE_DATETIME_VARIATION_TIME
					+ InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

			this.androidTypeByConfigType.put(TAbstractEntityFieldConfiguration.TYPE_EMAIL, InputType.TYPE_CLASS_TEXT
					+ InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

			this.androidTypeByConfigType.put(TAbstractEntityFieldConfiguration.TYPE_URL, InputType.TYPE_CLASS_TEXT
					+ InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

			this.androidTypeByConfigType.put(TAbstractEntityFieldConfiguration.TYPE_PHONE, InputType.TYPE_CLASS_PHONE
					+ InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

			this.androidTypeByConfigType.put(TAbstractEntityFieldConfiguration.TYPE_TIME, InputType.TYPE_CLASS_DATETIME
					+ InputType.TYPE_DATETIME_VARIATION_TIME
					+ InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		}

		public int getAndroidTypeByConfigType(int p_iConfigType) {
			return this.androidTypeByConfigType.get(p_iConfigType);
		}
	}
}
