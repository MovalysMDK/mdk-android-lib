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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.FixedListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.ListenerOnCollectionModified;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.ListenerOnFieldModified;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule.BusinessRule;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule.BusinessRuleRegistry;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule.PropertyTarget;

/**
 * Delegate for view model notifications on modified fields
 */
@SuppressWarnings("serial")
public class ViewModelNotificationDelegate implements Serializable {

	/** Number of parameters to find in a notification method with a validation */
	private static final int METHOD_PARAMS_NUMBER_WITH_VALIDATION = 4;
	
	/** Number of parameters to find in a notification method without a validation */
	private static final int METHOD_PARAMS_NUMBER_WITHOUT_VALIDATION = 3;
	
	/** Reference to the parent view model */
	private WeakReference<AbstractViewModel> viewModel;
	
	/** cached fields */
	private List<CachedFieldStatus> cachedFieldsStatusList = new ArrayList<CachedFieldStatus>();

	/** list cache fields methods */
	private HashMap<String, List<Method>> fieldsCacheMethods;
	
	/**
	 * Constructor
	 *
	 * @param p_oViewModel parent view model
	 */
	public ViewModelNotificationDelegate(AbstractViewModel p_oViewModel) {
		this.viewModel = new WeakReference<AbstractViewModel>(p_oViewModel);
	}
	
	/**
	 * Analyze annotation on class method
	 *
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public void analyseClass(Class<? extends AbstractViewModel> p_oClass) {

		AbstractViewModel oViewModel = this.viewModel.get();
		// dont analyze ListViewModel instance
		// ListViewModel do not have fields (so no listener too)
		if (oViewModel != null && !(oViewModel instanceof ListViewModel<?, ?>) ) {
		
			for (Method m : p_oClass.getDeclaredMethods()) {
				//ABE add listeners for fields
				ListenerOnFieldModified oDefine1 = m.getAnnotation(ListenerOnFieldModified.class);
				if (oDefine1!=null) {
					oViewModel.getVmDataObject().addMethodFieldNotifier(m, oDefine1.fields());
				}
	
				//FGO add listeners for collections
				ListenerOnCollectionModified oDefine2 = m.getAnnotation(ListenerOnCollectionModified.class);
				if (oDefine2!=null) {
					oViewModel.getVmDataObject().addMethodCollectionNotifier(m, oDefine2.fields());
				}
				
				// ABE add business rule listener
				BusinessRule oDefine3 = m.getAnnotation(BusinessRule.class);
				if (oDefine3!=null) {
					// ABE mandatory || hidden || enable
					// oViewModel.getVmDataObject().getBusinessRuleRegistry()
					// add compute use name in BusinessRuleRegistry
					// if name != "" use name : if key != null throw error
					// if name == "" && key != "" use key
					String sName = "";
					if (oDefine3.name() != null && oDefine3.name().length() > 0) {
						sName = oDefine3.name();
					} else if (oDefine3.key() != null && oDefine3.key().length() > 0) {
						sName = oDefine3.key();
						Application.getInstance().getLog().debug(Application.LOG_TAG, "Error in BusinessRules on " + this.getClass().getName() + 
								" a <key> is defined : this attribute is deprecated change to <name>");
					}
					oViewModel.getVmDataObject().addBusinessRuleRegistry(m, oDefine3.propertyTarget(), sName, oDefine3.fields(), oDefine3.triggers());
				}
			}
		}
	}
	
	/**
	 * Notify the field with new content value and {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ValidationState} of the component
	 *
	 * @param p_sKey key of the field in the view model
	 * @param p_oOldVal old content of the field
	 * @param p_oNewVal new content of the field
	 * @param p_oComponentValidation {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ValidationState} of the field
	 */
	public void notifyFieldChanged(String p_sKey, Object p_oOldVal, Object p_oNewVal, ValidationState p_oComponentValidation) {
		AbstractViewModel oViewModel = this.viewModel.get();
		
		ValidationState oValid = p_oComponentValidation;
		
		if (oValid == null) {
			oValid = ValidationState.INIT;
		}
		
		if (oViewModel != null && oViewModel.getVmDataObject().isAlwaysNotify()) {
			
			if (p_oComponentValidation == null) {
				oViewModel.getVmDataObject().updateComponentValidationStatus(p_sKey, oValid);
			}
			
			String sReadCurrentVm = oViewModel.getReadCurrentVm();
			
			if ( (oViewModel.isUpdatedValue() && p_sKey.equals(sReadCurrentVm)) || !p_sKey.equals(sReadCurrentVm)) {
				if (Application.getInstance().isOnUIThread()) {
					oViewModel.writeDataToComponent(p_sKey);
				}
				else{
					(Application.getInstance()).runOnUiThread(new WriteDataRunnable(oViewModel, p_sKey));
				}
			}

			executeFieldListeners(p_sKey, p_oOldVal, p_oNewVal, oViewModel, oValid);
		} else {
			this.cachedFieldsStatusList.add(new CachedFieldStatus(p_sKey, p_oOldVal, p_oNewVal, oValid));
		}
	}

