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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.factory.MParametersFactory;

//@non-generated-start[imports]

/**
 *
 * <p>MParametersFactoryImpl : </p>
 *
 *
 *
 */
public class MParametersFactoryImpl extends AbstractEntityFactory<MParameters> implements MParametersFactory {

	//@non-generated-start[attributes]
	//@non-generated-end

	/**
	 * {@inheritDoc}
	 *
	 * @see com.adeuza.movalysfwk.mf4model.model.MParametersFactory#createInstance()
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MParameters} object.
	 */
	@Override
	public MParameters createInstance() {
		MParameters r_oMParameters = new MParametersImpl();
		this.init(r_oMParameters);
		return r_oMParameters;
	}

	/**
	 * Méthode d'initialisation de l'objet
	 *
	 * @param p_oMParameters Entité d'interface MParameters
	 */
	protected void init(MParameters p_oMParameters) {
		//@non-generated-start[init]
		//@non-generated-end
	}

	//@non-generated-start[methods]
	//@non-generated-end
}
