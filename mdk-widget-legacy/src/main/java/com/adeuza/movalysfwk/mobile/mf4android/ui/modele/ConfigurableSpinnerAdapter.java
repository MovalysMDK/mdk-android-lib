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
package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * Basic adapter for spinners
 * @param <ITEM> class of the entity managed by the spinner
 * @param <ITEMVM> class of the item view model
 * @param <LISTVM> class of the list view model
 */
@Deprecated
public class ConfigurableSpinnerAdapter<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>> 
	extends AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> {

	/**
	 * Constructor
	 * @param p_oMasterVM master view model of the spinner
	 * @param p_iSpinnerItemLayoutId selected item layout
	 * @param p_iConfigurableMasterItemId selected item field identifier
	 * @param p_iSpinnerDropDowListLayoutId drop down layout
	 * @param p_iConfigurableMasterDropDowId drop down field identifier
	 * @param p_iSelectedComponent rank of the selected component
	 * @param p_iErrorComponent error field identifier
	 * @param p_bUseEmptyValue true if the empty value can be selected
	 */
	public ConfigurableSpinnerAdapter(LISTVM p_oMasterVM,
			int p_iSpinnerItemLayoutId, 
			int p_iConfigurableMasterItemId,
			int p_iSpinnerDropDowListLayoutId,
			int p_iConfigurableMasterDropDowId, 
			int p_iSelectedComponent,
			int p_iErrorComponent, boolean p_bUseEmptyValue) {
		
		super(p_oMasterVM, 
				p_iSpinnerItemLayoutId,
				p_iConfigurableMasterItemId,
				p_iSpinnerDropDowListLayoutId,
				p_iConfigurableMasterDropDowId, 
				p_iSelectedComponent, p_iErrorComponent, p_bUseEmptyValue);
	}

}
