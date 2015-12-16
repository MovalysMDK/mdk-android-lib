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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ListenerActionClassFilter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * <p>Contient les résultats d'analyse d'une classe</p>
 *
 */
public class ClassAnalyse {

	/** 
	 * contient les données d'analyse de classe : fait le lien pour l'écran en cours d'analyse
	 * avec une action, une étape de cette action et la méthode de callback à appeler 
	 **/
	private Map<Class<? extends Action<?,?,?,?>>, Map<String, Method>> actionAnalyse = null;
	/** 
	 * contient les données d'analyse de classe : fait le lien pour l'écran en cours d'analyse
	 * avec une action et la méthode de callback à appeler (valable pour toutes les étapes)
	 **/
	private Map<Class<? extends Action<?,?,?,?>>, Method> actionAnalyseAlone = null;
	/**
	 * filter on classes being part of the action launching which will react to the listener method
	 */
	private Map<Method, List<Class<?>>> actionsFilters = null;
	/**
	 * contient les informations de callback pour les dataloaders
	 */
	private Map<Class<? extends Dataloader<?>>, Map<Class<?>, Method>> dataloaderListenerCallBack = null;
	/**
	 * contient pour un dataloader donné l'ensemble des types possédant des méthodes de callback
	 */
	private Map<Class<?>, List<Class<? extends Dataloader<?>>>> dataloaderListener = null;
	/**
	 * contient les informations de callback pour les businessEvents
	 */
	private Map<Class<? extends BusinessEvent<?>>, Map<Class<?>, Map<Class<?>, Method>>> businessEventListenerCallBack = null;
	/**
	 * contient pour un businessEvent donné l'ensemble des types possédant des méthodes de callback
	 */
	private Map<Class<?>, Map<Class<?>, List<Class<? extends BusinessEvent<?>>>>> businessEventListener = null;
	/**
	 * contient pour un dataloader donné l'ensemble des types possédant des méthodes de callback
	 */
	private Set<Class<? extends Action<?,?,?,?>>> actionList = null;
	/**
	 * contient le lien entre l'item d'un menu d'une activité et la méthode lancée pour ce menu
	 */
	private Map<Integer,Method> menuItemListeners = null;
	/**
	 * Construit un nouveau résultat d'analyse
	 */
	public ClassAnalyse() {
		this.actionAnalyse = new HashMap<Class<? extends Action<?,?,?,?>>, Map<String, Method>>();
		this.actionAnalyseAlone = new HashMap<Class<? extends Action<?,?,?,?>>, Method>();
		this.actionsFilters = new HashMap<Method, List<Class<?>>>(); 
		this.dataloaderListenerCallBack = new HashMap<Class<? extends Dataloader<?>>, Map<Class<?>, Method>>();
		this.dataloaderListener = new HashMap<Class<?>, List<Class<? extends Dataloader<?>>>>();
		this.businessEventListenerCallBack = new HashMap<Class<? extends BusinessEvent<?>>, Map<Class<?>, Map<Class<?>, Method>>>();
		this.businessEventListener = new HashMap<Class<?>, Map<Class<?>, List<Class<? extends BusinessEvent<?>>>>>();
		this.actionList = new HashSet<Class<? extends Action<?,?,?,?>>>();
		this.menuItemListeners = new HashMap<Integer,Method>();
	}
	
	/**
	 * Indique si l'analyse de la classe courante a été réalisée pour une action spéciale
	 *
	 * @param p_oActionClass l'action
	 * @return true si l'analyse a déjà été realisée
	 */
	public boolean isAnalyseReadyForAction(Class<? extends Action<?,?,?,?>> p_oActionClass) {
		return this.actionAnalyse.get(p_oActionClass.getName()) != null;
	}