	/**
	 * @param p_sKey
	 * @param p_oOldVal
	 * @param p_oNewVal
	 * @param p_oViewModel
	 * @param p_oValid
	 */
	private void executeFieldListeners(String p_sKey, Object p_oOldVal, Object p_oNewVal, AbstractViewModel p_oViewModel, ValidationState p_oValid) {
		List<Method> oMethodsList = p_oViewModel.getVmDataObject().getNotifyMethodFields().get(p_sKey);
		if (oMethodsList != null) {
			invokeNotifyMethodsOnViewModel(p_sKey, p_oOldVal, p_oNewVal, p_oViewModel, p_oValid, oMethodsList);
		}

		// get all method by property triggered by the field
		Map<PropertyTarget, List<Method>> triggeredRules = p_oViewModel.getVmDataObject().getBusinessRuleRegistry().getRulesTriggeredBy(p_sKey);
		if (triggeredRules != null) {
			applyTriggeredRules(p_oViewModel, triggeredRules);
		}
	}

	/**
	 * Applies rules triggered by a component modification
	 *
	 * @param p_oViewModel Hosting view model
	 * @param p_oTriggeredRules list of triggered rules
	 */
	public void applyTriggeredRules(AbstractViewModel p_oViewModel, Map<PropertyTarget, List<Method>> p_oTriggeredRules) {
		// for all properties apply rules
		for (Entry<PropertyTarget, List<Method>> oEntryProp : p_oTriggeredRules.entrySet()) {
			List<Method> triggeredMethodsRules = oEntryProp.getValue();
			if (triggeredMethodsRules != null && !triggeredMethodsRules.isEmpty()) {
				List<ConfigurableVisualComponent> oAffectedListComp = null;
				Map<Method, List<ConfigurableVisualComponent>> methodsToComponents = new HashMap<Method, List<ConfigurableVisualComponent>>();
				// retrieve all affected components
				for (Method oMethod : triggeredMethodsRules) {
					oAffectedListComp = new ArrayList<ConfigurableVisualComponent>();
					List<String> listComponentKeys = p_oViewModel.getVmDataObject().getBusinessRuleRegistry().getTargetFieldsForMethod(oMethod);
					for (String componentKey : listComponentKeys) {
						List<ConfigurableVisualComponent> tmpComponentList = p_oViewModel.getVmDataObject().getComponents().get(componentKey);
						if (tmpComponentList != null && !tmpComponentList.isEmpty()) {
							oAffectedListComp.addAll(tmpComponentList);
						}
					}
					methodsToComponents.put(oMethod, oAffectedListComp);
				}
				// apply rules
				applyRulesFromTrigger(oEntryProp.getKey(), p_oTriggeredRules, methodsToComponents);
			}
		}
	}

