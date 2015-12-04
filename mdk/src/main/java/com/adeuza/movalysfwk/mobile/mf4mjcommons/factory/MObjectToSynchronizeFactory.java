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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.factory;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.EntityFactory;
//@non-generated-start[imports]
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;
//@non-generated-end

/**
 *
 * <p>MObjectToSynchronizeFactory : </p>
 *
 *
 *
 */
@Scope(ScopePolicy.SINGLETON)
public interface MObjectToSynchronizeFactory extends EntityFactory<MObjectToSynchronize> {

	//@non-generated-start[constants]
	//@non-generated-end

	/**
	 * Méthode de création de l'objet d'interface MObjectToSynchronize avec l'enregistrement des changements.
	 *
	 * @return MObjectToSynchronize
	 */
	@Override
	public MObjectToSynchronize createInstance();

	//@non-generated-start[methods]
	//@non-generated-end
}