	/**
	 * Ajoute une information d'analyse à l'objet résultat
	 *
	 * @param p_oActionClass l'action considérée
	 * @param p_oStep l'état considéré
	 * @param p_oMethod la méthode de callback a appeler
	 * @param p_oFilters classes to apply a filter on 
	 */
	public void addActionAnalyseByActionClass(Class<? extends Action<?, ?, ?, ?>> p_oActionClass, ActionStep p_oStep, Method p_oMethod, List<Class<?>> p_oFilters) {
		addActionAnalyseByActionClass(p_oActionClass, p_oStep.name(), p_oMethod, p_oFilters);
	}
	
	/**
	 * Ajoute une information d'analyse à l'objet résultat
	 *
	 * @param p_oActionClass l'action considérée
	 * @param p_oStep l'état considéré
	 * @param p_oMethod la méthode de callback a appeler
	 * @param p_oFilters classes to apply a filter on
	 */
	public void addActionAnalyseByActionClass(Class<? extends Action<?, ?, ?, ?>> p_oActionClass, String p_oStep, Method p_oMethod, List<Class<?>> p_oFilters) {
		Map<String, Method> oTemp = this.actionAnalyse.get(p_oActionClass);	
		if (oTemp == null) {
			oTemp = new HashMap<String, Method>();
			this.actionAnalyse.put(p_oActionClass, oTemp);
		}
		oTemp.put(p_oStep, p_oMethod);
		
		if (p_oFilters != null && !p_oFilters.isEmpty()) {
			List<Class<?>> oFilters = this.actionsFilters.get(p_oMethod);
			if (oFilters == null) {
				oFilters = new ArrayList<Class<?>>();
				this.actionsFilters.put(p_oMethod, oFilters);
			}
			oFilters.addAll(p_oFilters);
		}
		
		this.actionList.add(p_oActionClass);
	}
	
	/**
	 * Ajoute une information d'analsye l'objet résultat
	 *
	 * @param p_oActionClass l'action considérée
	 * @param p_oMethod la méthode de callback a appeler quelque soit l'étape
	 */
	public void addActionAnalyseByActionClass(Class<? extends Action<?, ?, ?, ?>> p_oActionClass, Method p_oMethod) {
		this.actionAnalyseAlone.put(p_oActionClass, p_oMethod);
		this.actionList.add(p_oActionClass);
	}
	
	/**
	 * Retrouve une méthode callback en fonction d'une action et d'une étape
	 *
	 * @param p_oActionClass l'action
	 * @param p_oStep l'étape
	 * @return la méthode de callback
	 */
	public Method getMethodOfClass(Class<? extends Action<?, ?, ?, ?>> p_oActionClass, ActionStep p_oStep) {
		Method r_oResult = null;
		Map<String, Method> oTemp = this.actionAnalyse.get(p_oActionClass);
		if (oTemp!=null) {
			r_oResult = oTemp.get(p_oStep.name());
		}
		if (r_oResult == null) {
			r_oResult = this.actionAnalyseAlone.get(p_oActionClass);
		}
		
		return r_oResult;
	}
	
	/**
	 * Retrouve une méthode callback en fonction d'une action et d'une étape
	 *
	 * @param p_oActionClass l'action
	 * @param p_oStep l'étape
	 * @param p_oActionResult the result of the action
	 * @param <OUT> the type of the action result object
	 * @return la méthode de callback
	 */
	public <OUT extends ActionParameter> Method getMethodOfClass(Class<? extends Action<?, ?, ?, ?>> p_oActionClass, ActionStep p_oStep, OUT p_oActionResult) {
		Method r_oResult = this.getMethodOfClass(p_oActionClass, p_oStep);
		if (r_oResult != null) {
			List<Class<?>> oFilters = this.actionsFilters.get(r_oResult);
			if (BeanLoader.getInstance().hasDefinition(p_oActionClass.getSimpleName() + "Filter")) {
				ListenerActionClassFilter<OUT> filter = (ListenerActionClassFilter<OUT>) BeanLoader.getInstance().getBeanByType(p_oActionClass.getSimpleName() + "Filter");
				//TODO SMA ajouter le in paramater ne marche pas pour le progress 
				if (filter!=null && oFilters != null && !oFilters.isEmpty() 
						&& !filter.filterListenerOnActionClass(oFilters, p_oActionResult)) {
					r_oResult = null;
				}
			}
		}
		
		return r_oResult;
	}