	/**
	 * Invokes the methods with a notify annotation on the view model
	 *
	 * @param p_sKey view model attribute key to notify
	 * @param p_oOldVal old value in the attribute
	 * @param p_oNewVal new value in the atttibute
	 * @param p_oViewModel view model being processed
	 * @param p_oValid {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ValidationState} of the component binded to the attribute
	 * @param p_oMethodsList list of methods to invoke
	 */
	public void invokeNotifyMethodsOnViewModel(String p_sKey, Object p_oOldVal, Object p_oNewVal,
			AbstractViewModel p_oViewModel, ValidationState p_oValid, List<Method> p_oMethodsList) {
		for (Method oMethod : p_oMethodsList) {
			try {
				if (oMethod.getParameterTypes().length == METHOD_PARAMS_NUMBER_WITH_VALIDATION 
					&& oMethod.getParameterTypes()[METHOD_PARAMS_NUMBER_WITHOUT_VALIDATION] == ValidationState.class ) {
					oMethod.invoke(p_oViewModel, p_sKey, p_oOldVal, p_oNewVal, p_oValid);
				} else {
					// si valid, on appelle l'invocation
					if (oMethod.getParameterTypes().length == METHOD_PARAMS_NUMBER_WITHOUT_VALIDATION) { 
						if (!p_oValid.isError()) {
							oMethod.invoke(p_oViewModel, p_sKey, p_oOldVal, p_oNewVal);
						}
					} else {
						Application.getInstance().getLog().error(Application.LOG_TAG, "Notify method '" 
								+ p_oViewModel.getClass().getSimpleName() + "." +  oMethod.getName() +"', key= '"+ p_sKey +"', with wrong argument number ");
					}
				}
				
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				Application.getInstance().getLog().error(Application.LOG_TAG, "Notify method error '" 
						+ p_oViewModel.getClass().getSimpleName() + "." +  oMethod.getName() +"', key= '"+ p_sKey, e );
			}
		}
	}
	
	/**
	 * Invokes the method to notify when collection has changed.
	 *
	 * @param p_oSubVm Sub view model to use for method invocation
	 * @param p_eAction action to use for method invocation
	 * @param p_iIdItem item id of the modified object to use for method invocation
	 * @param p_oNewOrCurrentOrDeletedObject object to modify to use for method invocation
	 */
	public void notifyCollectionChanged(ViewModel p_oSubVm, Action p_eAction, int p_iIdItem, ViewModel p_oNewOrCurrentOrDeletedObject) {

		AbstractViewModel oViewModel = this.viewModel.get();
		
		if (oViewModel != null && oViewModel.getVmDataObject().isAlwaysNotify()) {
			if (this.fieldsCacheMethods!= null) {
				// only fields with methods
				for (Entry<String, List<Method>> fieldCacheMethods : this.fieldsCacheMethods.entrySet()) {
					
					callMethodsList(oViewModel, fieldCacheMethods.getKey(), fieldCacheMethods.getValue(), p_oSubVm, p_eAction, p_iIdItem, p_oNewOrCurrentOrDeletedObject);
				}
				
			} else {
				// fields cache on first call
				this.fieldsCacheMethods = new HashMap<String, List<Method>>();
				// For each field...
				for(Field f : oViewModel.getClass().getDeclaredFields()) {
					FixedListViewModel annotation = f.getAnnotation(FixedListViewModel.class);
		
					// which has the ViewModelFieldList annotation...
					if (annotation != null) {
						String sKey = f.getName();
						
						List<Method> methodsList = oViewModel.getVmDataObject().getNotifyMethodCollection().get(sKey);
	
						if (methodsList != null) {
							this.fieldsCacheMethods.put(sKey, methodsList);
						}
						
						callMethodsList(oViewModel, sKey, methodsList, p_oSubVm, p_eAction, p_iIdItem, p_oNewOrCurrentOrDeletedObject);
						
					}
				}
			}
		}

	}
	
