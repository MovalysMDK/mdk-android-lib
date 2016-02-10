package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>Paramètre d'entrée pour les actions de type AbstractUpdateVMForDisplayDetailActionImpl InUpdateVMParameter</p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author smaitre
 *
 */

public class InUpdateVMParameter extends AbstractActionParameter implements ActionParameter {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 688564261118001154L;

	/** la liste des adapters associés */
	private List<Object> adapters = null;

	/** le dataloader à utiliser */
	private Class<? extends Dataloader<?>> dataLoader = null;

	/** la classe du vm à utiliser. */
	private Class<? extends ViewModel> vm = null;

	/**
	 * Map de paramètres utilisée pour calculer l'éditabilité du VM
	 */
	private Map<String, Object> editableVMParameters = null;

	/**
	 * instance of view model
	 */
	private ViewModel vmInstance;

	/**
	 * Construit un nouveau paramètre d'entrée
	 */
	public InUpdateVMParameter() {
		super();
		this.adapters = new ArrayList<>();
		this.editableVMParameters = new HashMap<>();
	}
	
	/**
	 * Ajoute un adapter
	 * @param p_oAdapter l'adapter a ajouté
	 */
	public void addAdapter(Object p_oAdapter) {
		if ( p_oAdapter != null ) {
			this.adapters.add(p_oAdapter);
		}
	}
	
	/**
	 * Lance la notification notifyDataSetChanged de tous les adapters référencés
	 */
	public void notifyAdapters() {
		for(Object oAdapter : this.adapters) {
			if (oAdapter instanceof BaseAdapter) {
				((BaseAdapter)oAdapter).notifyDataSetChanged();
			}
			else if (oAdapter instanceof BaseExpandableListAdapter) {
				((BaseExpandableListAdapter) oAdapter).notifyDataSetChanged();
			}
		}
	}
	/**
	 * getter of data loader instance
	 * @return data loader class
	 */
	public Class<? extends Dataloader<?>> getDataLoader() {
		return this.dataLoader;
	}
	/**
	 * setter of data loader instance
	 * @param p_oDataLoader data loader
	 */
	public void setDataLoader(Class<? extends Dataloader<?>> p_oDataLoader) {
		this.dataLoader = p_oDataLoader;
	}
	/**
	 * Getter of ViewModel obj
	 * @return viewModel
	 */
	public Class<? extends ViewModel> getVm() {
		return this.vm;
	}

	/**
	 * SETTER OF VIEW MODEL
	 * @param p_oVm viewModel
	 */
	public void setVm(Class<? extends ViewModel> p_oVm) {
		this.vm = p_oVm;
	}
	/**
	 * get editable ViewModel parameters
	 * @return editable VM parameters
	 */
	public Map<String, Object> getEditableVMParameters() {
		return this.editableVMParameters;
	}
	
	/**
	 * set editable VM parameters
	 * @param p_oEditableVMParameters map of editable VM parameters
	 */
	public void setEditableVMParameters(Map<String, Object> p_oEditableVMParameters) {
		this.editableVMParameters = p_oEditableVMParameters;
	}

	public ViewModel getViewModel() {
		return this.vmInstance;
	}
	
	public void setVm(ViewModel p_oVm) {
		this.vmInstance = p_oVm;
	}
}
