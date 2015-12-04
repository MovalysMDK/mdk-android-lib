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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor;

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.WidgetWrapperHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.AbstractEntityFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.EntityConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.common.AbstractNamableObject;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ExcludeFromBinding;

/**
 * <p>Describes a visual component without taking into account the configuration.</p>
 *
 *
 */
public class TheoriticalVisualComponentDescriptor extends AbstractNamableObject {
	
	/** list of subcomponents */
	private List<TheoriticalVisualComponentDescriptor> components = null;
	
	/** Indicates if the compoent must be link, eg the description is in more one file */
	private boolean linkedComponent = false;
	
	/** Indicates if the link operation is realised */
	private boolean linked = false; // operation de link faite ?
	
	/** the java class name of the component */
	private String className = null;
	
	/** the component can be grouped for massive treatment*/
	private List<String> groups = null;
	
	/** custom field (non portée par la conf uniquement pour le visual en cours) */
	private boolean mandatory = false;
	
	/** taille minimum (non portée par la conf uniquement pour le visual en cours) */
	private int minsize = 0;
	
	/** the name of the layout xml file hosting the component */
	private String sLayoutFile;
	
	/**
	 * Constructs a new descriptor
	 *
	 * @param p_sName the descriptor's name
	 * @param p_sClassName the descriptor's class
	 */
	public TheoriticalVisualComponentDescriptor(String p_sName, String p_sClassName) {
		this(p_sName, p_sClassName, null,false,0,null);
	}
	
