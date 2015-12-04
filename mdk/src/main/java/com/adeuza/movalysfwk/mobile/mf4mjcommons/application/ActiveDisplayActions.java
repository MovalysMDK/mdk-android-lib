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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.application;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ActionDialogFactory;

/**
 * Running actions for an active display
 */
public class ActiveDisplayActions {

	/**
	 * Running actions.
	 * We keep a weak reference on actions
	 */
	private List<WeakReference<MMActionTask<?, ?, ?, ?>>> runningActions = new ArrayList<>();
	
	/**
	 * Dialog types currently opened
	 */
	private List<String> openedDialogTypes = new ArrayList<>();
	
	/**
	 * Register a running
	 * Actions must be registered when they are started.
	 * @param p_oAction
	 */
	public void registerAction( MMActionTask<?, ?, ?, ?> p_oAction ) {
		this.runningActions.add(new WeakReference<MMActionTask<?,?,?,?>>(p_oAction));
	}
	
	/**
	 * Unregister an action
	 * Actions must be unregistered when they are stopped.
	 * @param p_oAction action to unregister
	 */
	public void unregisterAction( MMActionTask<?, ?, ?, ?> p_oAction ) {		
		List<WeakReference<MMActionTask<?, ?, ?, ?>>> listWeakRefToRemove = new ArrayList<WeakReference<MMActionTask<?, ?, ?, ?>>>();
		
		for( WeakReference<MMActionTask<?, ?, ?, ?>> oWeakRefAction : this.runningActions ) {
			MMActionTask<?, ?, ?, ?> oAction = oWeakRefAction.get();
			// remove matching actions, and empty weak references
			if (( oAction != null && p_oAction == oAction ) || oAction == null ) {
				listWeakRefToRemove.add(oWeakRefAction);
			}
		}
		if ( !listWeakRefToRemove.isEmpty()) {
			this.runningActions.removeAll(listWeakRefToRemove);
		}
	}
	
	/**
	 * Get running actions of the active display
	 * @return running actions of the active display
	 */
	public List<MMActionTask<?, ?, ?, ?>> getAll() {
		List<MMActionTask<?, ?, ?, ?>> r_listActions = new ArrayList<>();
		List<WeakReference<MMActionTask<?, ?, ?, ?>>> listWeakRefToRemove = new ArrayList<>();
		for( WeakReference<MMActionTask<?, ?, ?, ?>> oWeakRefAction : this.runningActions ) {
			MMActionTask<?, ?, ?, ?> oAction = oWeakRefAction.get();
			if ( oAction != null ) {
				r_listActions.add(oAction);
			}
			else {
				listWeakRefToRemove.add(oWeakRefAction);
			}
		}
		// Remove garbage collected actions
		this.runningActions.removeAll(listWeakRefToRemove);
		return r_listActions;
	}
	
	/**
	 * Register a progress dialog
	 * @param p_sDialogTag dialog tag
	 */
	public void registerProgressDialog(String p_sDialogTag) {
		this.openedDialogTypes.add(p_sDialogTag);
	}
	
	/**
	 * Unregister a progress dialog
	 * @param p_sDialogTag dialog tag
	 */
	public void unregisterProgressDialog(String p_sDialogTag) {
		this.openedDialogTypes.remove(p_sDialogTag);
	}
	
	/**
	 * Return true if a dialog of type is currently opened
	 * @param p_sTag current tag
	 * @return true if a dialog of type is currently opened
	 */
	public boolean hasDialogOfType(String p_sTag) {
		return this.openedDialogTypes.contains(p_sTag);
	}
	
	/**
	 * Return true if a running or pending action exists with the specified dialog tag
	 * @param p_sDialogTag dialog tag
	 * @return true if a running or pending action exists with the specified dialog tag
	 */
	public boolean hasRunningOrPendingAction(String p_sDialogTag) {
		boolean r_bFound = false;
		ActionDialogFactory oActionDialogFactory = BeanLoader.getInstance().getBean(ActionDialogFactory.class);
		
		for ( WeakReference<MMActionTask<?, ?, ?, ?>> oWeakRef : this.runningActions ) {
			MMActionTask oMMActionTask = oWeakRef.get();
			if ( oMMActionTask != null && oActionDialogFactory.getDialogTag(oMMActionTask).equals(p_sDialogTag) && !oMMActionTask.isProgressDialogDisabled()) {
				r_bFound = true;
				break;
			}
		}
		return r_bFound;
	}
	
	/**
	 * Return number of running actions (running or pending)
	 * @return number of running actions (running or pending)
	 */
	public int count() {
		return this.runningActions.size();
	}
}
