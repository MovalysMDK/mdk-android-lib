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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.apache.beanutils.ConvertUtilsBean;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.BaseConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.SelectedComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule.PropertyTarget;

/**
 * Delegate for view models to read and write data to components
 */
@SuppressWarnings("serial")
public class ViewModelDataManipulationDelegate implements Serializable {

	/** Reference to the parent view model */
	private WeakReference<AbstractViewModel> viewModel;
	
	/** The ConvertUtilsBean used to convert commons type */
	private ConvertUtilsBean oConvertUtilsBean = new ConvertUtilsBean();
	
	/**
	 * Constructor
	 *
	 * @param p_oViewModel the linked view model
	 */
	public ViewModelDataManipulationDelegate(AbstractViewModel p_oViewModel) {
		this.viewModel = new WeakReference<AbstractViewModel>(p_oViewModel);
	}
	
	/**
	 * Read data from valid component
	 *
	 * @param p_oComponent component to read
	 */
	public void readValidDataFromComponent(ConfigurableVisualComponent p_oComponent) {
		AbstractViewModel oViewModel = this.viewModel.get();
		
		if (oViewModel != null) {
			
			Object[] oValue = new Object[2];
			oValue[1] = true;
			String sCompletePath = null;
			
			Iterator<String> iterPath = null;
	
			//lecture des données
			if (p_oComponent != null && p_oComponent.getComponentFwkDelegate().isChanged()
					&& !p_oComponent.getComponentFwkDelegate().hasNoValue()) {

				// On cherche la valeur à mettre dans l'élément
				sCompletePath = p_oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath();
	
				oValue[0] = oViewModel;
				iterPath = p_oComponent.getComponentFwkDelegate().getDescriptor().getViewModelPath().iterator();
				while(iterPath.hasNext() && oValue[0] != null && (boolean)oValue[1]) {
					oValue = this.readValidDataFromComponentTreatPath(p_oComponent, oViewModel, oValue[0], sCompletePath, iterPath);
				}
			}
		}
	}

