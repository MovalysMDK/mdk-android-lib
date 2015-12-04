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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * Create and cached VM
 *
 */
public class DefaultViewModelCreator {

	/** One kilo byte */
	private static final int ONE_KILO_BYTE = 1024;
	
	/** view models cache */
	private Map<String, ViewModel> cache = null;

	/**
	 * Constructor
	 */
	public DefaultViewModelCreator() {
		this.cache = new HashMap<String, ViewModel>(ONE_KILO_BYTE);
	}
	
	/**
	 * Destructor
	 */
	public void destroy() {
		this.cache.clear();
	}
	
	/**
	 * Permet de retrouver l'id de l'objet en cache en fonction des paramètres  
	 * @param p_sId identifier de l'objet
	 * @param p_oType type of object
	 * @return une chaine de caractère décrivant l'identifiant de l'objet dans le cache
	 */
	private String getObjectIdInCache(String p_sId, Class<?> p_oType){
		String sEndOfKeyWithInterfaceName = null ;
		String sId = p_sId ;
		if (ListViewModel.class.isAssignableFrom(p_oType)) {
			if ( p_oType.isInterface() ){
				sId = p_oType.getName() ;
				sEndOfKeyWithInterfaceName = p_oType.getName();
			} else {
				sId = p_oType.getInterfaces()[0].getName() ;
				sEndOfKeyWithInterfaceName = p_oType.getInterfaces()[0].getName();
			}
		}else if ( ! p_oType.isInterface() ){
			// pas une interface , on prend la premiere implementée
			sEndOfKeyWithInterfaceName = p_oType.getInterfaces()[0].getName() ;
		} else {
			sEndOfKeyWithInterfaceName = p_oType.getName();
		}
		return new StringBuilder(sId).append('_').append(sEndOfKeyWithInterfaceName).toString() ;
	}
	
	/**
	 * Search an object in the cache with the number of uses
	 *
	 * @param p_sId identifier of the object
	 * @param p_oType type of the object
	 * @return the object himself or null if the object isn't in the cache
	 */
	public ViewModel getFromCache(String p_sId, Class<?> p_oType) {
		ViewModel r_oCachedObject = null ;
		if ( p_sId != null && p_oType != null ) {
			r_oCachedObject = this.cache.get(this.getObjectIdInCache(p_sId, p_oType));
		}
		return r_oCachedObject ;
	}
	
	/**
	 * Search an object in the cache
	 *
	 * @param p_sId identifier of the object
	 * @param p_oType type of the object
	 * @return the view model instance
	 */
	public <T extends ViewModel> T getViewModel(final String p_sId, final Class<T> p_oType) {
		return (T) this.getFromCache( p_sId, p_oType) ;
	}

	/**
	 * Search an object in the cache
	 *
	 * @param p_oType type of the object
	 * @return the view model instance
	 * @param <T> the view model type
	 */
	public <T extends ViewModel> T getViewModel(final Class<T> p_oType) {
		return this.getViewModel(p_oType.getName(), p_oType);
	}

	/**
	 * Add the object directly in cache
	 *
	 * @param p_sId identifier of the object
	 * @param p_oType type of the object
	 * @param p_oObject object to stock
	 */
	public void addToCache(String p_sId, Class<?> p_oType, ViewModel p_oObject) {
		this.cache.put(p_sId + "_" + p_oType.getName(), p_oObject);
	}
	
	/**
	 * Remove an object in the cache (for a list, try to remove the linked object)
	 *
	 * @param p_sId identifier of the object
	 * @param p_oType type of the object
	 * @return the object deleted in the cache or null if non deletion was done
	 */
	public ViewModel removeToCache(String p_sId, Class<?> p_oType) {
		ViewModel r_oCachedObject = this.getFromCache(p_sId, p_oType);
		if (r_oCachedObject != null) {
			if (ListViewModel.class.isAssignableFrom(p_oType)){
				ListViewModel oListViewModel = (ListViewModel)r_oCachedObject ; 
				int iCountOfSubViewModels = oListViewModel.getCount();
				for (int iPos= 0 ; iPos< iCountOfSubViewModels ;iPos++){
					ItemViewModel<MIdentifiable> oItem =  oListViewModel.getCacheVMByPosition(iPos);
					this.removeToCache(oItem.getIdVM(), oItem.getClass());
				}					
			}
			this.cache.remove( this.getObjectIdInCache(p_sId, p_oType) );
		}
		return r_oCachedObject;
	}
	
	/**
	 * Remove an object in the cache (for a list, try to remove the linked object)
	 *
	 * @param p_oListEntity list of identifiable objet linked to the view model to delete
	 * @param p_oTypeViewModel type of the view  model
	 */
	public void removeToCache(List<? extends MIdentifiable> p_oListEntity, Class<?> p_oTypeViewModel) {
		for (MIdentifiable oEntity : p_oListEntity){
			this.removeToCache(oEntity.idToString(), p_oTypeViewModel);
		}
	}

