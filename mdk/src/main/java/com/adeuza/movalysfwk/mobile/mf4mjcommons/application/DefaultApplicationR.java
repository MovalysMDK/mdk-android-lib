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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.application;

/**
 * <p>A2A_DOC LMI - Décrire la classe DefaultApplicationR</p>
 *
 *
 * @since Annapurna
 */
public enum DefaultApplicationR implements ApplicationR {

	/** A2A_DOC LMI */
	red(ApplicationRGroup.COLOR, "red"),
	/** A2A_DOC LMI */
	dark_grey(ApplicationRGroup.COLOR, "dark_grey"),
	
	/** A2A_DOC LMI */
	alert_application_notiddle(ApplicationRGroup.STRING, "alert_application_notiddle"),
	/** Utilisé comme titre dans l'affichage des écrans d'erreur. */
	error(ApplicationRGroup.STRING,"error"),
	/** A2A_DOC LMI */
	error_mandatory(ApplicationRGroup.STRING,"error_mandatory"),
	error_inform(ApplicationRGroup.STRING,"error_inform"),
	/** use a field need a minimum size */
	error_minsize1(ApplicationRGroup.STRING, "error_minsize1"),
	/** use a field need a minimum size */
	error_minsize2(ApplicationRGroup.STRING, "error_minsize2"),
	/** message d'erreur pour données invalides */
	error_invalid_data(ApplicationRGroup.STRING,"error_invalid_data"),
	/** message d'erreur pour données invalides */
	error_invalid_decimal_data(ApplicationRGroup.STRING,"error_invalid_decimal_data"),
	/** message d'erreur pour données invalides */
	error_invalid_integer_data(ApplicationRGroup.STRING,"error_invalid_integer_data"),
	/** message d'erreur pour données invalides suite à une erreur de format */
	error_invalid_data_format(ApplicationRGroup.STRING,"error_invalid_data_format"),
	/** message d'erreur pour données invalides suite à une erreur de longueur */
	error_invalid_data_format_max_length(ApplicationRGroup.STRING,"error_invalid_data_format_max_length"),
	/** message d'erreur pour données invalides suite à une erreur de longueur */
	error_invalid_data_max_value(ApplicationRGroup.STRING,"error_invalid_data_max_value"),
	/** message d'erreur pour données invalides suite à une erreur de longueur */
	error_invalid_data_min_value(ApplicationRGroup.STRING,"error_invalid_data_min_value"),
	/** message d'erreur pour données invalides suite à une date trop grande */
	error_invalid_data_format_max_date(ApplicationRGroup.STRING,"error_invalid_data_format_max_date"),
	/** message d'erreur pour données invalides suite à une date trop petite */
	error_invalid_data_format_min_date(ApplicationRGroup.STRING,"error_invalid_data_format_min_date"),	
	/** message d'erreur pour données invalides suite à une heure trop grande */
	error_invalid_data_format_max_hour(ApplicationRGroup.STRING,"error_invalid_data_format_max_hour"),
	/** message d'erreur pour données invalides suite à une heure trop petite */
	error_invalid_data_format_min_hour(ApplicationRGroup.STRING,"error_invalid_data_format_min_hour"),
	/** message d'erreur pour données invalides suite à un nb de décimales invalides */
	error_invalid_data_format_nb_digit(ApplicationRGroup.STRING,"error_invalid_data_format_nb_digit"),
	/** message d'erreur pour données invalides */
	error_application(ApplicationRGroup.STRING,"error_application"),
	/** message d'erreur pour données invalides */
	error_launch_action(ApplicationRGroup.STRING,"error_launch_action"),
	/** message d'erreur pour données invalides */
	error_action(ApplicationRGroup.STRING,"error_action"),
	
	/** message d'erreur pour données invalides */
	UserErrorMessage_0001(ApplicationRGroup.STRING,"UserErrorMessage_0001"),
	/** message d'erreur pour données invalides */
	UserErrorMessage_0002(ApplicationRGroup.STRING,"UserErrorMessage_0002"),
	/** message d'erreur pour données invalides */
	UserErrorMessage_0003(ApplicationRGroup.STRING,"UserErrorMessage_0003"),
	
	/** script de génération du model pour le fwk */
	sqlitecreate_fwkmodel(ApplicationRGroup.RAW, "sqlitecreate_fwkmodel"),
	/** script de suppression du model pour le fwk */
	sqlitedrop_fwkmodel(ApplicationRGroup.RAW, "sqlitedrop_fwkmodel"),
	/** script de génération des data pour le fwk */
	sqlitecreate_fwkdata(ApplicationRGroup.RAW, "sqlitecreate_fwkdata"),
	/** script de suppression des data pour le fwk */
	sqlitedrop_fwkdata(ApplicationRGroup.RAW, "sqlitedrop_fwkdata"),
	/** script de génération du model pour les users */
	sqlitecreate_usermodel(ApplicationRGroup.RAW, "sqlitecreate_usermodel"),
	/** script de suppression du model pour les users */
	sqlitedrop_usermodel(ApplicationRGroup.RAW, "sqlitedrop_usermodel"),
	/** script de génération des data pour les users */
	sqlitecreate_userdata(ApplicationRGroup.RAW, "sqlitecreate_userdata"),
	/** script de suppression des data pour les users */
	sqlitedrop_userdata(ApplicationRGroup.RAW, "sqlitedrop_userdata"),
	
	
	synchronisation_beforepreparesynchro(ApplicationRGroup.STRING, "synchronisation_beforepreparesynchro"),
	synchronisation_beforepreparedata(ApplicationRGroup.STRING, "synchronisation_beforepreparedata"),
	synchronisation_waitserver(ApplicationRGroup.STRING, "synchronisation_waitserver"),
	synchronisation_treatreturndata(ApplicationRGroup.STRING, "synchronisation_treatreturndata"),
	
	/** R.string.ko_incompatible_time_synchro_notification_tricker_text */
	ko_incompatible_time_synchro_notification_tricker_text(ApplicationRGroup.STRING, "ko_incompatible_time_synchro_notification_tricker_text"),
	/** R.string.ko_incompatible_time_synchro_notification_content_text */
	ko_incompatible_time_synchro_notification_content_text(ApplicationRGroup.STRING, "ko_incompatible_time_synchro_notification_content_text"),
	
	/** Tabs **/
	tab_default_title(ApplicationRGroup.STRING, "tab_default_title"),
	tab_default_empty_title(ApplicationRGroup.STRING, "tab_default_empty_title"),
	
	/** regex based fields error messages */
	component_url__bad_entry(ApplicationRGroup.STRING, "component_url__bad_entry"),
	component_email__bad_entry(ApplicationRGroup.STRING, "component_email__bad_entry"),
    component_phone__bad_entry(ApplicationRGroup.STRING, "component_phone__bad_entry"),
    component_string__bad_entry(ApplicationRGroup.STRING, "component_string__bad_entry")
    ;
	
	/** A2A_DOC LMI */
	private ApplicationRGroup group = null;
	/** A2A_DOC LMI */
	private String key = null;
	
	/**
	 * Construct a <em>DefaultApplicationR</em> enum.
	 * @param p_oGroup
	 * @param p_sKey
	 */
	private DefaultApplicationR(ApplicationRGroup p_oGroup, String p_sKey) {
		this.group = p_oGroup;
		this.key = p_sKey;
	}
	
	/** {@inheritDoc} */
	@Override
	public ApplicationRGroup getGroup() {
		return this.group;
	}
	
	/** {@inheritDoc} */
	@Override
	public String getKey() {
		return this.key;
	}
}
