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
package com.adeuza.movalysfwk.mobile.mf4javacommons.event;

/**
 * <p>Permet de définir un évènement métier</p>
 *
 * @param <DATA> type of data object in BusinessEvent
 */
public interface BusinessEvent<DATA extends Object> {

	/**
	 * Donne la source de l'évènement
	 *
	 * @return la source de l'évènement
	 */
	public Object getSource();
	
	/**
	 * Donne les données propre à l'évènement
	 *
	 * @return les données de l'évènement
	 */
	public DATA getData();
	
	/**
	 * Indique si l'évènement est appelé dans le cadre d'un exit
	 *
	 * @return a boolean.
	 */
	public boolean isExitMode();

	/**
	 * Définit si l'évènement est appelé dans le cadre d'un exit
	 *
	 * @param p_bExitMode a boolean.
	 */
	public void setExitMode(boolean p_bExitMode);
	
	/**
	 * Return if the event is consumed
	 *
	 * @return a boolean indicate if the event is consumed
	 */
	public boolean isConsummed();
	
	/**
	 * Setter for the event consumed
	 *
	 * @param p_bConsummed true if the event is consumed
	 */
	public void setConsummed(boolean p_bConsummed);
	
	/**
	 * Getter filter class
	 * @return the class to filter
	 */
	public Class<?> getFilterClass();
}
