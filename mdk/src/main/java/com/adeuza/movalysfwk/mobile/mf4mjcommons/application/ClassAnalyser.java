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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.listener.ListenerOnDataLoaderReload;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.listener.ListenerOnBusinessNotification;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionFail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccess;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnMenuItemClick;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnProgressAction;

/**
 * <p>Analyser de classe</p>
 *
 */
public class ClassAnalyser {

	/** résultat de l'analyse d'une classe */
	private Map<String, ClassAnalyse> classAnalyses = null;

	/**
	 * Construit un nouvel analyser
	 */
	public ClassAnalyser() {
		this.classAnalyses = new HashMap<String, ClassAnalyse>();
	}
	/**
	 * Donne l'analyse d'une classe
	 *
	 * @param p_oObject l'objet à analyser
	 * @return l'analyse
	 */
	public ClassAnalyse getClassAnalyse(Object p_oObject) {
		return this.analyzeClass(p_oObject);
	}
	/**
	 * Analyse d'une classe
	 * Recherche des annotations ListenerOnProgressAction, ListenerOnActionFail, ListenerOnActionSuccess
	 *
	 * @param p_oObjectClassToAnalyze l'objet à analyser
	 * @return not null object
	 */
	public ClassAnalyse analyzeClass(Object p_oObjectClassToAnalyze) {
		ClassAnalyse oAnalyse = this.classAnalyses.get(p_oObjectClassToAnalyze.getClass().getName());
		if (oAnalyse == null) {
			//on boucle sur les methodes de l'écran
			oAnalyse = new ClassAnalyse();

			boolean bDelegate = false;

			Class<?> oClass = p_oObjectClassToAnalyze.getClass();
			while (oClass != null) {
				//-1- Traitement de la classe elle-même
				this.analyzeClass(oAnalyse, p_oObjectClassToAnalyze.getClass(), oClass);

				//-2- Traitement du délégué si il existe et si un précédent délégué n'a pas été analysé.
				if (!bDelegate && this.hasDelegate(oClass)) {
					this.analyzeClass(oAnalyse, p_oObjectClassToAnalyze.getClass(), ((ListenerDelegator) p_oObjectClassToAnalyze).getListenerDelegate().getClass());
					bDelegate = true;
				}

				//-3- Taitement de la superclass.
				oClass = oClass.getSuperclass();
			}

			this.classAnalyses.put(p_oObjectClassToAnalyze.getClass().getName(), oAnalyse);
		}
		return oAnalyse;
	}

	/**
	 * <p>analyzeClass.</p>
	 *
	 * @param p_oAnalyse a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse} object.
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	protected void analyzeClass(ClassAnalyse p_oAnalyse, Class<?> p_oClass) {
		this.analyzeClass(p_oAnalyse, p_oClass, p_oClass);
	}
	
	/**
	 * <p>analyzeClass.</p>
	 *
	 * @param p_oAnalyse a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse} object.
	 * @param p_oClass a {@link java.lang.Class} object.
	 * @param p_oAncestorClass a {@link java.lang.Class} object.
	 */
	protected void analyzeClass(ClassAnalyse p_oAnalyse, Class<?> p_oClass, Class<?> p_oAncestorClass) {
		for(Method oMethod : p_oAncestorClass.getDeclaredMethods()) {
			ListenerOnProgressAction oDefine1 = oMethod.getAnnotation(ListenerOnProgressAction.class);
			ListenerOnActionFail oDefine2 = oMethod.getAnnotation(ListenerOnActionFail.class);
			ListenerOnActionSuccess oDefine3 = oMethod.getAnnotation(ListenerOnActionSuccess.class);
			ListenerOnDataLoaderReload oDefine4 = oMethod.getAnnotation(ListenerOnDataLoaderReload.class);
			ListenerOnBusinessNotification oDefine5 = oMethod.getAnnotation(ListenerOnBusinessNotification.class);
			ListenerOnMenuItemClick oDefine6 = oMethod.getAnnotation(ListenerOnMenuItemClick.class);
			if (oDefine1 != null) {
				this.doAddActionAnalyseByActionClass(p_oAnalyse, oDefine1.action(), oDefine1.steps(), oMethod, oDefine1.classFilters());
			}
			if (oDefine2 != null) {
				this.doAddActionAnalyseByActionClass(p_oAnalyse, oDefine2.action(), MMActionTask.ActionTaskStep.END_FAIL, oMethod, oDefine2.classFilters());
			}
			if (oDefine3 != null) {
				this.doAddActionAnalyseByActionClass(p_oAnalyse, oDefine3.action(), MMActionTask.ActionTaskStep.END_SUCCESS, oMethod, oDefine3.classFilters());
			}
			if (oDefine4 != null && p_oAnalyse.getMethodOfDataLoaderListener(oDefine4.value(), p_oClass) == null) {
				p_oAnalyse.addDataLoaderListener(oDefine4.value(), p_oClass, oMethod);
			}
			if (oDefine5 != null && p_oAnalyse.getMethodOfBusinessEventListener(oDefine5.value(), p_oClass, oDefine5.classFilters()) == null) {
				p_oAnalyse.addBusinessEventListener(oDefine5.value(), p_oClass, oMethod, oDefine5.classFilters());
			}
			if (oDefine6 != null && p_oAnalyse.getMethodOfMenuItemEventListener(oDefine6.value()) == null) {
				p_oAnalyse.addMenuItemListener(oDefine6.value(), oMethod);
			}
		}
	}

