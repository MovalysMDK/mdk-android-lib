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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.assembler;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.AppConfiguration;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.AbstractTEntityFieldConfiguration;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.TEntityConfiguration;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.list.TListDescriptor;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.property.TProperty;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.visual.TGraphConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.AbstractConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.AbstractEntityFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.BasicEntityFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.EntityConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.list.CustomListConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.property.Property;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.ExtBeanType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.serialization.SerializationDao;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.VisualComponentDescriptorsHandler;

/**
 * <p>Utility class used to transform the remote configuration, provided by synchronisation, into a ConfigurationsHandler.</p>
 *
 *
 */
public class TransferConfigurationAssembler {
	/**
	 * Name of the parameter that store the max length of a field.
	 */
	public static final String MAX_LENGTH_PARAMETER = "max-length";

	/**
	 * Pattern of properties that override a label.
	 */
	private static final Pattern LABEL_PROPERTY_PATTERN = Pattern.compile("^([a-zA-Z0-9]+__[a-zA-Z0-9]+)__label$");

	/**
	 * Suffix of properties that override visibility.
	 */
	public static final Pattern VISIBLE_PROPERTY_PATTERN = Pattern.compile("^([a-zA-Z0-9]+__[a-zA-Z0-9]+)__visible$");

	/**
	 * Reads the remote configuration, provided by synchronisation, and updates the local configuration.
	 *
	 * @param p_oContext
	 * 		The context that provides database/serialization access.
	 * @param p_oTransferObject
	 * 		The remote configuration.
	 */
	public void consolidate(MContext p_oContext, final AppConfiguration p_oTransferObject) {
		if (p_oTransferObject != null) {
			this.consolidateEntities(p_oTransferObject.getEntities());
			this.consolidateUIComponents(p_oTransferObject.getGraphComponents());
			this.consolidateProperties(p_oTransferObject.getProperties());
			this.consolidateList(p_oTransferObject.getLists());

			SerializationDao oDao = BeanLoader.getInstance().getBean(SerializationDao.class);
			oDao.saveObject(p_oContext, ConfigurationsHandler.SERIALIZATION_KEY,
					ConfigurationsHandler.getInstance());

			VisualComponentDescriptorsHandler.getInstance().resetAndComputeRealDescriptors();
		}
	}

	//-------------------------------------------------------------------------
	//							1ST PART : ENTITIES
	//-------------------------------------------------------------------------

	/**
	 * Loads the configuration of entities into the {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler}.
	 *
	 * @param p_oTransferObjects
	 * 		Configuration of entities. This configuration has been provided by a Restfull service.
	 */
	protected void consolidateEntities(final Collection<TEntityConfiguration> p_oTransferObjects) {
		if (p_oTransferObjects != null) {
			for (TEntityConfiguration oTransferObject : p_oTransferObjects) {
				this.consolidate(oTransferObject);
			}
		}
	}

	/**
	 * Loads the configuration of ONE entity into the {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler}
	 *
	 * @param p_oTransferObject
	 * 		Configuration if an entity. This configuration has been provided by a restfull service.
	 */
	protected void consolidate(final TEntityConfiguration p_oTransferObject) {
		if (p_oTransferObject != null) {
			final ConfigurationsHandler oHandler = ConfigurationsHandler.getInstance();
			final String sEntityName = p_oTransferObject.getName();

			EntityConfiguration oEntity = oHandler.getEntityConfiguration(sEntityName);
			if (oEntity == null) {
				oEntity = new EntityConfiguration(sEntityName);
				oHandler.addConfiguration(oEntity);
			}

			this.merge(p_oTransferObject, oEntity);
		}
	}

	/**
	 * Merges the configuration of an entity, provided by an external source, with the configuration
	 * of the same entity known by the system.
	 *
	 * @param p_oSource
	 * 		The configuration provided by an external source. Never null.
	 * @param p_oTarget
	 * 		The actual version of the entity's configuration. Never null.
	 */
	protected void merge(TEntityConfiguration p_oSource, EntityConfiguration p_oTarget) {

		// Merge des champs
		Collection<AbstractTEntityFieldConfiguration> oTransferFields = p_oSource.getFields();

		if (oTransferFields != null) {
			String sFieldName;
			AbstractEntityFieldConfiguration oLocalField = null;
			for (AbstractTEntityFieldConfiguration oTransferField: oTransferFields) {
				sFieldName = oTransferField.getName();
				oLocalField = new BasicEntityFieldConfiguration(sFieldName, oTransferField.getType());
				
				this.completeEntityFieldConfiguration(oLocalField, oTransferField);
				p_oTarget.addFieldConfiguration(oLocalField);
				this.merge(oTransferField, oLocalField);
			}
		}
	}
	
