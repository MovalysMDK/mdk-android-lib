package com.adeuza.movalysfwk.mobile.mf4android.test;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

public class TestHelper {

    /**
     * Create a context with a transaction
     * @return a new context
     */
    public MContext createTransactionContext() {
        MContextFactory oMContextFactory = BeanLoader.getInstance().getBean(MContextFactory.class);
        return oMContextFactory.createContext();
    }

    /**
     * Start an action (for unit test)
     * @param p_oActionClass action class
     * @param p_oParameterIn parameter in
     * @param p_oContext context
     * @return result of action
     * @throws ActionException action failure
     */
    protected ActionParameter launchAction( Class<? extends Action<?,?,?,?>>  p_oActionClass,
                                            ActionParameter p_oParameterIn,  MContext p_oContext) throws ActionException {

        Action oAction = BeanLoader.getInstance().getBean(p_oActionClass);

        ActionParameter r_oResult = oAction.doAction(p_oContext, p_oParameterIn);
        if (p_oContext.getMessages().hasErrors()) {
            r_oResult = oAction.doOnError(p_oContext, p_oParameterIn, r_oResult);
        } else {
            oAction.doOnSuccess(p_oContext, p_oParameterIn, r_oResult);
        }
        return r_oResult;
    }
}