	/**
	 * <p>doAddActionAnalyseByActionClass.</p>
	 *
	 * @param p_oAnalyse a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse} object.
	 * @param p_tActionClasses an array of {@link java.lang.Class} objects.
	 * @param p_t_sSteps an array of {@link java.lang.String} objects.
	 * @param p_oMethod a {@link java.lang.reflect.Method} object.
	 * @param p_oClassFilters an array of {@link java.lang.Class} objects.
	 */
	protected void doAddActionAnalyseByActionClass(ClassAnalyse p_oAnalyse, Class<? extends Action<?,?,?,?>>[] p_tActionClasses, String[] p_t_sSteps, Method p_oMethod, Class<?>[] p_oClassFilters) {
		if (p_t_sSteps != null && p_t_sSteps.length > 0) {
			for(String oStep : p_t_sSteps) {
				for (Class<? extends Action<?,?,?,?>> oActionClass : p_tActionClasses) {
					p_oAnalyse.addActionAnalyseByActionClass(oActionClass, oStep, p_oMethod, Arrays.asList(p_oClassFilters));
				}
			}
		}
		else {
			for (Class<? extends Action<?,?,?,?>> oActionClass : p_tActionClasses) {
				p_oAnalyse.addActionAnalyseByActionClass(oActionClass, p_oMethod);
			}
		}
	}

	/**
	 * <p>doAddActionAnalyseByActionClass.</p>
	 *
	 * @param p_oAnalyse a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ClassAnalyse} object.
	 * @param p_tActionClasses an array of {@link java.lang.Class} objects.
	 * @param p_oStep a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask.ActionTaskStep} object.
	 * @param p_oMethod a {@link java.lang.reflect.Method} object.
	 * @param p_oClassFilters an array of {@link java.lang.Class} objects.
	 */
	protected void doAddActionAnalyseByActionClass(ClassAnalyse p_oAnalyse, Class<? extends Action<?,?,?,?>>[] p_tActionClasses, MMActionTask.ActionTaskStep p_oStep, Method p_oMethod, Class<?>[] p_oClassFilters) {
		for (Class<? extends Action<?,?,?,?>> oActionClass : p_tActionClasses) {
			if (p_oAnalyse.getMethodOfClass(oActionClass, p_oStep) == null) {
				p_oAnalyse.addActionAnalyseByActionClass(oActionClass, p_oStep.name(), p_oMethod, Arrays.asList(p_oClassFilters));
			}
		}
	}

	/**
	 * <p>hasDelegate.</p>
	 *
	 * @param p_oClass a {@link java.lang.Class} object.
	 * @return a boolean.
	 */
	protected boolean hasDelegate(Class<?> p_oClass) {
		for (Class<?> oInterface : p_oClass.getInterfaces()) {
			if (oInterface.isAssignableFrom(ListenerDelegator.class)) {
				return true;
			}
		}
		return false;
	}
}
