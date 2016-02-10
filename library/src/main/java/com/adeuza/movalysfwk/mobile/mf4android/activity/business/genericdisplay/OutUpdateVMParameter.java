package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;

/**
 * update VM OUT param
 */
public class OutUpdateVMParameter extends AbstractActionParameter implements ActionParameter {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 274849204986327815L;

	/**
	 * data loader
	 */
	private Class<? extends Dataloader<?>> dataloader ;

	/**
	 * GETTER
	 * @return data loader
	 */
	public Class<? extends Dataloader<?>> getDataloader() {
		return dataloader;
	}

	/**
	 * SETTER of data loader
	 * @param p_sDataloader data loader
	 */
	public void setDataloader(Class<? extends Dataloader<?>> p_sDataloader) {
		this.dataloader = p_sDataloader;
	}
}
