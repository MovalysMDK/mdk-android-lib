package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay;

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.MMDataloader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.genericdisplay.InDisplayParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * Action to load data for several display actions.
 * Can be used to initialize tabs.
 * @author lmichenaud
 *
 */
//XXX CHANGE V3.2
public class LoadDataForMultipleDisplayDetailActionImpl
	extends AbstractGenericLoadActionImpl<LoadDataForMultipleDisplayDetailActionParameter, NullActionParameterImpl, DefaultActionStep, Void>
	implements LoadDataForMultipleDisplayDetailAction {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -391101021032623650L;

	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doAction(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public NullActionParameterImpl doAction(MContext p_oContext,
			LoadDataForMultipleDisplayDetailActionParameter p_oParameterIn) {

		//XXX CHANGE V3.2
		List<MMDataloader<?>> listDataLoaders = new ArrayList<>();
		List<String> listDtlKey = new ArrayList<>();
		MMDataloader<?> oDtl ;
		
		for( InDisplayParameter oDisplayParameter : p_oParameterIn.getDisplayInParameters()) {
			oDtl = reloadDataLoader(p_oContext, oDisplayParameter);
			listDtlKey.add(oDisplayParameter.getKey());
			listDataLoaders.add(oDtl);
		}
		
		int cpt = 0;
		for( MMDataloader<?> oDataLoader : listDataLoaders) {
			oDataLoader.notifyDataLoaderReload(p_oContext, listDtlKey.get(cpt));
			cpt++;
		}
		
		return new NullActionParameterImpl();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#getEmptyInParameter()
	 */
	@Override
	public LoadDataForMultipleDisplayDetailActionParameter getEmptyInParameter() {
		return new LoadDataForMultipleDisplayDetailActionParameter();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay.AbstractGenericLoadActionImpl.doNotify()
	 */
	@Override
	protected boolean doNotify() {
		return false;
	}
}
