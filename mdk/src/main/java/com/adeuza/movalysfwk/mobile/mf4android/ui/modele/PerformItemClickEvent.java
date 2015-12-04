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

import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPerformItemClickEventData;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPerformItemClickView;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.AbstractBusinessEvent;

public class PerformItemClickEvent extends AbstractBusinessEvent<MMPerformItemClickEventData> {

	public PerformItemClickEvent(Object p_oSource, String p_sTag, View p_oParamView, int p_iPosition, long p_lId,
			MMPerformItemClickView p_oListView) {
		super(p_oSource, new MMPerformItemClickEventData(p_sTag, p_oParamView, p_iPosition, p_lId, p_oListView));
	}
	
}