	/**
	 * Update an object cached by removing and adding the object
	 *
	 * @param p_sOldId old identifier of the object
	 * @param p_sNewId new identifier of the object
	 * @param p_oType type of the object
	 */
	public void updateCache(String p_sOldId, String p_sNewId, Class<?> p_oType) {
		if (p_sOldId != null && p_sNewId != null && !p_sOldId.equals(p_sNewId)) {
			ViewModel oCachedObject = this.removeToCache(p_sOldId, p_oType);
			if ( oCachedObject != null ){
				this.addToCache(p_sNewId, p_oType, oCachedObject);
			}
		}
	}

	/**
	 * Creates a view model of the given class
	 *
	 * @param p_sCacheKey cache key
	 * @param p_oViewModelClass view model class
	 * @return the view model instance
	 * @param <VM> the view model type
	 */
	@SuppressWarnings("unchecked")
	protected <VM extends ViewModel> VM createVM(String p_sCacheKey, Class<VM> p_oViewModelClass) {
		ViewModel oCo = this.getFromCache(p_sCacheKey, p_oViewModelClass);
		if (oCo == null) {
			oCo = BeanLoader.getInstance().getBean(p_oViewModelClass); 
			this.addToCache(p_sCacheKey, p_oViewModelClass, oCo);
		}
		return (VM) oCo;
	}

	/**
	 * Creates a view model of the given class
	 *
	 * @param p_oViewModelClass class of the view model to create
	 * @return the instance of the view model class
	 * @param <VM> the view model type
	 */
	protected <VM extends ViewModel> VM createVM(Class<VM> p_oViewModelClass) {
		return this.createVM(p_oViewModelClass.getName(), p_oViewModelClass);
	}
	
	/**
	 * Creates a view model of the given class with the given entity's data
	 *
	 * @param p_oData data to feed the view model with
	 * @param p_oViewModelClass class of the view model to create
	 * @return the instance of the view model class
	 * @param <ENTITY> the entity type
	 * @param <VM> the view model type
	 */
	protected <ENTITY extends MIdentifiable, VM extends ItemViewModel<ENTITY>> VM createVM(ENTITY p_oData, Class<VM> p_oViewModelClass) {
		VM r_oVm = null;
		if (p_oData != null) {
			try {
				Long lId = Long.valueOf( p_oData.idToString() );
				if (lId <= 0 ){
					this.removeToCache(p_oData.idToString(), p_oViewModelClass);
				}
			} catch (NumberFormatException oE) {
				Application.getInstance().getLog().info(Application.LOG_TAG, "NumberFormatException in DefaultViewModelCreator");
			}
			r_oVm = this.createVM(p_oData.idToString(), p_oViewModelClass);
		}
		return r_oVm;
	}

	/**
	 * Returns a view model of the given class with the given entity's data
	 *
	 * @param p_oData data to feed the view model with
	 * @param p_oViewModelClass class of the view model to create
	 * @return the instance of the view model class
	 * @param <ENTITY> the entity type
	 * @param <VM> the view model type
	 */
	protected <ENTITY extends MIdentifiable, VM extends ItemViewModel<ENTITY>> VM getViewModel(ENTITY p_oData, Class<VM> p_oViewModelClass) {
		VM r_oVm = null;
		if (p_oData != null) {
			r_oVm = this.getViewModel(p_oData.idToString(), p_oViewModelClass);
			if (r_oVm == null) {
				r_oVm = this.createVM(p_oData, p_oViewModelClass);
			}
		}
		return r_oVm;
	}

	/**
	 * Creates a list view model of the given class with the given entities data
	 *
	 * @param p_sCacheKey cache key
	 * @param p_oDatas list of data
	 * @param p_oListVMClass class of the list view model
	 * @param p_oItemVMClass class of the list view model's items
	 * @return the list view model
	 * @param <ENTITY> the entity type
	 * @param <VM> the view model type
	 * @param <VMLIST> the list view model type
	 */
	protected <ENTITY extends MIdentifiable, VM extends ItemViewModel<ENTITY>, VMLIST extends ListViewModel<ENTITY, VM>> VMLIST createVM(String p_sCacheKey,
			Collection<ENTITY> p_oDatas, Class<VMLIST> p_oListVMClass, Class<VM> p_oItemVMClass) {

		VMLIST r_oVm = this.createVM(p_sCacheKey, p_oListVMClass);
		r_oVm.clear();
		if (p_oDatas != null) {
			// do not create all VM
			r_oVm.setItems(p_oDatas);

		}
		return r_oVm; 
	}

}
