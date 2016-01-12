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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration;


/**
 * Describes the name of the properties available for MDK setup
 */
public enum FwkPropertyName implements PropertyName {
	
	/** true to enable debug mode */
	debug_mode,
	/** true to enable dev mode */
	dev_mode,
	/** true to enable mock synchronization mode */
	sync_mock_mode,
	/** gives the return code of mock synchronization mode */
	sync_mock_testid,
	/** true to deactivate authentication on synchronization */
	desactivate_auth,
	/** true to enable debug synchronization mode */
	sync_debug_mode,
	/** true to synchronization data stream from file mode */
	sync_data_stream_from_file_mode,
	/** true to synchronization without progress Dialogue mode */
	sync_transparent_mode,
	
	/** menu_base_doStopApplicationStartup visible */
	menu_base_doStopApplicationStartup$visible,
	/** menu_base_doRestartApplicationStartup$visible visible */
	menu_base_doRestartApplicationStartup$visible,
	/** menu_base_configuration_during_application_stop visible */
	menu_base_configuration_during_application_stop$visible,
	/** menu_base_configuration_during_application_stop_doDeleteSerializedConfiguration visible */
	menu_base_configuration_during_application_stop_doDeleteSerializedConfiguration$visible,
	/** menu_base_configuration_during_application_stop_doResetSetting visible */
	menu_base_configuration_during_application_stop_doResetSetting$visible,
	/** menu_base_configuration_during_application_stop_doResetDataBase visible */
	menu_base_configuration_during_application_stop_doResetDataBase$visible,
	/** menu_base_configuration visible */
	menu_base_configuration$visible,
	/** menu_base_configuration_doDisplaySetting visible */
	menu_base_configuration_doDisplaySetting$visible,
	/** menu_base_configuration_doResetSetting visible */
	menu_base_configuration_doResetSetting$visible,
	/** menu_base_configuration_doResetDataBase visible */
	menu_base_configuration_doResetDataBase$visible,
	/** menu_base_doDisplayExitApplicationDialog visible */
	menu_base_doDisplayExitApplicationDialog$visible,
	
	/** name of the logger */
	logger_name,
	/** database */
	database,
	/** name of the database */
	database_name,
	/** version of the database */
	database_version,
	
	/** true to synchronize empty database on 3g */
	synchronization_emptyDBSync_3GGPRS,
	/** true to synchronize empty database on wifi */
	synchronization_emptyDBSync_wifi,
	/** true to enable first synchronization on 3g */
	synchronization_firstSync_3GGPRS,
	/** true to enable first synchronization on wifi */
	synchronization_firstSync_wifi,
	/** true to enable synchronization on 3g */
	synchronization_otherSync_3GGPRS,
	/** true to enable synchronization on wifi */
	synchronization_otherSync_wifi,
	
	/** true to exit application on synchronization failure */
	synchronization_exitAppOnFirstSynchroFailure,
	/** true to display a popup on synchronization failure */
	synchronization_emptyDB_failure_popup,
	/** maximum duration without syncing application */
	synchronization_max_time_without_sync,

	/** maximum number of selectable items */
	default_maxNumberSelectableItem,
	
	/** Crash report, enable file report generation */
	crashreport_file_enabled,
	
	/** Crash report, include database in file report generation */
	crashreport_file_dbexport,
	
	/** Crash report, enable mail report generation */
	crashreport_mail_enabled,
	
	/** Crash report, email to send report */
	crashreport_mail_sender,

	/** pass of the crypted keystore */
	dbcrypt_keystorepass,
	/** servlet name used to download images */
	url_downloadImage,
	/** default width of the synced images */
	sync_image_default_width,
	
	/** Case-sensitive login parameter */
	case_sensitive_login
	
}
