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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.doopenmap;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMCustomDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displaymessage.AlertMessage;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.AddressLocation;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.DoOpenMapAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>A2A_DOC Décrire la classe DoOpenMapActionImpl</p>
 *
 *
 */
public class DoOpenMapActionImpl implements DoOpenMapAction {
	
	/** serial id */
	private static final long serialVersionUID = 5549384413733898481L;
	/** the default zoom level*/
	private static final String ZOOMLEVEL = "17";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AddressLocation getEmptyInParameter() {
		return new AddressLocation();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doAction(MContext p_oContext, AddressLocation p_oParameterIn) {
		
		if (p_oParameterIn.isAcurate()){
			if (p_oParameterIn.getCity()!=null && !"".equals(p_oParameterIn.getCity()) && p_oParameterIn.getCountry()!=null && !"".equals(p_oParameterIn.getCountry())){
				//affiche un dialog de choix et ouvre la carte correspondante
				displayDialogChooser(p_oParameterIn);
			}
			else{
				//on ne connait que les coordonnées
				this.openMap(this.getAcurateURI(p_oParameterIn));
			}
		}
		else {
			this.openMap(getUnAcurateURI(p_oParameterIn));
		}
		return null;
	}

	/**
	 * get unaccurate URI
	 * @param p_oAddressLocation address location
	 * @return URI string
	 */
	private String getUnAcurateURI(AddressLocation p_oAddressLocation){
		if ("".equals(p_oAddressLocation.getStreet())){
			return StringUtils.concat("geo:0,0?q=",p_oAddressLocation.getCompl()," ",p_oAddressLocation.getCity()," ", p_oAddressLocation.getCountry());
		}
		else{
			return StringUtils.concat("geo:0,0?q=",p_oAddressLocation.getStreet()," ",p_oAddressLocation.getCity()," ", p_oAddressLocation.getCountry());
		}
	}
	
	/**
	 * get accurate URI
	 * @param p_oAddressLocation address location
	 * @return URI string
	 */
	private String getAcurateURI(AddressLocation p_oAddressLocation){
		if (p_oAddressLocation.getLatitude()!=null && p_oAddressLocation.getLongitude()!=null){
			return StringUtils.concat("geo:",String.valueOf(p_oAddressLocation.getLatitude()),",",String.valueOf(p_oAddressLocation.getLongitude()),"?z=",ZOOMLEVEL);
			
		}
		return null;
	}
	
	/**
	 * open the map
	 * @param p_sURI map URI
	 */
	private void openMap(String p_sURI){
		try {
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(p_sURI));
			((AndroidApplication) Application.getInstance()).startActivity(mapIntent);
		} catch (ActivityNotFoundException e) {
			(Application.getInstance().getController()).doDisplayMessage(new AlertMessage(AndroidApplicationR.alert_map_missing, AlertMessage.LONG));
		}
	}
	
	/**
	 * display dialog chooser
	 * @param p_oParameterIn address location
	 */
	private void displayDialogChooser(final AddressLocation p_oParameterIn) {
		AbstractMMActivity oCurrentActivity = (AbstractMMActivity) ((AndroidApplication) Application.getInstance()).getCurrentVisibleActivity();
		
		// lancement de la fenêtre de dialog pour valider ou non la fermeture de l'application
		final MMCustomDialogFragment.Builder oBuilder = new MMCustomDialogFragment.Builder(oCurrentActivity);

		oBuilder.setTitle(AndroidApplicationR.component_position__mapDialog__title);
		oBuilder.setMessage(AndroidApplicationR.component_position__mapDialog__message);
		oBuilder.setIcon(android.R.drawable.ic_dialog_map);

		oBuilder.setPositiveButton(AndroidApplicationR.component_position__mapDialog_Coordinates__label, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface p_oDialog, int p_iId) {
					DoOpenMapActionImpl.this.openMap(DoOpenMapActionImpl.this.getAcurateURI(p_oParameterIn));
				}
		});

		oBuilder.setNegativeButton(AndroidApplicationR.component_position__mapDialog_Address__label, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface p_oDialog, int p_iId) {
					DoOpenMapActionImpl.this.openMap(DoOpenMapActionImpl.this.getUnAcurateURI(p_oParameterIn));
				}
		});

		MMCustomDialogFragment oAlert = oBuilder.create();
		oAlert.setCancelable(true);
		oAlert.show(oCurrentActivity.getSupportFragmentManager(), MMCustomDialogFragment.CUSTOM_DIALOG_TAG);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, AddressLocation p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doOnError(MContext p_oContext, AddressLocation p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//nothing to do
		return p_oResultOut;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getConcurrentAction() {
		return Action.DEFAULT_QUEUE;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPreExecute(AddressLocation p_oAddressLocation, MContext p_oContext) throws ActionException {
		//Nothing do do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, AddressLocation p_oParameterIn, NullActionParameterImpl p_oParameterOut)
			throws ActionException {
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPublishProgress(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep, PROGRESS[])
	 */
	@Override
	public void doPublishProgress(MContext p_oContext, DefaultActionStep p_oState, Void... p_oProgressInformations) {
		//Nothing to do
	}
}
