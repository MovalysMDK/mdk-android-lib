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
package com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.listview;

import android.view.View;
import android.widget.AbsListView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKBaseAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnectorWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ListAdapterDelegateImpl;

public class MDKListViewConnectorWrapper implements MDKViewConnectorWrapper {
	
	public void configure( MDKBaseAdapter p_oAdapter, View view ) {
		AbsListView listView = (AbsListView) view;
		
		MDKAdapter<?,?,?> oAdapter = (MDKAdapter<?,?,?>) p_oAdapter;
		MDKListViewConnector listViewConnector = new MDKListViewConnector(oAdapter);
		listView.setAdapter(listViewConnector);
		p_oAdapter.setViewConnector(listViewConnector);
		p_oAdapter.setAdapterDelegate( 
			new ListAdapterDelegateImpl<MDKAdapter<?,?,?>>(oAdapter));
	}
}