	/**
	 * Ajoute des informations de callback pour un dataloader
	 *
	 * @param p_oValue le type du dataloader
	 * @param p_oClass le type d'un listener du dataloader
	 * @param p_oMethod la méthode de callback du listener
	 */
	public void addDataLoaderListener(final Class<? extends Dataloader<?>> p_oValue, final Class<?> p_oClass, final Method p_oMethod) {
		Map<Class<?>, Method> mapMethodsByClass = this.dataloaderListenerCallBack.get(p_oValue);
		if (mapMethodsByClass == null) {
			mapMethodsByClass = new HashMap<Class<?>, Method>();
			this.dataloaderListenerCallBack.put(p_oValue, mapMethodsByClass);
		}
		mapMethodsByClass.put(p_oClass, p_oMethod);

		List<Class<? extends Dataloader<?>>> listDataloader = this.dataloaderListener.get(p_oClass);
		if (listDataloader == null) {
			listDataloader = new ArrayList<Class<? extends Dataloader<?>>>();
			this.dataloaderListener.put(p_oClass, listDataloader);
		}
		listDataloader.add(p_oValue);
	}
	
	/**
	 * Ajoute des informations de callback pour un évènement
	 *
	 * @param p_oValue le type du dataloader
	 * @param p_oClass le type d'un listener du dataloader
	 * @param p_oMethod la méthode de callback du listener
	 * @param p_oFilterClasses class filters
	 */
	public void addBusinessEventListener(final Class<? extends BusinessEvent<?>> p_oValue, final Class<?> p_oClass, final Method p_oMethod, Class<?>[] p_oFilterClasses) {
		Map<Class<?>, Map<Class<?>, Method>> mapMethodByFilter = this.businessEventListenerCallBack.get(p_oValue);
		if (mapMethodByFilter == null) {
			mapMethodByFilter = new HashMap<Class<?>, Map<Class<?>, Method>>();
			this.businessEventListenerCallBack.put(p_oValue, mapMethodByFilter);
		}
		Map<Class<?>, Method> mapMethodByClass = null;
		if (p_oFilterClasses == null || p_oFilterClasses.length == 0) {
			mapMethodByClass = mapMethodByFilter.get(Object.class);
			if (mapMethodByClass == null) {
				mapMethodByClass = new HashMap<Class<?>, Method>();
				mapMethodByFilter.put(Object.class, mapMethodByClass);
			}
			mapMethodByClass.put(p_oClass, p_oMethod);
		} else {
			for (Class<?> oFilterClass : p_oFilterClasses) {
				mapMethodByClass = mapMethodByFilter.get(oFilterClass);
				if (mapMethodByClass == null) {
					mapMethodByClass = new HashMap<Class<?>, Method>();
					mapMethodByFilter.put(oFilterClass, mapMethodByClass);
				}
				mapMethodByClass.put(p_oClass, p_oMethod);
			}
		}

		Map<Class<?>, List<Class<? extends BusinessEvent<?>>>> mapBusinessEvent = this.businessEventListener.get(p_oClass);
		if (mapBusinessEvent == null) {
			mapBusinessEvent = new HashMap<Class<?>, List<Class<? extends BusinessEvent<?>>>>();
			this.businessEventListener.put(p_oClass, mapBusinessEvent);
		}
		List<Class<? extends BusinessEvent<?>>> listBusinessEvent = null;
		if (p_oFilterClasses == null || p_oFilterClasses.length == 0) {
			listBusinessEvent = mapBusinessEvent.get(Object.class);
			if (listBusinessEvent == null) {
				listBusinessEvent = new ArrayList<Class<? extends BusinessEvent<?>>>();
				mapBusinessEvent.put(Object.class, listBusinessEvent);
			}
			listBusinessEvent.add(p_oValue);
		} else {
			for (Class<?> oFilterClass : p_oFilterClasses) {
				listBusinessEvent = mapBusinessEvent.get(oFilterClass);
				if (listBusinessEvent == null) {
					listBusinessEvent = new ArrayList<Class<? extends BusinessEvent<?>>>();
					mapBusinessEvent.put(oFilterClass, listBusinessEvent);
				}
				listBusinessEvent.add(p_oValue);
			}
		}
	}

