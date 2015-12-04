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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericsave;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.EntityActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * Save Detail action
 *
 * @param <ITEM> item entity
 * @param <STATE> action step
 * @param <PROGRESS> progress object
 */
public interface SaveDetailAction<ITEM extends MEntity,
	STATE extends ActionStep, 
	PROGRESS extends Object> 
	extends Action<NullActionParameterImpl, EntityActionParameterImpl<ITEM>, STATE, PROGRESS> {

	
	/**
	 * validate data
	 * @param p_oContext context
	 * @param p_oParameterIn NULL action IN parameter
	 * @return true if data is valid
	 * @throws ActionException error
	 */
	public boolean validateData(NullActionParameterImpl p_oParameterIn, MContext p_oContext) throws ActionException;
	
	/**
	 * Prepare entity for save.
	 * Entity must be updated with view models if needed.
	 * @param p_oContext context
	 * @param p_oParameterIn NULL action IN parameter
	 * @return entity
	 * @throws ActionException error
	 */
	public ITEM preSaveData( NullActionParameterImpl p_oParameterIn, MContext p_oContext ) throws ActionException ;
	
	/**
	 * Save the entity.
	 * @param p_oContext context
	 * @param p_oParameterIn NULL action IN parameter
	 * @param p_oItem entity
	 * @return item entity
	 * @throws ActionException error
	 */
	public ITEM saveData( ITEM p_oItem, NullActionParameterImpl p_oParameterIn, MContext p_oContext) throws ActionException;
	
	/**
	 * Method executed after save.
	 * View model must be updated back with the persisted entity.
	 * @param p_oITEM saved entity
	 * @param p_oContext context
	 * @param p_oParameterIn NULL action IN parameter
	 * @throws ActionException error
	 */
	public void postSaveData( ITEM p_oITEM, NullActionParameterImpl p_oParameterIn, MContext p_oContext ) throws ActionException ;
	
	/**
	 * modify entity before save
	 * @param p_oEntityToSave item entity
	 * @throws ActionException error
	 */
	public void modifyEntityBeforeSave(ITEM p_oEntityToSave) throws ActionException ;
}
