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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker;


import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.IRestResponse;

/**
 * Helper to invoke a rest service
 */
public class RestInvokerHelper {
	
	/**
	 * Invoke a rest service
	 *
	 * @param p_oInvocationConfig configuration of rest invocation
	 * @param p_oRestConnectionConfig connection configuration
	 * @param p_oContext conntext
	 * @return response of rest service
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException if any.
	 * @param <R> a R object.
	 */
	public <R extends IRestResponse> R invoke( RestInvocationConfig<R> p_oInvocationConfig,
			RestConnectionConfig p_oRestConnectionConfig, MContext p_oContext ) throws RestException {
		
		R r_oResponse = null;
		
		p_oRestConnectionConfig.setMockMode(ConfigurationsHandler.getInstance().getProperty(
				FwkPropertyName.sync_mock_mode).getBooleanValue());
		
		p_oRestConnectionConfig.setMockStatusCode(
				ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.sync_mock_testid).getIntValue());
		
		IRestInvoker<R> oRestInvoker = BeanLoader.getInstance().getBean(IRestInvoker.class);
		
		oRestInvoker.init(null, p_oRestConnectionConfig);
		
		oRestInvoker.prepare();
	
		// if data to send
		if ( p_oInvocationConfig.getRequestWriter() != null ) {
			p_oInvocationConfig.getRequestWriter().prepareNoSync(p_oContext);
		}
	
		// invocation
		oRestInvoker.invoke(p_oInvocationConfig, p_oContext);
		
		// process response
		r_oResponse = oRestInvoker.process(p_oInvocationConfig, p_oRestConnectionConfig, null, p_oContext );
		
		return r_oResponse;
	}
}