	/**
	 * Constructs a new descriptor
	 *
	 * @param p_sName the descriptor's name
	 * @param p_sClassName the descriptor's class
	 * @param p_sGroups the groups of component
	 * @param p_sEntityName a {@link java.lang.String} object.
	 * @param p_bMandatory a boolean.
	 * @param p_iMinsize a int.
	 */
	public TheoriticalVisualComponentDescriptor(String p_sName, String p_sClassName, String p_sEntityName ,boolean p_bMandatory, int p_iMinsize, String p_sGroups) {
		super(p_sClassName == null);
		this.setName(p_sName);
		this.className = p_sClassName;
		this.mandatory = p_bMandatory ;
		this.minsize = p_iMinsize;
		this.components = new ArrayList<TheoriticalVisualComponentDescriptor>();
		this.groups = new ArrayList<String>();
		if (p_sGroups!=null) {
			for(String sGroup : p_sGroups.split(",")) {
				this.groups.add(sGroup);
			}
		}
		//lors de la création d'un élément des configurations sont créés à la volée ....
		if ((this.isEdit() || this.isValue()) && !UNKNOWN.equals(this.getModel())) { //Auto binding and Hide value
			BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) ConfigurationsHandler.getInstance().getVisualConfiguration(this.getConfigurationName());
			AbstractEntityFieldConfiguration oFieldConfiguration = null;
			if (p_sEntityName!=null && p_sEntityName.contains("_")) {
				String[] oNames = p_sEntityName.split("_");
				String sEntityName = oNames[0];
				String sFieldName = oNames[1];
				EntityConfiguration oEntityConfiguration = ConfigurationsHandler.getInstance().getEntityConfiguration(sEntityName);
				if (oEntityConfiguration!=null) {
					oFieldConfiguration = oEntityConfiguration.getEntityFieldConfiguration(sFieldName);
				}
			}
			if (oFieldConfiguration!=null && oConfiguration!=null) {
				oConfiguration.setEntityFieldConfiguration(oFieldConfiguration);
			}
			else if (oFieldConfiguration!=null && oConfiguration==null) {
				oConfiguration = new BasicComponentConfiguration(this.getConfigurationName(), oFieldConfiguration);
				ConfigurationsHandler.getInstance().addConfiguration(oConfiguration);
			}
			else if (oConfiguration==null) {
				oConfiguration = new BasicComponentConfiguration(this.getConfigurationName());
				ConfigurationsHandler.getInstance().addConfiguration(oConfiguration);
			}
			if (this.isValue()) {
				oConfiguration.hideEmpty();
			}
		}
	}
	
	/**
	 * returns the name of the layout file hosting the component
	 * @return the name of the layout file hosting the component
	 */
	public String getLayoutFile() {
		return sLayoutFile;
	}

	/**
	 * sets the name of the layout file hosting the component
	 * @param sLayoutFile the layout name to set
	 */
	public void setLayoutFile(String sLayoutFile) {
		this.sLayoutFile = sLayoutFile;
	}

	/**
	 * <p>isInGroup.</p>
	 *
	 * @param p_sGroup the name of group
	 * @return true if component is in group p_sGroup
	 */
	public boolean isInGroup(String p_sGroup) {
		return this.groups.contains(p_sGroup);
	}
	
	/**
	 * <p>isInGroups.</p>
	 *
	 * @param p_sGroups an array of {@link java.lang.String} objects.
	 * @return true if component is in group p_sGroup
	 */
	public boolean isInGroups(String[] p_sGroups) {
		boolean isIn = true;
		for(String sGroup : p_sGroups) {
			isIn = isIn && isInGroup(sGroup);
			if (!isIn) {
				break;
			}
		}
		return isIn;
	}
	
	/**
	 * transform component Manager in theoritical descriptor
	 */
	public void unmanager() {
		for(TheoriticalVisualComponentDescriptor oDescriptor : this.components) {
			oDescriptor.unmanager();	
		}
	}
	
	/**
	 * Son course components and replace components "link" with the real tree component.
	 * Once the replacement is made in the field "linked" changes to true.
	 */
	public void unlink() {
		if (!this.linked) {
			List<TheoriticalVisualComponentDescriptor> oTemp = new ArrayList<TheoriticalVisualComponentDescriptor>();
			TheoriticalVisualComponentDescriptor oLinked = null;
			for(TheoriticalVisualComponentDescriptor oDescriptor : this.components) {
				if (oDescriptor.linkedComponent && !oDescriptor.linked) {
					oLinked = VisualComponentDescriptorsHandler.getInstance().getTheoriticalDescriptor(oDescriptor.getName()).cloneInstance();
					oLinked.unlink();
					for(TheoriticalVisualComponentDescriptor oDescriptor2 : oLinked.components) {
						oTemp.add(oDescriptor2);
					}
				}
				else {
					oTemp.add(oDescriptor);
				}
			}
			this.components = oTemp;
			this.linked = true;
		}
	}

	/**
	 * Clones a component
	 *
	 * @return a clone
	 */
	protected TheoriticalVisualComponentDescriptor cloneInstance() {
		TheoriticalVisualComponentDescriptor r_oClone = new TheoriticalVisualComponentDescriptor(this.getName(), this.className);
		r_oClone.linkedComponent = this.linkedComponent;
		for (TheoriticalVisualComponentDescriptor oDescriptor : this.components) {
			r_oClone.addComponentDescriptor(oDescriptor.cloneInstance());
		}
		return r_oClone;
	}
	
	/**
	 * Adds a component descriptor son
	 *
	 * @param p_oDescriptor the componant descriptor son to add
	 */
	public void addComponentDescriptor(TheoriticalVisualComponentDescriptor p_oDescriptor) {
		this.components.add(p_oDescriptor);
	}
	
	/**
	 * Affects the component name to link
	 *
	 * @param p_sName the nom of componant to link
	 */
	public void linkTo(String p_sName) {
		this.linkedComponent = true;
		this.setName(p_sName);
	}
	
	/**
	 * Returns true if the component is binded, ie it has a wrapper
	 * @return true if the component is binded
	 */
	public boolean isBinded() {
		return true;
	}
	
	/**
	 * Lists the components son
	 *
	 * @return a list of componants
	 */
	public List<TheoriticalVisualComponentDescriptor> getComponents() {
		return this.components;
	}
	
	/**
	 * Reduces the descriptor, ie removes children unnecessary for managing the configuration.
	 */
	public void reduce() {
		// suppression des fils (layout) inutile
		List<TheoriticalVisualComponentDescriptor> oTemp = new ArrayList<TheoriticalVisualComponentDescriptor>();
		for (TheoriticalVisualComponentDescriptor oDescriptor : this.components) {
			// TODO : à améliorer
			String oComponentClassString = oDescriptor.className;
			
			if (!oComponentClassString.contains(".")) {
				// may be an Android class, we add the widget package
				oComponentClassString = "android.widget." + oComponentClassString;
			}
			
			try {
				Class<?> oComponentClass = Class.forName(oComponentClassString);
				if (componentIsBinded(oDescriptor, oComponentClass)) {
					oTemp.add(oDescriptor);
				} 
				oDescriptor.reduce();
				for(TheoriticalVisualComponentDescriptor oDescriptor2 : oDescriptor.getComponents()) {
					oTemp.add(oDescriptor2);
				}
			} catch (ClassNotFoundException e) {
				Application.getInstance().getLog().debug("TheoriticalVisualComponentDescriptor.reduce", "Class not found " + oComponentClassString);
			}
		}
		this.components = oTemp;
	}
	
	private boolean componentIsBinded(TheoriticalVisualComponentDescriptor p_oDescriptor, Class<?> p_oComponentClass) {
		boolean r_bIsBinded = false;

		// ExcludeFromBinding : rustine temporaire pour les MMRadioButton qui ne devraient pas être bindés...
		r_bIsBinded = ConfigurableVisualComponent.class.isAssignableFrom(p_oComponentClass) && ! ExcludeFromBinding.class.isAssignableFrom(p_oComponentClass)
				|| WidgetWrapperHelper.getInstance().componentHasWrapper(p_oComponentClass);
		
		// an unknown component has a wrong id, meaning it will not be binded
		r_bIsBinded &= !p_oDescriptor.isUnknown();
		
		return r_bIsBinded;
	}
	
	/**
	 * Retourne l'objet mandatory
	 *
	 * @return Objet mandatory
	 */
	public boolean isMandatory() {
		return this.mandatory;
	}
	/**
	 * Affecte l'objet mandatory
	 *
	 * @param p_oMandatory Objet mandatory
	 */
	public void setMandatory(boolean p_oMandatory) {
		this.mandatory = p_oMandatory;
	}
	
	/**
	 * <p>Getter for the field <code>minsize</code>.</p>
	 *
	 * @return the size min
	 */
	public int getMinsize() {
		return this.minsize;
	}

	/**
	 * Set minsize
	 *
	 * @param p_iMinsize a int.
	 */
	public void setMinsize(int p_iMinsize) {
		this.minsize = p_iMinsize;
	}
	
}