	/**
	 * Treats path on valid read component
	 * @param p_oComponent component to process
	 * @param p_oViewModel linked view model
	 * @param p_oValue value to push
	 * @param p_sCompletePath complete path of component in model
	 * @param p_oIterPath iterator in component's view model path
	 * @return false if there was an error
	 * @throws BreakException sent when the including while statement has to be stopped
	 */
	private Object[] readValidDataFromComponentTreatPath(ConfigurableVisualComponent p_oComponent, AbstractViewModel p_oViewModel, 
			Object p_oValue, String p_sCompletePath, Iterator<String> p_oIterPath) {
		Object[] r_oValue = new Object[2];
		r_oValue[0] = p_oValue;
		r_oValue[1] = true;
		String sName = p_oIterPath.next();
		try {
			if (p_oIterPath.hasNext()) {
				sName = "get"+ sName.substring(0,1).toUpperCase() + sName.substring(1);
				r_oValue[0] = r_oValue[0].getClass().getMethod(sName).invoke(r_oValue[0]);

				if (r_oValue[0] == null) {
					if (p_oIterPath.hasNext()) {
						r_oValue[0] = StringUtils.concat("Impossible to bind : ", p_sCompletePath);
						Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_30050, (String) r_oValue[0]);
					}
					r_oValue[1] = false;
				}
			}
			else {
				sName = StringUtils.capitalize(sName);
				computeViewModelValue(sName, r_oValue[0], p_oComponent, p_oViewModel);
				r_oValue[1] = false;
			}
		} catch (IllegalArgumentException | SecurityException |  IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			r_oValue[0] = StringUtils.concat("Impossible to bind : ", p_sCompletePath);
			Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_30050, (String) r_oValue[0], e);
		}
		return r_oValue;
	}
	
	/**
	 * Computes the value of a component
	 * @param p_sName Attribute name in the view model
	 * @param p_oValue Attribute value in the view model
	 * @param p_oComponent Linked component
	 * @param p_oViewModel view model used to set the value
	 * @throws NoSuchMethodException exception during introspection 
	 * @throws InvocationTargetException exception during introspection
	 * @throws IllegalAccessException exception during introspection
	 */
	private void computeViewModelValue(String p_sName, Object p_oValue, ConfigurableVisualComponent p_oComponent, AbstractViewModel p_oViewModel) 
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String sSourceValueTypeName = null;
		String sTargetValueTypeName = null;
		Method oMethod = null;
		Class<?> oReturnClass ;
		try {
			oReturnClass = p_oValue.getClass().getMethod("get" + p_sName).getReturnType();
		} catch ( NoSuchMethodException e ){
			oReturnClass = p_oValue.getClass().getMethod("is" + p_sName).getReturnType();
		}
		try {
			oMethod = p_oValue.getClass().getMethod("set" + p_sName , oReturnClass );
		} catch ( NoSuchMethodException e ) {
			Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_30050, (String) p_oValue);
			return;
		}

		//Conversion de la valeur avant de la mettre dans le ViewModel
		Object oSourceValue = p_oComponent.getComponentDelegate().configurationGetValue();
		Object oTargetValue = oSourceValue;

		if ( oSourceValue != null ) {
			sSourceValueTypeName = oSourceValue.getClass().getSimpleName();
			sSourceValueTypeName = sSourceValueTypeName.substring(0, 1).toUpperCase() + sSourceValueTypeName.substring(1);

			sTargetValueTypeName = oReturnClass.getSimpleName();

			if(p_oComponent.getComponentFwkDelegate().customConverter() != null) {
				CustomConverter oCustomConverter = p_oComponent.getComponentFwkDelegate().customConverter();
				oTargetValue = oCustomConverter.convertFromComponentToVM(oSourceValue, oReturnClass);
			}
			else {
				BaseConverter oConverter = new BaseConverter();
				//Call the converter
				oTargetValue = oConverter.convert(sSourceValueTypeName, sTargetValueTypeName, oSourceValue);
			}
		}
		
		// updateValue est vrai si oTargetValue et oSourceValue sont différents et qu'ils sont de la même classe ou qu'un des deux est null
		if (oSourceValue != null && oTargetValue != null ) {
			p_oViewModel.setUpdatedValue(! oTargetValue.equals(oSourceValue));
			
			if (p_oViewModel.isUpdatedValue()) {
				p_oViewModel.setUpdatedValue(oSourceValue.getClass().equals(oTargetValue.getClass()));
			}
		} else if (oSourceValue == null && oTargetValue == null) {
			p_oViewModel.setUpdatedValue(false);
		} else {
			p_oViewModel.setUpdatedValue(true);
		}
		
		oMethod.invoke(p_oValue, oTargetValue);
	}
	
	/**
	 * Read invalid data from component
	 *
	 * @param p_oComponent component to read
	 */
	public void readInvalidDataFromComponent(ConfigurableVisualComponent p_oComponent) {
		
		AbstractViewModel oViewModel = this.viewModel.get();
		
		if (oViewModel != null) {
			
			// write data in errorMap
			Object oValue[] = new Object[2];
			oValue[1] = true;
			String sCompletePath = null;
			Iterator<String> iterPath = null;
			//lecture des données
			if (p_oComponent != null && p_oComponent.getComponentFwkDelegate().isChanged()) {
	
				// On cherche la valeur à écrire
				// on cherche la valeur à mettre dans l'élément
				sCompletePath = p_oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath();
	
				oValue[0] = oViewModel;
				iterPath = p_oComponent.getComponentFwkDelegate().getDescriptor().getViewModelPath().iterator();
				while(iterPath.hasNext() && (boolean) oValue[1]) {
					oValue = readInvalidDataFromComponentTreatPath(p_oComponent, oViewModel, oValue[0], sCompletePath, iterPath);
				}
				
				oViewModel.notifyFieldChanged(sCompletePath, oValue[0], p_oComponent.getComponentDelegate().configurationGetValue(), oViewModel.validComponent(p_oComponent, null));
			}
		}
	}

	/**
	 * Treats path on invalid read component
	 * @param p_oComponent component to process
	 * @param p_oViewModel linked view model
	 * @param p_oValue value in the view model
	 * @param p_sCompletePath complete path of the component in the view model
	 * @param p_oIterPath iterator in component view model path
	 * @return false if there was an error
	 * @throws BreakException sent when the including while statement has to be stopped 
	 */
	private Object[] readInvalidDataFromComponentTreatPath(ConfigurableVisualComponent p_oComponent,
			AbstractViewModel p_oViewModel, Object p_oValue, String p_sCompletePath, Iterator<String> p_oIterPath) {
		Object[] oValue = new Object[2];
		oValue[0] = p_oValue;
		oValue[1] = true;
		String sName;
		sName = p_oIterPath.next();
		try {
			if (p_oIterPath.hasNext()) {
				sName = "get"+ sName.substring(0,1).toUpperCase() + sName.substring(1);
				oValue[0] = oValue[0].getClass().getMethod(sName).invoke(oValue[0]);

				if (oValue[0] == null) {
					if (p_oIterPath.hasNext()) {
						oValue[0] = StringUtils.concat("Impossible to bind : ", p_sCompletePath);
						Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_30050, (String) oValue[0]);
					}
					oValue[1] = false;
				}
			}
			else {
				p_oViewModel.getVmDataObject().getFieldsErrorMap().put(p_sCompletePath, p_oComponent.getComponentDelegate().configurationGetValue());
			}
		} catch (IllegalArgumentException | SecurityException |  IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			oValue[0] = StringUtils.concat("Impossible to bind : ", p_sCompletePath);
			Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_30050, (String) oValue[0], e);
		}
		return oValue;
	}
	
	/**
	 * write invalid data to a component
	 *
	 * @param p_oComponent component to modify
	 * @param p_oInvalideValue valud to write
	 */
	public void writeInvalidDataForComponent(ConfigurableVisualComponent p_oComponent, Object p_oInvalideValue) {
		if ( p_oComponent != null ) {
			p_oComponent.getComponentDelegate().configurationSetValue(p_oInvalideValue);
		}
	}

	/**
	 * Write data for component p_oComponent
	 *
	 * @param p_sPath path to look for components
	 * @param p_oComponent component to write
	 */
	@SuppressWarnings({ "unchecked" })
	public void writeDataForComponent(String p_sPath, ConfigurableVisualComponent p_oComponent) {
		
		AbstractViewModel oViewModel = this.viewModel.get();
		
		Object[] oValue = new Object[2];
		oValue[1] = true;
		String sCompletePath = null;
		Iterator<String> iterPath;

		if (oViewModel != null) {
			// mise à jour de l'éditabilité du composant
			// compute specific
			updateComponentEditableStatus(p_sPath, p_oComponent, oViewModel);
			
			// seulement si le composant n'a pas de valeur en base
			if (!p_oComponent.getComponentFwkDelegate().hasNoValue()) {
				// On cherche la valeur à écrire
				// on cherche la valeur à mettre dans l'élément
				sCompletePath = p_oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath();
	
				// obligation de la mettre en seconde étape (pas dans le if précédent pour effacer l'ancienne valeur
				// même s'il y a une exception
				iterPath = null;
				if (oValue[0] == null) {
					oValue[0] = oViewModel;
					iterPath = p_oComponent.getComponentFwkDelegate().getDescriptor().getViewModelPath().iterator();
					while (iterPath.hasNext() && (boolean)oValue[1]) {
						oValue = writeDataForComponentTreatPath(p_oComponent, oValue[0], sCompletePath, iterPath);
					}
				}
				
				try {
					if(p_oComponent.getComponentFwkDelegate().customConverter() != null) {
						CustomConverter oCustomConverter = p_oComponent.getComponentFwkDelegate().customConverter();
						oValue[0] = oCustomConverter.convertFromVMToComponent(oValue[0]);
					}
					p_oComponent.getComponentDelegate().configurationSetValue(oValue[0]);
				} catch( ClassCastException oException ) {
					Class<?> oParamType = p_oComponent.getComponentDelegate().getValueType();
					Object oConvertedValue = oConvertUtilsBean.convert(oValue[0], oParamType);
					p_oComponent.getComponentDelegate().configurationSetValue(oConvertedValue);
				}
				p_oComponent.getComponentFwkDelegate().resetChanged();
			}
		}
	}

	/**
	 * Updates the editable status of a component
	 * @param p_sPath path of the component in the view model
	 * @param p_oComponent component to update
	 * @param p_oViewModel view model hosting the binding of the component
	 */
	private void updateComponentEditableStatus(String p_sPath, ConfigurableVisualComponent p_oComponent, AbstractViewModel p_oViewModel) {
		if (p_oComponent.getComponentDelegate().isAlwaysEnabled() || p_oComponent.getComponentFwkDelegate().isEdit() && p_oViewModel.isEditable()) {
			// implement get rules
			Map<PropertyTarget, List<Method>> oRules = p_oViewModel.getVmDataObject().getBusinessRuleRegistry().getRulesWithField(p_sPath);
			if (oRules != null) {
				List<ConfigurableVisualComponent> oAffectedListComp = p_oViewModel.getVmDataObject().getComponents().get(p_sPath);
				p_oViewModel.applyRulesOnComponents(PropertyTarget.ENABLE, oRules, oAffectedListComp);
			} else {
				List<ConfigurableVisualComponent> oComponents = p_oViewModel.getVmDataObject().getComponents().get(p_sPath);
				for (ConfigurableVisualComponent configurableVisualComponent : oComponents) {
					configurableVisualComponent.getComponentDelegate().configurationEnabledComponent();
				}
			}
		} else {
			List<ConfigurableVisualComponent> oComponents = p_oViewModel.getVmDataObject().getComponents().get(p_sPath);
			for (ConfigurableVisualComponent configurableVisualComponent : oComponents) {
				configurableVisualComponent.getComponentDelegate().configurationDisabledComponent();
			}
		}
	}

	/**
	 * Treats path on valid write component
	 * @param <VALUE_TYPE> mapping type of components
	 * @param p_oComponent component to process
	 * @param p_bIsCustom wether the component is custom
	 * @param p_oValue value to assign to the component
	 * @param p_sCompletePath complete path of the component in the view model
	 * @param p_oIterPath iterator in the component's view model path
	 * @return false if there was an error
	 * @throws BreakException sent when the including while statement has to be stopped 
	 */
	private <VALUE_TYPE> Object[] writeDataForComponentTreatPath(ConfigurableVisualComponent p_oComponent, Object p_oValue, String p_sCompletePath, Iterator<String> p_oIterPath) {
		Object[] r_oValue = new Object[2];
		r_oValue[0] = p_oValue;
		r_oValue[1] = true;
		String sFieldName;
		String sAccessorName;
		sFieldName = p_oIterPath.next();
		
		sAccessorName = computeGetAccessorOnObject(p_oComponent, r_oValue[0], sFieldName);

		try {
			if (p_oIterPath.hasNext()) {
				r_oValue[0] = r_oValue[0].getClass().getMethod(sAccessorName).invoke(r_oValue[0]);
				if (r_oValue[0] == null) {
					r_oValue[1] = false;
				}
			}
			else if (!sFieldName.equalsIgnoreCase("list")) {
				r_oValue[0] = r_oValue[0].getClass().getMethod(sAccessorName).invoke(r_oValue[0]);
			}
		}
		catch (IllegalArgumentException | SecurityException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			if(r_oValue[0] != null){
				r_oValue[0] = StringUtils.concat("Impossible to bind : ", p_sCompletePath, " on object ", r_oValue[0].getClass().getName());
			}else{
				r_oValue[0] = StringUtils.concat("Impossible to bind : ", p_sCompletePath);
			}
			Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_30050, (String) r_oValue[0], e);
		}
		
		return r_oValue;
	}

	/**
	 * Returns the get accessor of p_sFieldName on object p_oValue
	 * @param p_oComponent the component to analyse
	 * @param p_oValue object from viewmodel
	 * @param p_sFieldName the name of the field in the view model binded to the component
	 * @return the accessor name
	 */
	private String computeGetAccessorOnObject(ConfigurableVisualComponent p_oComponent,
			Object p_oValue, String p_sFieldName) {
		String sAccessorName;
		if (p_oValue instanceof SelectedListViewModel && p_oComponent instanceof SelectedComponent) {
			sAccessorName = "getSelectedItemList";
		} else {
			String sAccessor = null;
			Field oFieldToAccess = null;
			try {
				oFieldToAccess = p_oValue.getClass().getDeclaredField(p_sFieldName); // cas normal
			} catch (NoSuchFieldException e) {
				try {
					oFieldToAccess = p_oValue.getClass().getDeclaredField("o" + p_sFieldName); // cas des comboboxs
				} catch (NoSuchFieldException e1) {
					Application.getInstance().getLog().info(ErrorDefinition.FWK_MOBILE_E_30050, "NoSuchField [" + p_sFieldName + "] inexistant !");
				}
			} catch (SecurityException e) {
				Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_30050, "SecurityField [" + p_sFieldName + "] !", e);
			}
			
			if (oFieldToAccess!=null && (Boolean.class.equals(oFieldToAccess.getType()) || boolean.class.equals(oFieldToAccess.getType()))) {
				sAccessor = "is";
			} else {
				sAccessor = "get";	
			}
			
			sAccessorName = new StringBuilder()
			.append(sAccessor)
			.append(p_sFieldName.substring(0,1).toUpperCase())
			.append(p_sFieldName.substring(1)).toString();
		}
		return sAccessorName;
	}
	
	/**
	 * Serialises map content to object
	 * @param p_oObjectOutputStream stream to write to
	 * @throws IOException exception thrown
	 */
	private void writeObject(ObjectOutputStream p_oObjectOutputStream) throws IOException {
		AbstractViewModel oVm = viewModel.get();
		viewModel = null;
		oConvertUtilsBean = null;
		p_oObjectOutputStream.defaultWriteObject();
		viewModel = new WeakReference<AbstractViewModel>(oVm);
		oConvertUtilsBean = new ConvertUtilsBean();
	}
	
	/**
	 * unserialises data from stream
	 * @param p_oInputStream stream from which we deserialises
	 * @throws ClassNotFoundException exception thrown
	 * @throws IOException exception thrown
	 */
	private void readObject(ObjectInputStream p_oInputStream) throws ClassNotFoundException, IOException {
		p_oInputStream.defaultReadObject();
		oConvertUtilsBean = new ConvertUtilsBean();
	}
}
