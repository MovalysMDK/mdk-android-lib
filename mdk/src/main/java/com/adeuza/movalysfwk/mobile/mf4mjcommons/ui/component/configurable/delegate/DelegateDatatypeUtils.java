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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.delegate;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.EMailSVMImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.MPhotoVO;

public class DelegateDatatypeUtils {

	public static NullOrEmptyComputer getNullOrEmptyComputer(Class<?> p_oType) {
		
		NullOrEmptyComputer r_NullOrEmptyComputer = null;
		
		if (p_oType == null ||
				p_oType.equals(NoneType.class) ||
				p_oType.equals(Boolean.class) ||
				p_oType.equals(ItemViewModel.class) || 
				p_oType.equals(Enum.class)) {
			r_NullOrEmptyComputer = new NullOrEmptyNonTypeComputer();
		}
		
		else if (p_oType.equals(String.class)) {
			return new NullOrEmptyStringComputer();
		}
		
		else if ( p_oType.equals(Integer.class) ||
				p_oType.equals(Long.class) ||
				p_oType.equals(Double.class) ) {
			return new NullOrEmptyNumberComputer();
		}
		
		else if (p_oType.equals(MPhotoVO.class)) {
			return new NullOrEmptyMPhotoVOComputer();
		}
		
		else if (p_oType.equals(ListViewModel.class)) {
			return new NullOrEmptyListViewModelComputer();
		}
		
		else if (p_oType.equals(AddressLocationSVMImpl.class)) {
			return new NullOrEmptyAddressLocationSVMImplComputer();
		}
		
		else if (p_oType.equals(EMailSVMImpl.class)) {
			return new NullOrEmptyEMailSVMImplComputer();
		}
		
		else {
			// search in bean loader
			StringBuilder builder = new StringBuilder("NullOrEmpty");
			builder.append(p_oType.getSimpleName());
			builder.append("Computer");
			if (BeanLoader.getInstance().hasDefinition(builder.toString())) {
				r_NullOrEmptyComputer = BeanLoader.getInstance().instantiatePrototype(builder.toString());
			} else {
				r_NullOrEmptyComputer = new NullOrEmptyNonTypeComputer();
			}
		}
		
		return r_NullOrEmptyComputer;
	}
	
}
