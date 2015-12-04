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

import java.util.List;

/**
 * Dataloader spécialisé dans le traitement des listes
 *
 * @param <ITEMRESULT> le type de données contenu dans la liste
 */
//XXX CHANGE V3.2
public interface ListDataloader<ITEMRESULT> extends MMDataloader<List<ITEMRESULT>> {

	/**
	 * Retourne un élément de la liste en fonction de son identifiant.
	 *
	 * @param p_sItemId L'identifiant de l'élément recherché.
	 * @return null ou l'élément d'identifiant p_sItemId
	 */
	@Deprecated
	public ITEMRESULT getItem(final String p_sItemId);
}
