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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.model;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.AbstractEntityFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.factory.MObjectToSynchronizeFactory;

//@non-generated-start[imports]

/**
 *
 * <p>MObjectToSynchronizeFactoryImpl : </p>
 *
 *
 *
 */
public class MObjectToSynchronizeFactoryImpl extends AbstractEntityFactory<MObjectToSynchronize> implements MObjectToSynchronizeFactory {

	//@non-generated-start[attributes]
	//@non-generated-end

	/**
	 * {@inheritDoc}
	 *
	 * @see com.adeuza.movalysfwk.mf4model.model.MObjectToSynchronizeFactory#createInstance()
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize} object.
	 */
	@Override
	public MObjectToSynchronize createInstance() {
		MObjectToSynchronize r_oMObjectToSynchronize = new MObjectToSynchronizeImpl();
		this.init(r_oMObjectToSynchronize);
		return r_oMObjectToSynchronize;
	}

	/**
	 * Méthode d'initialisation de l'objet
	 *
	 * @param p_oMObjectToSynchronize Entité d'interface MObjectToSynchronize
	 */
	protected void init(MObjectToSynchronize p_oMObjectToSynchronize) {
		//@non-generated-start[init]
		p_oMObjectToSynchronize.setId(-1);
		//@non-generated-end
	}

	//@non-generated-start[methods]
	//@non-generated-end
}