	/**
	 * Complete the configuration of a field
	 *
	 * @param p_oConfiguration the configuration to complete
	 * @param p_oTransfert the object source to use
	 */
	protected void completeEntityFieldConfiguration(AbstractEntityFieldConfiguration p_oConfiguration, AbstractTEntityFieldConfiguration p_oTransfert) {
		Object oMaxLength = p_oTransfert.getParameters().get(MAX_LENGTH_PARAMETER);
		if (oMaxLength != null) {
			String sMaxLength = (String) oMaxLength;
			if (sMaxLength.length() > 0) {
				p_oConfiguration.setMaxLength(Integer.parseInt(sMaxLength));
			}
		}
		p_oConfiguration.putAllParameters(p_oTransfert.getParameters());
	}

	/**
	 * Merges the configuration of a field, provided by an external source, with the actual version of this field.
	 *
	 * @param p_oSource
	 * 		The remote version of the field configuration.
	 * @param p_oTarget
	 * 		The local version of the field configuration.
	 */
	public void merge(final AbstractTEntityFieldConfiguration p_oSource,
			AbstractEntityFieldConfiguration p_oTarget) {

// A2A_DEV EMA Exception gérer message
//		if (p_oSource.isCustomisable() != p_oTarget.isCustomisable()) {
//			throw new MobileFwkException();
//		}

		// A2A_DEV EMA OVERRIDE
		//p_oTarget.setOverridden(true);
		//p_oTarget.setMandatory(p_oSource.isMandatory());
		
		// A2A_DEV EMA MERGE WORKFLOW, PARAMETERS, etc.
	}

	//-------------------------------------------------------------------------
	//							2ND PART : UI COMPONENTS
	//-------------------------------------------------------------------------

	/**
	 * Loads the configuration of UI components into the {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler}
	 *
	 * @param p_oTransferObjects
	 * 		Configuration of UI components. This configuration has been provided by a Restfull service.
	 */
	protected void consolidateUIComponents(final Collection<TGraphConfiguration> p_oTransferObjects) {
		if (p_oTransferObjects != null) {
			for (TGraphConfiguration oTransferObject : p_oTransferObjects) {
				this.consolidate(oTransferObject);
			}
		}
	}

	/**
	 * Updates the configuration of an UI component.
	 *
	 * @param p_oTransferObject the remote version of the configuration to update.
	 */
	protected void consolidate(final TGraphConfiguration p_oTransferObject) {
		if (p_oTransferObject != null) {
			final ConfigurationsHandler oHandler = ConfigurationsHandler.getInstance();
			final String sComponentName = p_oTransferObject.getName();

			TransferConfigEltAssembler<AbstractConfiguration, TGraphConfiguration> oAssembler = (TransferConfigEltAssembler<AbstractConfiguration, TGraphConfiguration>) BeanLoader.getInstance().getBean(ExtBeanType.Transfer, p_oTransferObject.getClass()); 
			
			VisualComponentConfiguration oComponent = oHandler.getVisualConfiguration(sComponentName);
			if (oComponent == null) {
				oHandler.addConfiguration(oAssembler.toBusinessObject(p_oTransferObject));
			}
			else {
				oAssembler.merge(p_oTransferObject, oComponent);
			}
		}
	}

	//-------------------------------------------------------------------------
	//							3TD PART : MANAGEMENT CONFIGURATION
	//-------------------------------------------------------------------------

	/**
	 * Loads the configuration of UI components into the {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler}
	 *
	 * @param p_oTransferObjects
	 * 		Configuration of UI components. This configuration has been provided by a Restfull service.
	 */
	protected void consolidateManagementConfiguration(final Collection<ManagementConfiguration> p_oTransferObjects) {
		if (p_oTransferObjects != null) {
			for (ManagementConfiguration oTransferObject : p_oTransferObjects) {
				this.consolidate(oTransferObject);
			}
		}
	}

	/**
	 * Updates the configuration of an UI component.
	 *
	 * @param p_oTransferObject the remote version of the configuration to update.
	 */
	protected void consolidate(final ManagementConfiguration p_oTransferObject) {
		if (p_oTransferObject != null) {
			final ConfigurationsHandler oHandler = ConfigurationsHandler.getInstance();

			oHandler.removeManagementConfiguration(p_oTransferObject.getName());

			final ManagementConfiguration oExistingConfiguration = oHandler.getManagementConfiguration(p_oTransferObject.getName().concat(".bak")).clone();
			oExistingConfiguration.setName(p_oTransferObject.getName());
			oHandler.addConfiguration(oExistingConfiguration);
			oExistingConfiguration.merge(p_oTransferObject);
		}
	}

