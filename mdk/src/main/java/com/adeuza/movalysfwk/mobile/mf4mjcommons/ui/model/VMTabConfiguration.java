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

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * <p>VMTabConfiguration class.</p>
 *
 */
public class VMTabConfiguration implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4555972388183797645L;
	/**
	 * Tab configurations (tab id => label)
	 * TreeMap: order is important
	 */
	private LinkedHashMap<String,TabTitleConfig> tabTitleConfig = new LinkedHashMap<String,TabTitleConfig>();
	
	/**
	 * <p>setTabTitle.</p>
	 *
	 * @param p_sTabId a {@link java.lang.String} object.
	 * @param p_sTabTitle a {@link java.lang.String} object.
	 * @param p_iTabIcon a {@link java.lang.String} object.
	 */
	public void setTabTitle( String p_sTabId, String p_sTabTitle, String p_iTabIcon ) {
		TabTitleConfig oTabTitleConfig = this.tabTitleConfig.get(p_sTabId);
		if ( oTabTitleConfig == null ) {
			oTabTitleConfig = new TabTitleConfig(p_sTabId, p_sTabTitle, p_iTabIcon);
			this.tabTitleConfig.put( p_sTabId, oTabTitleConfig);
		}
		else {
			oTabTitleConfig.setTabTitle(p_sTabTitle);
			oTabTitleConfig.setTabIcon(p_iTabIcon);
		}
	}
	
	/**
	 * <p>getTabTitle.</p>
	 *
	 * @param p_iTabPos a int.
	 * @return a {@link java.lang.String} object.
	 */
	public String getTabTitle( int p_iTabPos ) {
		if (this.getElementAt(p_iTabPos) != null) {
			return this.getElementAt(p_iTabPos).getTitle();
		} else {
			return null;
		}
	}
	
	/**
	 * <p>getTabIcon.</p>
	 *
	 * @param p_iTabPos a int.
	 * @return a {@link java.lang.String} object.
	 */
	public String getTabIcon( int p_iTabPos ) {
		if (this.getElementAt(p_iTabPos) != null) {
			return this.getElementAt(p_iTabPos).getIcon();
		} else {
			return null;
		}
	}
	
	/**
	 * <p>Getter for the field <code>tabTitleConfig</code>.</p>
	 *
	 * @return a {@link java.util.Collection} object.
	 */
	public Collection<TabTitleConfig> getTabTitleConfig() {
		return this.tabTitleConfig.values();
	}

	/**
	 * @param index
	 * @return
	 */
	private TabTitleConfig getElementAt(int index) {
	    for (Entry<String, TabTitleConfig> entry : this.tabTitleConfig.entrySet()) {
	        if (index-- == 0) {
	            return entry.getValue();
	        }
	    }
	    return null;
	}

	/**
	 * <p>getTabCount.</p>
	 *
	 * @return a int.
	 */
	public int getTabCount() {
		return this.tabTitleConfig.size();
	}
	
	/**
	 *
	 */
	public class TabTitleConfig {
		
		/**
		 * 
		 */
		private String id ;
		
		/**
		 * Tab Title
		 */
		private String title ;
		
		/**
		 * Tab icon
		 */
		private String icon ;
		
		/**
		 * @param p_sTitle
		 * @param p_iIcon
		 */
		public TabTitleConfig(String p_sId, String p_sTitle, String p_iIcon) {
			super();
			this.id = p_sId;
			this.title = p_sTitle;
			this.icon = p_iIcon;
		}
		
		/**
		 * @return
		 */
		public String getId() {
			return id;
		}

		/**
		 * @return
		 */
		public String getTitle() {
			return title;
		}
		
		/**
		 * @return
		 */
		public String getIcon() {
			return icon;
		}
		
		/**
		 * @param p_iTabIcon
		 */
		public void setTabIcon(String p_iTabIcon) {
			this.icon = p_iTabIcon ;
		}

		/**
		 * @param p_sTabTitle
		 */
		public void setTabTitle(String p_sTabTitle) {
			this.title = p_sTabTitle ;
		}
	}
}
