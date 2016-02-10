package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay;

import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.MMDataloader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.genericdisplay.InDisplayParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;

public abstract class AbstractGenericLoadActionImpl<IN extends ActionParameter, OUT extends ActionParameter, PROGRESS extends ActionStep, PROGRESSPARAMETER extends Object> extends AbstractGenericActionImpl<IN, OUT, PROGRESS, PROGRESSPARAMETER> {


	protected MMDataloader<?> reloadDataLoader(MContext p_oContext, InDisplayParameter p_oParam){
		MMDataloader<?> r_oDtl = null;
		try {
			r_oDtl = BeanLoader.getInstance().getBean(p_oParam.getDataLoader());
			long iId = -1L ;
			if (p_oParam.getId()!=null){
				iId = Long.parseLong(p_oParam.getId() ) ;
			}
			r_oDtl.addItemId(p_oParam.getKey(),  iId);
			r_oDtl.addOptions(p_oParam.getKey(), p_oParam.getOptions());
			r_oDtl.reload(p_oContext, p_oParam.getKey(), doNotify(), p_oParam.getReload());
		} catch (DataloaderException e) {
			Log.e(this.getClass().getSimpleName(), "Erreur in loadData", e);
			p_oContext.getMessages().addMessage(ExtFwkErrors.ActionError);

		}
		
		return r_oDtl;
		
	}

	/**
	 * return true if the dataloader need to notify context
	 * @return true if the dataloader need to notify context false otherwise
	 */
	protected abstract boolean doNotify();
	

}