	/**
	 * Donne la méthode de callback pour un dataloader et listener donnés
	 *
	 * @param p_oValue le type du dataloader
	 * @param p_oClass le type de listener
	 * @return la méthode de callback
	 */
	public Method getMethodOfDataLoaderListener(Class<? extends Dataloader> p_oValue, Class<?> p_oClass) {
		Method r_oResult = null;
		Map<Class<?>, Method> oTemp = this.dataloaderListenerCallBack.get(p_oValue);
		if (oTemp!=null) {
			r_oResult = oTemp.get(p_oClass);
		}
		return r_oResult;
	}	
	/**
	 * Donne l'ensemble des dataloaders pour un listener
	 *
	 * @param p_oClass le type d'un listener
	 * @return les dataloaders associés
	 */
	public List<Class<? extends Dataloader<?>>> getDataLoaderForObject(Class<?> p_oClass) {
		return this.dataloaderListener.get(p_oClass);
	}
	/**
	 * Donne l'ensemble des actions pour un listener
	 *
	 * @return les actions associées
	 */
	public Set<Class<? extends Action<?,?,?,?>>> getActions() {
		return this.actionList;
	}
	
	public Method getMethodOfBusinessEventListener(Class<? extends BusinessEvent> p_oValue, Class<?> p_oClass, Class<?>[] p_oFilterClasses) {
		Method r_oResult = null;
		for (Class<?> oFilterClass : p_oFilterClasses) {
			r_oResult = getMethodOfBusinessEventListener(p_oValue, p_oClass, oFilterClass);
			if (r_oResult == null) {
				break;
			}
		}
		return r_oResult;
	}
	
	/**
	 * Donne la méthode de callback pour un event et listener donnés
	 *
	 * @param p_oValue le type du event
	 * @param p_oClass le type de listener
	 * @return la méthode de callback
	 */
	public Method getMethodOfBusinessEventListener(Class<? extends BusinessEvent> p_oValue, Class<?> p_oClass, Class<?> p_oFilterClass) {
		Method r_oResult = null;
		Map<Class<?>, Map<Class<?>, Method>> oFilterMapTemp = this.businessEventListenerCallBack.get(p_oValue);
		if (oFilterMapTemp!=null) {
			Map<Class<?>, Method> oTemp = null;
			if (p_oFilterClass == null) {
				oTemp = oFilterMapTemp.get(Object.class);
			} else {
				oTemp = oFilterMapTemp.get(p_oFilterClass);
			}
			if (oTemp != null) {
				r_oResult = oTemp.get(p_oClass);
			}
		}
		return r_oResult;
	}
	/**
	 * Donne l'ensemble des events pour un listener
	 *
	 * @param p_oClass le type d'un listener
	 * @return les events associés
	 */
	public Map<Class<?>, List<Class<? extends BusinessEvent<?>>>> getBusinessEventForObject(Class<?> p_oClass) {
		return this.businessEventListener.get(p_oClass);
	}
	/**
	 * Ajout d'un listener sur le menu
	 *
	 * @param p_iMenuItem identifie le menu ( en provenance du R.menu )
	 * @param p_oListenerMethod methode à lancer en cas d'événement déclenché
	 */
	public void addMenuItemListener( int p_iMenuItem , Method p_oListenerMethod ){
		this.menuItemListeners.put( Integer.valueOf(p_iMenuItem), p_oListenerMethod);
	}
	/**
	 * Donne la méthode de callback pour un menu item click event et listener donnés
	 *
	 * @param p_iMenuItem identifie le menu ( en provenance du R.menu )
	 * @return la méthode de callback
	 */
	public Method getMethodOfMenuItemEventListener( int p_iMenuItem) {
		return this.menuItemListeners.get(Integer.valueOf(p_iMenuItem));
	}
}
