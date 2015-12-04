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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * <p>
 * Define a list view model to be used with MMList, MMFixedList and MMSpinner.
 * </p>
 *
 * @param <ITEM> entity linked to the view model
 * @param <ITEMVM> the type of the ViewModel
 */
public class ListViewModelImpl<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>>
extends AbstractViewModel implements ListViewModel<ITEM, ITEMVM> {


	/**
	 * Default generated ID
	 */
	private static final long serialVersionUID = -8597939569241797713L;

	/** Indicates if this class should manage ITEM or ITEMVM */
	private boolean workOnItems;

	/** list of items */
	private List<ITEM> listItem = null;
	/** filtered list items */
	private List<ITEM> filterListItems = null;
	/** created ITEMVM SparseArray */
	private List<ITEMVM> cacheListItems = null;

	/** the current selected item list */
	protected List<? extends ItemViewModel<?>> selectedItemList = null;
	/** optionnal parameters map */
	private Map<Object, Object> params = null;
	/** observable */
	private DataSetObservable mObservable = new DataSetObservable();
	/** itemVM interface name */
	private Method createItemVMMethod;
	/** VM class interface */
	private Class<ITEMVM> itemVMInterface;

	/**
	 * Create a new <code>ListViewModel</code> object.
	 */
	public ListViewModelImpl() {
		this(new ArrayList<ITEMVM>(), false);
	}

	/**
	 * Construct a new <code>ListViewModel</code> based on another list.
	 *
	 * @param p_oList
	 *            the list to populate the graphical component
	 * @param p_bWorkOnItems a boolean.
	 */
	public ListViewModelImpl(List<ITEMVM> p_oList, boolean p_bWorkOnItems) {
		this(p_oList, null, p_bWorkOnItems);
	}


	/**
	 * Create a new <code>ListViewModel</code> object.
	 *
	 * @param p_bWorkOnItems
	 *            Indicates if the ListViewModel should work on Entities (item) or ViewModels (ItemVM)
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public ListViewModelImpl(Class<ITEMVM> p_oClass, boolean p_bWorkOnItems) {
		this(new ArrayList<ITEM>(), p_oClass, p_bWorkOnItems);
	}

	/**
	 * Construct a new <code>ListViewModel</code> based on another list.
	 *
	 * @param p_oListVM a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelImpl} object.
	 * @param p_bWorkOnItems a boolean.
	 */
	public ListViewModelImpl(ListViewModelImpl<ITEM, ITEMVM> p_oListVM, boolean p_bWorkOnItems) {
		this(new ArrayList<ITEMVM>(p_oListVM.cacheListItems), null, p_bWorkOnItems);
	}

	/**
	 * Construct a new <code>ListViewModel</code> based on another list.
	 *
	 * @param p_oList
	 *            the list to populate the graphical component
	 * @param p_oSelectedItemList a {@link java.util.List} object.
	 * @param p_bWorkOnItems a boolean.
	 */
	public ListViewModelImpl(List<ITEMVM> p_oList, List<ITEMVM> p_oSelectedItemList, boolean p_bWorkOnItems) {
		this(new ArrayList<ITEM>(), null, null, p_bWorkOnItems);
	}


	/**
	 * Create a new <code>ListViewModel</code> object.
	 *
	 * @param p_bWorkOnItems
	 *            Indicates if the ListViewModel should work on Entities (item) or ViewModels (ItemVM)
	 * @param p_oParent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AbstractViewModel} object.
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public ListViewModelImpl(AbstractViewModel p_oParent, Class<ITEMVM> p_oClass, boolean p_bWorkOnItems) {
		this(new ArrayList<ITEM>(), p_oClass, p_bWorkOnItems);
		this.setParent(p_oParent);
	}

	/**
	 * Construct a new <code>ListViewModel</code> based on another list.
	 *
	 * @param p_oList
	 *            the list to populate the graphical component
	 * @param p_bWorkOnItems
	 *            Indicates if the ListViewModel should work on Entities (item) or ViewModels (ItemVM)
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public ListViewModelImpl(List<ITEM> p_oList, Class<ITEMVM> p_oClass, boolean p_bWorkOnItems) {
		this(p_oList, null, p_oClass, p_bWorkOnItems);
	}

	/**
	 * Construct a new <code>ListViewModel</code> based on another list.
	 *
	 * @param p_bWorkOnItems
	 *            Indicates if the ListViewModel should work on Entities (item) or ViewModels (ItemVM)
	 * @param p_oListVM a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelImpl} object.
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public ListViewModelImpl(ListViewModelImpl<ITEM, ITEMVM> p_oListVM, Class<ITEMVM> p_oClass, boolean p_bWorkOnItems) {
		this(new ArrayList<ITEM>(p_oListVM.listItem), null, p_oClass, p_bWorkOnItems);
	}

	/**
	 * Construct a new <code>ListViewModel</code> based on another list.
	 *
	 * @param p_oList
	 *            the list to populate the graphical component
	 * @param p_bWorkOnItems
	 *            Indicates if the ListViewModel should work on Entities (item) or ViewModels (ItemVM)
	 * @param p_oSelectedItemList a {@link java.util.List} object.
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public ListViewModelImpl(List<ITEM> p_oList, List<ITEMVM> p_oSelectedItemList, Class<ITEMVM> p_oClass, boolean p_bWorkOnItems) {
		if(p_bWorkOnItems) {
			if(p_oList == null) {
				this.listItem  = new ArrayList<>();
			}
			else {
				this.listItem = p_oList;
			}
			this.filterListItems = new ArrayList<>(p_oList);
		}
		this.selectedItemList = p_oSelectedItemList;
		if(p_bWorkOnItems) {
			this.cacheListItems = new ArrayList<ITEMVM>(this.listItem.size());
		}
		else {
			this.cacheListItems = new ArrayList<ITEMVM>();
		}

		this.params = new HashMap<Object, Object>();

		if(p_bWorkOnItems) {
			this.itemVMInterface = p_oClass;
			computeCreateItemMethod();
		}

		this.workOnItems = p_bWorkOnItems;

	}

	/**
	 * Create a new <code>ListViewModel</code> object.
	 *
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public ListViewModelImpl(Class<ITEMVM> p_oClass) {
		this(new ArrayList<ITEM>(), p_oClass, true);
	}

	/**
	 * Create a new <code>ListViewModel</code> object.
	 *
	 * @param p_oParent
	 *            the parent of the List.
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public ListViewModelImpl(AbstractViewModel p_oParent, Class<ITEMVM> p_oClass) {
		this(new ArrayList<ITEM>(), p_oClass, true);
		this.setParent(p_oParent);
	}

	/**
	 * Construct a new <code>ListViewModel</code> based on another list.
	 *
	 * @param p_oList
	 *            the list to populate the graphical component
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public ListViewModelImpl(List<ITEM> p_oList, Class<ITEMVM> p_oClass) {
		this(p_oList, null, p_oClass, true);
	}
	
	/**
	 * Construct a new <code>ListViewModel</code> based on another list.
	 *
	 * @param p_oList
	 *            the list to populate the graphical component
	 * @param p_oParent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AbstractViewModel} object.
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public ListViewModelImpl(List<ITEM> p_oList, AbstractViewModel p_oParent, Class<ITEMVM> p_oClass) {
		this(p_oList, null, p_oClass, true);
		this.setParent(p_oParent);
	}

	/**
	 * Construct a new <code>ListViewModel</code> based on another list.
	 *
	 * @param p_oListVM a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelImpl} object.
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public ListViewModelImpl(ListViewModelImpl<ITEM, ITEMVM> p_oListVM, Class<ITEMVM> p_oClass) {
		this(new ArrayList<ITEM>(p_oListVM.listItem), null, p_oClass, true);
	}

	/**
	 * Construct a new <code>ListViewModel</code> based on another list.
	 *
	 * @param p_oList
	 *            the list to populate the graphical component
	 * @param p_oSelectedItemList a {@link java.util.List} object.
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public ListViewModelImpl(List<ITEM> p_oList, List<ITEMVM> p_oSelectedItemList, Class<ITEMVM> p_oClass) {
		this(p_oList, p_oSelectedItemList, p_oClass, true);
	}

	/** {@inheritDoc} */
	@Override
	public Class<ITEMVM> getItemVmInterface() {
		return this.itemVMInterface;
	}

	/** {@inheritDoc} */
	@Override
	public void setItems(Collection<ITEM> p_oList) {
		this.listItem.clear();
		this.listItem.addAll(p_oList);
		this.filterListItems.clear();
		this.filterListItems.addAll(p_oList);
		this.cacheListItems = new ArrayList<ITEMVM>(this.listItem.size());
		this.mObservable.notifyInvalidated();
	}

	/** {@inheritDoc} */
	@Override
	public List<? extends ItemViewModel<?>> getSelectedItemList() {
		return this.selectedItemList;
	}

	/** {@inheritDoc} */
	@Override
	public void setSelectedItemList(
			List<? extends ItemViewModel<?>> p_oSelectedItemList) {
		this.selectedItemList = p_oSelectedItemList;
	}


	/** {@inheritDoc} */
	@Override
	public void addFirst(ITEMVM p_oItem, boolean p_bAddToFilteredList) {
		if(workOnItems) {
			this.listItem.add(0, null);
			if(p_bAddToFilteredList) {
				this.filterListItems.add(0, null);
			}
		}
		this.cacheListItems.add(0, p_oItem);

		p_oItem.setParent(this);
		this.mObservable.notifyChanged();
	}

	/** {@inheritDoc} */
	@Override
	public void add(ITEMVM p_oItem, boolean p_bAddToFilteredList)  {
		if(workOnItems) {
			this.listItem.add(null);
			if(p_bAddToFilteredList) {
				this.filterListItems.add(null);
			}
		}
		this.cacheListItems.add(p_oItem);

		p_oItem.setParent(this);
		int iIdItem = this.cacheListItems.indexOf(p_oItem);

		if (this.getParent() != null) {
			this.getParent().notifyCollectionChanged(this, Action.ADD, iIdItem, p_oItem);
		}
		this.mObservable.notifyChanged();
	}
	
	/** {@inheritDoc} */
	@Override
	public void addAll(Collection<ITEMVM> p_oListItem, boolean p_bAddToFilteredList) {
		for (ITEMVM oItemVM : p_oListItem) {
			this.cacheListItems.add(oItemVM);
			if(workOnItems) {
				this.listItem.add(null);
				if(p_bAddToFilteredList) {
					this.filterListItems.add(null);
				}
			}
			oItemVM.setParent(this);
		}
		this.mObservable.notifyInvalidated();
	}

	/**
	 * <p>update.</p>
	 *
	 * @param p_oItem a ITEMVM object.
	 * @param p_bAddToFilteredList a boolean.
	 */
	public void update(ITEMVM p_oItem, boolean p_bAddToFilteredList) {
		int iIdItem = this.cacheListItems.indexOf(p_oItem);

		if (this.getParent() != null) {
			this.getParent().notifyCollectionChanged(this, Action.UPDATE, iIdItem, p_oItem);
		}
		this.mObservable.notifyChanged();
	}

	/**
	 * <p>update.</p>
	 *
	 * @param p_iIndexOfItem a int.
	 * @param p_oItem a ITEMVM object.
	 * @param p_bAddToFilteredList a boolean.
	 */
	public void update(int p_iIndexOfItem, ITEMVM p_oItem, boolean p_bAddToFilteredList) { 
		this.cacheListItems.set(p_iIndexOfItem, p_oItem);

		if (this.getParent() != null) {
			this.getParent().notifyCollectionChanged(this, Action.UPDATE, p_iIndexOfItem, p_oItem);
		}
		this.mObservable.notifyChanged();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param p_oItem a ITEMVM object.
	 */
	@Override
	public void addFirst(ITEMVM p_oItem) {
		addFirst(p_oItem, false);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param p_oItem a ITEMVM object.
	 */
	//@Override
	@Override
	public void add(ITEMVM p_oItem) {
		add(p_oItem, false);
	}

	//@Override
	/** {@inheritDoc} */
	@Override
	public void addAll(Collection<ITEMVM> p_oListItem) {
		addAll(p_oListItem, false);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param p_oItem a ITEMVM object.
	 */
	//@Override
	@Override
	public void update(ITEMVM p_oItem) {
		update(p_oItem, false);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param p_iIndexOfItem a int.
	 * @param p_oItem a ITEMVM object.
	 */
	//@Override
	@Override
	public void update(int p_iIndexOfItem, ITEMVM p_oItem) {
		update(p_iIndexOfItem, p_oItem, false);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @param p_oItem a ITEMVM object.
	 */
	//@Override
	@Override
	public void remove(ITEMVM p_oItem) {
		if (p_oItem != null && this.cacheListItems.contains(p_oItem)) {
			int iIdItem = this.cacheListItems.indexOf(p_oItem);

			this.cacheListItems.remove(iIdItem);
			// l'index dans les items filtrer n'est pas le même...
			int iIndexFiltered = 0;
			if(workOnItems) {
				iIndexFiltered = this.indexOfItem(filterListItems, this.listItem.get(iIdItem));
				this.filterListItems.remove(iIndexFiltered);
				this.listItem.remove(iIdItem);
			} else {
				iIndexFiltered = this.cacheListItems.indexOf(p_oItem);
			}

			if (this.getParent() != null) {
				// return iIndexFiltered (index of the displayed list)
				this.getParent().notifyCollectionChanged(this, Action.REMOVE, iIndexFiltered, p_oItem);
			}
		}
		this.mObservable.notifyChanged();
	}

	/** {@inheritDoc} */
	@Override
	public ITEMVM getCacheVMByPosition(int p_oPosition) {
		ITEMVM r_oItemVm = null;
		if(this.workOnItems) {
			if (p_oPosition < this.filterListItems.size()) {
				ITEM oItem = this.filterListItems.get(p_oPosition);
				// get the index
				int iIndex = this.indexOfItem(this.listItem, oItem);
				r_oItemVm = this.getOrCreateItemVmAtIndex(iIndex);
			}
		}
		else {
			if (p_oPosition < cacheListItems.size()) {
				r_oItemVm = cacheListItems.get(p_oPosition);
			} else {
				r_oItemVm = null;
			}
		}
		return r_oItemVm;

	}


	/** {@inheritDoc} */
	@Override
	public ITEMVM getCacheVMById(String p_sId) {
		ITEMVM r_oItem = null;
		if(workOnItems) {
			for(ITEM oItem:this.filterListItems) {
				if (oItem.idToString().equals(p_sId)) {
					int iIndex = this.indexOfItem(this.listItem, oItem);
					r_oItem = getOrCreateItemVmAtIndex(iIndex);
					break;
				}
			}
		}
		else {
			for (ITEMVM o : cacheListItems) {
				if (o.getIdVM().equals(
						o.transformIdFromIdentifiable(p_sId))) {
					r_oItem = o;
					break;
				}
			}
		}
		return r_oItem;
	}

	/** {@inheritDoc} */
	@Override
	public ITEMVM getCacheVMById(ITEM p_oModel) {
		// this is weird because we can implement the "same" without using filtered items...
		// we "just" do not check if the p_oModel is in filtered items
		ITEMVM r_oItem = null;
		if(workOnItems) {
			int iFilteredIndex = this.indexOfItem(this.filterListItems, p_oModel);
			if (iFilteredIndex != -1) {
				int iIndex = this.indexOfItem(this.listItem, p_oModel);
				r_oItem = getOrCreateItemVmAtIndex(iIndex);
			}
		}
		else {
			for (ITEMVM o : cacheListItems) {
				if (o.getIdVM().equals(
						o.transformIdFromIdentifiable(p_oModel.idToString()))) {
					r_oItem = o;
					break;
				}
			}
		}
		return r_oItem;
	}

	/**
	 * Get item vm from cache or create it if is not in cache
	 * @param p_iIndex the index of the item in listItem
	 * @return the cache element
	 */
	private ITEMVM getOrCreateItemVmAtIndex(int p_iIndex) {

		ITEMVM r_oItemVm = null;
		if (p_iIndex >= 0 && p_iIndex < this.cacheListItems.size()) {
			r_oItemVm = this.cacheListItems.get(p_iIndex);
		}
		ITEM oItem = this.listItem.get(p_iIndex);
		if (r_oItemVm == null && oItem != null) {
			try {
				r_oItemVm = (ITEMVM) this.createItemVMMethod.invoke(Application.getInstance().getViewModelCreator(), oItem);
				if(p_iIndex >= this.cacheListItems.size()) {
					int initialSize = this.cacheListItems.size();
					for(int  i = 0 ; i < p_iIndex - initialSize ; i++) {
						this.cacheListItems.add(null);
					}
					this.cacheListItems.add(p_iIndex, r_oItemVm);
				}
				else {
					this.cacheListItems.set(p_iIndex, r_oItemVm);
				}
				
			

				
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return r_oItemVm;
	}

	/** {@inheritDoc} */
	@Override
	public int getCount() {
		int iCount = 0;
		if(workOnItems) {
			iCount = this.filterListItems.size();
		}
		else {
			iCount = this.cacheListItems.size();
		}
		return iCount;
	}


	/** {@inheritDoc} */
	@Override
	public int indexOf(ITEMVM p_oItem) {
		if(workOnItems) {
			int r_iIndex = this.cacheListItems.indexOf(p_oItem);
			// normally never appends
			if (r_iIndex == -1) {
				for (int i = 0; i < this.listItem.size(); i++) {
					ITEM oItem = this.listItem.get(i);
					if (oItem.idToString().equals(p_oItem.getIdVM())) {
						r_iIndex = i;
						break;
					}
				}
			}
			// get the index of the filtered list
			return this.indexOfItem(this.filterListItems, this.listItem.get(r_iIndex));
		}
		else {
			return this.cacheListItems.indexOf(p_oItem);
		}
	}

	/** {@inheritDoc} */
	@Override
	public final String getIdVM() {
		return String.valueOf(this.cacheListItems.hashCode());
	}

	/** {@inheritDoc} */
	@Override
	public final void modifyToIdentifiable(List<ITEM> p_oIdentifiables) {
		ITEMVM itemVM = null;
		for (ITEM item : p_oIdentifiables) {
			itemVM = this.getCacheVMById(item);
			if (((AbstractViewModel) itemVM).isDirectlyModified()) {
				itemVM.modifyToIdentifiable(item);
			}
		}

	}

	/**
	 * <p>updateFromIdentifiable.</p>
	 *
	 * @param p_oIdentifiable a ITEM object.
	 */
	@Override
	public final void updateFromIdentifiable(ITEM p_oIdentifiable) {
		ITEMVM itemVM = this.getCacheVMById(p_oIdentifiable);
		itemVM.updateFromIdentifiable(p_oIdentifiable);
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
		this.listItem.clear();
		this.filterListItems.clear();
		this.cacheListItems.clear();
		this.params.clear();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel#addParam(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public void addParam(Object p_oKey, Object p_oParamValue) {
		this.params.put(p_oKey, p_oParamValue);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel#getParam(java.lang.Object)
	 */
	@Override
	public Object getParam(Object p_oKey) {
		return this.params.get(p_oKey);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AbstractViewModel#isReadyToChanged()
	 */
	@Override
	public boolean isReadyToChanged() {
		boolean bFound = false;
		Iterator<ITEMVM> iterItem = this.cacheListItems.iterator();
		while (!bFound && iterItem.hasNext()) {
			ITEMVM oItem = (ITEMVM) iterItem.next();
			if (oItem != null) {
				bFound = oItem.isReadyToChanged();
			}
		}
		return bFound;
	}

	private void writeObject(ObjectOutputStream p_oOos) throws IOException {
		// default serialization
		if(workOnItems) {
			this.createItemVMMethod = null;
		}
		p_oOos.defaultWriteObject();
	}

	private void readObject(ObjectInputStream p_oOis) throws ClassNotFoundException, IOException {
		// default deserialization
		if(workOnItems) {
			computeCreateItemMethod();
		}
		p_oOis.defaultReadObject();
	}

	/** {@inheritDoc} */
	@Override
	public void registerObserver(DataSetObserver p_oObserver) {
		this.mObservable.registerObserver(p_oObserver);
	}

	/** {@inheritDoc} */
	@Override
	public void unregisterObserver(DataSetObserver p_oObserver) {
		this.mObservable.unregisterObserver(p_oObserver);
	}

	/** {@inheritDoc} */
	@Override
	public <PARAM> void filter(ListViewModelFilter<ITEM, ITEMVM, PARAM> p_oFilter, PARAM... p_oParam) {
		this.filterListItems.clear();
		this.filterListItems.addAll(this.listItem);
		for (ITEM oItem : this.listItem) {
			if (!p_oFilter.filter(oItem, this.getCacheVMById(oItem), p_oParam)) {
				this.filterListItems.remove(oItem);
			}
		}
	}

	private void computeCreateItemMethod() {
		try {
			for (Method oMethod : Application.getInstance().getViewModelCreator().getClass().getMethods()) {
				if (oMethod.getName().contains("createOrUpdate") && oMethod.getName().contains(itemVMInterface.getSimpleName()) && oMethod.getParameterTypes().length > 0) {
					this.createItemVMMethod = oMethod;
					break;
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}


	private int indexOfItem(List<ITEM> p_oListItems, ITEM p_oItem) {
		int index = -1;
		for(int iIndex = 0 ; iIndex < p_oListItems.size() ; iIndex++) {
			ITEM oItem = p_oListItems.get(iIndex);
			if(p_oItem != null) {
				if(oItem != null) {
					if(oItem.idToString().equalsIgnoreCase(p_oItem.idToString())) {
						index = iIndex;
						break;
					}
				}
			}
			else {
				if(oItem == null) {
					index = iIndex;
					break;
				}
			}
		}
		return index;
	}

	/** {@inheritDoc} */
	@Override
	public void notifyCollectionChanged() {
		if (this.getParent() != null) {
			this.getParent().notifyCollectionChanged(this, Action.UPDATE, -1, null);
		}
	}
}
