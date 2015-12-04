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
package com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationListener;

/**
 * Dataloader spécialisé dans le traitement d'éléments sensibles à la synchronisation
 *
 * @param <ITEMRESULT> le type de données contenu dans la liste
 */
//XXX CHANGE V3.2
public interface SynchronisableDataLoader <ITEMRESULT> extends MMDataloader<ITEMRESULT>, SynchronizationListener {

	/** {@inheritDoc} */
	@Override
	public void setItemId(final long p_lItemId);
}