	//-------------------------------------------------------------------------
	//							4TH PART : PROPERTIES
	//-------------------------------------------------------------------------

	/**
	 * Updates the properties of the {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler}
	 *
	 * @param p_oTransferObjects The properties provided by synchronisation
	 */
	protected void consolidateProperties(final Collection<TProperty> p_oTransferObjects) {
		if (p_oTransferObjects != null) {
			for (TProperty oTransferObject : p_oTransferObjects) {
				this.consolidate(oTransferObject);
			}
		}
	}

	/**
	 * Creates or delete the property registered into the {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler}
	 *
	 * @param p_oTransferObject
	 * 		The property provided by synchronisation.
	 */
	protected void consolidate(final TProperty p_oTransferObject) {
		if (p_oTransferObject != null) {
			String sComponentName = this.getComponentName(p_oTransferObject);
			if (sComponentName == null) {
				if (p_oTransferObject.deleted) {
					ConfigurationsHandler.getInstance().removeProperty(p_oTransferObject.name);
				}
				else {
					ConfigurationsHandler.getInstance().addConfiguration(new Property(p_oTransferObject));
				}
			}
			else { // Propriété représentant une configuration d'un object graphique (en l'occurence un champ de l'application)
				BasicComponentConfiguration oComponent = (BasicComponentConfiguration) ConfigurationsHandler.getInstance().getVisualConfiguration(sComponentName);
				if (oComponent == null) {
					oComponent = new BasicComponentConfiguration(sComponentName);
					ConfigurationsHandler.getInstance().addConfiguration(oComponent);
				}

				if (this.isUILabelProperty(p_oTransferObject.name)) {
					oComponent.setLabel(p_oTransferObject.value);
				}
				else if (this.isUIVisibleProperty(p_oTransferObject.name)) {
					oComponent.setVisible(p_oTransferObject.value == null ? true : Boolean.parseBoolean(p_oTransferObject.value));
				}
			}
		}
	}

	
	
	/**
	 * TODO Décrire la méthode getComponentName de la classe PropertyWrapper
	 *
	 * @param p_oProperty a {@link com.adeuza.movalysfwk.mf4jcommons.model.configuration.property.TProperty} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getComponentName(TProperty p_oProperty) {
		return this.getComponentName(p_oProperty.name);
	}

	/**
	 * <p>getComponentName.</p>
	 *
	 * @param p_sPropertyName a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getComponentName(final String p_sPropertyName) {
		// 1ère tentative: est-ce un libellé?
		String r_sComponentName = this.getComponentName(p_sPropertyName, LABEL_PROPERTY_PATTERN);
		if (r_sComponentName == null) {
			// 2ème tentative: est-ce une propriété liée à la visibilité du composant
			r_sComponentName = this.getComponentName(p_sPropertyName, VISIBLE_PROPERTY_PATTERN);
		}

		return r_sComponentName;
	}

	/**
	 * TODO Décrire la méthode getComponentName de la classe PropertyWrapper
	 *
	 * @param p_sPropertyName a {@link java.lang.String} object.
	 * @param p_oPattern a {@link java.util.regex.Pattern} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String getComponentName(String p_sPropertyName, Pattern p_oPattern) {
		final Matcher oMatcher = p_oPattern.matcher(p_sPropertyName);
		if (oMatcher.find()) {
			return oMatcher.group(1);
		}
		return null;
	}

	/**
	 * Returns <code>true</code> if the provided property is not null and if its name ends with "_label".
	 *
	 * @param p_sProperty The property to check.
	 * @return a boolean.
	 */
	public boolean isUILabelProperty(String p_sProperty) {
		return p_sProperty != null && LABEL_PROPERTY_PATTERN.matcher(p_sProperty).find();
		
	}

	/**
	 * Returns <code>true</code> if the provided property is not null and if its name ends with "__visible".
	 *
	 * @param p_sProperty The property to check.
	 * @return <code>true</code> if provided property is not null and if its name ends with "__visible".
	 */
	public boolean isUIVisibleProperty(String p_sProperty) {
		return p_sProperty != null && VISIBLE_PROPERTY_PATTERN.matcher(p_sProperty).find();
	}

	//-------------------------------------------------------------------------
	//							4TH PART : LISTS
	//-------------------------------------------------------------------------

	/**
	 * Adds list provided by synchronisation into the {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler}.
	 *
	 * @param p_oDescriptors list to add to {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler}.
	 */
	protected void consolidateList(final Collection<TListDescriptor> p_oDescriptors) {
		if (p_oDescriptors != null) {
			for (TListDescriptor oDescriptor : p_oDescriptors) {
				ConfigurationsHandler.getInstance().addConfiguration(new CustomListConfiguration(oDescriptor));
			}
		}
	}
}
