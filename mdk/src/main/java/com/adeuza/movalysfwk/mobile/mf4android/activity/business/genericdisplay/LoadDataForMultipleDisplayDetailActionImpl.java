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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay;

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.MMDataloader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.genericdisplay.InDisplayParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * Action to load data for several display actions.
 * Can be used to initialize tabs.
 *
 */
//XXX CHANGE V3.2
public class LoadDataForMultipleDisplayDetailActionImpl
	extends AbstractGenericLoadActionImpl<LoadDataForMultipleDisplayDetailActionParameter, NullActionParameterImpl, DefaultActionStep, Void>
	implements LoadDataForMultipleDisplayDetailAction {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -391101021032623650L;

	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doAction(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public NullActionParameterImpl doAction(MContext p_oContext,
			LoadDataForMultipleDisplayDetailActionParameter p_oParameterIn) {

		//XXX CHANGE V3.2
		List<MMDataloader<?>> listDataLoaders = new ArrayList<>();
		List<String> listDtlKey = new ArrayList<>();
		MMDataloader<?> oDtl ;
		
		for( InDisplayParameter oDisplayParameter : p_oParameterIn.getDisplayInParameters()) {
			oDtl = reloadDataLoader(p_oContext, oDisplayParameter);
			listDtlKey.add(oDisplayParameter.getKey());
			listDataLoaders.add(oDtl);
		}
		
		int cpt = 0;
		for( MMDataloader<?> oDataLoader : listDataLoaders) {
			oDataLoader.notifyDataLoaderReload(p_oContext, listDtlKey.get(cpt));
			cpt++;
		}
		
		return new NullActionParameterImpl();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#getEmptyInParameter()
	 */
	@Override
	public LoadDataForMultipleDisplayDetailActionParameter getEmptyInParameter() {
		return new LoadDataForMultipleDisplayDetailActionParameter();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay.AbstractGenericLoadActionImpl.doNotify()
	 */
	@Override
	protected boolean doNotify() {
		return false;
	}
}
