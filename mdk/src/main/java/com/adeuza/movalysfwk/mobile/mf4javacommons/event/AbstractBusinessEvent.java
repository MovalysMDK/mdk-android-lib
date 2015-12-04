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
 * <p>Basic class for events creation</p>
 * @param <DATA> the class of the data used by the event 
 *
 */
public class AbstractBusinessEvent<DATA extends Object> implements BusinessEvent<DATA> {

	/** The source of the event */
	private Object source = null;
	
	/** particular data of the event */
	private DATA data = null;

	/** filter class use in listener */
	private Class<?> filter;
	
	private boolean exitMode = false;
	
	private boolean consummed = false;


	/**
	 * Builds an event
	 *
	 * @param p_oSource a {@link java.lang.Object} object.
	 * @param p_oData a DATA object.
	 */
	public AbstractBusinessEvent(Object p_oSource, DATA p_oData) {
		this.source = p_oSource;
		this.data = p_oData;
		this.filter = p_oSource.getClass();
	}
	
	/**
	 * Builds an event
	 *
	 * @param p_oSource a {@link java.lang.Object} object.
	 * @param p_oData a DATA object.
	 */
	public AbstractBusinessEvent(Object p_oSource, DATA p_oData, Class<?> p_oFilterClass) {
		this.source = p_oSource;
		this.data = p_oData;
		this.filter = p_oFilterClass;
	}
	
	/**
	 * Return the source of the event
	 *
	 * @return the source of the event
	 */
	@Override
	public Object getSource() {
		return this.source;
	}
	
	/**
	 * Returns the data of the event
	 *
	 * @return the data of the event
	 */
	@Override
	public DATA getData() {
		return this.data;
	}

	/**
	 * <p>isExitMode.</p>
	 *
	 * @return a boolean.
	 */
	@Override
	public boolean isExitMode() {
		return exitMode;
	}

	/** {@inheritDoc} */
	@Override
	public void setExitMode(boolean p_bExitMode) {
		this.exitMode = p_bExitMode;
	}
	
	/**
	 * <p>isConsummed.</p>
	 *
	 * @return a boolean.
	 */
	@Override
	public boolean isConsummed() {
		return consummed;
	}

	/** {@inheritDoc} */
	@Override
	public void setConsummed(boolean p_bConsummed) {
		this.consummed = p_bConsummed;
	}

	@Override
	public Class<?> getFilterClass() {
		return this.filter;
	}
}
