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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable;

/**
 * This interface is the basis of an MDK component.
 * Setting it on your custom component and implementing its functions properly will enable 
 * <ul>
 * 	<li>data auto-binding</li>
 * 	<li>auto hiding of the field when no value is set</li>
 * 	<li>management of mandatory feature</li>
 * 	<li>use of business rules</li>
 * 	<li>etc</li>
 * </ul>
 * 
 * The name of a visual component is used by the MDK view model mechanics to bind the component to the appropriate data,
 * and has the following format: [prefix]__[ViewModelProperty]__[Type]
 * <ul>
 * 	<li>ViewModelProperty: the name of the property in the hosting ViewModel</li>
 * 	<li>type: indicates the type of component
 * 		<ul>
 * 			<li>label    : a label in resource string or configuration</li>
 * 			<li>value    : a read only value in the viewModel</li>
 * 			<li>edit     : a read/write value in the viewModel</li>
 * 			<li>master   : identifies master component (screen, element list)</li>
 * 		</ul>
 *	</li>
 * </ul>
 * 
 * It implements getters for two objects:
 * <ul>
 * 	<li>ComponentDelegate: manages all the common features of components. These features are grouped by the data type manipulated by the component</li>
 * 	<li>FwkDelegate: gathers the inner processes of MDK, it should never be overridden without caution</li>
 * </ul>
 */
public interface ConfigurableVisualComponent {

	/** type label */
	public static final String TYPE_LABEL = "label";
	/** type value */
	public static final String TYPE_VALUE = "value";
	/** type edit */
	public static final String TYPE_EDIT = "edit";
	/** type novalue */
	public static final String TYPE_NOVALUE = "_novalue";

	/**
	 * Xml attributes available on MDK components
	 */
	public enum Attribute {
		/** Xml attribute for the label of a field */
		LABEL_ATTRIBUTE("label"),
		/** Xml attribute for the regular expression used to validate the content of the field */
		REGEX_ATTRIBUTE("regex"),
		/** Xml attribute for the error message to be displayed when regular expression validation fails */
		REGEX_ERROR_ATTRIBUTE("regex_error_message"),
		/** Xml attribute for type validation of field */
		DATA_TYPE_ATTRIBUTE("data-type"),
		/** Xml attribute defining field validation on a given format */
		FORMAT_ATTRIBUTE("format"),
		/** Xml attribute for maximum length validation on the field */
		MAX_LENGTH_ATTRIBUTE("max-length"),
		/** Xml attribute defining maximum number of digits on a floating value */
		NB_DIGITS_ATTRIBUTE("nb-digit"),
		/** Xml attribute defining the maximum value of a numeric field */
		MAX_VALUE_ATTRIBUTE("max-value"),
		/** Xml attribute defining the minimum value of a numeric field */
		MIN_VALUE_ATTRIBUTE("min-value"),
		/** Xml attribute defining a minimum date input on date fields */
		MIN_DATE_ATTRIBUTE("min-date"),
		/** Xml attribute defining a maximum date input on date fields  */
		MAX_DATE_ATTRIBUTE("max-date"),
		/** Xml attribute defining a minimum hours input on date fields */
		MIN_HOURS_ATTRIBUTE("min-hour"),
		/** Xml attribute defining a maximum hours input on date fields  */
		MAX_HOURS_ATTRIBUTE("max-hour"),
		/** Xml attribute defining a minimum minutes input on date fields */
		MIN_MINUTES_ATTRIBUTE("min-minutes"),
		/** Xml attribute defining a maximum minutes input on date fields */
		MAX_MINUTES_ATTRIBUTE("max-minutes"),
//		/** Xml attribute defining the default number of minutes set (can be relative) */
//		DEFAULT_MINUTES_ATTRIBUTE("default-minutes"),
//		/** Xml attribute defining the default number of hours set (can be relative) */
//		DEFAULT_HOURS_ATTRIBUTE("default-hour"),
//		/** Xml attribute defining the default number of days set (can be relative) */
//		DEFAULT_INCR_DAY_ATTRIBUTE("default-increment-day"),
//		/** Etiquette de la propriété de définition du nombre de jour relatif pour l'incrementation
//		 * Xml attribute defining the number of days increment */
//		INCREMENT_DAY_ATTRIBUTE("increment-day"),
//		/** Etiquette de la propriété de définition du nombre de jour relatif pour la décrementation */
//		DECREMENT_DAY_ATTRIBUTE("decrement-day"),
		/** Etiquette de la propriété définissant si on autorise la saisie de minutes seulement dans un champ de type durée */
		ONLY_MINUTES_ATTRIBUTE("only-minutes-authorized"),
//		/** Etiquette de la propriété définissant la valeur par défaut */
//		DEFAULT_VALUE("default-value"),
		/** Xml attribute defining the default time format */
		TIME_FORMAT_ATTRIBUTE("time_format"),
		/** Xml attribute defining the default date format */
		DATE_FORMAT_ATTRIBUTE("date_format"),
		/** Xml attribute defining a converter {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter} to use on field  */
		CONVERTER_NAME_ATTRIBUTE("converter_name"),
		/** Xml attribute defining a formatter {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter} to use on field  */
		FORMATTER_NAME_ATTRIBUTE("formatter_name")
		;

		/** name of the attribute */
		private String name ;

		/**
		 * Constructor of the enum
		 * @param p_sName the name of the attribute
		 */
		private Attribute(String p_sName){
			this.name = p_sName ;
		}

		/**
		 * Getter for name
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return this.name ;
		}
	}
	
	/**
	 * Returns the framework delegate instance of type {@link VisualComponentFwkDelegate}
	 * @return the framework delegate of the component, never null
	 */
	public VisualComponentFwkDelegate getComponentFwkDelegate();
	
	/**
	 * Returns the component delegate instance of type {@link VisualComponentDelegate}
	 * @return the delegate of the component, never null
	 */
	@SuppressWarnings("rawtypes")
	public VisualComponentDelegate getComponentDelegate();

}
