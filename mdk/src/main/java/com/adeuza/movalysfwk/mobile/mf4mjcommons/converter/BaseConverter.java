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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition;

/**
 * <p>Contains an abstract converter used to convert any type to another compatible type.</p>
 */
public class BaseConverter {

	/** Converts package */
	private static String converterPackage = "com.adeuza.movalysfwk.mobile.mf4mjcommons.converter";

	/** Set of objects linked to primitive java types */
	private static final Set<String> WRAPPER_TYPES = getWrapperTypes();

	/**
	 * Return true if the object which name is given in parameter has a primitive equivalent in java
	 * @param p_sClass the name of the object to check
	 * @return true if the object has a primitive equivalent
	 */
    private static boolean isWrapperType(String p_sClass) {
        return WRAPPER_TYPES.contains(p_sClass);
    }

    /**
     * Sets the WRAPPER_TYPES collection
     * @return the WRAPPER_TYPES collection
     */
    private static Set<String> getWrapperTypes() {
        Set<String> r_oSet = new HashSet<String>();
        r_oSet.add("Boolean");
        r_oSet.add("Character");
        r_oSet.add("Byte");
        r_oSet.add("Short");
        r_oSet.add("Integer");
        r_oSet.add("Long");
        r_oSet.add("Float");
        r_oSet.add("Double");
        r_oSet.add("Void");
        return r_oSet;
    }
	
	/**
	 * This method applies a converter to a value, following the specified source and target class.
	 *
	 * @param p_sSourceClassName The name of the class of the source value
	 * @param p_sTargetClassName The target class name for the source value after conversion
	 * @param p_sSourceValue The source value to convert
	 * @return a {@link java.lang.Object} object.
	 */
	@SuppressWarnings("rawtypes")
	public Object convert(String p_sSourceClassName, String p_sTargetClassName, Object p_sSourceValue) {
		
		Object convertValue = p_sSourceValue;
		String sConverterClassName = null;
		Method oConversionMethod = null;
		String log = "";
		
		/* If the source class name and the target class name are equal, it is not needed to convert.
		 * Otherwise, a converter class is built from the specified source and target class, and the corresponding
		 * method is called to do the conversion.
		 */
		if(p_sSourceClassName.equalsIgnoreCase(p_sTargetClassName)) {
			return convertValue;
		}
		else {
			String sTargetClassName = p_sTargetClassName;
			
			if (Character.isUpperCase(sTargetClassName.charAt(0)) && isWrapperType(sTargetClassName)) {
					sTargetClassName = sTargetClassName.toLowerCase() + "Object";
			}
			
			String lowerCaseTargetClassName = sTargetClassName.substring(0, 1).toLowerCase() + sTargetClassName.substring(1);
			
			try {
				sConverterClassName = BaseConverter.converterPackage+"."+StringUtils.concat(correctClassName(p_sSourceClassName), "ToDataConverter");

				Class conversionClass = Class.forName(sConverterClassName);
				
				oConversionMethod = conversionClass.getDeclaredMethod(StringUtils.concat(lowerCaseTargetClassName, "From" + p_sSourceClassName + "Value"), p_sSourceValue.getClass());
				if(oConversionMethod != null) {
					convertValue = oConversionMethod.invoke(null, p_sSourceValue);
				}
			}
			catch (NoSuchMethodException e) {
				log = StringUtils.concat("NoSuchMethodException Impossible to find : ", StringUtils.concat(lowerCaseTargetClassName, "From" + p_sSourceClassName + "Value"));
				Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_30050, (String) log);
			} 
			catch (InvocationTargetException e) {
				log = StringUtils.concat("InvocationTargetException Impossible to find : ", oConversionMethod.getName());
				Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_30050, (String) log);
			}
			catch (ClassNotFoundException e) {
				log = StringUtils.concat("ClassNotFoundException Impossible to find : ", sConverterClassName);
				Application.getInstance().getLog().info(ErrorDefinition.FWK_MOBILE_E_30050, (String) log);
			} 
			catch (IllegalAccessException e) {
				log = StringUtils.concat("IllegalAccessException Impossible to find : ", oConversionMethod.getName());
				Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_30050, (String) log);
			}
		}
		return convertValue;
	}
	
	/**
	 * This method allows to correct a className to call a unique converter for
	 * 2 similar types. Example : int and Integer are similar types.
	 * The "int" baseClassName will be corrected to "Integer"
	 * @param p_sBaseClassName The base className that will be enventuyally corrected
	 * @return A corrected or not className
	 */
	private String correctClassName(String p_sBaseClassName) {
		if(p_sBaseClassName.equals("int")) {
			return "Integer";
		}
		if(p_sBaseClassName.equals("char")) {
			return "Character";
		}
		return p_sBaseClassName;
	}
}
