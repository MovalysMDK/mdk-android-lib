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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * <p>BusinessRuleRegistry class.</p>
 *
 */
public class BusinessRuleRegistry {

	private Map<PropertyTarget, List<Method>> methodByProp;
	private Map<String, Map<PropertyTarget, List<Method>>> methodForField;
	private Map<String, Map<PropertyTarget, List<Method>>> methodForTrigger;
	
	/**
	 * <p>Constructor for BusinessRuleRegistry.</p>
	 */
	public BusinessRuleRegistry() {
		this.methodByProp = new HashMap<>();
		for (PropertyTarget propTarget : PropertyTarget.values()) {
			this.methodByProp.put(propTarget, new ArrayList<Method>());
		}
		this.methodForField = new HashMap<String, Map<PropertyTarget,List<Method>>>();
		this.methodForTrigger = new HashMap<String, Map<PropertyTarget,List<Method>>>();
	}

	/**
	 * <p>add.</p>
	 *
	 * @param m a {@link java.lang.reflect.Method} object.
	 * @param p_oPropertyTarget a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule.PropertyTarget} object.
	 * @param p_sKey a {@link java.lang.String} object.
	 * @param p_oFields an array of {@link java.lang.String} objects.
	 * @param p_oTriggers an array of {@link java.lang.String} objects.
	 */
	public void add(Method m, PropertyTarget p_oPropertyTarget, String p_sKey, String[] p_oFields, String[] p_oTriggers) {
		// add method in list
		List<Method> oListMethod = this.methodByProp.get(p_oPropertyTarget);
		oListMethod.add(m);
		
		// add method to fields
		for (String field : p_oFields) {
			Map<PropertyTarget, List<Method>> mapMethod = this.methodForField.get(field);
			if (mapMethod == null) {
				initMapMethodForField(field);
				mapMethod = this.methodForField.get(field);
			}
			List<Method> oFiledListMethod = mapMethod.get(p_oPropertyTarget);
			oFiledListMethod.add(m);
		}
		
		for (String trigger : p_oTriggers) {
			Map<PropertyTarget, List<Method>> mapMethod = this.methodForTrigger.get(trigger);
			if (mapMethod == null) {
				initMapMethodForTrigger(trigger);
				mapMethod = this.methodForTrigger.get(trigger);
			}
			List<Method> oFiledListMethod = mapMethod.get(p_oPropertyTarget);
			oFiledListMethod.add(m);
		}
	}
	
	private void initMapMethodForField(String p_sField) {
		Map<PropertyTarget, List<Method>> oMap = initMapPropertyMethod();
		this.methodForField.put(p_sField, oMap );
	}

	private void initMapMethodForTrigger(String p_sField) {
		Map<PropertyTarget, List<Method>> oMap = initMapPropertyMethod();
		this.methodForTrigger.put(p_sField, oMap );
	}
	
	private Map<PropertyTarget, List<Method>> initMapPropertyMethod() {
		Map<PropertyTarget, List<Method>> oMap = new HashMap<PropertyTarget, List<Method>>();
		for (PropertyTarget propTarget : PropertyTarget.values()) {
			oMap.put(propTarget, new ArrayList<Method>());
		}
		return oMap;
	}

	/**
	 * <p>getRules.</p>
	 *
	 * @param p_sKey a {@link java.lang.String} object.
	 * @param p_oPropertyTarget a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule.PropertyTarget} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<Method> getRules(String p_sKey, PropertyTarget p_oPropertyTarget) {
		List<Method> r_oList = methodByProp.get(p_oPropertyTarget);
		if (p_sKey == null || p_sKey.equals("")) {
			return r_oList;
		} else {
			List<Method> r_oListDeleted = r_oList;
			for (Method oMethod : r_oList) {
				BusinessRule oDefine = oMethod.getAnnotation(BusinessRule.class);
				
				String sName = "";
				if (oDefine.name() != null && oDefine.name().length() > 0) {
					sName = oDefine.name();
				} else if (oDefine.key() != null && oDefine.key().length() > 0) {
					sName = oDefine.key();
					Application.getInstance().getLog().debug(Application.LOG_TAG, "Error in BusinessRules on " + this.getClass().getName() + 
							" a <key> is defined : this attribute is deprecated change to <name>");
				}
				
				if (!p_sKey.equals(sName)) {
					r_oListDeleted.remove(oMethod);
				}
			}
			r_oList = r_oListDeleted;
		}
		return r_oList;
	}

	/**
	 * <p>getTargetFieldsForMethod.</p>
	 *
	 * @param p_oMethod a {@link java.lang.reflect.Method} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<String> getTargetFieldsForMethod(Method p_oMethod) {
		return Arrays.asList(p_oMethod.getAnnotation(BusinessRule.class).fields());
	}

	/**
	 * <p>getRulesWithField.</p>
	 *
	 * @param p_sPath a {@link java.lang.String} object.
	 * @return a {@link java.util.Map} object.
	 */
	public Map<PropertyTarget, List<Method>> getRulesWithField(String p_sPath) {
		return this.methodForField.get(p_sPath);
	}

	/**
	 * <p>getRulesWithTr.</p>
	 *
	 * @param p_sPath a {@link java.lang.String} object.
	 * @return a {@link java.util.Map} object.
	 */
	public Map<PropertyTarget, List<Method>> getRulesWithTr(String p_sPath) {
		return this.methodForField.get(p_sPath);
	}

	/**
	 * <p>getRulesTriggeredBy.</p>
	 *
	 * @param p_sKey a {@link java.lang.String} object.
	 * @return a {@link java.util.Map} object.
	 */
	public Map<PropertyTarget, List<Method>> getRulesTriggeredBy(String p_sKey) {
		return this.methodForTrigger.get(p_sKey);
	}
}
