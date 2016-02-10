package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMSpinnerAdapterHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.generic.InParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * <p>TODO Décrire la classe AbstractConfigurableFixedListAdapter</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 */

public abstract class AbstractConfigurableFixedListAdapter<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>> {

	public static final String SELECTED_VM = "SELECTED_VM";

	private LISTVM masterVM;

	/** android id of layout (the name of file) for item */
	private int layoutid = 0;
	/** android id of configurable layout for item */
	private int configurableLayout = 0;
	/** */
	private int detailLayoutId;
	/** */
	private SparseArrayCompat<AbstractConfigurableSpinnerAdapter<?,?,?>> referencedAdapter;

	public AbstractConfigurableFixedListAdapter(int p_iLayoutId, int p_iConfigurableLayoutId, int p_iDetailLayoutId) {
		this.layoutid = p_iLayoutId;
		this.configurableLayout = p_iConfigurableLayoutId;
		this.detailLayoutId = p_iDetailLayoutId;
		this.referencedAdapter = new SparseArrayCompat<>();
	}

	public AbstractConfigurableFixedListAdapter(LISTVM p_oMasterVM, int p_iLayoutId, int p_iConfigurableLayoutId, int p_iDetailLayoutId) {
		this(p_iLayoutId, p_iConfigurableLayoutId, p_iDetailLayoutId);
		this.masterVM = p_oMasterVM;
	}

	public int getLayout(ITEMVM p_oViewModel) {
		return this.layoutid;
	}

	public int getConfigurableLayout(ITEMVM p_oViewModel) {
		return this.configurableLayout;
	}

	public int getDetailLayout() {
		return this.detailLayoutId;
	}

	public LISTVM getMasterVM() {
		return this.masterVM;
	}
	
	public void setMasterVM(LISTVM p_oMasterVM) {
		this.masterVM = p_oMasterVM;
	}

	public void add(ITEMVM p_oViewModel) {
		this.masterVM.add(p_oViewModel, true);
	}
	
	public void update(ITEMVM p_oViewModel) {
		this.masterVM.update(p_oViewModel);
	}

	public void remove(ITEMVM p_oViewModel) {
		this.masterVM.remove(p_oViewModel);
	}

	public void addReferenceTo(int p_iComponent, AbstractConfigurableSpinnerAdapter<?, ?, ?> p_oAdapter) {
		this.referencedAdapter.put(p_iComponent, p_oAdapter);
	}

	public abstract ITEMVM createEmptyVM();

	public abstract Class<? extends Action<InParameter, ?, ?, ?>> getDoAfterDeleteAction();

	public abstract Class<? extends Action<InParameter, ?, ?, ?>> getDoBeforeAddAction();

	public void wrapDetailView(MasterVisualComponent<?> p_oDetailComponent) {
		ViewGroup oDetailView = (ViewGroup) p_oDetailComponent;
		for( int i = 0 ; i < this.referencedAdapter.size(); i++ ) {
			View oView = oDetailView.findViewById(this.referencedAdapter.keyAt(i));
			((MMSpinnerAdapterHolder<?, ?>) oView ).setAdapter(this.referencedAdapter.valueAt(i));
		}
	}

	public Class<ITEMVM> getItemVMInterface() {
		return this.masterVM.getItemVmInterface();
	}
	
}