	private void callMethodsList(AbstractViewModel p_oViewModel, String p_sKey, List<Method> p_oMethodsList, ViewModel p_oSubVm, Action p_eAction, int p_iIdItem, ViewModel p_oNewOrCurrentOrDeletedObject) {
		// for each registered notification method
		if (p_oMethodsList != null) {
			for (Method method : p_oMethodsList) {
				try {
					// invoke the method to notify
					method.invoke(p_oViewModel, p_oSubVm, p_eAction, p_iIdItem, p_oNewOrCurrentOrDeletedObject);
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
					Application.getInstance().getLog().error(Application.LOG_TAG, "Notify method '" 
							+ p_oViewModel.getClass().getSimpleName() + "." +  method.getName() +"', key= '"+ p_sKey, e );
				}
			}
		}
	}

	/**
	 * Apply rules to components from a trigger
	 * @param p_oProp property target to use
	 * @param p_oRules rules to apply
	 * @param p_oMethodsToComponents components to analyse
	 */
	@SuppressWarnings("unchecked")
	private void applyRulesFromTrigger(PropertyTarget p_oProp, Map<PropertyTarget, List<Method>> p_oRules, 
			Map<Method, List<ConfigurableVisualComponent>> p_oMethodsToComponents) {
		
		AbstractViewModel oViewModel = this.viewModel.get();
		
		if (oViewModel != null) {
		
			List<Method> propertyRules = p_oRules.get(p_oProp);
			Map<ConfigurableVisualComponent, Boolean> appliedRuleForComponent = new HashMap<ConfigurableVisualComponent, Boolean>();
			if (propertyRules != null && !propertyRules.isEmpty()) {
				for (Method method : propertyRules) {
					boolean bPropResult = false;
					try {
						bPropResult = (Boolean) method.invoke(oViewModel);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						Application.getInstance().getLog().error(Application.LOG_TAG, "[AbstractViewModel.applyRulesFromTrigger] InstantiationException !", e);
					}
					for (ConfigurableVisualComponent configurableVisualComponent : p_oMethodsToComponents.get(method)) {
						if (appliedRuleForComponent.containsKey(configurableVisualComponent)) {
							appliedRuleForComponent.put(configurableVisualComponent, appliedRuleForComponent.get(configurableVisualComponent) || bPropResult);
						} else {
							appliedRuleForComponent.put(configurableVisualComponent, bPropResult);
						}
					}
				}
				
				for (Entry<ConfigurableVisualComponent, Boolean> oEntry : appliedRuleForComponent.entrySet()) {
					p_oProp.getComputeHelper().applyRuleForComponent(oEntry.getKey(), oEntry.getValue());
				}
			}
		}
	}
	
	/**
	 * Exec rules for the specific key
	 *
	 * @param p_sKey the key to compute rules
	 * @param p_oPropertyTargets the rules to compute if empty all rules are computed
	 */
	@SuppressWarnings("unchecked")
	public void execRules(String p_sKey, PropertyTarget... p_oPropertyTargets) {
		AbstractViewModel oViewModel = this.viewModel.get();
		
		if (oViewModel != null) {
			PropertyTarget[] tPropertyTargets = p_oPropertyTargets;
			if (tPropertyTargets == null || tPropertyTargets.length == 0) {
				tPropertyTargets = PropertyTarget.values();
			}
			for (PropertyTarget propertyTarget : tPropertyTargets) {
				List<Method> oMethods = oViewModel.getVmDataObject().getBusinessRuleRegistry().getRules(p_sKey, propertyTarget);
				Object ruleResult = null;
				for (Method method : oMethods) {
					try {
						ruleResult = method.invoke(oViewModel);
						List<String> fields = oViewModel.getVmDataObject().getBusinessRuleRegistry().getTargetFieldsForMethod(method);
						for (String field : fields) {
							List<ConfigurableVisualComponent> listComponents = oViewModel.getVmDataObject().getComponents().get(field);
							for (ConfigurableVisualComponent configurableVisualComponent : listComponents) {
								propertyTarget.getComputeHelper().applyRuleForComponent(configurableVisualComponent, ruleResult);
							}
						}
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						Application.getInstance().getLog().error(Application.LOG_TAG, "[AbstractViewModel.execRules] InstantiationException !", e);
					}
				}
			}
		}
	}
	
	/**
	 * Serialises map content to object
	 * @param p_oObjectOutputStream stream to write to
	 * @throws IOException exception thrown
	 */
	private void writeObject(ObjectOutputStream p_oObjectOutputStream) throws IOException {
		AbstractViewModel oViewModel = this.viewModel.get();
		
		if (oViewModel != null) {
			Map<String, List<Method>> a = oViewModel.getVmDataObject().getNotifyMethodFields();
			Map<String, List<Method>> a2 = oViewModel.getVmDataObject().getNotifyMethodCollection();
			BusinessRuleRegistry b = oViewModel.getVmDataObject().getBusinessRuleRegistry();
			
			oViewModel.getVmDataObject().setNotifyMethodFields(null);
			oViewModel.getVmDataObject().setNotifyMethodCollection(null);
			oViewModel.getVmDataObject().setBusinessRuleRegistry(null);
			
			this.viewModel = null;
			this.cachedFieldsStatusList = null;
			
			p_oObjectOutputStream.defaultWriteObject();
			
			oViewModel.getVmDataObject().setNotifyMethodFields(a);
			oViewModel.getVmDataObject().setNotifyMethodCollection(a2);
			oViewModel.getVmDataObject().setBusinessRuleRegistry(b);
			
			this.viewModel = new WeakReference<AbstractViewModel>(oViewModel);
			this.cachedFieldsStatusList = new ArrayList<CachedFieldStatus>();
		}
	}
	
	/**
	 * unserialises data from stream
	 * @param p_oInputStream stream from which we deserialises
	 * @throws ClassNotFoundException exception thrown
	 * @throws IOException exception thrown
	 */
	private void readObject(ObjectInputStream p_oInputStream) throws ClassNotFoundException, IOException {
		p_oInputStream.defaultReadObject();
		this.viewModel = new WeakReference<AbstractViewModel>(null);
		
		if (this.cachedFieldsStatusList != null) {
			this.cachedFieldsStatusList.clear();
		} else {
			this.cachedFieldsStatusList = new ArrayList<CachedFieldStatus>();
		}
	}
	
	/**
	 * <p>executeCachedListeners.</p>
	 *
	 * @param p_oViewModel a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AbstractViewModel} object.
	 */
	public void executeCachedListeners(AbstractViewModel p_oViewModel) {
		for (CachedFieldStatus oFieldStatus : cachedFieldsStatusList) {
			this.executeFieldListeners(oFieldStatus.getKey(), oFieldStatus.getOldVal(), oFieldStatus.getNewVal(), p_oViewModel, oFieldStatus.getValid());
		}
		cachedFieldsStatusList.clear();
	}
	
	/**
	 * Clear the fields method cache
	 */
	public void clear() {
		if (this.fieldsCacheMethods != null) {
			this.fieldsCacheMethods.clear();
		}
	}
	
	static class CachedFieldStatus {
		private String sKey; 
		private Object oOldVal; 
		private Object oNewVal;
		private ValidationState oValid;
		
		public CachedFieldStatus(String p_sKey, Object p_oOldVal, Object p_oNewVal, ValidationState p_oValid) {
			this.sKey = p_sKey;
			this.oOldVal = p_oOldVal;
			this.oNewVal = p_oNewVal;
			this.oValid = p_oValid;
		}

		/**
		 * @return the sKey
		 */
		public String getKey() {
			return sKey;
		}

		/**
		 * @return the oOldVal
		 */
		public Object getOldVal() {
			return oOldVal;
		}

		/**
		 * @return the oNewVal
		 */
		public Object getNewVal() {
			return oNewVal;
		}

		/**
		 * @return the oValid
		 */
		public ValidationState getValid() {
			return oValid;
		}
	}
}
